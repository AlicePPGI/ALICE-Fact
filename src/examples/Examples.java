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
public class Examples {

	private List<String> positiveExamples = new ArrayList<String>();
	private List<String> negativeExamples = new ArrayList<String>();
	
	public List<String> getPositiveExamples(){
		return positiveExamples;
	}
	
	public void setPositiveExamples(List<String> positiveExamples){
		this.positiveExamples = positiveExamples;
	}
	
	public List<String> getNegativeExamples(){
		return negativeExamples;
	}
	
	public void setNegativeExamples(List<String> negativeExamples){
		this.negativeExamples = negativeExamples;
	}
	
	public void addPositiveExample(String positiveExample){
		this.positiveExamples.add(positiveExample);
	}
	
	public void addNegativeExamples(String negativeExample){
		this.negativeExamples.add(negativeExample);
	}

	public boolean hasExamples(){
		if(this.negativeExamples.size() > 0 || this.positiveExamples.size() > 0){
			return true;
		}
		return false;
	}
}
