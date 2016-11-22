package theory;

import java.util.ArrayList;
import java.util.List;

public class Clause {

	private Head head;
	private List<Antecedent> antecedents = new ArrayList<Antecedent>();
	private ClauseType type;

	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public List<Antecedent> getAntecedents() {
		return antecedents;
	}

	public void setAntecedents(List<Antecedent> antecedents) {
		this.antecedents = antecedents;
	}

	public ClauseType getType() {
		return type;
	}

	public void setType(ClauseType type) {
		this.type = type;
	}

	public void addAntecedent(Antecedent antecedent){
		if(antecedent != null){
			this.antecedents.add(antecedent);
		}
	}

}
