package cs3246.a2.test;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import com.google.gson.Gson;

import cs3246.a2.Util;

public class GenerateSimilarityMatrix {

	public static void main(String[] args) {
		double[][] yImages = new double[64][3];
		double[][] diff = new double[64][64];
		double[][] sim = new double[64][64];
		
		double m[][] = { 
				{ 0.2989f,	0.5866f, 	0.1145f },
				{ -0.1687f, -0.3312f, 	0.5000f },
				{ 0.5000f, 	-0.4183f, 	-0.0816f }
				};
		
		for (int i=0; i<64; i++) {
			int r, g, b;
			r = (i & 48) << 6;
			g = (i & 12) << 6;
			b = (i & 3)  << 6;
			
			yImages[i][0] = r*m[0][0] + g*m[0][1] + b*m[0][2];
			yImages[i][1] = r*m[1][0] + g*m[1][1] + b*m[1][2];
			yImages[i][2] = r*m[2][0] + g*m[2][1] + b*m[2][2];
		}
		
		double maxDiff = 0;
		for (int i=0; i<64; i++) {
			for (int j=0; j<64; j++) {
				double y1 = yImages[i][0];
				double y2 = yImages[j][0];
				double cb1 = yImages[i][1];
				double cb2 = yImages[j][1];
				double cr1 = yImages[i][2];
				double cr2 = yImages[j][2];
				
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
