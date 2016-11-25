package operators;

import org.jpl7.Query;

public class DeleteFact {

	private static final DeleteFact instance = new DeleteFact();
	
	private DeleteFact() {
		
	}
	
	public static final DeleteFact getInstaance() {
		return instance;
	}
	
	public Boolean execute(String clause) {
		String retractClause = "retract("+clause+")";
		return Query.hasSolution(retractClause); 
	}

}
