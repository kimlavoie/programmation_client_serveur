package com;

import java.util.ArrayList;
import java.io.Serializable;

/**
  * Container class representing a query from the client to the server
  */
public class Query implements Serializable{
	String className;
	String methodName;
	ArrayList<Object> params;
	byte[] bytecode;
	Object result = null;
	
	/**
	  * Constructor without parameters.
	  */
	public Query(String className, String methodName){
		this.className = className;
		this.methodName = methodName;
		this.params =  new ArrayList<Object>();
	}
	
	/**
	  * Constructor with parameters
	  */
	public Query(String className, String methodName, ArrayList<Object> params){
		this.className = className;
		this.methodName = methodName;
		if(params != null){
			this.params = params;
		}
		else{
			this.params = new ArrayList<Object>();
		}
	}

	public String getClassName(){return className;}
	public String getMethodName(){return methodName;}
	public ArrayList<Object> getParams(){return params;}
	public byte[] getBytecode(){return bytecode;}
	public Object getResult(){return result;}
	
	public void setBytecode(byte[] bytecode){this.bytecode = bytecode;}
	public void setResult(Object result){this.result = result;}
}