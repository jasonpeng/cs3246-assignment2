package cs3246.a2.test;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import com.google.gson.Gson;

import cs3246.a2.Util;

public class GenerateSimilarityMatrix {

	public static void main(String[] args) {
		BufferedImage[] yImages = new BufferedImage[64];
		double[][] diff = new double[64][64];
		double[][] sim = new double[64][64];
		
		for (int i=0; i<64; i++) {
			int rgb;
			int r, g, b;
			r = (i & 48) << 6;
			g = (i & 12) << 6;
			b = (i & 3)  << 6;
			rgb = (r << 16) | (g << 8) | b;
			BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			bi.setRGB(0, 0, rgb);
			yImages[i] = Util.RGBToYCbCr(bi);
		}
		
		double maxDiff = 0;
		for (int i=0; i<64; i++) {
			for (int j=0; j<64; j++) {
				Raster raster1 = yImages[i].getRaster();
				Raster raster2 = yImages[j].getRaster();
				int y1 = raster1.getSample(0, 0, 0);
				int y2 = raster2.getSample(0, 0, 0);
				int cb1 = raster1.getSample(0, 0, 1);
				int cb2 = raster2.getSample(0, 0, 1);
				int cr1 = raster1.getSample(0, 0, 2);
				int cr2 = raster2.getSample(0, 0, 2);
				
				double sum = 1.4*sqr(y1-y2) + 0.8*sqr(cb1-cb2) + 0.8*(cr1-cr2);
				if (sum < 0) {
					sum = 0;
				}
				diff[i][j] = Math.sqrt(sum);
				if (diff[i][j] > maxDiff) {
					maxDiff = diff[i][j];
				}
			}
		}
		
		for (int i=0; i<64; i++) {
			for (int j=0; j<64; j++) {
				sim[i][j] = 1 - diff[i][j] / maxDiff;
			}
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(sim);
		System.out.println(json);
	}
	
	private static double sqr(double x) {
		return x * x;
	}

}
