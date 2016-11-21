/**
 * 
 */
package theory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jpl7.Query;

import examples.Example;

/**
 * @author wsantos
 *
 */
public class Theory {

	private List<String> sClauses = new ArrayList<String>();
	private Double accuracy = null;
	private String fileName = null;
	private Boolean loaded = null;
	private List<Example> misclassifiedExamples = new ArrayList<Example>();
	private List<Clause> clauses = new ArrayList<Clause>();
	
	public List<String> getSClauses() {
		return this.sClauses;
	}

	public void setSClauses(List<String> sClauses) {
		this.sClauses = sClauses;
	}

	public Double getAccuracy(){
		return accuracy;
	}

	public void setAccuracy(Double accuracy){
		this.accuracy = accuracy;
	}

	public void addSClause(String sClause) {
		if(sClause != null){
			this.sClauses.add(sClause);
		}
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

	public List<Example> getMisclassifiedExamples() {
		return this.misclassifiedExamples;
	}

	public void setMisclassifiedExamples(List<Example> misclassifiedExamples) {
		this.misclassifiedExamples = misclassifiedExamples;
	}

	public List<Clause> getClauses() {
		return clauses;
	}

	public void setClauses(List<Clause> clauses) {
		this.clauses = clauses;
	}

	public void addClause(Clause clause) {
		if(clause != null){
			this.clauses.add(clause);
		}
	}

	public Double computeAccuracy(double totalNumberOfExamples, double totalNumberOfExamplesMisclassified){
		this.accuracy = ((totalNumberOfExamples - totalNumberOfExamplesMisclassified) / totalNumberOfExamples) * 100.00;
		return this.accuracy;
	}

	public void sortSClauses(){
		Collections.sort(this.sClauses);
	}
	
	public boolean hasMisclassifiedExamples(){
		if(this.misclassifiedExamples.size() > 0){
			return true;
		}
		return false;
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

	public void load() {
		System.out.println("Loading the theory.");
		String t = "consult('" + this.getFileName() + "')";
		this.loaded = Query.hasSolution(t);
	}
	
}
