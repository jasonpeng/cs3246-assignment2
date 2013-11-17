package cs3246.a2.model;

import cs3246.a2.Constant;

public class Product {

	private String mFilename;
	private int mId;
	private String mName;
	private String mUrl;
	private String mBrand;
	private double mPrice;
	
	public Product(int id, String name, String url, String brand, double price) {
		mId = id;
		mName = name;
		mUrl = url;
		mBrand = brand;
		mPrice = price;
		
		mFilename = Constant.PRODUCT_DIR + id + ".jpg";
	}
	
	public String getFilename() {
		return mFilename;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		this.mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		this.mUrl = url;
	}

	public String getBrand() {
		return mBrand;
	}

	public void setBrand(String brand) {
		this.mBrand = brand;
	}

	public double getPrice() {
		return mPrice;
	}

	public void setPrice(double price) {
		this.mPrice = price;
	}
}
