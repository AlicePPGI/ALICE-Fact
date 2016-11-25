/**
 * 
 */
package revision;

import java.util.List;

import examples.ExampleController;
import operators.OperatorsController;
import solutionsSpace.SolutionsSpace;
import solutionsSpace.SolutionsSpaceController;
import theory.Theory;
import theory.TheoryController;

/**
 * @author wsantos
 *
 */
public class Revisor {
	
	private String sourceDir = "";
	private String theoryFileName = "";
	private String examplesFileName = "";
	private String newTheoryFileName = "";
	private String classesFileName = "";
	private String alignmentFunctorFileName = "";
	private TheoryController theoryController = TheoryController.getInstance();
	private ExampleController exampleController = ExampleController.getInstance();
	private SolutionsSpaceController solutionsSpaceController = SolutionsSpaceController.getInstance();
	private OperatorsController operatorsController = OperatorsController.getInstance();

	public Revisor(String sourceDir, String theoryFileName, String examplesFileName, String classesFileName, String alignmentPredicateFileName) {
		this.sourceDir = sourceDir;
		this.theoryFileName = theoryFileName;
		this.examplesFileName = examplesFileName;
		this.classesFileName = classesFileName;
		this.alignmentFunctorFileName = alignmentPredicateFileName;
		String[] fileName = theoryFileName.split("\\.");
		this.newTheoryFileName = fileName[0] +"_new." + fileName[1]; 
	}

	public void execute() throws Exception{
		SolutionsSpace solutionsSpace = this.solutionsSpaceController.getSolutionsSpace(this.sourceDir+"/"+this.classesFileName, this.sourceDir+"/"+this.alignmentFunctorFileName);
		Theory theory = this.createTheory(solutionsSpace.getDynamicPredicates());
		int MEIndex = 0;
		int maximumNumberOfAttempts = 100;
		boolean wasLoaded = this.theoryController.isLoad(theory);
		if (wasLoaded) {
			this.exampleController.createExamples(this.sourceDir+"/"+this.examplesFileName);
			this.exampleController.generateMisclassifiedExamples();
			theory.setAccuracy(this.exampleController.computeAccuracy());
			boolean loop = this.exampleController.getMisclassifiedExamples().size() > 0;
			while(loop){
				Boolean wasImproved = this.operatorsController.execute(solutionsSpace, theory);
				if(wasImproved){
					theory = this.theoryController.getBestTheory();
					wasLoaded = this.theoryController.isLoad(theory);
					if(wasLoaded){
						this.exampleController.generateMisclassifiedExamples();
						MEIndex = 0;
						theory.setAccuracy(this.exampleController.computeAccuracy());
					}else{
						throw new RuntimeException("Invalid Theory created!!!");
					}
				}else{
					MEIndex++;
				}
				if(MEIndex > this.exampleController.getMisclassifiedExamples().size()
					|| this.exampleController.getMisclassifiedExamples().size() == 0
					|| theory.getAccuracy() == 100.00 || maximumNumberOfAttempts == 0){
					loop = false;
				}
				maximumNumberOfAttempts--;
			}
		}else{
			throw new RuntimeException("Invalid initial Theory!!!");
		}
		Theory finalTheory = this.theoryController.getBestTheory();
		if(finalTheory != null) {
			this.theoryController.saveTheory(theory, this.sourceDir + "/" + this.newTheoryFileName);
		}		
	}

	private Theory createTheory(List<String> dynamicPredicates) throws Exception {
		String theoryFileName = this.sourceDir + "/" + this.theoryFileName;
		Theory theory = this.theoryController.createTheory(theoryFileName, dynamicPredicates);
		this.theoryController.addTheory(theory);
		return theory;
	}

}
