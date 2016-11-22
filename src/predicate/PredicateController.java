package predicate;

public class PredicateController {

	private static final PredicateController instance = new PredicateController();
	
	private PredicateController(){
		
	}
	
	public static final PredicateController getInstance() {
		return instance;
	}
	
	public Predicate getPredicate(String predicate) {
		if(predicate == null || predicate.equals("")){
			return null;
		}
		String[] parts = predicate.split("\\(");
		Predicate p = new Predicate();
		p.setName(parts[0]);
		String[] args = parts[1].split("\\)");
		String[] arguments = args[0].split(",");
		Integer index =1;
		Boolean wasAdded;
		for(String arg:arguments){
			Argument argument = new Argument();
			argument.setName(arg);
			argument.setType(this.getArgumentType(arg));
			wasAdded = p.addArgument(index, argument);
			if(!wasAdded){
				throw new RuntimeException("Argument didn't add!!!");
			}
			index++;
		}
		return p;
	}

	private ArgumentType getArgumentType(String argument) {
		String initial = argument.substring(0, 1);
		try{
			Integer.valueOf(argument);
			return ArgumentType.NUMBER;
		}catch(Exception e){
			if(initial.equals(initial.toUpperCase())){
				return ArgumentType.VARIABLE;
			}else{
				return ArgumentType.ATOM;
			}
		}
	}

}
