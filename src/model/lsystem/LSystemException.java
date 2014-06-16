package model.lsystem;

import errors.JFLAPException;

/**
 * Exception class for anything having to do with L-Systems.
 * @author Ian McMahon
 *
 */
public class LSystemException extends JFLAPException {
	
	public LSystemException(){
		super();
	}
	
	public LSystemException(String arg0, Throwable arg1){
		super(arg0, arg1);
	}
	
	public LSystemException(String arg0){
		super(arg0);
	}
	
	public LSystemException(Throwable arg0){
		super(arg0);
	}
}
