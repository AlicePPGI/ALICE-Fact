/**
 * 
 */
package revision;

import java.util.List;

import examples.ExampleController;
import examples.Examples;
import theory.Theory;
import theory.TheoryController;

/**
 * @author wsantos
 *
 */
public class Revisor {
	
	private String sourceDir = "d:/Usuarios/010447310388/Documents/Prolog";
	private String theoryFileName = "alignment_revision.pl";
	private String examplesFileName = "examples.dat";
	private String theoryBaseFileName = "alignments_";
	TheoryController theoryController = TheoryController.getInstance();
	ExampleController exampleController = ExampleController.getInstance();

	public void execute() throws Exception{
		String name = this.theoryFileName;
		String theoryFileName = this.sourceDir + "/" + name;
		Theory theory = this.theoryController.createTheory(theoryFileName);
		this.theoryController.addTheory(theory);
		int index = 1;
		int MEIndex = 0;
		boolean wasLoaded = this.theoryController.isLoad(theory);
		if (wasLoaded) {
			Examples examples = this.exampleController.createExamples(this.sourceDir+"/"+this.examplesFileName);
			this.theoryController.generateMisclassifiedExamples(examples, theory);
			this.theoryController.computeAccuracy(examples, theory);
			List<String> faults = theory.getMisclassifiedExamples();
			boolean loop = MEIndex < faults.size();
			boolean tryAgain = false;
			while(loop){
				List<String> list = this.theoryController.getNextFact(faults.get(MEIndex));
				MEIndex++;
				String action = list.get(0);
				String fact = list.get(1);
				theoryFileName = this.sourceDir + "/" + this.theoryBaseFileName + index + ".pl";
				index++;
				Theory t = this.theoryController.createTheory(theory, fact, action, theoryFileName);
				this.theoryController.addTheory(t);
				wasLoaded = this.theoryController.isLoad(t);
				if(wasLoaded){
					this.theoryController.generateMisclassifiedExamples(examples, t);
					this.theoryController.computeAccuracy(examples, t);
					list.clear();
					if (theory.getAccuracy() <= t.getAccuracy()) {
						theory = t;
						tryAgain = true;
					}
				}else{
					throw new RuntimeException("Invalid Theory created!!!");
				}
				if(MEIndex == faults.size() && theory.getAccuracy() < 100.00 && tryAgain){
					MEIndex = 0;
					faults = theory.getMisclassifiedExamples();
					tryAgain = false;
				}
				loop = MEIndex < faults.size();
			}
		}
	}

}
