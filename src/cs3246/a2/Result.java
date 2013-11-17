package cs3246.a2;

import cs3246.a2.model.Product;

public class Result implements Comparable<Result> {
        
        private Product product;
        private double score;
        
        public double getScore(){
                return score;
        }
        
        public Product getProduct(){
                return product;
        }
        
        public Result(Product product, double score){
                this.product = product;
                this.score = score;
        }
        
        @Override
        public int compareTo(Result another) {        
                double difference = this.score - another.getScore();
                if (difference > 0){
                        return -1;
                } else if (difference == 0){
                        return 0;
                } else{
                        return 1;
                }
        }

}
