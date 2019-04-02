import java.sql.SQLException;


public class ThreadA extends Thread {

	
	String myName = "A";
	Data myData;
	Data myData2;
	int myShareMode;
	int myExclusiveMode;
	int myShareMode2;
	int myExclusiveMode2;
	
	
	
	public ThreadA(Data data, Data data2){
		myData = data;
		myData2 = data2;
		if (myData.LOCKING == Data.LOCKING){
			myShareMode = myData.SHARE_LOCKING;
			myExclusiveMode = myData.EXCLUSIVE_LOCKING;
		}
		else{
			myShareMode = myData.NONLOCKING;
			myExclusiveMode = myData.NONLOCKING;
		}
		if (myData2.LOCKING == Data.LOCKING){
			myShareMode2 = myData2.SHARE_LOCKING;
			myExclusiveMode2 = myData2.EXCLUSIVE_LOCKING;
		}
		else{
			myShareMode2 = myData2.NONLOCKING;
			myExclusiveMode2 = myData2.NONLOCKING;
		}
	}
	
	
	
	public void run(){
		
		int counter = 0;
		int counter2 = 0;
		boolean committed;
		boolean committed2;
		myData.synchronyze();
		myData2.synchronyze();
		
		while ( counter  < myData.NUMBER_OF_ITERATIONS || counter2 < myData2.NUMBER_OF_ITERATIONS){
			if (counter  < myData.NUMBER_OF_ITERATIONS) {
				committed = myData.procedureF(myName,counter,myShareMode,myExclusiveMode);
				if(committed){
					counter = counter +1;
				}
			}
			if (counter2  < myData2.NUMBER_OF_ITERATIONS) {
				committed2= myData2.procedureF(myName, counter2, myShareMode2, myExclusiveMode2);

			if(committed2){
				counter2 = counter2 +1;
			}
		}
		myData.finish();
		myData2.finish();
	}
	
	}
}