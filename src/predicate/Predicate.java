package predicate;

import java.util.HashMap;
import java.util.Map;

public class Predicate {

	private String functor;
	private Map<Integer,Argument> arguments = new HashMap<Integer,Argument>();

	public String getFunctor() {
		return this.functor;
	}

	public void setFunctor(String functor) {
		this.functor = functor;
	}

	public Map<Integer, Argument> getArguments() {
		return arguments;
	}

	public void setArguments(Map<Integer, Argument> arguments) {
		this.arguments = arguments;
	}

	public Integer getAridity() {
		return this.arguments.size();
	}
	
	public Boolean addArgument(Integer index, Argument argument) {
		if(this.arguments.containsKey(index) || (this.arguments.containsValue(argument) && !argument.getType().equals(ArgumentType.NUMBER))){
			return Boolean.FALSE;
		}
		this.arguments.put(index, argument);
		return Boolean.TRUE;
	}

	public Boolean hasAtom() {
		Boolean achou = Boolean.FALSE;
		for(int i = 1; i<=this.arguments.size();i++){
			Argument argument = this.arguments.get(Integer.valueOf(i));
			if(argument.isAtom()){
				achou = Boolean.TRUE;
				break;
			}
		}
		return achou;
	}

	public Boolean hasVariable() {
		Boolean achou = Boolean.FALSE;
		for(int i = 1; i<=this.arguments.size();i++){
			Argument argument = this.arguments.get(Integer.valueOf(i));
			if(argument.isVariable()){
				achou = Boolean.TRUE;
				break;
			}
		}
		return achou;
	}

	public Boolean hasNumber() {
		Boolean achou = Boolean.FALSE;
		for(int i = 1; i<=this.arguments.size();i++){
			Argument argument = this.arguments.get(Integer.valueOf(i));
			if(argument.isNumber()){
				achou = Boolean.TRUE;
				break;
			}
		}
		return achou;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Predicate)){
			return false;
		}
		Predicate predicate = (Predicate) obj;
		if(this.getAridity() != predicate.getAridity()){
			return false;
		}
		return this.functor.replaceAll(" ", "").equals(predicate.getFunctor().replaceAll(" ", ""));
	}

}
