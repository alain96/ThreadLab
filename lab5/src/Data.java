import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ThreadLocalRandom;

public class Data {
	
	Connection connLOCAL;
	
	Statement st;
	
	String sentence;
	
	public static final int NONLOCKING = 0;
	public static final int LOCKING = 1;
	public static final int SHARE_LOCKING = LOCKING;
	public static final int EXCLUSIVE_LOCKING = 2*LOCKING;
	public static final int NUMBER_OF_ITERATIONS = 100;
	public static final int NUMBER_OF_THREADS = 3;
	
	private String IP="";
	public static final String PORTUA="8306";
	
	private String USER="";
	private String PASS="";
	public static final String DATABASE="concurrency_control";
	
	public static final String X = "X";
	public static final String Y = "Y";
	public static final String Z = "Z";
	public static final String T = "T";
	public static final String A = "A";
	public static final String B = "B";
	public static final String C = "C";
	public static final String D = "D";
	public static final String E = "E";
	public static final String F = "F";
	public static final String M = "M";
	
	int SHARE_MODE;
	int EXCLUSIVE_MODE;
	
	
	public Data(int myShareMode, int myExclusiveMode,String user,String pass,String ip){
		SHARE_MODE=myShareMode;
		EXCLUSIVE_MODE=myExclusiveMode;
		USER=user;
		PASS=pass;
		IP=ip;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			//cambiar datos manualmente para conexion
			connLOCAL = DriverManager.getConnection(
//					"jdbc:mysql://127.0.0.1:3306/concurrency_control",
					"jdbc:mysql://"+IP+":"+PORTUA+"/"+DATABASE,
					USER,PASS);
//					"root", "alainc1996");
			connLOCAL.setAutoCommit(false);
			st = connLOCAL.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 public Boolean initializeSharedVariables(){
         try {
             PreparedStatement posted = connLOCAL.prepareStatement("update concurrency_control.variables set value=0");       
			posted.executeUpdate();
			connLOCAL.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
         return true;
	 }

	 public	  Boolean showVariableValues(String thread, String time){
		 try{
			 System.out.println(thread +" thread. "+time +" values." );
			 String agindua="Select * from concurrency_control.variables;";
			 PreparedStatement statement = connLOCAL.prepareStatement(agindua);
	         
	         ResultSet result = statement.executeQuery();
	         while(result.next()){
	        	 System.out.println(result.getString("name")+" aldagaiaren balioa: "+ result.getString("value"));
	         }
			 
		 }catch (Exception e) {
			 e.printStackTrace();
			 return false;
		}
		 
		 return true;
	 }

	 
	 public	 int getValue(int mode, String variable) throws SQLException{
		 String agindua=null;
		 if(mode==Data.NONLOCKING){
	        agindua="Select value from concurrency_control.variables where name='"+variable+"';";       
		 }
		 else{
			 if(mode==Data.SHARE_LOCKING){
				 agindua="Select value from concurrency_control.variables where name='"+variable+"' LOCK IN SHARE MODE;";       

			 }else{
				 if(mode==Data.EXCLUSIVE_LOCKING){
					 agindua="Select value from concurrency_control.variables where name='"+variable+"' FOR UPDATE;";       

				 }
			 }
		 }
		 PreparedStatement statement = connLOCAL.prepareStatement(agindua);
         
         ResultSet result = statement.executeQuery();
         int val=Integer.MIN_VALUE;
         while(result.next()){
        	 val = result.getInt("value");
         }
         
		return val;
	 }
	 
	 
	 public	 Boolean setValue(int mode, String variable, int value) throws SQLException{
	     String agindua="Update concurrency_control.variables set value="+value+" where name='"+variable+"';";       
		 PreparedStatement statement = connLOCAL.prepareStatement(agindua);
		 statement.executeUpdate();
		// conn.commit();
         return true;
	 }


	
		
	public boolean procedureA(String name, int i, int myShareMode, int myExclusiveMode) {
		int xValue, tValue, aValue, yValue;
			try{
				xValue = getValue(EXCLUSIVE_MODE,X);
				xValue = xValue+1;
				setValue(EXCLUSIVE_MODE,X,xValue);
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + X + "," + Integer.toString(xValue-1) + "," + Integer.toString(xValue)+");");
				tValue = getValue(EXCLUSIVE_MODE,T);
				aValue = getValue(EXCLUSIVE_MODE,A);
				yValue = getValue(SHARE_MODE,Y);
				tValue = tValue+yValue;
				aValue = aValue+yValue ;
				setValue(EXCLUSIVE_MODE,T,tValue);
				setValue(EXCLUSIVE_MODE,A,aValue);
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + T + "," + Integer.toString(tValue-yValue) + "," + Integer.toString(tValue)+");");
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + A + "," + Integer.toString(aValue-yValue) + "," + Integer.toString(aValue)+");");
				//conn.commit();
				System.out.println("END TRANSACTION " + name + Integer.toString(i+1));
				return true;
			}
			catch (Exception e) {
				e.printStackTrace();
				try {
					connLOCAL.rollback();
					System.out.println(name+ Integer.toString(i+1)+" rollback");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				return false;
		}
	}
		public boolean procedureB(String name, int i, int myShareMode, int myExclusiveMode) {
			int yValue, tValue, bValue, zValue;
			try{
				yValue = getValue(EXCLUSIVE_MODE,Y);
				yValue = yValue+1;
				setValue(EXCLUSIVE_MODE,Y,yValue);
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + Y + "," + Integer.toString(yValue-1) + "," + Integer.toString(yValue)+");");
				tValue = getValue(EXCLUSIVE_MODE,T);
				bValue = getValue(EXCLUSIVE_MODE,B);
				zValue = getValue(SHARE_MODE,Z);
				tValue = tValue+zValue;
				bValue = bValue+zValue ;
				setValue(EXCLUSIVE_MODE,T,tValue);
				setValue(EXCLUSIVE_MODE,B,bValue);
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + T + "," + Integer.toString(tValue-zValue) + "," + Integer.toString(tValue)+");");
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + B + "," + Integer.toString(bValue-zValue) + "," + Integer.toString(bValue)+");");
				//conn.commit();
				System.out.println("END TRANSACTION " + name + Integer.toString(i+1));
				return true;
			}catch (Exception e) {
				e.printStackTrace();
				try {
					connLOCAL.rollback();
					System.out.println(name+ Integer.toString(i+1)+" rollback");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				return false;
			}
		}
		public boolean procedureC(String name, int i, int myShareMode, int myExclusiveMode) {
			int zValue, tValue, cValue, xValue;
			try{
				zValue = getValue(EXCLUSIVE_MODE,Z);
				zValue = zValue+1;
				setValue(EXCLUSIVE_MODE,Z,zValue);
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + Z + "," + Integer.toString(zValue-1) + "," + Integer.toString(zValue)+");");
				tValue = getValue(EXCLUSIVE_MODE,T);
				cValue = getValue(EXCLUSIVE_MODE,C);
				xValue = getValue(SHARE_MODE,X);
				tValue = tValue+xValue;
				cValue = cValue+xValue ;
				setValue(EXCLUSIVE_MODE,T,tValue);
				setValue(EXCLUSIVE_MODE,C,cValue);
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + T + "," + Integer.toString(tValue-xValue) + "," + Integer.toString(tValue)+");");
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + C + "," + Integer.toString(cValue-xValue) + "," + Integer.toString(cValue)+");");
				//conn.commit();
				System.out.println("END TRANSACTION " + name + Integer.toString(i+1));
				return true;
			}catch (Exception e) {
				e.printStackTrace();
				try {
					connLOCAL.rollback();
					System.out.println(name+ Integer.toString(i+1)+" rollback");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				return false;
			}
		}
		public boolean procedureD(String name, int i, int myShareMode, int myExclusiveMode) {
			int tValue, dValue, zValue, xValue;
				
			try{
				tValue=getValue(EXCLUSIVE_MODE,T);
				dValue=getValue(EXCLUSIVE_MODE,D);
				zValue=getValue(SHARE_MODE,Z);
				tValue=tValue+zValue;
				dValue=dValue+zValue;
				setValue(EXCLUSIVE_MODE,T,tValue);
				setValue(EXCLUSIVE_MODE,D,dValue);
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + T + "," + Integer.toString(tValue-zValue) + "," + Integer.toString(tValue)+");");
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + D + "," + Integer.toString(dValue-zValue) + "," + Integer.toString(dValue)+");");
				xValue=getValue(EXCLUSIVE_MODE,X);
				xValue=xValue-1;
				setValue(EXCLUSIVE_MODE, X, xValue);
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + X + "," + Integer.toString(xValue+1) + "," + Integer.toString(xValue)+");");
				//conn.commit();
				System.out.println("END TRANSACTION " + name + Integer.toString(i+1));
				return true;
			}catch (Exception e) {
				e.printStackTrace();
				try {
					connLOCAL.rollback();
					System.out.println(name+ Integer.toString(i+1)+" rollback");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				return false;
			}
			
		}


		public boolean procedureE(String name, int i, int myShareMode, int myExclusiveMode) {
			int tValue, eValue, xValue, yValue;
			
			try{
				tValue=getValue(EXCLUSIVE_MODE,T);
				eValue=getValue(EXCLUSIVE_MODE,E);
				xValue=getValue(SHARE_MODE,X);
				tValue=tValue+xValue;
				eValue=eValue+xValue;
				setValue(EXCLUSIVE_MODE,T,tValue);
				setValue(EXCLUSIVE_MODE,E,eValue);
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + T + "," + Integer.toString(tValue-xValue) + "," + Integer.toString(tValue)+");");
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + E + "," + Integer.toString(eValue-xValue) + "," + Integer.toString(eValue)+");");
				yValue=getValue(EXCLUSIVE_MODE,Y);
				yValue=yValue-1;
				setValue(EXCLUSIVE_MODE, Y, yValue);
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + Y + "," + Integer.toString(yValue+1) + "," + Integer.toString(yValue)+");");
				//conn.commit();
				System.out.println("END TRANSACTION " + name + Integer.toString(i+1));
				return true;
			}catch (Exception e) {
				e.printStackTrace();
				try {
					connLOCAL.rollback();
					System.out.println(name+ Integer.toString(i+1)+" rollback");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				return false;
			}
		}


		public boolean procedureF(String name, int i, int myShareMode, int myExclusiveMode) {
			int tValue, fValue, yValue, zValue;
			
			try{
				tValue=getValue(EXCLUSIVE_MODE,T);
				fValue=getValue(EXCLUSIVE_MODE,F);
				yValue=getValue(SHARE_MODE,Y);
				tValue=tValue+yValue;
				fValue=fValue+yValue;
				setValue(EXCLUSIVE_MODE,T,tValue);
				setValue(EXCLUSIVE_MODE,F,fValue);
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + T + "," + Integer.toString(tValue-yValue) + "," + Integer.toString(tValue)+");");
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + F + "," + Integer.toString(fValue-yValue) + "," + Integer.toString(fValue)+");");
				zValue=getValue(EXCLUSIVE_MODE,Z);
				zValue=zValue-1;
				setValue(EXCLUSIVE_MODE, Z, zValue);
				System.out.println("WRITE(" + name +Integer.toString(i+1) + "," + Z + "," + Integer.toString(zValue+1) + "," + Integer.toString(zValue)+");");
				//conn.commit();
				System.out.println("END TRANSACTION " + name + Integer.toString(i+1));
				return true;
			}catch (Exception e) {
				e.printStackTrace();
				try {
					connLOCAL.rollback();
					System.out.println(name+ Integer.toString(i+1)+" rollback");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				return false;
			}
		}
		public void synchronyze() {
			int barrierValue;
			increaseBarrier();
			barrierValue=getBarrierValue();
			while(barrierValue<Data.NUMBER_OF_THREADS){
				try {
					Thread.sleep(ThreadLocalRandom.current().nextInt(1,11));
					barrierValue = getBarrierValue();
				}	catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		public void increaseBarrier(){
			try {
				st.executeUpdate("update concurrency_control.variables set value=value+1 where name='M';");
				connLOCAL.commit();
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		public void decreaseBarrier(){
			try {
				st.executeUpdate("update concurrency_control.variables set value=value-1 where name='M';");
				connLOCAL.commit();
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		public int getBarrierValue(){
			int mValue=0;
			try {
				ResultSet rs = st.executeQuery("select value from concurrency_control.variables where name = 'M';");
				connLOCAL.commit();
				while (rs.next()) {
					mValue=rs.getInt("value");
				}
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			return mValue;
		}

		public void finish() {
			decreaseBarrier();
		}
		
		public Boolean showInitialValues() {
			try {
				System.out.println("Initial value of " + X + ": " + Integer.toString(getValue(NONLOCKING, X)));
				System.out.println("Initial value of " + Y + ": " + Integer.toString(getValue(NONLOCKING, Y)));
				System.out.println("Initial value of " + Z + ": " + Integer.toString(getValue(NONLOCKING, Z)));
				System.out.println("Initial value of " + T + ": " + Integer.toString(getValue(NONLOCKING, T)));
				System.out.println("Initial value of " + A + ": " + Integer.toString(getValue(NONLOCKING, A)));
				System.out.println("Initial value of " + B + ": " + Integer.toString(getValue(NONLOCKING, B)));
				System.out.println("Initial value of " + C + ": " + Integer.toString(getValue(NONLOCKING, C)));
				System.out.println("Initial value of " + D + ": " + Integer.toString(getValue(NONLOCKING, D)));
				System.out.println("Initial value of " + E + ": " + Integer.toString(getValue(NONLOCKING, E)));
				System.out.println("Initial value of " + F + ": " + Integer.toString(getValue(NONLOCKING, F)));
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		public Boolean showFinalValues() {
			int barrierValue;
			barrierValue=getBarrierValue();
			
			while(barrierValue<1) {
				try {
					Thread.sleep(ThreadLocalRandom.current().nextInt(1,11));
					barrierValue=getBarrierValue();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			while(barrierValue>0) {
				try {
					Thread.sleep(ThreadLocalRandom.current().nextInt(1,11));
					barrierValue=getBarrierValue();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				System.out.println("Final value of " + X + ": " + Integer.toString(getValue(NONLOCKING, X)));
				System.out.println("Final value of " + Y + ": " + Integer.toString(getValue(NONLOCKING, Y)));
				System.out.println("Final value of " + Z + ": " + Integer.toString(getValue(NONLOCKING, Z)));
				System.out.println("Final value of " + T + ": " + Integer.toString(getValue(NONLOCKING, T)));
				System.out.println("Final value of " + A + ": " + Integer.toString(getValue(NONLOCKING, A)));
				System.out.println("Final value of " + B + ": " + Integer.toString(getValue(NONLOCKING, B)));
				System.out.println("Final value of " + C + ": " + Integer.toString(getValue(NONLOCKING, C)));
				System.out.println("Final value of " + D + ": " + Integer.toString(getValue(NONLOCKING, D)));
				System.out.println("Final value of " + E + ": " + Integer.toString(getValue(NONLOCKING, E)));
				System.out.println("Final value of " + F + ": " + Integer.toString(getValue(NONLOCKING, F)));
				
				System.out.println("Expected final value of " + X + ": " + Integer.toString(0));
				System.out.println("Expected final value of " + Y + ": " + Integer.toString(0));
				System.out.println("Expected final value of " + Z + ": " + Integer.toString(0));
				System.out.println("Expected final value of " + T + ": " + Integer.toString(getValue(NONLOCKING, A)+getValue(NONLOCKING, B)+getValue(NONLOCKING, C)+getValue(NONLOCKING, D)+getValue(NONLOCKING, E)+getValue(NONLOCKING, F)));
				
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}

	}
	
	
	
	