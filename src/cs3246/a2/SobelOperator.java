package cs3246.a2;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

/**
 * Sobel operator for edge detection
 * 
 * @author jasonpeng
 * 
 */

public class SobelOperator implements FeatureExtractor {
	private BufferedImage mImage;
	private int mSizeX;
	private int mSizeY;
	
	private double[][] mMatrix;
	private double[] mFeature;

	private static final double GAUSSIAN_COEFFICIENT = 1.0f / 159.0f;
	private static final int[][] GAUSSIAN_5x5 = new int[][] {
		{2, 4, 5, 4, 2},
		{4, 9, 12, 9, 4},
		{5, 12, 15, 12, 5},
		{4, 9, 12, 9, 4},
		{2, 4, 5, 4, 2}
	};
	
	private static final int[][] KERNEL_0 = new int[][] { 
		{ 1, 0, -1 },
		{ 2, 0, -2 },
		{ 1, 0, -1 }
	};
	
	private static final int[][] KERNEL_1 = new int[][] {
		{ 0, -1, -2 },
		{ 1,  0, -1 },
		{ 2,  1,  0 }
	};

	private static final int[][] KERNEL_2 = new int[][] { 
		{ -1, -2, -1 },
		{  0,  0,  0 },
		{  1,  2,  1 }
	};
	
	private static final int[][] KERNEL_3 = new int[][] { 
		{ -2, -1,  0 },
		{ -1,  0,  1 },
		{  0,  1,  2 }
	};

	/**
	 * @param image
	 *            the RGB image to process
	 */
	public SobelOperator() {

	}
	
	/** 
	 * blur image to reduce noise
	 */
	public void applyGaussianFilter() {
		int x, y;
		double[][] gaussian = new double[mSizeX][mSizeY];
		BufferedImage YCbCrImage = Util.RGBToYCbCr(mImage);
		Raster YCbCrRaster = YCbCrImage.getRaster();
		
		for (x=2; x<mSizeX-3; x++) {
			for (y=2; y<mSizeY-3; y++) {
				double sum = 0;
				
				for (int i=-2; i<=2; i++) {
					for (int j=-2; j<2; j++) {
						sum += GAUSSIAN_5x5[i+2][j+2] * YCbCrRaster.getSample(x + i, y + j, 0);
					}
				}
				gaussian[x][y] = sum / GAUSSIAN_COEFFICIENT;
			}
		}
		
		mMatrix = gaussian;
	}
	

	/**
	 * computes the edge gradients on Y channel using kernels
	 * 
	 * @return
	 */
	public void compute() {
		int x, y;
		
		List<int[][]> kernels = new ArrayList<int[][]>();
		kernels.add(KERNEL_0);
		kernels.add(KERNEL_1);
		kernels.add(KERNEL_2);
		kernels.add(KERNEL_3);
		
		// first order feature extraction
		List<double[][]> firstOrderList = new ArrayList<double[][]>();
		for (int[][] kernel : kernels) {
			double[][] firstOrder = new double[mSizeX][mSizeY];
			
			for (x = 1; x < mSizeX - 1; x++) {
				for (y = 1; y < mSizeY - 1; y++) {
					double sum = 0;
					
					for (int i = -1; i <= 1; i++) {
						for (int j = -1; j <= 1; j++) {
							sum += kernel[i+1][j+1]
									* mMatrix[x+i][y+j];
						}
					}
					firstOrder[x][y] = sum < 0 ? -sum : sum; //rectify
				}
			}
			firstOrderList.add(firstOrder);
		}
		
		// second order feature extraction
		List<double[][]> secondOrderList = new ArrayList<double[][]>();
		for (double[][] firstOrder : firstOrderList) {
			for (int[][] kernel : kernels) {
				double[][] secondOrder = new double[mSizeX][mSizeY];
				
				for (x = 1; x < mSizeX - 1; x++) {
					for (y = 1; y < mSizeY - 1; y++) {
						double sum = 0;
						
						for (int i = -1; i <= 1; i++) {
							for (int j = -1; j <= 1; j++) {
								sum += kernel[i+1][j+1]
										* firstOrder[x+i][y+j];
							}
						}
						secondOrder[x][y] = sum < 0 ? -sum : sum; //rectify
					}
				}
				secondOrderList.add(secondOrder);
			}
		}
		
		// third order
		mFeature = new double[kernels.size()*kernels.size()*kernels.size()];
		int fIndex = 0;
		double base = 0;
		for (double[][] secondOrder : secondOrderList) {
			for (int[][] kernel : kernels) {
				for (x = 1; x < mSizeX - 1; x++) {
					for (y = 1; y < mSizeY - 1; y++) {
						double sum = 0;
						
						for (int i = -1; i <= 1; i++) {
							for (int j = -1; j <= 1; j++) {
								sum += kernel[i+1][j+1]
										* secondOrder[x+i][y+j];
							}
						}
						mFeature[fIndex] += sum < 0 ? -sum : sum;
					}
				}
				base += mFeature[fIndex] * mFeature[fIndex];
				fIndex ++;
			}
		}
		
		// normalize vector
		base = Math.sqrt(base);
		for (int i=0; i<fIndex; i++) {
			mFeature[i] = mFeature[i] / base;
		}
	}
	
	public int getSizeX() {
		return mSizeX;
	}
	
	public int getSizeY() {
		return mSizeY;
	}

	@Override
	public double[] getFeature(BufferedImage bi) {
		this.mImage = bi;
		this.mSizeX = this.mImage.getWidth();
		this.mSizeY = this.mImage.getHeight();
		
		applyGaussianFilter();
		compute();

		return mFeature;
	}

	@Override
	public double computeSimilarity(double[] feature, Similarity sim) {
		return sim.compute(feature, mFeature);
	}

}