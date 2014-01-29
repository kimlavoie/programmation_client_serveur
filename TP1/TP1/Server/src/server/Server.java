package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import com.Logger;
import com.SocketUtil;
import com.Query;

/**
  * Multithreaded server to do remote processing
  */
public class Server{
	ServerSocket server;
	NetworkClassLoader loader = new NetworkClassLoader();
	boolean memorizeLoadedClasses = false;
	
	/**
	  * Constructor. Needs the port number and whether or not it should memorize defined classes.
	  * Memorizing is needed if static methods are needed.
	  * If memorizing is deactivated, the client can send a modified version of the bytecode of a class.
	  */
	public Server(int port, boolean memorize){
		System.out.println("Server listening on port " + Integer.toString(port));
		memorizeLoadedClasses = memorize;
		try{
	        server = new ServerSocket(port);
		} catch (Exception e){
			Logger.logError(e);
			System.exit(-1);
		}
	}

	/**
	  * Make the server start accepting requests.
	  */
	public void start(){
	    while(true){
			try{
				Socket socket = server.accept();
				Logger.log("Connection established");
				new Thread(new RequestProcessor(socket)).start();
			} catch (IOException e){
				Logger.logError(e);
			}
		}
	}
	
	/**
	  * Inner class serving as thread to process each request.
	  */
	class RequestProcessor implements Runnable{
	    Socket socket;
		
		/**
		  * Constructor. Only need the socket to communicate with the client. The socket is part of the state.
		  */
	    public RequestProcessor(Socket socket){
		    this.socket = socket;
			if(!memorizeLoadedClasses) loader = new NetworkClassLoader();
		}
	
		/**
		  * Called by Thread.start(). Get the query, process it, then send the result.
		  */
	    public void run(){
			try{
				Query query = getQuery();
				Object result = process(query);
				sendResult(result);
			} catch (Exception e){
				Logger.logError(e);
			}
		}		
		
		/**
		  * Send the resulting object back to client, via the socket.
		  */
		private void sendResult(Object result) throws Exception{
			Logger.log("Sending result: " + result);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(result);
			SocketUtil.writeToSocket(bos.toByteArray(), socket);
			Logger.log("Processing finished successfully");
			Logger.log("");
		}
		
		/**
		  * Get the query from the client, via the socket.
		  */
		private Query getQuery() throws Exception{
			byte[] queryBytes = SocketUtil.readFromSocket(socket);
			ByteArrayInputStream bis = new ByteArrayInputStream(queryBytes);
			ObjectInputStream in = new ObjectInputStream(bis);
			return (Query) in.readObject();
		}
		
		/**
		  * Finds the type of the parameters of the method to call on the class.
		  */
		private ArrayList<Class> getTypes(Class cl, String method) throws Exception{
			ArrayList<Class> types = new ArrayList<Class>();
			for(Method meth: cl.getMethods()){
				if(meth.getName().equals(method)){
					for(Class c: meth.getParameterTypes()){
						types.add(c);
					}
					break;
				}
			}
			return types;
		}
		
		/**
		  * Dynamically process the query provided by defining a new class, creating an instance of that class, then calling the method.
		  */
		private Object process(Query query) throws Exception {
			Class testClass = loader.loadClassFromBytes(query.getClassName(), query.getBytecode());
			ArrayList<Class> types = getTypes(testClass, query.getMethodName());
			Object obj = testClass.newInstance();
			Method meth = testClass.getMethod(query.getMethodName(), types.toArray(new Class[0]));
			
			return meth.invoke(obj, query.getParams().toArray());
		}
	}
	
	/**
	  * Use to start the server from command line. Default to port 6767.
	  * Usage: Server [-m] [PORT]
	  */
    public static void main(String[] args){
	    int port = 6767;
		boolean mem = false;
		if(args.length > 0){
			for(String arg: args){
				if(arg.equals("-m")){
					mem = true;
				}
				else{
					port = Integer.parseInt(arg);
				}
				
			}
		}
		Server server = new Server(port, mem);
		server.start();
	}
}