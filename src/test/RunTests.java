package test;

import java.util.Arrays;

import javax.swing.JOptionPane;

import debug.JFLAPDebug;

import errors.JFLAPError;
import errors.JFLAPException;

public class RunTests {

	public static Class[] myTests = new Class[]{
		PDATest.class,
		RegExTest.class, 
		FSATest.class, 
		GrammarTest.class,
		ParserTest.class,
		FileTester.class,
		BuildingBlockTesting.class,
		MooreAndMealyTest.class
	};
	
	public static void main(String[] args) {
		for (Class c: myTests)
			tryTest(c);
	}

	public static void tryTest(Class c) {
		try{
		System.out.println("Running: " + c);
		c.newInstance();
		}catch(Exception e){
			if (e instanceof JFLAPException){
				showMessage((JFLAPException) e);
				JFLAPDebug.printErr(e);
			}
			else
				e.printStackTrace();
		}
	}

	private static void showMessage(JFLAPException e) {
		JFLAPError.show(e.getMessage(), "UHOH!");
	}
}
