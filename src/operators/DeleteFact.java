package operators;

import org.apache.log4j.Logger;
import org.jpl7.Query;

public class DeleteFact {

	private static final Logger LOGGER = Logger.getLogger(DeleteFact.class);
	private static final DeleteFact instance = new DeleteFact();
	
	private DeleteFact() {
		
	}
	
	public static final DeleteFact getInstaance() {
		return instance;
	}
	
	public Boolean execute(String clause) {
		LOGGER.info("Deleting clause: "+clause);
		String retractClause = "retract("+clause+")";
		return Query.hasSolution(retractClause); 
	}

}
