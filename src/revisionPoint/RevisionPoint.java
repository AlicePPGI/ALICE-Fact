package revisionPoint;

import java.util.ArrayList;
import java.util.List;

import examples.Example;
import theory.Clause;

public class RevisionPoint implements Comparable<RevisionPoint>{

	private String failure;
	private Integer score;
	private Clause clause;
	private List<Example> misclassifiedExamples = new ArrayList<Example>();

	public String getFailure() {
		return failure;
	}

	public void setFailure(String failure) {
		this.failure = failure;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Clause getClause() {
		return clause;
	}

	public void setClause(Clause clause) {
		this.clause = clause;
	}

	public List<Example> getMisclassifiedExamples() {
		return this.misclassifiedExamples;
	}

	public void setMisclassifiedExamples(List<Example> misclassifiedExamples) {
		this.misclassifiedExamples = misclassifiedExamples;
	}

	public void addMisclassifiedExample(Example example) {
		if(example != null){
			this.misclassifiedExamples.add(example);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof RevisionPoint)){
			return false;
		}
		RevisionPoint revisionPoint = (RevisionPoint) obj;
		if(this.failure == null || revisionPoint.getFailure() == null){
			return false;
		}
		return  this.failure.equals(revisionPoint.getFailure()) && this.clause.equals(revisionPoint.getClause());
	}

	@Override
	public String toString() {
		if(this.failure == null){
			return "";
		}
		return this.failure;
	}

	@Override
	public int compareTo(RevisionPoint revisionPoint) {
		if(revisionPoint == null || revisionPoint.getScore() == null){
			return -1;
		}
		if(this.score == null){
			return 1;
		}
		if(this.score < revisionPoint.getScore()){
			return -1;
		}
		if(this.score > revisionPoint.getScore()){
			return 1;
		}
		return 0;
	}
	
}
