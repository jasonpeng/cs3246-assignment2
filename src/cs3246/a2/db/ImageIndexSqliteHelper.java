package cs3246.a2.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import cs3246.a2.model.ImageIndex;
import cs3246.a2.model.Product;

public class ImageIndexSqliteHelper extends SqliteHelper {

	public ImageIndexSqliteHelper(String dbName) throws ClassNotFoundException {
		super(dbName);
	}
	
	public ImageIndex getById(int id) {
		return getByClauses("where id=" + id).get(0);
	}
	
	public List<ImageIndex> getFromProducts(List<Product> products) {
		List<Integer> ids = new ArrayList<Integer>();
		for (Product product : products) {
			ids.add(product.getId());
		}
		
		String clauses = "where id IN (" + generateCommaSeperatedString(ids) + ")";
		return getByClauses(clauses);
	}
	
	public boolean createTable() {
		try {
			Statement statement = mConnection.createStatement();
			
			statement.execute("create table image_index (id integer primary key, ccv text, hist text, edge text)");
			return true;
		} catch (SQLException e) {
			System.err.print(e.getMessage());
			return false;
		}
	}
	
	public boolean persist(List<ImageIndex> imageIndexList) {
		try {
			Statement statement = mConnection.createStatement();
			
			for (ImageIndex imageIndex : imageIndexList) {
				int id;
				String ccv;
				String hist;
				String edge;
				Gson gson = new Gson();
				
				id = imageIndex.getId();
				ccv = gson.toJson(imageIndex.getSimilarityCCV());
				hist = gson.toJson(imageIndex.getSimilarityHist());
				edge = gson.toJson(imageIndex.getSimilarityEdge());
				
				statement.addBatch("insert or replace into image_index values(" +
						"'" + id + "'," +
						"'" + ccv + "'," +
						"'" + hist + "'," +
						"'" + edge +"')");
			}
			
			statement.executeBatch();
			return true;
		} catch (SQLException e) {
			System.err.print(e.getMessage());
			return false;
		}
		
	}
	
	private List<ImageIndex> getByClauses(String clauses) {
		List<ImageIndex> indexList = null;
		
		try {
			Statement statement = mConnection.createStatement();
			indexList = new ArrayList<ImageIndex>();
			
			ResultSet rs =
					statement.executeQuery("select * from image_index " 
							+ clauses);
			
			while (rs.next()) {
				int id;
				double[] ccv;
				double[] hist;
				double[] edge;
				Gson gson = new Gson();
				
				id = rs.getInt("id");
				ccv = gson.fromJson(rs.getString("ccv"), double[].class);
				hist = gson.fromJson(rs.getString("hist"), double[].class);
				edge = gson.fromJson(rs.getString("edge"), double[].class);
				
				ImageIndex imageIndex = new ImageIndex(
						id,
						ccv,
						hist,
						edge);
				
				indexList.add(imageIndex);
			}
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return indexList;
	}

}
