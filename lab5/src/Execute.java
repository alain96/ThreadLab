public class Execute {
	//modua aldatu NONLOCKING / LOCKING
	static final int mode = Data.LOCKING;
	
	public static void main(String[] args) {
		
		Data DATA1 = new Data(mode,mode,"root","Alain-b-96","127.0.0.1");
		Data DATA2 = new Data(mode,mode,"root","Alain-b-96","127.0.0.1");
		
		ThreadA threadA = new ThreadA(DATA1,DATA2);
		ThreadB threadB = new ThreadB(DATA1,DATA2);
		ThreadC threadC = new ThreadC(DATA1,DATA2);
		ThreadD threadD = new ThreadD(DATA1,DATA2);
		ThreadE threadE = new ThreadE(DATA1,DATA2);
		ThreadF threadF = new ThreadF(DATA1,DATA2);
		

		
	
		
		DATA1.initializeSharedVariables();
		DATA1.showInitialValues();
		DATA2.initializeSharedVariables();
		DATA2.showInitialValues();
		
		new Thread(threadA).start();
		new Thread(threadB).start();
		new Thread(threadC).start();
		new Thread(threadD).start();
		new Thread(threadE).start();
		new Thread(threadF).start();
		

		
		DATA1.showFinalValues();
		DATA2.showFinalValues();
	}
}