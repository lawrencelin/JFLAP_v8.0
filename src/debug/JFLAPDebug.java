package debug;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class JFLAPDebug {

	private static JTextArea myLABEL;

	static{
		JScrollPane pane = new JScrollPane();
		pane.setViewportView(myLABEL = new JTextArea());
		JFrame frame = new JFrame("DEBUG");
		pane.setPreferredSize(new Dimension(300, 500));
		frame.add(pane);
		frame.setLocation(1000, 2);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public static void print(Object message, int s){
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String printout = stackTraceElements[2+s].getClassName() + "." + 
							stackTraceElements[2+s].getMethodName() + "(" +
							"Line: " + stackTraceElements[2+s].getLineNumber() + ")\n" +
							((message == null) ? "" : "Message: " + message);
		
		System.out.println(printout);
		myLABEL.append(printout +"\n");
	}
	
	public static void print(Object message){
		print(message, 1);
	}
	
	public static void print(){
		print(null, 1);
	}

	public static void printErr(Throwable e) {
		e.printStackTrace();
		myLABEL.append("---------ERROR-----------\n" + e.getMessage() +"\n------------END------------\n");
	}
	
}
