package cs3246.a2.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs3246.a2.model.Product;

public class ProductSqliteHelper extends SqliteHelper {

	public ProductSqliteHelper(String dbName) throws ClassNotFoundException {
		super(dbName);
	}
	
	public Product getById(int id) {
		String clauses = "where id=" + id;
		return getByClauses(clauses).get(0);
	}
	
	public List<Product> getByIds(List<Integer> ids) {
		// TODO implement IN clause
		String clauses = "where id IN (" + generateCommaSeperatedString(ids) + ")";
		return getByClauses(clauses);
	}
	
	private String generateCommaSeperatedString(List<Integer> ids) {
		if (ids.size()==0) {
			return "";
		}
		
		String s = "";
		for (int i=0; i<ids.size(); i++) {
			s += ids.get(i).toString();
			if (i < ids.size()-1) {
				s += ",";
			}
		}
		
		return s;
	}

	public List<Product> getDresses() {
		return getFromCategory(21);
	}
	
	public List<Product> getTops() {
		return getFromCategory(7);
	}
	
	public List<Product> getBags() {
		return getFromCategory(3);
	}
	
	public List<Product> getShoes() {
		return getFromCategory(2);
	}
	
	private List<Product> getFromCategory(int categoryId) {
		String clauses = "where category_id=" + categoryId;
		return getByClauses(clauses);
	}
	
	private List<Product> getByClauses(String clauses) {
		List<Product> productList = null;
		
		try {
			Statement statement = mConnection.createStatement();
			productList = new ArrayList<Product>();
			
			ResultSet rs =
					statement.executeQuery("select * from product_preview " 
							+ clauses);
			
			while (rs.next()) {
				Product product = new Product(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getString("url"),
						rs.getString("brand"),
						rs.getDouble("price"));
				productList.add(product);
			}
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return productList;
	}

}
