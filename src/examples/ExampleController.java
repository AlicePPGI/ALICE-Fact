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
	
	public Examples createExamples(String fileName) throws Exception{
		Examples examples = new Examples();
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
	
	private void insertExample(Examples examples, int content, String example){
		switch (content){
			case 1:
				examples.addPositiveExample(example);
				break;
			case 2:
				examples.addNegativeExamples(example);
		}
	}
}
