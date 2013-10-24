package cs3246.a2;

public class AdjacentSimilarity extends Similarity {

	@Override
	double compute(double[] query, double[] document) {
		
		double sum = 0.0; 
        for(int i = 0; i < query.length; i++) { 
        	for (int j = 0; j < query.length; j++){
        		double sim_i = computeNormalizedSimilarity(query[i], document[i]);
        		double sim_j = computeNormalizedSimilarity(query[j], document[j]);
        		
        		sum += sim_i * sim_j * 1;
        	}
        	
        } 
        return sum; 
        
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
