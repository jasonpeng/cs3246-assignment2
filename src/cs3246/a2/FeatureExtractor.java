package cs3246.a2;

import java.awt.image.BufferedImage;

public interface FeatureExtractor {
	
	double[] getFeature(BufferedImage bi);
	
}
