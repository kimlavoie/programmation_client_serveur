package client;

import java.util.ArrayList;
import java.lang.reflect.Method;
import client.Client;

/**
  * A command line interface example.
  */
public class CommandLineUserExample{
	public static void main(String[] args) throws Exception{
		//Printing common usage of the command line interface if not enough arguments
	    if(args.length < 2){
		    System.out.println("Usage: java Client className methodName [params]");
		} 
		else{
			//Give some meaningful names to the arguments
		    final int PORT = 6767;
		    final String ADRESS = "localhost";
			String className = args[0];
			String methodName = args[1];
			
			//Quite complex. Dynamically determine types of parameters and put them in an ArrayList. Put the return type in a variable.
			ArrayList<String> types = new ArrayList<String>();
			Class c = Class.forName(className);
			String returnType = new String();
			for(Method meth : c.getDeclaredMethods()){
				if(meth.getName().equals(methodName)){
					returnType = meth.getReturnType().getCanonicalName();
					for(Class cl: meth.getParameterTypes()){
						types.add(cl.getCanonicalName());
					}
					break;
				}
			}
			
			//Cast parameters according to the ArrayList of Classes defined before, then puts them in an ArrayList of Objects.
			System.out.println("Sending parameters:");
			ArrayList<Object> params = new ArrayList<Object>();
			for(int i = 2; i < args.length; i++){
				Object param = new Object();
				String strParam = args[i];
				
				//More types available within programs, but only these make sense in command line mode
				switch(types.get(i-2)){
					case "java.lang.Integer":
						System.out.println("    Integer: " + strParam);
						param = Integer.parseInt(strParam);
						break;
					case "java.lang.String":
						System.out.println("    String: " + strParam);
						param = strParam;
						break;
					case "java.lang.Double":
						System.out.println("    Double: " + strParam);
						param = Double.parseDouble(strParam);
						break;
					case "java.lang.Float":
						System.out.println("    Float: " + strParam);
						param = Float.parseFloat(strParam);
						break;
				}
			    params.add(param);
			}
			
			//Fetching result, storing it in an Object.
			System.out.println("Fetching result:");
	        Client client = new Client(ADRESS, PORT);
			Object resultObject = client.fetchObject(className, methodName, params);
			String result = new String();
			
			//Casting return type dynamically
			//More types available, but only these make sense in command line mode
			switch(returnType){
					case "java.lang.Integer":
						System.out.println("    type: Integer");
						result = Integer.toString((Integer) resultObject);
						break;
					case "java.lang.String":
						System.out.println("    type: String");
						result = (String) resultObject;
						break;
					case "java.lang.Double":
						System.out.println("    type: Double");
						result = Double.toString((Double) resultObject);
						break;
					case "java.lang.Float":
						System.out.println("    type: Float");
						result = Float.toString((Float) resultObject);
						break;
				}
			
			//Finally, printing result
			System.out.println("    result: " + result);
		}
	}
}