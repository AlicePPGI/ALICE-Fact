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

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Clause)){
			return false;
		}
		Clause clause = (Clause) obj;
		if(!this.head.equals(clause.getHead())){
			return false;
		}
		boolean result = false;
		for(Antecedent antecedent:this.antecedents){
			for(Antecedent antec:clause.getAntecedents()){
				if(antecedent.equals(antec)){
					result = true;
					break;
				}
			}
			if(!result){
				return result;
			}else{
				result = false;
			}
		}
		return true;
	}

}
