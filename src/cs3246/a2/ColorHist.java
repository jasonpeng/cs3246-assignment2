package cs3246.a2;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cs3246.a2.sample.Histogram;
  
public class ColorHist extends JFrame implements FeatureExtractor{ 
    private JButton imageField1; // button to select the image 1  
    private JButton imageField2; // button to select the image 2 
      
    private JLabel imageLabel1; 
    private JLabel imageLabel2; 
    private String imagePath1; 
    private String imagePath2; 
    private Image img1; 
    private Image img2; 
    private JLabel distLabel; 
      
    private JPanel contentPane; 
    private JPanel imagePanel; 
    private JPanel histPanel; 
      
    private JButton computeSimilarityButton; 
    private String basePath; 
    private int width = 300; // the size for each image result 
    private int height = 300; 
      
    private int dim = 64; 
      
    private BufferedImage buffered1; 
    private BufferedImage buffered2; 
    private Histogram hist1; 
    private Histogram hist2; 
    
    private double bins[];
      
    public ColorHist() { 
        
    } 
      
    public void init() { 
    	
    	basePath = "E:\\workspace\\ImageSearchFramework\\"; // change it when necessary 
        
        imageField1 = new JButton("Choose image 1"); 
        imageField2 = new JButton("Choose image 2"); 
          
        imageLabel1 = new JLabel(); 
        imageLabel2 = new JLabel(); 
          
        computeSimilarityButton = new JButton("Compute similarity"); 
        distLabel = new JLabel("", JLabel.CENTER); 
        distLabel.setFont(new Font("Serif", Font.BOLD, 20)); 
          
        imagePanel = new JPanel(); 
  
        hist1 = new Histogram(); 
        hist2 = new Histogram(); 
        contentPane = (JPanel)this.getContentPane(); 
          
        setSize(1200,600); 
          
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
          
/*      imagePanel.setLayout(new GridLayout(3,2)); 
          
        imagePanel.add(imageField1); 
        imagePanel.add(imageLabel1); 
          
        //imagePanel.add(hist1); 
        imagePanel.add(imageField2); 
        imagePanel.add(imageLabel2); 
        //imagePanel.add(hist2); 
  
        imagePanel.add(computeSimilarityButton); 
        imagePanel.add(distLabel); 
          
        imagePanel.setVisible(true); 
        hist1.setVisible(true); 
        hist2.setVisible(true); 
          
          
        GridLayout gridLayout = new GridLayout();  // grid layout 1 * 2 
        gridLayout.setColumns(1);  
        gridLayout.setRows(3);  
        contentPane.setLayout(gridLayout);  
          
        contentPane.add(imagePanel); 
        contentPane.add(hist1); 
        contentPane.add(hist2); 
        contentPane.setVisible(true); 
        setVisible(true); 
        repaint(); 
*/      
          
        GridLayout gridLayout = new GridLayout();  // grid layout 1 * 2 
        gridLayout.setColumns(2);  
        gridLayout.setRows(4);  
        contentPane.setLayout(gridLayout); 
        contentPane.add(imageField1); 
        contentPane.add(imageField2); 
        contentPane.add(imageLabel1); 
        contentPane.add(imageLabel2); 
        contentPane.add(hist1); 
        contentPane.add(hist2); 
        contentPane.add(computeSimilarityButton); 
        contentPane.add(distLabel); 
        contentPane.setVisible(true); 
        setVisible(true); 
        repaint(); 
          
        imageField1.addActionListener(new ActionListener() { 
  
            public void actionPerformed(ActionEvent e) { 
  
                JFileChooser fileChooser = new JFileChooser(); 
                fileChooser.setDialogTitle("Please select a sample image"); 
                String path = ""; 
                int returnVal =  fileChooser.showOpenDialog(ColorHist.this); 
                if (returnVal == JFileChooser.APPROVE_OPTION) { 
                    imagePath1 = fileChooser.getSelectedFile().getAbsolutePath(); 
                    img1 = null; 
                    try { 
                        img1 = ImageIO.read(new File(imagePath1)); 
                        buffered1 = ImageIO.read(new File(imagePath1)); 
                        hist1.load(imagePath1); // paint histogram 
                        hist1.repaint(); 
                        img1 = img1.getScaledInstance(width, -1, img1.SCALE_DEFAULT); 
                    } catch (IOException e1) { 
                        // TODO Auto-generated catch block 
                        e1.printStackTrace(); 
                    } 
                    imageLabel1.setIcon(new ImageIcon(img1)); 
                } 
            } 
        }); 
      
        imageField2.addActionListener(new ActionListener() { 
  
            public void actionPerformed(ActionEvent e) { 
  
                JFileChooser fileChooser = new JFileChooser(); 
                fileChooser.setDialogTitle("Please select a sample image"); 
                String path = ""; 
                int returnVal =  fileChooser.showOpenDialog(ColorHist.this); 
                if (returnVal == JFileChooser.APPROVE_OPTION) { 
                    imagePath2 = fileChooser.getSelectedFile().getAbsolutePath(); 
                    img2 = null; 
                    try { 
                        img2 = ImageIO.read(new File(imagePath2)); 
                        buffered2 = ImageIO.read(new File(imagePath2)); 
                        hist2.load(imagePath2); 
                        hist2.repaint(); 
                        img2 = img2.getScaledInstance(width, -1, img2.SCALE_DEFAULT); 
                    } catch (IOException e1) { 
                        // TODO Auto-generated catch block 
                        e1.printStackTrace(); 
                    } 
                    imageLabel2.setIcon(new ImageIcon(img2)); 
                      
                } 
            } 
        }); 
          
        computeSimilarityButton.addActionListener(new ActionListener() { 
            /* 
             * replace and write your own code here 
             */
              
            public void actionPerformed(ActionEvent e) { 
                // TODO Auto-generated method stub 
                double sim = computeSimilarity(); 
                distLabel.setText("Similarity is " + Double.toString(sim)); 
            } 
        }); 
          
        repaint(); 
          
    } 
      
    public double computeSimilarity() { 
          
        double[] hist1 = getHist(buffered1); 
        double[] hist2 = getHist(buffered2); 
          
        //double distance = calculateDistance(hist1, hist2); 
        //return 1-distance; 
        return computeSimilarity(hist1, hist2);
    }
    
    public double computeSimilarity(double[] array1, double[] array2) 
    { 
        double Sum = 0.0; 
        for(int i = 0; i < array1.length; i++) { 
        	double distance = Math.abs(array1[i] - array2[i]);
        	double max = array1[i] > array2[i] ? array1[i] : array2[i];
        	if (max == 0){
        		continue;
        	}
        	Sum += (array1[i] * ( 1 - distance / max)); 
        } 
        return Sum; 
    } 
    
    public double calculateDistance(double[] array1, double[] array2) 
    { 
        // Euclidean distance 
        double Sum = 0.0; 
        for(int i = 0; i < array1.length; i++) { 
           Sum = Sum + Math.pow((array1[i]-array2[i]),2.0); 
        } 
        return Math.sqrt(Sum); 
    }
      
    public double[] getHist(BufferedImage image) { 
        int imHeight = image.getHeight(); 
        int imWidth = image.getWidth(); 
        bins = new double[3*dim]; 
        int step = 256 / dim; 
        Raster raster = image.getRaster(); 
        for(int i = 0; i < imWidth; i++) 
        { 
            for(int j = 0; j < imHeight; j++) 
            { 
                // rgb->ycrcb 
                int r = raster.getSample(i,j,0); 
                int g = raster.getSample(i,j,1); 
                int b = raster.getSample(i,j,2); 
                
                // Remove white background:
                if (r >= 250 && g >= 250 && b >= 250){
                	continue;
                }
                  
                int rbin = r / step; 
                int gbin = g / step; 
                int bbin = b / step; 
                bins[rbin]++; 
                bins[gbin+dim]++; 
                bins[bbin+2*dim]++; 
            } 
        } 
          
        //normalize 
        for(int i = 0; i < 3*dim; i++) { 
            bins[i] = bins[i]/(3*imHeight*imWidth); 
        } 
        
        
        return bins; 
    } 
      
    public static void main(String[] args) { 
        ColorHist example = new ColorHist(); 
        example.init();
    }

	@Override
	public double[] getFeature(BufferedImage bi) {
		return getHist(bi);
	}

	@Override
	public double computeSimilarity(double[] document, Similarity sim) {
		return sim.compute(bins, document);
	} 
} 