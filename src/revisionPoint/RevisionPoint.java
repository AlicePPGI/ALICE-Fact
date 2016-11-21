package revisionPoint;

public class RevisionPoint implements Comparable<RevisionPoint>{

	private String failure;
	private Integer score;

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

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof RevisionPoint)){
			return false;
		}
		RevisionPoint revisionPoint = (RevisionPoint) obj;
		if(this.failure == null || revisionPoint.getFailure() == null){
			return false;
		}
		return  this.failure.equals(revisionPoint.getFailure());
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
