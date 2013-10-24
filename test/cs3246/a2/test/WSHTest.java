package cs3246.a2.test;

import cs3246.a2.web.WebServiceHandler;

public class WSHTest {

	public static void main(String[] args) {
		try {
			String[] results = WebServiceHandler.query("query2.jpg");
			for (String result : results) {
				System.out.println(result);
			}
		} catch (Exception e) {
			
		}
	}

}
