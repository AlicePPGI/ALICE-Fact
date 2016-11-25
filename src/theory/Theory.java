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

	private List<String> sClauses = new ArrayList<String>();
	private Double accuracy = null;
	private String fileName = null;
	private Boolean loaded = null;
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

	public void sortSClauses(){
		Collections.sort(this.sClauses);
	}
	
	public void load() {
		System.out.println("Loading the theory.");
		String t = "consult('" + this.getFileName() + "')";
		this.loaded = Query.hasSolution(t);
	}

	public void setLoaded(Boolean loaded){
		this.loaded = loaded;
	}

}
