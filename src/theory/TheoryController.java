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

/**
 * @author wsantos
 *
 */
public class TheoryController {

	private static final TheoryController instance = new TheoryController();

	private PredicateController predicateController = PredicateController.getInstance();
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
			Clause clause = this.createClause(line);
			theory.addClause(clause);
			line = br.readLine();
		}
		br.close();
		theory.load();
		return theory;
	}

	private Clause createClause(String line) {
		Clause clause = new Clause();
		if(!line.contains(":-")){
			Predicate predicate = this.predicateController.getPredicate(line);
			Head head = new Head();
			head.setPredicate(predicate);
			clause.setHead(head);
			clause.setType(ClauseType.FACT);
		}else{
			List<String> predicates = this.getPredicates(line);
			boolean isHead = true;
			for(String predicate:predicates){
				Predicate p = this.predicateController.getPredicate(predicate);
				clause.setType(ClauseType.RULE);
				if(isHead){
					Head head = new Head();
					head.setPredicate(p);
					clause.setHead(head);
					isHead = false;
				}else{
					Antecedent antecedent = new Antecedent();
					antecedent.setPredicate(p);
					clause.addAntecedent(antecedent);
				}
			}
		}
		return clause;
	}

	private List<String> getPredicates(String clause){
		List<String> predicates = new ArrayList<String>();
		String[] parts = clause.split(":-");
		predicates.add(parts[0].replaceAll(" ", ""));
		char[] characters = parts[1].replaceAll(" ", "").toCharArray();
		StringBuffer sb = new StringBuffer();
		boolean isOpenedParenthesis = false;
		boolean hasComma = false;
		boolean isTheEndOfTheAntecedent = false;
		for(char c:characters){
			switch (c){
				case '(':
					hasComma = false;
					isOpenedParenthesis = true;
					isTheEndOfTheAntecedent = false;
					sb.append(c);
					break;
				case ')':
					hasComma = false;
					isOpenedParenthesis = false;
					isTheEndOfTheAntecedent = true;
					sb.append(c);
					break;
				case ',':
					if(hasComma){
						isTheEndOfTheAntecedent = true;
					}else{
						if(isOpenedParenthesis){
							sb.append(c);
						}else{
							isOpenedParenthesis = false;
							hasComma = true;
						}
					}
					break;
				default:
					sb.append(c);
			}
			if(isTheEndOfTheAntecedent){
				predicates.add(sb.toString());
				isTheEndOfTheAntecedent = false;
				sb.setLength(0);
			}
		}
		return predicates;
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
