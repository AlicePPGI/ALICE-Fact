package operators;

import examples.Example;
import examples.ExampleController;
import examples.TypeOfExample;
import solutionsSpace.SolutionsSpace;
import theory.Clause;
import theory.Theory;
import theory.TheoryController;

public class OperatorsController {

	private static final OperatorsController instance = new OperatorsController();
	
	private AddFact addFact = AddFact.getInstance();
	private DeleteFact deleteFact = DeleteFact.getInstaance();
	private ExampleController exampleController = ExampleController.getInstance();
	private TheoryController theoryController = TheoryController.getInstance();

	private OperatorsController() {
		
	}
	
	public static final OperatorsController getInstance() {
		return instance;
	}
	
	public Boolean execute(Example misclassifiedExample, SolutionsSpace solutionsSpace, Theory theory) throws Exception {
		if(misclassifiedExample.getTypeOfClassification().equals(TypeOfExample.POSITIVE)){
			return this.add(misclassifiedExample, solutionsSpace, theory);
		}else{
			return this.delete(misclassifiedExample, theory, solutionsSpace);
		}
	}
	
	private Boolean add(Example misclassifiedExample, SolutionsSpace solutionsSpace, Theory theory) throws Exception {
		String bestSolution = "";
		Double bestAccuracy = theory.getAccuracy();
		Boolean actionResult = Boolean.FALSE;
		Boolean wasImproved = Boolean.FALSE;
		for(String solution:solutionsSpace.getAvailableSolutions()){
			actionResult = this.addFact.execute(solution);
			if(actionResult){
				this.exampleController.generateMisclassifiedExamples();
				if(!this.exampleController.getMisclassifiedExamples().contains(misclassifiedExample)){
					Double solutionAccuracy = this.exampleController.computeAccuracy();
					if(solutionAccuracy > bestAccuracy){
						wasImproved = Boolean.TRUE;
						bestAccuracy = solutionAccuracy;
						solutionsSpace.setAsUsedSolution(solution);
						solutionsSpace.addSolution(bestSolution);
						bestSolution = solution;
					}else{
						actionResult = this.deleteFact.execute(solution);
						this.exampleController.generateMisclassifiedExamples();
						if(!actionResult){
							throw new RuntimeException("Error undoing add clause.");
						}
					}
				}else{
					actionResult = this.deleteFact.execute(solution);
					this.exampleController.generateMisclassifiedExamples();
					if(!actionResult){
						throw new RuntimeException("Error undoing add clause.");
					}
				}
			}else{
				throw new RuntimeException("Error adding new clause.");
			}
		}
		if(wasImproved){
			Clause alignmentClause = this.theoryController.createClause(bestSolution+".");
			theory.addClause(alignmentClause);
			theory.getSClauses().add(bestSolution+".");
			theory.setAccuracy(bestAccuracy);
			theory.sortSClauses();
		}
		return wasImproved;
	}

	private Boolean delete(Example misclassifiedExample, Theory theory, SolutionsSpace solutionsSpace) throws Exception {
		Double theoryAccuracy = theory.getAccuracy();
		Double accuracy = null;
		Boolean actionResult = Boolean.FALSE;
		Boolean wasImproved = Boolean.FALSE;
		for(String clause:theory.getSClauses()){
			for(String functor:solutionsSpace.getFunctors()){
				if(clause.replaceAll(" ", "").startsWith(functor)){
					this.deleteFact.execute(clause.substring(0, clause.length()-1));
					this.exampleController.generateMisclassifiedExamples();
					if(!this.exampleController.getMisclassifiedExamples().contains(misclassifiedExample)){
						accuracy = this.exampleController.computeAccuracy();
						if(accuracy > theoryAccuracy){
							
						}
					}
				}
			}
		}
		return wasImproved;
	}
}
