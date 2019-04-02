import java.sql.SQLException;


public class ThreadA extends Thread {

	
	String myName = "A";
	Data myData;
	int myShareMode;
	int myExclusiveMode;
	
	
//	public ThreadA(){
//		myData = new Data(myData.NONLOCKING,myData.NONLOCKING);
//		myShareMode = myData.NONLOCKING;
//		myExclusiveMode = myData.NONLOCKING;
//	}
	
	public ThreadA(Data data){
		myData = data;
		if (myData.LOCKING == Data.LOCKING){
			myShareMode = myData.SHARE_LOCKING;
			myExclusiveMode = myData.EXCLUSIVE_LOCKING;
		}
		else{
			myShareMode = myData.NONLOCKING;
			myExclusiveMode = myData.NONLOCKING;
		}
	}
	
	
	
	public void run(){
		
		int counter = 0;
		int counter2 = 0;
		boolean committed;
		boolean committed2;
		myData.synchronyze();
		
		while ( counter  < myData.NUMBER_OF_ITERATIONS || counter2 < myData.NUMBER_OF_ITERATIONS){
			if (counter  < myData.NUMBER_OF_ITERATIONS) {
				committed = myData.procedureF(myName,counter,myShareMode,myExclusiveMode);
				if(committed){
					counter = counter +1;
				}
			}
			if (counter2  < myData.NUMBER_OF_ITERATIONS) {
				committed2= myData.procedureF(myName, counter2, myShareMode, myExclusiveMode);

			if(committed2){
				counter2 = counter2 +1;
			}
		}
		myData.finish();
	}
	
	}
}