package cs3246.a2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;

public class ImageDBReader {
	
	List<Image> mImages;

	public ImageDBReader(String db) {
		BufferedReader br; 
	    try {
	    	mImages = new ArrayList<Image>();
	        
	        Gson gson = new Gson();
	        Image[] images = gson.fromJson(new FileReader(db), Image[].class);
	        Collections.addAll(mImages, images);
	        
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public List<Image> getImages() {
		return mImages;
	}

}
