/**
 * 
 */
package theory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	private List<String> dynamicDatabase = new ArrayList<String>();
	
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

	public List<String> getDynamicDatabase() {
		return this.dynamicDatabase;
	}

	public void setDynamicDatabase(List<String> dynamicDatabase) {
		this.dynamicDatabase = dynamicDatabase;
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
	
	public void createDynamicDatabase(List<String> dynamicPredicates) {
		this.dynamicDatabase.addAll(dynamicPredicates);
		for(Clause clause:this.clauses){
			String dynamic = ":- dynamic " + clause.getHead().getPredicate().getFunctor().trim()+"/"+clause.getHead().getPredicate().getAridity()+".";
			if(!this.dynamicDatabase.contains(dynamic)){
				this.dynamicDatabase.add(dynamic);
			}
		}
		this.dynamicDatabase.addAll(this.sClauses);
	}

	public void setLoaded(Boolean loaded){
		this.loaded = loaded;
	}

}
