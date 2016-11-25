package operators;

import examples.ExampleController;
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
	
	public Boolean execute(SolutionsSpace solutionsSpace, Theory theory) throws Exception{
		Boolean resultPositive = Boolean.TRUE;
		Boolean resultNegative = Boolean.TRUE;
		if(this.exampleController.hasMisclassifiedPositiveExamples()){
			resultPositive = this.add(solutionsSpace, theory);
		}
		if(this.exampleController.hasMisclassifiedNegativeExamples()){
			resultNegative = this.delete(solutionsSpace, theory);
		}
		return resultPositive && resultNegative;
	}

	private Boolean add(SolutionsSpace solutionsSpace, Theory theory) throws Exception {
		String bestSolution = "";
		Double bestAccuracy = theory.getAccuracy();
		Boolean actionResult = Boolean.FALSE;
		Boolean wasImproved = Boolean.FALSE;
		String solution = "";
		for(int i=0; i<solutionsSpace.getAvailableSolutions().size();i++){
			solution = solutionsSpace.getAvailableSolutions().get(i);
			if(!theory.getSClauses().contains(solution)){
				actionResult = this.addFact.execute(solution);
				if(actionResult){
					this.exampleController.generateMisclassifiedExamples();
					Double solutionAccuracy = this.exampleController.computeAccuracy();
					if(solutionAccuracy > bestAccuracy){
						wasImproved = Boolean.TRUE;
						bestAccuracy = solutionAccuracy;
						bestSolution = solution;
						if(bestAccuracy == 100.00){
							break;
						}
					}
					actionResult = this.deleteFact.execute(solution);
					if(!actionResult){
						throw new RuntimeException("Error undoing add clause.");
					}
				}else{
					throw new RuntimeException("Error adding new clause.");
				}
			}
		}
		if(wasImproved){
			solutionsSpace.setAsUsedSolution(bestSolution);
			solutionsSpace.getAvailableSolutions().remove(bestSolution);
			Clause alignmentClause = this.theoryController.createClause(bestSolution+".");
			Theory newTheory = new Theory();
			newTheory.setAccuracy(bestAccuracy);
			newTheory.setLoaded(wasImproved);
			String[] parts = bestSolution.split("\\(");
			boolean wasInserted = false;
			for(String clause:theory.getSClauses()){
				if(clause.replaceAll(" ", "").startsWith(parts[0]) && !wasInserted){
					newTheory.addSClause(bestSolution+".");
					wasInserted = true;
				}
				newTheory.addSClause(clause);
			}
			if(!wasInserted){
				newTheory.addSClause(bestSolution+".");
			}
			wasInserted = false;
			for(Clause clause:theory.getClauses()){
				if(clause.getHead().getPredicate().getFunctor().replaceAll(" ", "").startsWith(parts[0]) && !wasInserted){
					newTheory.addClause(alignmentClause);
					wasInserted = true;
				}
				newTheory.addClause(clause);
			}
			if(!wasInserted){
				newTheory.addClause(alignmentClause);
				wasInserted = true;
			}
			this.theoryController.addTheory(newTheory);
		}
		this.addFact.execute(bestSolution);
		return wasImproved;
	}

	private Boolean delete(SolutionsSpace solutionsSpace, Theory theory) throws Exception {
		String bestSolution = "";
		Double bestAccuracy = theory.getAccuracy();
		Boolean actionResult = Boolean.FALSE;
		Boolean wasImproved = Boolean.FALSE;
		String clause = "";
		for(int i = 0; i < theory.getSClauses().size(); i++){
			clause = theory.getSClauses().get(i);
			for(int j = 0; j < solutionsSpace.getFunctors().size(); j++){
				if(clause.replaceAll(" ", "").startsWith(solutionsSpace.getFunctors().get(j))){
					actionResult = this.deleteFact.execute(clause.substring(0, clause.length()-1));
					if(actionResult){
						this.exampleController.generateMisclassifiedExamples();
						Double solutionAccuracy = this.exampleController.computeAccuracy();
						if(solutionAccuracy > bestAccuracy){
							wasImproved = Boolean.TRUE;
							bestAccuracy = solutionAccuracy;
							bestSolution = clause;
							if(bestAccuracy == 100.00){
								break;
							}
						}
						actionResult = this.addFact.execute(clause.substring(0, clause.length()-1));
						if(!actionResult){
							throw new RuntimeException("Error undoing delete clause.");
						}
					}else{
						throw new RuntimeException("Error adding new clause.");
					}
				}
			}
		}
		if(wasImproved){
			Theory newTheory = new Theory();
			newTheory.setAccuracy(bestAccuracy);
			newTheory.setLoaded(wasImproved);
			for(String sClause:theory.getSClauses()){
				if(!sClause.replaceAll(" ", "").equals(bestSolution)){
					newTheory.addSClause(sClause);
				}
			}
			Clause c = this.theoryController.createClause(bestSolution);
			for(Clause cls:theory.getClauses()){
				if(!cls.equals(c)){
					newTheory.addClause(cls);
				}
			}
			this.theoryController.addTheory(newTheory);
		}
		this.addFact.execute(bestSolution);
		return wasImproved;
	}

}
