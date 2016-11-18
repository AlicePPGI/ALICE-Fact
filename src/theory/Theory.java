/**
 * 
 */
package theory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jpl7.Query;

/**
 * @author wsantos
 *
 */
public class Theory {

	private List<String> clauses = new ArrayList<String>();
	private Double accuracy = null;
	private String fileName = null;
	private Boolean loaded = null;
	private List<String> misclassifiedExamples = new ArrayList<String>();
	
	public List<String> getClauses() {
		return clauses;
	}

	public void setClauses(List<String> clauses) {
		this.clauses = clauses;
	}

	public Double getAccuracy(){
		return accuracy;
	}

	public void setAccuracy(Double accuracy){
		this.accuracy = accuracy;
	}

	public void addClause(String clause) {
		this.clauses.add(clause);
	}
	
	public void loadNewClause(String clause) {
/*		String assertClause = "assert("+clause.substring(0, clause.lastIndexOf("."))+")";
		this.loaded = Query.hasSolution(assertClause); */
		this.load();
	}
	
	public void unloadClause(String clause) {
/*		String retractClause = "retract("+clause.substring(0, clause.lastIndexOf("."))+")";
		this.loaded = Query.hasSolution(retractClause); */
		this.load();
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Boolean wasLoaded() {
		if(this.loaded == null){
			return Boolean.FALSE;
		}
		return this.loaded;
	}
	public List<String> getMisclassifiedExamples() {
		return misclassifiedExamples;
	}

	public void setMisclassifiedExamples(List<String> misclassifiedExamples) {
		this.misclassifiedExamples = misclassifiedExamples;
	}

	public Double computeAccuracy(double totalNumberOfExamples, double totalNumberOfExamplesMisclassified){
		this.accuracy = ((totalNumberOfExamples - totalNumberOfExamplesMisclassified) / totalNumberOfExamples) * 100.00;
		return this.accuracy;
	}

	public void sortClauses(){
		Collections.sort(this.clauses);
	}
	
	public boolean hasMisclassifiedExamples(){
		if(this.misclassifiedExamples.size() > 0){
			return true;
		}
		return false;
	}
	
	public void load() {
		System.out.println("Loading the theory.");
		String t = "consult('" + this.getFileName() + "')";
		this.loaded = Query.hasSolution(t);
	}
	
}
