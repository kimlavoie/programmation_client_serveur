package client;

import java.util.ArrayList;
import com.Query;
/**
  * Helper class. Help to manage multiple asynchronous queries.
  */
public class QueryManager{
	ArrayList<Query> queries = new ArrayList<Query>();
	
	/**
	  * Add a new query to the list.
	  */
	public void add(Query query){
		queries.add(query);
	}
	
	/**
	  * Check if a query result is available.
	  */
	public boolean available(){
		for(Query query: queries){
			if(query.getResult() != null){
				return true;
			}
		}
		return false;
	}
	
	/**
	  * Get the next available query with a result. Null if no result available yet.
	  */
	public Query nextAvailable(){
		for(int i = 0; i < queries.size(); i++){
			if(queries.get(i).getResult() != null){
				Query q = queries.get(i);
				queries.remove(i);
				return q;
			}
		}
		return null;
	}
	
	/**
	  * Return all available queries with a result in an ArrayList;
	  */
	public ArrayList<Query> allAvailables(){
		ArrayList<Query> availables = new ArrayList<Query>();
		for(int i = 0; i < queries.size(); i++){
			if(queries.get(i).getResult() != null){
				availables.add(queries.get(i));
				queries.remove(i);
			}
		}
		return availables;
	}
	
	/**
	  * True if the query list is empty, false otherwise.
	  */
	public boolean noQuery(){
		if(queries.size() == 0) return true;
		return false;
	}
}