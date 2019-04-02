public class Execute {
	//modua aldatu NONLOCKING / LOCKING
	static final int mode = Data.LOCKING;
	
	public static void main(String[] args) {
		
//		Data DATA1 = new Data(Data.NONLOCKING,Data.NONLOCKING,"root","anderdbk","127.0.0.1");
		Data DATA2 = new Data(Data.NONLOCKING,Data.NONLOCKING,"root","Alain-b-96","127.0.0.1");
		
//		ThreadA threadA = new ThreadA(DATA1);
//		ThreadB threadB = new ThreadB(DATA1);
//		ThreadC threadC = new ThreadC(DATA1);
//		ThreadD threadD = new ThreadD(DATA1);
//		ThreadE threadE = new ThreadE(DATA1);
//		ThreadF threadF = new ThreadF(DATA1);
		
		ThreadA threadA2 = new ThreadA(DATA2);
		ThreadB threadB2 = new ThreadB(DATA2);
		ThreadC threadC2 = new ThreadC(DATA2);
		ThreadD threadD2 = new ThreadD(DATA2);
		ThreadE threadE2 = new ThreadE(DATA2);
		ThreadF threadF2 = new ThreadF(DATA2);
		
//		DATA1.initializeSharedVariables();
//		DATA1.showInitialValues();
		DATA2.initializeSharedVariables();
		DATA2.showInitialValues();
		
//		new Thread(threadA).start();
//		new Thread(threadB).start();
//		new Thread(threadC).start();
//		new Thread(threadD).start();
//		new Thread(threadE).start();
//		new Thread(threadF).start();
		
		new Thread(threadA2).start();
		new Thread(threadB2).start();
		new Thread(threadC2).start();
		new Thread(threadD2).start();
		new Thread(threadE2).start();
		new Thread(threadF2).start();
		
//		DATA1.showFinalValues();
		DATA2.showFinalValues();
	}
}