package cs3246.a2;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.lang.Math;

/**
 * Sobel operator for edge detection
 * 
 * @author jasonpeng
 * 
 */

public class SobelOperator {
	private BufferedImage mImage;
	private int mSizeX;
	private int mSizeY;
	private double[][] mGradient;
	private double mGradientMax;
	private double[][] mDirection;
	private double[] mFeature;

	private static final int[][] KERNEL_X = new int[][] { { 1, 0, -1 },
			{ 2, 0, -2 }, { 1, 0, -1 } };

	private static final int[][] KERNEL_Y = new int[][] { { 1, 2, 1 },
			{ 0, 0, 0 }, { -1, -2, -1 } };

	/**
	 * @param image
	 *            the RGB image to process
	 */
	public SobelOperator(BufferedImage image) {
		this.mImage = image;
		this.mSizeX = this.mImage.getWidth();
		this.mSizeY = this.mImage.getHeight();
	}

	/**
	 * computes the edge gradients using KERNEL_X and KERNEL_Y
	 * 
	 * @return
	 */
	public void compute() {
		mGradient = new double[mSizeX][mSizeY];
		mDirection = new double[mSizeX][mSizeY];
		int gx, gy;
		int x, y;
		BufferedImage YCbCrImage = Util.RGBToYCbCr(mImage);
		Raster YCbCrRaster = YCbCrImage.getRaster();

		mGradientMax = 0;
		
		// ignore border pixels
		for (x = 1; x < mSizeX - 1; x++) {
			for (y = 1; y < mSizeY - 1; y++) {
				// compute gx and gy for each pixel in Y channel (band 0)
				gx = gy = 0;
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						gx += KERNEL_X[i + 1][j + 1]
								* YCbCrRaster.getSample(x + i, y + j, 0);
						gy += KERNEL_Y[i + 1][j + 1]
								* YCbCrRaster.getSample(x + i, y + j, 0);
					}
				}

				// compute gradient and direction
				mGradient[x][y] = Math.sqrt(gx * gx + gy * gy);
				if (mGradient[x][y] > mGradientMax) {
					mGradientMax = mGradient[x][y];
				}
				
				mDirection[x][y] = Math.atan2(gy, gx);
			}
		}
	}
	
	/**
	 * Normalizes gradient values to between 0 and 1
	 */
	public void normalizeGradient() {
		if (mGradient != null) {			
			for (int i=0; i < mSizeX; i++) {
				for (int j=0; j < mSizeY; j++) {
					mGradient[i][j] = mGradient[i][j] / mGradientMax;
				}
			}
		}
	}
	
	public void quantize() {
		mFeature = new double[64];
		
		for (int i=0; i < mSizeX; i++) {
			for (int j=0; j < mSizeY; j++) {
				int qG = (int) (mGradient[i][j] * 8); // 0..7
				int qD = (int) ((mDirection[i][j] + Math.PI) / (2*Math.PI) * 8); // 0..7
				mFeature[qG + 8 * qD] ++; 
			}
		}
	}
	
	public int getSizeX() {
		return mSizeX;
	}
	
	public int getSizeY() {
		return mSizeY;
	}

	public double[][] getGradient() {
		return mGradient;
	}

	public double[][] getDirection() {
		return mDirection;
	}
	
	public double computeSimilarity(SobelOperator o){
		double similarity = 0;
		return similarity;
	}
	
	public double[] getFeature() {
		return mFeature;
	}

}