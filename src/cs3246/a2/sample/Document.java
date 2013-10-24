package cs3246.a2.sample;

public class Document implements Comparable<Document> {
	
	private String fileName;
	private double score;
	
	public double getScore(){
		return score;
	}
	
	public String getFileName(){
		return fileName;
	}
	
	public Document(String fileName, double score){
		this.fileName = fileName;
		this.score = score;
	}
	
	@Override
	public int compareTo(Document another) {	
		double difference = this.score - another.getScore();
		if (difference > 0){
			return -1;
		} else if (difference == 0){
			return 0;
		} else{
			return 1;
		}
	}

}
