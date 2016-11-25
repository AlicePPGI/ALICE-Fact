package solutionsSpace;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class SolutionsSpace {

	private List<String> availableSolutions = new ArrayList<String>();
	private List<String> usedSolutions = new ArrayList<String>();
	private List<String> dynamicPredicates = new ArrayList<String>();
	private List<String> functors = new ArrayList<String>();

	public List<String> getAvailableSolutions() {
		return this.availableSolutions;
	}

	public void setAvailableSolutions(List<String> availableSolutions) {
		this.availableSolutions = availableSolutions;
	}

	public List<String> getUsedSolutions() {
		return this.usedSolutions;
	}

	public void setUsedSolutions(List<String> usedSolutions) {
		this.usedSolutions = usedSolutions;
	}

	public List<String> getDynamicPredicates() {
		return this.dynamicPredicates;
	}

	public void setDynamicPredicates(List<String> dynamicPredicates) {
		this.dynamicPredicates = dynamicPredicates;
	}

	public List<String> getFunctors() {
		return this.functors;
	}

	public void setFunctors(List<String> functors) {
		this.functors = functors;
	}

	public Boolean addSolution(String solution) {
		if(solution == null || solution.equals("")) {
			return Boolean.FALSE;
		}
		this.availableSolutions.add(solution);
		return Boolean.TRUE;
	}
	
	public Boolean setAsUsedSolution(String solution) {
		if(solution == null || solution.equals("")){
			return Boolean.FALSE;
		}
		if(!this.availableSolutions.contains(solution)){
			return Boolean.FALSE;
		}
		this.usedSolutions.add(solution);
		this.availableSolutions.remove(solution);
		return Boolean.TRUE;
	}

	public String getASolution(Integer index){
		if(index == null || index < 0 || index > (this.availableSolutions.size()-1)){
			return null;
		}
		return this.availableSolutions.get(index);
	}

	public void loadSolutions(String classesFileName, String alignmentFunctorFileName) throws Exception {
		this.setAlignmentFunctorNames(alignmentFunctorFileName);
		List<String> pairOfClasses = this.getPairOfClasses(classesFileName);
		for(String functor:functors){
			for(String pairOfClass:pairOfClasses){
				this.availableSolutions.add(functor+"("+pairOfClass+")");
			}
		}
	}

	private void setAlignmentFunctorNames(String alignmentFunctorFileName) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(alignmentFunctorFileName));
		String line = br.readLine();
		while(line != null){
			if(!line.equals("")){
				this.dynamicPredicates.add(":- dynamic "+ line + "/2.");
				this.functors.add(line);
			}
			line = br.readLine();
		}
		br.close();
		if(this.functors.size() == 0){
			throw new RuntimeException("Predicate file is empty.");
		}
	}

	private List<String> getPairOfClasses(String classesFileName) throws Exception {
		List<String> pairOfClasses = new ArrayList<String>();
		List<String> classes = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(classesFileName));
		String line = br.readLine();
		while(line != null){
			if(!line.equals("")){
				classes.add(line);
			}
			line = br.readLine();
		}
		br.close();
		if(classes.size() == 0){
			throw new RuntimeException("Classes file is empty.");
		}
		for(String clazz:classes){
			for(String clzz:classes){
				if(!clazz.equals(clzz)){
					pairOfClasses.add(clazz+","+clzz);
				}
			}
		}
		return pairOfClasses;
	}

}
