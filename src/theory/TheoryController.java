/**
 * 
 */
package theory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.jpl7.Query;

import examples.Example;
import examples.SetOfExamples;
import examples.TypeOfClassification;
import examples.TypeOfExample;
import predicate.Predicate;
import predicate.PredicateController;
import revisionPoint.RevisionPointController;

/**
 * @author wsantos
 *
 */
public class TheoryController {

	private static final TheoryController instance = new TheoryController();

	private PredicateController predicateController = PredicateController.getInstance();
	private RevisionPointController revisionPointController = RevisionPointController.getInstance();
	private List<Theory> theories = new ArrayList<Theory>();
	
	private TheoryController(){
		
	}
	
	public static final TheoryController getInstance(){
		return instance;
	}
	
	public List<Theory> getTheories(){
		return theories;
	}

	public void setTheories(List<Theory> theories){
		this.theories = theories;
	}

	public Theory createTheory(String fileName) throws Exception{
		Theory theory = new Theory();
		theory.setFileName(fileName);
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = br.readLine();
		while (line != null) {
			theory.addSClause(line);
			Clause clause = this.createClause(line.replaceAll(" ", ""), theory);
			theory.addClause(clause);
			line = br.readLine();
		}
		br.close();
		theory.load();
		return theory;
	}

	private Clause createClause(String line, Theory theory) {
		Clause main = new Clause();
		if(!line.contains(":-")){
			Predicate predicate = this.predicateController.getPredicate(line);
			if(!predicate.hasAtom() && !predicate.hasNumber() && predicate.hasVariable()){
				main.setType(ClauseType.RULE);
				Head head = new Head();
				head.setPredicate(predicate);
				main.setHead(head);
			}else{
				Head head = new Head();
				head.setPredicate(predicate);
				main.setHead(head);					
				main.setType(ClauseType.FACT);
			}
		}else{
			main.setType(ClauseType.RULE);
			List<String> predicates = this.predicateController.getPredicates(line);
			boolean isHead = true;
			for(String predicate:predicates){
				Predicate p = this.predicateController.getPredicate(predicate);
				if(isHead){
					Head head = new Head();
					head.setPredicate(p);
					main.setHead(head);
					isHead = false;
				}else{
					Clause antecedent = new Clause();
					if((p.hasAtom() || p.hasNumber()) && !p.hasVariable()){
						antecedent.setType(ClauseType.FACT);
					}else{
						antecedent.setType(ClauseType.RULE);
					}
					Head head = new Head();
					head.setPredicate(p);
					antecedent.setHead(head);
					main.addAntecedent(antecedent);
				}
			}
		}
		return main;
	}

	public Theory createTheory(Theory oldTheory, Example example, String revision, String theoryFileName) throws Exception{
		Theory theory = new Theory();
		if (example.getTypeOfExample().equals(TypeOfExample.POSITIVE)) {
			System.out.println("Adding the revision: " + revision);
			theory.getSClauses().addAll(oldTheory.getSClauses());
			theory.addSClause(revision);
			theory.sortSClauses();
			this.saveTheory(theory, theoryFileName);
			theory.loadNewClause(revision);
		} else {
			if (example.getTypeOfExample().equals(TypeOfExample.NEGATIVE)) {
				System.out.println("Deleting the revision: " + revision);
				for (String oldClause:oldTheory.getSClauses()) {
					if (!oldClause.replaceAll(" ", "").equals(revision.replaceAll(" ", ""))) {
						theory.addSClause(oldClause);
					}
				}
				this.saveTheory(theory, theoryFileName);
				theory.unloadClause(revision);
			} else {
				throw new RuntimeException();
			}
		}
		return theory;
	}

	public void addTheory(Theory theory){
		this.theories.add(theory);
	}
	
	public Theory getBestTheory(){
		Double accuracy = 0.00;
		Theory th = null;
		for (Theory theory:this.theories) {
			if (theory.getAccuracy() > accuracy) {
				accuracy = theory.getAccuracy();
				th = theory;
			}
		}
		return th;
	}

	public Theory getATheory(int index){
		return this.theories.get(index);
	}
	
	public void saveTheory(Theory theory, String theoryFileName) throws Exception{
		File file = new File(theoryFileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for (String clause:theory.getSClauses()) {
			bw.write(clause+System.lineSeparator());
		}
		bw.close();
		fw.close();
		theory.setFileName(theoryFileName);
	}

	public Boolean isLoad(Theory theory){
		return theory.wasLoaded();
	}
	
	public List<Example> generateMisclassifiedExamples(SetOfExamples examples, Theory theory) throws Exception{
		theory.setMisclassifiedExamples(new ArrayList<Example>());
		Boolean result = null;
		for (Example example:examples.getExamples()) {
			try{
				result = Query.hasSolution(example.getInstance());
			}catch(Exception e){
				result = null;
			}
			if(result == null
				|| (example.getTypeOfExample().equals(TypeOfExample.POSITIVE) && !result)
				|| (example.getTypeOfExample().equals(TypeOfExample.NEGATIVE) && result)){
				example.setTypeOfClassification(TypeOfClassification.MISCLASSIFIED);
				theory.getMisclassifiedExamples().add(example);
				this.revisionPointController.generateRevisionPoints(theory, example);
			}else{
				example.setTypeOfClassification(TypeOfClassification.CORRECTLY_CLASSIFIED);
			}
		}
		return theory.getMisclassifiedExamples();
	}

	public void computeAccuracy(SetOfExamples examples, Theory theory){
		double totalNumberOfExamples = 0.00;
		double totalNumberOfExamplesMisclassified = 0.00;
		if (theory.hasMisclassifiedExamples() && examples.hasExamples()) {
			totalNumberOfExamplesMisclassified = theory.getMisclassifiedExamples().size();
			totalNumberOfExamples = examples.getPositiveExamples().size() + examples.getNegativeExamples().size();
	 		theory.computeAccuracy(totalNumberOfExamples, totalNumberOfExamplesMisclassified);
		} else {
			theory.setAccuracy(100.00);
		}
		System.out.printf("Theory's accuracy: %.2f%s%n%n", theory.getAccuracy(),"%");
	}

}
