package chouc.java.proxy;

public class MyPerson implements PersonInterface{

	public void doSomeThing(){
		System.out.println("MyPerson is doing its thing.....");
	}

	public void saySomeThing() {
		System.out.println("MyPerson is saying its thing.....");
		
	}
	
	
	private void xx(){
		System.out.println("MyPerson is xx its thing.....");
	}

}
