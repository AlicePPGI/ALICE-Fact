package theory;

import predicate.Predicate;

public class Antecedent {

	private Predicate predicate;
	
	public Predicate getPredicate() {
		return predicate;
	}

	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Antecedent)){
			return false;
		}
		Antecedent antecedent = (Antecedent) obj;
		return this.predicate.equals(antecedent.getPredicate());
	}

}
