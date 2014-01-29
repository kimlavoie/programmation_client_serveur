package client;

import java.util.ArrayList;
import client.Client;
import client.QueryManager;
import com.Query;

/**
  * Example of using the client within a program.
  */
public class UserExample{
	public static void main(String[] args) throws Exception{
		//ArrayList of parameters (only one in this case)
		ArrayList<Object> params = new ArrayList<Object>();
		params.add("This is a test");
	
		//Create client
		Client client = new Client("localhost", 6767);
		
		//Create QueryManager
		QueryManager QMan = new QueryManager();
				
		//Fetching queries. Fetching is asynchronous.
		QMan.add(client.fetchQuery("client.TestClass", "qmanTest1"));
		QMan.add(client.fetchQuery("client.TestClass", "qmanTest2", null));
		QMan.add(client.fetchQuery("client.TestClass", "qmanTest3", params));
		
		//Event loop.
		while(!QMan.noQuery()){
				while(QMan.available()){
					Query q = QMan.nextAvailable();
					String result = (String) q.getResult();
					System.out.println("Result: " + result);
				}
				//Do other things
		}
	}
}