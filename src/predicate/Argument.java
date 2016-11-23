package predicate;

public class Argument {

	private String name;
	private ArgumentType type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArgumentType getType() {
		return type;
	}

	public void setType(ArgumentType type) {
		this.type = type;
	}

	public Boolean isAtom() {
		if(this.type != null && this.type.equals(ArgumentType.ATOM)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public Boolean isVariable() {
		if(this.type != null && this.type.equals(ArgumentType.VARIABLE)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean isNumber() {
		if(this.type != null && this.type.equals(ArgumentType.NUMBER)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

}
