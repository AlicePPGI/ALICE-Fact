/**
 * 
 */
package examples;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.jpl7.Query;

import predicate.Predicate;
import predicate.PredicateController;

/**
 * @author wsantos
 *
 */
public class ExampleController {

	private static final ExampleController instance = new ExampleController();
	
	private PredicateController predicateController = PredicateController.getInstance();

	private List<Example> misclassifiedExamples = new ArrayList<Example>();
	private SetOfExamples setOfExamples;

	private ExampleController(){
		
	}
	
	public static final ExampleController getInstance(){
		return instance;
	}

	public List<Example> getMisclassifiedExamples() {
		return this.misclassifiedExamples;
	}

	public SetOfExamples createExamples(String fileName) throws Exception{
		if(this.setOfExamples == null){
			this.setOfExamples = new SetOfExamples();
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = br.readLine();
			int content = 0;
			while (line != null) {
				if (!line.contains("(")) {
					content = this.validateSession(line);
				} else {
					this.insertExample(this.setOfExamples, content, line);
				}
				line = br.readLine();
			}
			br.close();
		}
		return this.setOfExamples;
	}

	public SetOfExamples getSetOfExamples() {
		return this.setOfExamples;
	}

	private int validateSession(String line){
		if (line.contains("[positive examples]")) {
			return 1;
		} else {
			if (line.contains("[negative examples]")) {
				return 2;
			} else {
				return 0;
			}
		}
	}
	
	private void insertExample(SetOfExamples examples, int content, String instance){
		Predicate predicate = this.predicateController.getPredicate(instance);
		switch (content){
			case 1:
				Example positiveExample = new Example(instance, TypeOfExample.POSITIVE, TypeOfClassification.UNKNOW);
				positiveExample.setPredicate(predicate);
				examples.addExample(positiveExample);
				break;
			case 2:
				Example negativeExample = new Example(instance, TypeOfExample.NEGATIVE, TypeOfClassification.UNKNOW);
				negativeExample.setPredicate(predicate);
				examples.addExample(negativeExample);
		}
	}

	public List<Example> generateMisclassifiedExamples() throws Exception{
		Boolean result = null;
		if(this.misclassifiedExamples.size() > 0){
			this.misclassifiedExamples.clear();
		}
		for (Example example:this.setOfExamples.getExamples()) {
			try{
				result = Query.hasSolution(example.getInstance());
			}catch(Exception e){
				result = null;
			}
			if(result == null
				|| (example.getTypeOfExample().equals(TypeOfExample.POSITIVE) && !result)
				|| (example.getTypeOfExample().equals(TypeOfExample.NEGATIVE) && result)){
				example.setTypeOfClassification(TypeOfClassification.MISCLASSIFIED);
				this.misclassifiedExamples.add(example);
			}else{
				example.setTypeOfClassification(TypeOfClassification.CORRECTLY_CLASSIFIED);
			}
		}
		return this.misclassifiedExamples;
	}

	public Double computeAccuracy(){
		double totalNumberOfExamples = 0.00;
		double totalNumberOfExamplesMisclassified = 0.00;
		Double accuracy = null;
		if (this.misclassifiedExamples.size() > 0 && this.setOfExamples.getExamples().size() > 0) {
			totalNumberOfExamplesMisclassified = this.misclassifiedExamples.size();
			totalNumberOfExamples = this.setOfExamples.getExamples().size();
			accuracy = ((totalNumberOfExamples - totalNumberOfExamplesMisclassified)/totalNumberOfExamples) * 100.00;
			System.out.printf("Theory's accuracy: %.2f%s%n%n", accuracy,"%");
	 		return accuracy;
		} else {
			if(this.setOfExamples.getExamples().size() > 0){
				accuracy = 100.00;
				System.out.printf("Theory's accuracy: %.2f%s%n%n", accuracy,"%");
				return accuracy;
			}else{
				System.out.println("There aren't examples");
				return null;
			}
		}
	}

	public boolean hasMisclassifiedPositiveExamples() {
		if(this.misclassifiedExamples == null || this.misclassifiedExamples.size() == 0){
			return false;
		}
		for(Example example:this.misclassifiedExamples){
			if(example.getTypeOfExample().equals(TypeOfExample.POSITIVE)){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasMisclassifiedNegativeExamples() {
		if(this.misclassifiedExamples == null || this.misclassifiedExamples.size() == 0){
			return false;
		}
		for(Example example:this.misclassifiedExamples){
			if(example.getTypeOfExample().equals(TypeOfExample.NEGATIVE)){
				return true;
			}
		}
		return false;
	}

}
