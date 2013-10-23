package cs3246.a2;

import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public final class Util {
	public static BufferedImage RGBToYCbCr(BufferedImage image) {
		float matrix3[][] = { 
				{ 0.2989f,	0.5866f, 	0.1145f },
				{ -0.1687f, -0.3312f, 	0.5000f },
				{ 0.5000f, 	-0.4183f, 	-0.0816f }
				};
		
		BandCombineOp combine;
		if (image.getRaster().getNumBands() == 3) {
			combine = new BandCombineOp(matrix3, null);
		
			Raster RGBRaster = image.getRaster();
			WritableRaster YCbCrRaster = RGBRaster.createCompatibleWritableRaster();
			combine.filter(RGBRaster, YCbCrRaster);
	
			return new BufferedImage(image.getColorModel(), YCbCrRaster, true, null);
		} else {
			return image;
		}
	}
}
