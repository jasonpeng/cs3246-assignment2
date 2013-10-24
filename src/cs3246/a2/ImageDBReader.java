package cs3246.a2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class ImageDBReader {
	
	List<Image> mImages;

	public ImageDBReader(String db) {
		BufferedReader br; 
	    try {
	    	mImages = new ArrayList<Image>();
	    	
	    	br = new BufferedReader(new FileReader(db));
	        String line = br.readLine();
	        Gson gson = new Gson();
	        
	        while (line != null) {
	        	Image image = gson.fromJson(line, Image.class);
	        	
	        	mImages.add(image);
	        	
	            line = br.readLine();
	        }
	        br.close();
	        
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public List<Image> getImages() {
		return mImages;
	}

}
