package cs3246.a2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;

public class ImageDBWriter {
	
	List<Image> mImages;
	String mOutFile;

	public ImageDBWriter(List<Image> images, String outFile) {
		mImages = images;
		mOutFile = outFile;
	}
	
	public void write() {
		Gson gson = new Gson();
		
		String json = gson.toJson(mImages);
		
		try {
			FileWriter writer = new FileWriter(mOutFile);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
