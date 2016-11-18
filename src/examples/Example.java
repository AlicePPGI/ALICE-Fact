package examples;

public class Example {

	private String example;
	private TypeOfExample typeOfExample;
	private TypeOfClassification typeOfClassification;

	public Example(String example, TypeOfExample typeOfExample, TypeOfClassification typeOfClassification) {
		this.example = example;
		this.typeOfExample = typeOfExample;
		this.typeOfClassification = typeOfClassification;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
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

}
