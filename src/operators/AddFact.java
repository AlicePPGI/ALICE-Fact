package operators;

import java.util.List;

import org.apache.log4j.Logger;
import org.jpl7.Query;

import examples.Example;
import examples.ExampleController;
import solutionsSpace.SolutionsSpace;
import theory.Clause;
import theory.Theory;
import theory.TheoryController;

public class AddFact {

	private static final Logger LOGGER = Logger.getLogger(AddFact.class);

	private ExampleController exampleController = ExampleController.getInstance();
	private TheoryController theoryController = TheoryController.getInstance();

	public Boolean add(SolutionsSpace solutionsSpace, Theory theory, DeleteFact deleteFact) throws Exception {
		String bestSolution = "";
		Double bestAccuracy = theory.getAccuracy();
		Boolean actionResult = Boolean.FALSE;
		Boolean wasImproved = Boolean.FALSE;
		String solution = "";
		boolean wasInserted = false;
		for(int i=0; i<solutionsSpace.getAvailableSolutions().size();i++){
			solution = solutionsSpace.getAvailableSolutions().get(i);
			if(!theory.getSClauses().contains(solution+".")){
				List<Example> initialMisclassifiedNegativeExamples = this.exampleController.getMisclassifiedNegativeExamples();
				actionResult = this.execute(solution);
				if(actionResult){
					this.exampleController.generateMisclassifiedExamples();
					List<Example> posMisclassifiedNegativeExamples = this.exampleController.getMisclassifiedNegativeExamples();
					boolean isDisposable = true;
					if(initialMisclassifiedNegativeExamples.size() == 0 && posMisclassifiedNegativeExamples.size() == 0){
						isDisposable = false;
					}else{
						if(initialMisclassifiedNegativeExamples.containsAll(posMisclassifiedNegativeExamples)){
							isDisposable = false;
						}
					}
					if(!isDisposable){
						Double solutionAccuracy = this.exampleController.computeAccuracy();
						if(solutionAccuracy > bestAccuracy){
							wasImproved = Boolean.TRUE;
							bestAccuracy = solutionAccuracy;
							bestSolution = solution;
							if(bestAccuracy == 100.00){
								break;
							}
						}
					}
					actionResult = deleteFact.execute(solution);
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
		if(wasInserted){
			this.execute(bestSolution);
		}
		return wasImproved;
	}

	protected Boolean execute(String solution) {
		LOGGER.info("Adding clause: "+solution);
		String assertSolution = "asserta("+solution+")";
		return Query.hasSolution(assertSolution); 
	}

}
