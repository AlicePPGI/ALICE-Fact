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

import examples.Examples;

/**
 * @author wsantos
 *
 */
public class TheoryController {

	public static final String ADD_FACT = "ADD";
	public static final String DELETE_FACT = "DELETE";

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

	public Theory createTheory(Theory oldTheory, String fact, String action, String theoryFileName) throws Exception{
		Theory theory = new Theory();
		if (action.equals(ADD_FACT)) {
			theory.getClauses().addAll(oldTheory.getClauses());
			theory.addClause(fact);
			theory.sortClauses();
			this.saveTheory(theory, theoryFileName);
			theory.loadNewClause(fact);
		} else {
			if (action.equals(DELETE_FACT)) {
				for (String oldClause:oldTheory.getClauses()) {
					if (!oldClause.equals(fact)) {
						theory.addClause(oldClause);
					}
				}
				this.saveTheory(theory, theoryFileName);
				theory.unloadClause(fact);
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
	
	public void generateMisclassifiedExamples(Examples examples, Theory theory) throws Exception{
		theory.getMisclassifiedExamples().clear();
		for (String example:examples.getPositiveExamples()) {
			if (!Query.hasSolution(example)) {
				theory.getMisclassifiedExamples().add("+"+example);
			}
		}
		for (String example:examples.getNegativeExamples()) {
			if (Query.hasSolution(example)) {
				theory.getMisclassifiedExamples().add("-"+example);
			}
		}
	}

	public void computeAccuracy(Examples examples, Theory theory){
		double totalNumberOfExamples = 0.00;
		double totalNumberOfExamplesMisclassified = 0.00;
		if (theory.hasMisclassifiedExamples() && examples.hasExamples()) {
			totalNumberOfExamplesMisclassified = theory.getMisclassifiedExamples().size();
			totalNumberOfExamples = examples.getPositiveExamples().size() + examples.getNegativeExamples().size();
	 		theory.computeAccuracy(totalNumberOfExamples, totalNumberOfExamplesMisclassified);
		} else {
			theory.setAccuracy(100.00);
		}
	}

	public List<String> getNextFact(String fault) throws Exception{
		List<String> strings = new ArrayList<String>();
		if(fault.replace(" ", "").length() > 4){
			if (fault.substring(0, 1).equals("+")){
				strings.add(ADD_FACT);
			} else {
				strings.add(DELETE_FACT);
			}
			strings.add(fault.substring(1));
		}
		return strings;
	}

}
