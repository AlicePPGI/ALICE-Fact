/**
 * 
 */
package examples;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @author wsantos
 *
 */
public class ExampleController {

	private static final ExampleController instance = new ExampleController();

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
	
	private void insertExample(SetOfExamples examples, int content, String example){
		switch (content){
			case 1:
				Example positiveExample = new Example(example, TypeOfExample.POSITIVE, TypeOfClassification.UNKNOW);
				examples.addExample(positiveExample);
				break;
			case 2:
				Example negativeExample = new Example(example, TypeOfExample.NEGATIVE, TypeOfClassification.UNKNOW);
				examples.addExample(negativeExample);
		}
	}
}
