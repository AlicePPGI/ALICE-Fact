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

import org.apache.log4j.Logger;
import org.jpl7.Query;

import predicate.Predicate;
import predicate.PredicateController;

/**
 * @author wsantos
 *
 */
public class TheoryController {

	private static final Logger LOGGER = Logger.getLogger(TheoryController.class); 
	private static final TheoryController instance = new TheoryController();

	private PredicateController predicateController = PredicateController.getInstance();
	private List<Theory> theories = new ArrayList<Theory>();
	private String dynamicDatabaseName = "dynamicDatabase.pl";
	
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

	public Theory createTheory(String fileName, List<String> dynamicPredicates) throws Exception{
		Theory theory = new Theory();
		theory.setFileName(fileName);
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = br.readLine();
		while (line != null) {
			theory.addSClause(line);
			Clause clause = this.createClause(line.replaceAll(" ", ""));
			theory.addClause(clause);
			line = br.readLine();
		}
		br.close();
		theory.createDynamicDatabase(dynamicPredicates);
		return theory;
	}

	public Clause createClause(String line) {
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

	public Boolean loadDynamicDatabase(Theory theory, String sourceDir) throws Exception {
		String databaseName = sourceDir+"/"+this.dynamicDatabaseName;
		this.saveDynamicDatabase(theory, databaseName);
		return Query.hasSolution("consult('" + databaseName + "')");
	}

	private void saveDynamicDatabase(Theory theory, String theoryFileName) throws Exception {
		this.saveFile(theory.getDynamicDatabase(), theoryFileName);
	}

	public void saveTheory(Theory theory, String theoryFileName) throws Exception {
		this.saveFile(theory.getSClauses(), theoryFileName);
		theory.setFileName(theoryFileName);
	}

	private void saveFile(List<String> clauses, String fileName) throws Exception {
		File file = new File(fileName);
		if(!file.exists()){
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for(String clause:clauses){
			bw.write(clause+System.lineSeparator());
		}
		bw.close();
		fw.close();
	}

	public Boolean isLoad(Theory theory){
		return theory.wasLoaded();
	}

	public void deleteDynamicDataase(String sourceDir) {
		File file = new File(sourceDir+"/"+this.dynamicDatabaseName);
		if(file.exists()){
			if(file.delete()){
				LOGGER.info("Temporary dynamic database was deleted.");
			}else{
				LOGGER.info("Temporary dynamic database wasn't deleted.");
			}
		}
	}

}
