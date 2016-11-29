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

public class DeleteFact {

	private static final Logger LOGGER = Logger.getLogger(DeleteFact.class);

	private ExampleController exampleController = ExampleController.getInstance();
	private TheoryController theoryController = TheoryController.getInstance();

	public Boolean delete(SolutionsSpace solutionsSpace, Theory theory, AddFact addFact) throws Exception {
		String bestSolution = "";
		Double bestAccuracy = theory.getAccuracy();
		Boolean actionResult = Boolean.FALSE;
		Boolean wasImproved = Boolean.FALSE;
		String clause = "";
		for(int i = 0; i < theory.getSClauses().size(); i++){
			clause = theory.getSClauses().get(i);
			for(int j = 0; j < solutionsSpace.getFunctors().size(); j++){
				if(clause.replaceAll(" ", "").startsWith(solutionsSpace.getFunctors().get(j))){
					List<Example> initialMisclassifiedPositieExamples = this.exampleController.getMisclassifiedPositiveExamples();
					actionResult = this.execute(clause.substring(0, clause.length()-1));
					if(actionResult){
						this.exampleController.generateMisclassifiedExamples();
						List<Example> posMisclassifiedPositiveExamples = this.exampleController.getMisclassifiedPositiveExamples();
						boolean isDisposable = true;
						if(initialMisclassifiedPositieExamples.size() == 0 && posMisclassifiedPositiveExamples.size() == 0){
							isDisposable = false;
						}else{
							if(initialMisclassifiedPositieExamples.containsAll(posMisclassifiedPositiveExamples)){
								isDisposable = false;
							}
						}
						if(!isDisposable){
							Double solutionAccuracy = this.exampleController.computeAccuracy();
							if(solutionAccuracy > bestAccuracy){
								wasImproved = Boolean.TRUE;
								bestAccuracy = solutionAccuracy;
								bestSolution = clause.substring(0, clause.length()-1);
								if(bestAccuracy == 100.00){
									break;
								}
							}
						}
						actionResult = addFact.execute(clause.substring(0, clause.length()-1));
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
				if(!sClause.replaceAll(" ", "").equals(bestSolution+".")){
					newTheory.addSClause(sClause);
				}
			}
			Clause c = this.theoryController.createClause(bestSolution+".");
			for(Clause cls:theory.getClauses()){
				if(!cls.getHead().getPredicate().getFunctor().equals(c.getHead().getPredicate().getFunctor())){
					newTheory.addClause(cls);
				}else{
					if(cls.getHead().getPredicate().getAridity() != c.getHead().getPredicate().getAridity()){
						newTheory.addClause(cls);
					}else{
						boolean achou = true;
						for(int i = 1; i<=cls.getHead().getPredicate().getArguments().size(); i++){
							if(achou){
								achou = false;
								for(int j = 1; j<=c.getHead().getPredicate().getArguments().size(); j++){
									if(cls.getHead().getPredicate().getArguments().get(i).getName().equals(c.getHead().getPredicate().getArguments().get(j).getName())){
										achou = true;
										break;
									}
								}
							}else{
								break;
							}
						}
						if(achou){
							newTheory.addClause(cls);
						}
					}
				}
			}
			this.theoryController.addTheory(newTheory);
			this.execute(bestSolution);
		}
		return wasImproved;
	}

	protected Boolean execute(String clause) {
		LOGGER.info("Deleting clause: "+clause);
		String retractClause = "retract("+clause+")";
		return Query.hasSolution(retractClause); 
	}

}
