package predicate;

import java.util.ArrayList;
import java.util.List;

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
		p.setFunctor(parts[0]);
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

	public List<String> getPredicates(String clause){
		List<String> predicates = new ArrayList<String>();
		String[] parts = clause.split(":-");
		predicates.add(parts[0].replaceAll(" ", ""));
		char[] characters = parts[1].replaceAll(" ", "").toCharArray();
		StringBuffer sb = new StringBuffer();
		boolean isTheEndOfTheAntecedent = false;
		int numberOfParenthesis = 0;
		for(char c:characters){
			switch (c){
				case '(':
					isTheEndOfTheAntecedent = false;
					sb.append(c);
					numberOfParenthesis++;
					break;
				case ')':
					sb.append(c);
					numberOfParenthesis--;
					if(numberOfParenthesis == 0){
						isTheEndOfTheAntecedent = true;
					}
					break;
				case ',':
					if(numberOfParenthesis > 0){
						sb.append(c);
					}else{
						if(sb.length()>0){
							isTheEndOfTheAntecedent = true;
						}
					}
					break;
				default:
					sb.append(c);
			}
			if(isTheEndOfTheAntecedent){
				predicates.add(sb.toString());
				isTheEndOfTheAntecedent = false;
				sb.setLength(0);
			}
		}
		if(sb.length() > 0 && !sb.toString().equals(".")){
			predicates.add(sb.toString());
			sb.setLength(0);
		}
		return predicates;
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
