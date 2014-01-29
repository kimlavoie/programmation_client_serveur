package server;

import java.util.ArrayList;

/**
  * Custom class loader. It memorize the class already defined.
  */
public class NetworkClassLoader extends ClassLoader{
	ArrayList<Class> classes = new ArrayList<Class>();
	
	/**
	  * Takes bytecode in an array of bytes and define the class by it's name. 
	  * Race conditions happen because the server is multithreaded, making defining same class twice possible, so we have to synchronize.
	  */
    public synchronized Class loadClassFromBytes(String name, byte[] bytes){
		for(Class cl: classes){
			if(cl.getCanonicalName().equals(name)){
				return cl;
			}
		}
		Class cl = defineClass(name, bytes, 0, bytes.length);
		classes.add(cl);
		return cl;
	}
}