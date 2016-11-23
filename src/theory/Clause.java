package theory;

import java.util.ArrayList;
import java.util.List;

public class Clause {

	private Head head;
	private List<Clause> antecedents = new ArrayList<Clause>();
	private ClauseType type;

	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public List<Clause> getAntecedents() {
		return antecedents;
	}

	public void setAntecedents(List<Clause> antecedents) {
		this.antecedents = antecedents;
	}

	public ClauseType getType() {
		return type;
	}

	public void setType(ClauseType type) {
		this.type = type;
	}

	public void addAntecedent(Clause antecedent){
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
		for(Clause antecedent:this.antecedents){
			for(Clause antec:clause.getAntecedents()){
				if(antecedent.getHead().equals(antec.getHead())){
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
