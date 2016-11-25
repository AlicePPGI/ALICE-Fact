package operators;

import org.jpl7.Query;

public class AddFact {

	private static final AddFact instance = new AddFact();
	
	private AddFact() {
		
	}
	
	public static final AddFact getInstance() {
		return instance;
	}
	
	public Boolean execute(String solution) {
		String assertSolution = "asserta("+solution+")";
		return Query.hasSolution(assertSolution); 
	}

}
