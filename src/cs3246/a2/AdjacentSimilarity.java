package cs3246.a2;

import java.io.FileReader;

import com.google.gson.Gson;

public class AdjacentSimilarity extends Similarity {
	
	private static double[][] S;

	@Override
	double compute(double[] query, double[] document) {
		double s[][] = getSimilarityMatrix();
		double sum = 0.0; 
        for(int i = 0; i < query.length; i++) { 
        	for (int j = 0; j < query.length; j++){
        		double sim_i = computeNormalizedSimilarity(query[i], document[i]);
        		double sim_j = computeNormalizedSimilarity(query[j], document[j]);
        		
        		sum += sim_i * sim_j * s[i][j];
        	}
        	
        } 
        return sum; 
        
	}
	
	private double[][] getSimilarityMatrix() {
		if (S != null) {
			return S;
		} else {
			Gson gson = new Gson();
			try {
				S = gson.fromJson(new FileReader("colorsimilarity.txt"), double[][].class);
			} catch (Exception e) {
				
			}
			return S;
		}
	}
	
	private double computeNormalizedSimilarity(double a, double b){
		double result = 0; 
		
		double distance = Math.abs(a - b);
    	double max = a > b ? a : b;
    	if (max == 0){
    		return 0;
    	}
    	return a * ( 1 - distance / max); 
	}
	
}
