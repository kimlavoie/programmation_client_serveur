package client;

/**
  * Class used to test the communication between client and server. Used in CommandLineUserExample and UserExample.
  */
public class TestClass {
    private static int x = 0;

    public String getResult(String x, Float y){
	    String str = new String();
	    for(int i = 0; i < y; i++){
		    str += x;
		}
	    return str;
	}
	
	public String test(String x){
	    return "test: " + x;
	}
	
	public static synchronized Integer bibi(){
	    return ++x;
	}
	
	public String qmanTest1() throws Exception{
		Thread.sleep(6000);
		return "qmanTest1";
	}
	
	public String qmanTest2() throws Exception{
		Thread.sleep(4000);
		return "qmanTest2";
	}
	
	public String qmanTest3(String str) throws Exception{
		Thread.sleep(2000);
		return "qmanTest3: " + str;
	}
}