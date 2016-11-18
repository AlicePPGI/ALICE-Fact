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
import examples.TypeOfExample;

/**
 * @author wsantos
 *
 */
public class TheoryController {

	private static final TheoryController instance = new TheoryController();
	
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
			theory.addClause(line);
			line = br.readLine();
		}
		br.close();
		theory.load();
		return theory;
	}

	public Theory createTheory(Theory oldTheory, Example example, String revision, String theoryFileName) throws Exception{
		Theory theory = new Theory();
		if (example.getTypeOfExample().equals(TypeOfExample.POSITIVE)) {
			System.out.println("Adding the revision: " + revision);
			theory.getClauses().addAll(oldTheory.getClauses());
			theory.addClause(revision);
			theory.sortClauses();
			this.saveTheory(theory, theoryFileName);
			theory.loadNewClause(revision);
		} else {
			if (example.getTypeOfExample().equals(TypeOfExample.NEGATIVE)) {
				System.out.println("Deleting the revision: " + revision);
				for (String oldClause:oldTheory.getClauses()) {
					if (!oldClause.replaceAll(" ", "").equals(revision.replaceAll(" ", ""))) {
						theory.addClause(oldClause);
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
		for (String clause:theory.getClauses()) {
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
		for (Example example:examples.getExamples()) {
			if (!Query.hasSolution(example.getExample())) {
				theory.getMisclassifiedExamples().add(example);
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
