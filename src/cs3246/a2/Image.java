package cs3246.a2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Image entity class
 * @author jason
 *
 */
public class Image {
	private String mFilename;
	
	private double[] mSimilarityCCV;
	private double[] mSimilarityHist;
	private double[] mSimilarityEdge;
	
	private List<String> mTags;
	private String mTagAll;
	

	public Image(String filename, double[] ccv, double[] hist, double[] edge, String tagAll) {
		this.mFilename = filename;
		this.mSimilarityCCV = ccv;
		this.mSimilarityHist = hist;
		this.mSimilarityEdge = edge;
		setTagAll(tagAll);
	}

	public String getFilename() {
		return mFilename;
	}

	public void setFilename(String mFilename) {
		this.mFilename = mFilename;
	}

	public List<String> getTags() {
		return mTags;
	}

	public void setTags(List<String> mTags) {
		this.mTags = mTags;
		
		String tagAll = "";
		for (String tag : mTags) {
			tagAll += tag + ";";
		}
		
		this.mTagAll = tagAll;
	}

	public String getTagAll() {
		return mTagAll;
	}
	
	public void setTagAll(String tagAll) {
		this.mTagAll = tagAll;
		
		String[] tags = tagAll.split(";");
		this.mTags = new ArrayList<String>();
		Collections.addAll(this.mTags, tags);
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
