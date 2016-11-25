package examples;

import predicate.Predicate;

public class Example {

	private String instance;
	private TypeOfExample typeOfExample;
	private TypeOfClassification typeOfClassification;
	private Predicate predicate;

	public Example(String instance, TypeOfExample typeOfExample, TypeOfClassification typeOfClassification) {
		this.instance = instance;
		this.typeOfExample = typeOfExample;
		this.typeOfClassification = typeOfClassification;
	}

	public String getInstance() {
		return this.instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public TypeOfExample getTypeOfExample() {
		return typeOfExample;
	}

	public void setTypeOfExample(TypeOfExample typeOfExample) {
		this.typeOfExample = typeOfExample;
	}

	public TypeOfClassification getTypeOfClassification() {
		return typeOfClassification;
	}

	public void setTypeOfClassification(TypeOfClassification typeOfClassification) {
		this.typeOfClassification = typeOfClassification;
	}

	public Predicate getPredicate() {
		return predicate;
	}

	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Example)){
			return false;
		}
		Example example = (Example) obj;
		if(this.instance == null || example.getInstance() == null){
			return false;
		}
		return this.instance.equals(example.getInstance());
	}

}
