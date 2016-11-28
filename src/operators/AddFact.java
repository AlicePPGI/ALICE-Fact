package operators;

import org.apache.log4j.Logger;
import org.jpl7.Query;

public class AddFact {

	private static final Logger LOGGER = Logger.getLogger(AddFact.class);
	private static final AddFact instance = new AddFact();
	
	private AddFact() {
		
	}
	
	public static final AddFact getInstance() {
		return instance;
	}
	
	public Boolean execute(String solution) {
		LOGGER.info("Adding clause: "+solution);
		String assertSolution = "asserta("+solution+")";
		return Query.hasSolution(assertSolution); 
	}

}
