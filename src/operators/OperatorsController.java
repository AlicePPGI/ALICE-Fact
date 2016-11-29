package operators;

import examples.ExampleController;
import solutionsSpace.SolutionsSpace;
import theory.Theory;
import theory.TheoryController;

public class OperatorsController {

	private static final OperatorsController instance = new OperatorsController();
	
	private AddFact addFact = new AddFact();
	private DeleteFact deleteFact = new DeleteFact();
	private ExampleController exampleController = ExampleController.getInstance();
	private TheoryController theoryController = TheoryController.getInstance();

	private OperatorsController() {
		
	}
	
	public static final OperatorsController getInstance() {
		return instance;
	}
	
	public Boolean execute(SolutionsSpace solutionsSpace) throws Exception{
		Boolean resultPositive = Boolean.TRUE;
		Boolean resultNegative = Boolean.TRUE;
		if(this.exampleController.hasMisclassifiedPositiveExamples()){
			Theory theory = this.theoryController.getBestTheory();
			resultPositive = this.addFact.add(solutionsSpace, theory, this.deleteFact);
			this.exampleController.generateMisclassifiedExamples();
		}
		if(this.exampleController.hasMisclassifiedNegativeExamples()){
			Theory theory = this.theoryController.getBestTheory();
			resultNegative = this.deleteFact.delete(solutionsSpace, theory, this.addFact);
			this.exampleController.generateMisclassifiedExamples();
		}
		return resultPositive && resultNegative;
	}

}
