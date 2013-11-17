package cs3246.a2.model;


/**
 * Image entity class
 * @author jason
 *
 */
public class ImageIndex {
	private int mId;
	
	private double[] mSimilarityCCV;
	private double[] mSimilarityHist;
	private double[] mSimilarityEdge;
	

	public ImageIndex(int id, double[] ccv, double[] hist, double[] edge) {
		this.mId = id;
		this.mSimilarityCCV = ccv;
		this.mSimilarityHist = hist;
		this.mSimilarityEdge = edge;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		this.mId = id;
	}

	public double[] getSimilarityCCV() {
		return mSimilarityCCV;
	}

	public void setSimilarityCCV(double[] mSimilarityCCV) {
		this.mSimilarityCCV = mSimilarityCCV;
	}

	public double[] getSimilarityHist() {
		return mSimilarityHist;
	}

	public void setSimilarityHist(double[] mSimilarityHist) {
		this.mSimilarityHist = mSimilarityHist;
	}

	public double[] getSimilarityEdge() {
		return mSimilarityEdge;
	}

	public void setSimilarityEdge(double[] mSimilarityEdge) {
		this.mSimilarityEdge = mSimilarityEdge;
	}
	
	
}
