package theory;

import predicate.Predicate;

public class Head {

	private Predicate predicate;

	public Predicate getPredicate() {
		return predicate;
	}

	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Head)){
			return false;
		}
		Head head = (Head) obj;
		return this.predicate.equals(head.getPredicate());
	}

}
