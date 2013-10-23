package cs3246.a2;

import java.lang.Math;

/**
 * Sobel operator for edge detection
 * 
 * @author jasonpeng
 *
 */

class SobelOperator {
  private byte[][] mMatrix;
  private int mSizeX;
  private int mSizeY;
  private double[][] mGradient;
  private double[][] mDirection;
  
  
  private static final int[][] KERNEL_X = new int[][]{
    { 1,  0, -1 },
    { 2,  0, -2 },
    { 1,  0, -1 }
  };
  
  private static final int[][] KERNEL_Y = new int[][]{
    { 1,  2,  1 },
    { 0,  0,  0 },
    {-1, -2, -1 }
  };
  
  /**
   * @param imageMatrix the luminance matrix of the image
   * @param sizeX x dimension (left to right) of the image
   * @param sizeY y dimension (up to down)    of the image
   */
  public SobelOperator(byte[][] imageMatrix, int sizeX, int sizeY) {
    this.mMatrix = imageMatrix;
    this.mSizeX = sizeX;
    this.mSizeY = sizeY;
  }
  
  /**
   * computes the edge gradients using KERNEL_X and KERNEL_Y
   * @return 
   */
  public void compute() {
    mGradient = new double[mSizeX][mSizeY];
    mDirection = new double[mSizeX][mSizeY];
    int gx, gy;
    int x, y;
    
    // ignore border pixels
    for (x = 1; x < mSizeX - 1; x++) {
      for (y = 1; y < mSizeY - 1; y++) {
        // compute gx and gy for each pixel
        gx = gy = 0;
        for (int i = -1; i <= 1; i++) {
          for (int j = -1; j <= 1; j++) {
            gx += KERNEL_X[i+1][j+1] * mMatrix[x+i][y+j];
            gy += KERNEL_Y[i+1][j+1] * mMatrix[x+i][y+j];
          }
        }
        
        // compute gradient and direction
        mGradient[x][y] = Math.sqrt(gx * gx + gy * gy);
        mDirection[x][y] = Math.atan2(gy, gx);
      }
    }
    
  }
}