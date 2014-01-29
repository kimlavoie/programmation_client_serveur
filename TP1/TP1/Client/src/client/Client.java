package client;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.reflect.Method;
import com.SocketUtil;
import com.Logger;
import com.Query;
import client.TestClass;

/**
  * The client, which communicate with the server. Communication can be synchronous or asynchronous.
  */
public class Client {	
	String adress;
	int port;
	
	/**
	  * Constructor. Needs the adress and the port to which connect the socket.
	  */
	public Client(String adress, int port){
	    this.adress = adress;
		this.port = port;
	}
	
	/**
	  * Fetch the result asynchronously, given the name of the class, the name of the method, and the parameters.
	  */
	public Query fetchQuery(String className, String methodName, ArrayList<Object> params){
		Query query = new Query(className, methodName, params);
	    try{
		    Socket socket = new Socket(adress, port);
			Thread t = new Thread(new QueryProcessor(query, socket));
			t.start();
		} catch(Exception e){
		    Logger.logError(e);
		}
		return query;
	}
	
	/**
	  * Fetch the result asynchronously, given the name of the class and the name of the method, if no parameters needed.
	  */
	public Query fetchQuery(String className, String methodName){
		return fetchQuery(className, methodName, null);
	}
	
	/**
	  * Fetch the result asynchronously, given a pre-made Query object.
	  */
	public Query fetchQuery(Query query){
		return fetchQuery(query.getClassName(), query.getMethodName(), query.getParams());
	}
	
	/**
	  * Fetch the result synchronously, given the name of the class, the name of the method, and the parameters.
	  */
	public Object fetchObject(String className, String methodName, ArrayList<Object> params){
		Query query = fetchQuery(className, methodName, params);
		while(query.getResult() == null){
			try{
				Thread.sleep(1);
			} catch(Exception e){
				Logger.logError(e);
			}
		}
		return query.getResult();
	}
	
	/**
	  * Fetch the result synchronously, given the name of the class and the name of the method, if no parameter needed.
	  */
	public Object fetchObject(String className, String methodName){
		return fetchObject(className, methodName, null);
	}
	
	/**
	  * Fetch the result synchronously, given a pre-made Query object.
	  */
	public Object fetchObject(Query query){
		return fetchObject(query.getClassName(), query.getMethodName(), query.getParams());
	}
	
	/**
	  * Class serving as a thread to process queries.
	  */
	class QueryProcessor implements Runnable{
	    Query query;
		Socket socket;
		
		/**
		  * Constructor. Needs a socket, and uses the query to pass the result.
		  */
        public QueryProcessor(Query query, Socket socket){
		    this.query = query;
			this.socket = socket;
		}
	
		/**
		  * Called when thread starts. Send the query, then waits for the result, which is finally put in the query object.
		  */
		public void run(){
			try{
				query.setBytecode(loadFile("build/classes" + makePath(query.getClassName()) + ".class"));
				sendQuery();
				
				byte[] bytesResult = SocketUtil.readFromSocket(socket);
				ByteArrayInputStream bis = new ByteArrayInputStream(bytesResult);
				ObjectInputStream in = new ObjectInputStream(bis);
				query.setResult(in.readObject());
			} catch(Exception e){
				Logger.logError(e);
			}
		}
		
		/**
		  * Send the query object of the QueryProcessor through the socket.
		  */
		public void sendQuery() throws Exception{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(query);
			SocketUtil.writeToSocket(bos.toByteArray(), socket);
		}
		
		/**
		  * Build the path used to get the bytecode file.
		  */
		private String makePath(String name){
			String[] nodes = name.split("\\.");
			String path = new String();
			for(String node: nodes){
				path += "/" + node;
			}
			return path;
		}
		
		/**
		  * Load a file, given a path.
		  */
		private byte[] loadFile(String name)throws Exception{
			return  Files.readAllBytes(Paths.get(name));
		}
	}

    
}