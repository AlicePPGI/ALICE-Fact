package predicate;

import java.util.HashMap;
import java.util.Map;

public class Predicate {

	private String name;
	private Map<Integer,Argument> arguments = new HashMap<Integer,Argument>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Predicate)){
			return false;
		}
		Predicate predicate = (Predicate) obj;
		if(this.getAridity() != predicate.getAridity()){
			return false;
		}
		boolean achou = false;
		for(int i=1;i<=this.getAridity();i++){
			Argument argument = this.arguments.get(Integer.valueOf(i));
			if(predicate.getArguments().containsValue(argument)){
				achou = true;
				break;
			}
		}
		if(!achou){
			return false;
		}
		return this.name.replaceAll(" ", "").equals(predicate.getName().replaceAll(" ", ""));
	}

}
