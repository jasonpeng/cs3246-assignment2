package cs3246.a2.test;

import java.util.ArrayList;
import java.util.List;

import cs3246.a2.Image;
import cs3246.a2.ImageDBReader;
import cs3246.a2.ImageDBWriter;

public class ImageReaderWriterTest {

	public static void main(String[] args) {
		List<Image> images = new ArrayList<Image>();
		Image image1 = new Image("001.jpg", new double[] {0f, 1f, 0f}, new double[] {1f, 0f, 0f}, new double[] {0f, 0f, 1f}, "tag1 tag2");
		images.add(image1);
		ImageDBWriter writer = new ImageDBWriter(images, "test/db.txt");
		writer.write();
		
		ImageDBReader reader = new ImageDBReader("test/db.txt");
		List<Image> resultImages = reader.getImages();
		Image image2 = resultImages.get(0);
		System.out.println("Image name: " + image2.getFilename());
	}
}
