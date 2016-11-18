/**
 * 
 */
package examples;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wsantos
 *
 */
public class SetOfExamples {

	private List<Example> examples = new ArrayList<Example>();

	public List<Example> getExamples() {
		return examples;
	}

	public void setExamples(List<Example> examples) {
		this.examples = examples;
	}

	public List<Example> getPositiveExamples(){
		List<Example> positiveExamples = new ArrayList<Example>();
		for(Example example:this.examples){
			if(example.getTypeOfExample().equals(TypeOfExample.POSITIVE)) {
				positiveExamples.add(example);
			}
		}
		return positiveExamples;
	}
	
	public List<Example> getNegativeExamples(){
		List<Example> negativeExamples = new ArrayList<Example>();
		for(Example example:this.examples){
			if(example.getTypeOfExample().equals(TypeOfExample.NEGATIVE)) {
				negativeExamples.add(example);
			}
		}
		return negativeExamples;
	}
	
	public void addExample(Example example){
		this.examples.add(example);
	}
	
	public boolean hasExamples(){
		if(this.examples.size() > 0){
			return true;
		}
		return false;
	}

}
