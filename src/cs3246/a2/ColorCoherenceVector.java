package cs3246.a2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;


public class ColorCoherenceVector implements FeatureExtractor{

	public  BufferedImage img;
	public  int[] originalImage;
	public  int[] currentImage;
	public  int[][]  colorTagged;
	public  int numOfDifferentAreas;
	public  double[] alpha;
	public  double[] beta;
	private  final double u = 0.9;
	private  final double t = 0.00000001;
	private final int THRESHOLD = 15;
	private  final static String FILE_PATH = "./server/image/";

	
	/**
	 * We first blur the image slightly by Gaussian Filter.
	 * This eliminates small variations between neighboring pixels.
	 * Algorithm from: http://en.wikipedia.org/wiki/Gaussian_blur and 
	 * http://d.hatena.ne.jp/nowokay/touch/20081007
	 * @param w
	 * @param h
	 */
	private  void applyGaussianFilter(int w, int h){		
		
		// The pre-defined Gaussian Filter
		int[][] GaussianFilter = {
            {1, 2, 1},
            {2, 4, 2},
            {1, 2, 1}};
		
		for(int x = 0; x < w; x++){
        	for(int y = 0; y < h; y++){
                
        		int tr = 0;
                int tg = 0;
                int tb = 0;
                int t = 0;
                
                for(int i = -1; i < 2; i++){
                    for(int j = -1; j < 2; j++){
                        
                    	if(y + i < 0 || x + j < 0 || y + i >= h || x + j >= w) continue;
                        
                        t += GaussianFilter[i + 1][j + 1];
                        int currentAddress = (x + j) + (y + i) * w;
                        int currentGFilter = GaussianFilter[i + 1][j + 1];
                        tr += currentGFilter * ((originalImage[currentAddress] >> 16) & 255);
                        tg += currentGFilter * ((originalImage[currentAddress] >> 8)  & 255);
                        tb += currentGFilter * ( originalImage[currentAddress]        & 255);
                    }
                }
                currentImage[x + y * w] = ((tr / t) << 16) + ((tg / t) << 8) + tb / t;
            }
        }
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, w, h, currentImage, 0, w);  
	}
	
	private  void reduceColor(){
		// 192 in binary: 11000000
		int flag = 192;
		
		for(int i = 0; i < currentImage.length; ++i){
            int r = (currentImage[i] >> 16) & flag;
            int g = (currentImage[i] >> 8) & flag;
            int b = currentImage[i] & flag;
            currentImage[i] = (r << 16) + (g << 8) + b;
        }
	}
	
	private  void tagColor(int w, int h){

        colorTagged = new int[h][w];
        numOfDifferentAreas = 0;
        for(int row = 0; row < h; row++){
            for(int column = 0; column < w; column++){
                int color = currentImage[row * w + column];
                if(row > 0){
                    // Same with the upper-left
                    if(column > 0){
                        if(currentImage[(row - 1) * w + column - 1] == color){
                            colorTagged[row][column] = colorTagged[row - 1][column - 1];
                            continue;
                        }
                    }
                    // Same with the above
                    if(currentImage[(row - 1) * w + column] == color){
                        colorTagged[row][column] = colorTagged[row - 1][column];
                        continue;
                    }
                    // Same with the upper-right
                    if(column < w - 1){
                        if(currentImage[(row - 1) * w + column + 1] == color){
                            colorTagged[row][column] = colorTagged[row - 1][column + 1];
                            continue;
                        }
                    }
                }
                // Same with the left
                if(column > 0){
                    if(currentImage[row * w + column - 1] == color){
                        colorTagged[row][column] = colorTagged[row][column - 1];
                        continue;
                    }
                }
                colorTagged[row][column] = numOfDifferentAreas;
                numOfDifferentAreas++;
            }
        }
	}
	
	private  void computeCoherence(int w, int h){
		int[] count = new int[numOfDifferentAreas];
        int[] color = new int[numOfDifferentAreas];
        for(int x = 0; x < h; x++){
            for(int y = 0; y < w; y++){
                count[colorTagged[x][y]]++;
                color[colorTagged[x][y]] = currentImage[x * w + y];
            }
        }
        
        alpha = new double[64];
        beta = new double[64];
        
        for(int i = 0; i < numOfDifferentAreas; ++i){
        	// d: current color, represented in 24-bit RGB
            int d = color[i];
            // Convert d to 6-bit RGB, ranging from 0 to 63
            color[i] = (((d >> 22) & 3) << 4) + (((d >> 14) & 3) << 2) + ((d >> 6) & 3);
            
            if(count[i] < t * w * h || count[i] < THRESHOLD){
                beta[color[i]] += count[i];
                //beta[color[i]] ++;
            }else{
                alpha[color[i]] += count[i];
                //alpha[color[i]] ++;
            }
        }
	}
	
	public  void computeCCV(String fileName) throws IOException{
		BufferedImage imgsrc = ImageIO.read(new File(fileName));
		computeCCV(imgsrc);
	}
	
	public  void computeCCV(BufferedImage imgsrc) throws IOException{

        
        int w = imgsrc.getWidth();
        int h = imgsrc.getHeight();
        
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D grp = (Graphics2D) img.getGraphics();
        grp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        grp.drawImage(imgsrc, 0, 0, w, h, null);
        grp.dispose();
        
		originalImage = img.getRGB(0, 0, w, h, null, 0, w);
        currentImage = new int[originalImage.length];
        
        // Gaussian Filter
        applyGaussianFilter(w, h);
        //applyAverageFilter(w, h);
        
        // Color Reduction
        reduceColor();
        
        // Tagging
        tagColor(w, h);
        
        // Aggregate
        computeCoherence(w, h);
        
        // Normalize
        for (int i = 0; i < alpha.length; i++){
        	if(alpha[i] == 0 && beta[i] == 0) continue;
        	alpha[i] /= h * w;
        	beta[i] /= h * w;
        }
        
        // Remove white colour (background colour)
        alpha[63] = 0;
        beta[63] = 0;
	}
	
	public  double similarityMeasure(double[] alphaQuery, double[] alphaDoc, double[] betaQuery, double[] betaDoc){
		double alphaSim = computeNormalizedSimilarity(alphaQuery, alphaDoc);
		double betaSim = computeNormalizedSimilarity(betaQuery, betaDoc);

		return u * alphaSim + (1 - u) * betaSim;
	}
	
	public  double computeNormalizedSimilarity(double[] arr1, double[] arr2){
		double result = 0;
		
		for(int i = 0; i < arr1.length; i++){
			if(arr1[i] == 0 && arr1[i] == 0) continue;
			
			double difference = Math.abs( arr1[i] - arr2[i] );
			double max = (arr1[i] > arr2[i]) ? arr1[i] : arr2[i];
			result += arr1[i] * (1 - difference / max );
	    }
		
		return result;
	}
	
	@Override
	public double[] getFeature(BufferedImage bi) {
		try{
			computeCCV(bi);
		} catch (Exception e){
			System.out.println("Error when getFeature from ColorCoherenceVector");
			e.printStackTrace();
		}
		double[] result = new double[128];
		
		for(int i = 0; i < 64; i++){
			result[i] = alpha[i];
		}
		for(int i = 64; i < 128; i++){
			result[i] = beta[i - 64];
		}
		return result;
	}
	
	@Override
	public double computeSimilarity(double[] document, Similarity sim) {
		double alphaInDoc[] = new double[64];
		double betaInDoc[] = new double[64];
		
		for (int i = 0; i < 64; i++){
			alphaInDoc[i] = document[i];
		}
		for (int j = 64; j < 128; j++){
			betaInDoc[j - 64] = document[j];
		}
		double alphaSim = sim.compute(alpha, alphaInDoc);
		double betaSim = sim.compute(beta, betaInDoc);
		
		return u * alphaSim + (1 - u) * betaSim;	
	}
	
    public static void main(String[] args) throws IOException{
    	
    	// Test the result of two images:
    	ArrayList<String> list = new ArrayList<String>();
    	
//    	list.add("64.jpg");
//    	list.add("108.jpg");
//    	list.add("1.jpg");
//    	list.add("398.jpg");
//    	list.add("34.jpg");
//    	list.add("116.jpg");
//    	
//    	list.add("30.jpg");
//    	list.add("158.jpg");
//    	list.add("147.jpg");
//    	list.add("141.jpg");
//    	list.add("248.jpg");
//
//    	String file1;
//    	String file2 = "23.jpg";
    	
    	
    	list.add("27.jpg");
//    	list.add("120.jpg");
//    	list.add("30.jpg");
//    	list.add("34.jpg");
//    	list.add("101.jpg");
//    	list.add("1.jpg");
//    	
//    	list.add("110.jpg");
//    	list.add("108.jpg");
//    	list.add("149.jpg");
//    	list.add("86.jpg");
//    	list.add("83.jpg");

    	String file1;
    	String file2 = "26.jpg";
    	
    	for (int i = 0; i < 1; i++){
    		file1 = list.get(i);
    		ColorCoherenceVector vec = new ColorCoherenceVector();
        	vec.computeCCV(FILE_PATH + file1);
        	
        	ColorCoherenceVector vec2 = new ColorCoherenceVector();
        	double[] document = vec2.getFeature(ImageIO.read(new File(FILE_PATH + file2)));
    		
    		double result = vec.computeSimilarity(document, new NormalizedSimilarity());
        	System.out.println("Result for " + file1 + ": "  + result);
    	}
    }
}