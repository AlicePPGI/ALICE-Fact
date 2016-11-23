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

	public List<String> getPredicates(String clause){
		List<String> predicates = new ArrayList<String>();
		String[] parts = clause.split(":-");
		predicates.add(parts[0].replaceAll(" ", ""));
		char[] characters = parts[1].replaceAll(" ", "").toCharArray();
		StringBuffer sb = new StringBuffer();
		boolean isOpenedParenthesis = false;
		boolean hasComma = false;
		boolean isTheEndOfTheAntecedent = false;
		for(char c:characters){
			switch (c){
				case '(':
					hasComma = false;
					isOpenedParenthesis = true;
					isTheEndOfTheAntecedent = false;
					sb.append(c);
					break;
				case ')':
					hasComma = false;
					isOpenedParenthesis = false;
					isTheEndOfTheAntecedent = true;
					sb.append(c);
					break;
				case ',':
					if(hasComma){
						isTheEndOfTheAntecedent = true;
					}else{
						if(isOpenedParenthesis){
							sb.append(c);
						}else{
							isOpenedParenthesis = false;
							hasComma = true;
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
