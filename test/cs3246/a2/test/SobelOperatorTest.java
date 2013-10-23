package cs3246.a2.test;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import cs3246.a2.SobelOperator;

public class SobelOperatorTest {

	public static void main(String[] args) {
		try {
			BufferedImage image = ImageIO.read(new File("image/test.jpg"));
			SobelOperator sobel = new SobelOperator(image);
			sobel.compute();
			double[][] gradient = sobel.getGradient();
			int x = sobel.getSizeX();
			int y = sobel.getSizeY();
			BufferedImage imageConverted = new BufferedImage(x, y, BufferedImage.TYPE_BYTE_GRAY);
			for (int i=0; i<x; i++) {
				for (int j=0; j<y; j++) {
					imageConverted.setRGB(i, j, (int)gradient[i][j]);
				}
			}
			ImageIO.write(imageConverted, "bmp", new File("image/edge.bmp"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
