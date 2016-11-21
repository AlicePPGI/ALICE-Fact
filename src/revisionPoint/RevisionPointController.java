package revisionPoint;

import java.util.List;

import examples.Example;
import theory.Theory;

public class RevisionPointController {

	private static final RevisionPointController instance = new RevisionPointController();
	
	private RevisionPointController() {
		
	}
	
	public static final RevisionPointController getInstance() {
		return instance;
	}
	public List<RevisionPoint> generateRevisionPoints(Theory theory, List<Example> misclassifiedExamples) {
		for(Example example:misclassifiedExamples){
			String predicateName = example.getPredicate().getName();
		}
		return null;
	}

}
