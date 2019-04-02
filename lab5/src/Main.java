
public class Main {

	public void myMethod() {
		MyThread newThread = new MyThread();
		new Thread(newThread).start();
	}
}
