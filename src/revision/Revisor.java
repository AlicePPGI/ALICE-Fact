/**
 * 
 */
package revision;

import java.io.File;
import java.util.List;

import examples.Example;
import examples.ExampleController;
import examples.SetOfExamples;
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
	private String classFileName = "";
	private String temporaryFileName = "alignment_";
	TheoryController theoryController = TheoryController.getInstance();
	ExampleController exampleController = ExampleController.getInstance();

	public Revisor(String sourceDir, String theoryFileName, String examplesFileName, String classFileName) {
		this.sourceDir = sourceDir;
		this.theoryFileName = theoryFileName;
		this.examplesFileName = examplesFileName;
		this.classFileName = classFileName;
		String[] fileName = theoryFileName.split("\\.");
		this.newTheoryFileName = fileName[0] +"_new." + fileName[1]; 
	}

	public void execute() throws Exception{
		Theory theory = this.createTheory();
		int index = 1;
		int MEIndex = 0;
		boolean wasLoaded = this.theoryController.isLoad(theory);
		if (wasLoaded) {
			SetOfExamples examples = this.exampleController.createExamples(this.sourceDir+"/"+this.examplesFileName);
			List<Example> misclassifiedExamples = this.theoryController.generateMisclassifiedExamples(examples, theory);
			this.theoryController.computeAccuracy(examples, theory);
			boolean loop = misclassifiedExamples.size() > 0;
			boolean tryAgain = false;
			while(loop){
				Example example = misclassifiedExamples.get(0);
				theoryFileName = this.sourceDir + "/" + this.temporaryFileName + index + ".pl";
				index++;
				Theory t = this.theoryController.createTheory(theory, example, "", theoryFileName);
				this.theoryController.addTheory(t);
				wasLoaded = this.theoryController.isLoad(t);
				if(wasLoaded){
					this.theoryController.generateMisclassifiedExamples(examples, t);
					this.theoryController.computeAccuracy(examples, t);
					if (theory.getAccuracy() <= t.getAccuracy()) {
						theory = t;
						tryAgain = true;
					}
				}else{
					throw new RuntimeException("Invalid Theory created!!!");
				}
				if(MEIndex == misclassifiedExamples.size() && theory.getAccuracy() < 100.00 && tryAgain){
					MEIndex = 0;
					misclassifiedExamples = theory.getMisclassifiedExamples();
					tryAgain = false;
				}
				loop = MEIndex < misclassifiedExamples.size();
			}
		}
		Theory finalTheory = this.theoryController.getBestTheory();
		if(finalTheory != null) {
			String originalName = finalTheory.getFileName();
			this.theoryController.saveTheory(theory, this.sourceDir + "/" + this.newTheoryFileName);
			finalTheory.setFileName(originalName);
		}
		
		for(int i = 0; i < this.theoryController.getTheories().size(); i++) {
			if(!this.theoryController.getTheories().get(i).getFileName().contains(this.theoryFileName)){
				File file = new File(this.theoryController.getTheories().get(i).getFileName());
				file.delete();
			}
		}
	}

	private Theory createTheory() throws Exception {
		String name = this.theoryFileName;
		String theoryFileName = this.sourceDir + "/" + name;
		Theory theory = this.theoryController.createTheory(theoryFileName);
		this.theoryController.addTheory(theory);
		return theory;
	}

}
