package errors;

import java.util.ArrayList;

import javax.swing.JOptionPane;


public class BooleanWrapper {
	
	public boolean state;
	String myMessage;
	
	public BooleanWrapper(boolean errorOccurred, String message){
		state = errorOccurred;
		myMessage = message;
	}
	
	public BooleanWrapper(boolean b) {
		this(b, "");
	}

	
	public static BooleanWrapper combineWrappers(boolean desired, BooleanWrapper ... wrappers) {
		for (BooleanWrapper bw: wrappers){
			if (desired != bw.state)
				return bw;
		}
		if (wrappers.length == 0)
			return new BooleanWrapper(desired);
		return wrappers[wrappers.length-1];
		
	}
	
	public static BooleanWrapper combineWrappers(BooleanWrapper ... wrappers) {
		return combineWrappers(true, wrappers);
		
	}

	public String getMessage(){
		return myMessage;
	}
	
	public String toString(){
		return this.getMessage();
	}

	public boolean isTrue() {
		return state;
	}

	public boolean isError() {
		return !state;
	}
	
	public static String createErrorLog(BooleanWrapper ... errors) {
		StringBuilder sb = new StringBuilder();
		for (BooleanWrapper error: errors){
			if (error.isError())
				sb.append(error.getMessage() + "\n");
		}
		return sb.toString();
		
	}

}
