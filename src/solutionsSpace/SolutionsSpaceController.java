package solutionsSpace;

public class SolutionsSpaceController {

	private static final SolutionsSpaceController instance = new SolutionsSpaceController();
	
	private SolutionsSpace solutionsSpace;
	
	private SolutionsSpaceController() {
		
	}
	
	public static final SolutionsSpaceController getInstance() {
		return instance;
	}

	public SolutionsSpace getSolutionsSpace(String classesFileName, String alignmentPredicateFileName) throws Exception {
		if(this.solutionsSpace != null && (this.solutionsSpace.getAvailableSolutions().size() > 0 
			|| (this.solutionsSpace.getAvailableSolutions().size() == 0 
				&& this.solutionsSpace.getUsedSolutions().size() > 0))){
			return this.solutionsSpace;
		}
		if(classesFileName == null || classesFileName.equals("") || alignmentPredicateFileName == null 
			|| alignmentPredicateFileName.equals("")){
			throw new RuntimeException("Invalid file name. Solutions space can't be built.");
		}
		this.solutionsSpace.loadSolutions(classesFileName, alignmentPredicateFileName);
		return this.solutionsSpace;
	}

}
