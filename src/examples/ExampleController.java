/**
 * 
 */
package examples;

import java.io.BufferedReader;
import java.io.FileReader;

import predicate.Predicate;
import predicate.PredicateController;

/**
 * @author wsantos
 *
 */
public class ExampleController {

	private static final ExampleController instance = new ExampleController();
	
	private PredicateController predicateController = PredicateController.getInstance();

	private ExampleController(){
		
	}
	
	public static final ExampleController getInstance(){
		return instance;
	}
	
	public SetOfExamples createExamples(String fileName) throws Exception{
		SetOfExamples examples = new SetOfExamples();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = br.readLine();
		int content = 0;
		while (line != null) {
			if (!line.contains("(")) {
				content = this.validateSession(line);
			} else {
				this.insertExample(examples, content, line);
			}
			line = br.readLine();
		}
		br.close();
		return examples;
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

}
