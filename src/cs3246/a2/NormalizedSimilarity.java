package cs3246.a2;

public class NormalizedSimilarity extends Similarity{

	@Override
	double compute(double[] query, double[] document) {
		
		double Sum = 0.0; 
		double querySqr = 0.0;
		double docSqr = 0.0;
		
        for(int i = 0; i < query.length; i++) { 
//        	double distance = Math.abs(query[i] - document[i]);
//        	double max = query[i] > document[i] ? query[i] : document[i];
//        	if (max == 0){
//        		continue;
//        	}
//        	Sum += (query[i] * ( 1 - distance / max)); 
        	
        	Sum += query[i] * document[i];
        	querySqr += query[i] * query[i];
        	docSqr += document[i] * document[i];
        } 
        return Sum / Math.sqrt(docSqr) / Math.sqrt(querySqr); 
        
	}

}
