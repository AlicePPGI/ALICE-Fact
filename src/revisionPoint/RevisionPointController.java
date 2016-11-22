package revisionPoint;

import java.util.ArrayList;
import java.util.List;

import examples.Example;
import theory.Clause;
import theory.Theory;

public class RevisionPointController {

	private static final RevisionPointController instance = new RevisionPointController();

	private List<RevisionPoint> revisionPoints = new ArrayList<RevisionPoint>();

	private RevisionPointController() {
		
	}
	
	public static final RevisionPointController getInstance() {
		return instance;
	}

	public List<RevisionPoint> generateRevisionPoints(Theory theory, Example misclassifiedExample) {
		List<Clause> clauses = this.identifyClausesWithFailures(theory, misclassifiedExample);
		return this.revisionPoints;
	}

	public List<RevisionPoint> getRevisionPoints() {
		return revisionPoints;
	}

	private List<Clause> identifyClausesWithFailures(Theory theory, Example misclassifiedExample) {
		List<Clause> clauses = new ArrayList<Clause>();
		for(Clause clause:theory.getClauses()){
			if(clause.getHead().getPredicate().getName().replaceAll(" ", "").equals(misclassifiedExample.getPredicate().getName().replaceAll(" ", ""))){
				clauses.add(clause);
			}
		}
		return clauses;
	}
	
	private void createRevisionPoints(List<String> failures, Clause clause, Example misclassifiedExample) {
		boolean achou;
		for(String failure:failures){
			achou = false;
			for(RevisionPoint rp:this.revisionPoints){
				if(rp.getFailure().replaceAll(" ", "").equals(failure.replaceAll(" ", ""))
						&& rp.getClause().equals(clause)){
					rp.addMisclassifiedExample(misclassifiedExample);
					rp.setScore(rp.getMisclassifiedExamples().size());
					achou = true;
				}
			}
			if(!achou){
				RevisionPoint revisionPoint = new RevisionPoint();
				revisionPoint.setFailure(failure);
				revisionPoint.setClause(clause);
				revisionPoint.addMisclassifiedExample(misclassifiedExample);
				revisionPoint.setScore(revisionPoint.getMisclassifiedExamples().size());
				this.revisionPoints.add(revisionPoint);
			}
		}
	}

}
