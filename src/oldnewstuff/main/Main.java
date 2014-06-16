/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package oldnewstuff.main;

import java.awt.event.KeyEvent;

import javax.swing.UIManager;

import universe.JFLAPUniverse;
import errors.ThrowableCatcher;

/**
 * This is the class that starts JFLAP.
 * 
 * @author Thomas Finley
 * @author Moti Ben-Ari
 * @modified by Kyung Min (Jason) Lee
 */

public class Main {
	
	private static boolean dontQuit;  // Don't quit when Quit selected
	
	public static boolean getDontQuit() {
		return dontQuit;
	}
	/**
	 * Starts JFLAP. This sets various system properties. If there are command
	 * line arguments, this will attempt to open them as JFLAP files. If there
	 * are no arguments, this will call on {@link jflap.actions.NewAction#showNew}
	 * to display a choice for a new structure.
	 * 
	 * @param args
	 *            the command line arguments, which may hold files to open
	 */
	public static void main(String[] args, boolean dont) {

        

		dontQuit=dont;
		
		try {
			System.setProperty("sun.awt.exception.handler",
					ThrowableCatcher.class.getName());
		} catch (SecurityException e) {
			System.err.println("Warning: could not set the "
					+ "AWT exception handler.");
		}		
		Thread.setDefaultUncaughtExceptionHandler(new ThrowableCatcher());
		
		// Make sure we're not some old version.
		try {
			String v = System.getProperty("java.specification.version");
			double version = Double.parseDouble(v) + 0.00001;
			if (version < 1.5) {
				javax.swing.JOptionPane.showMessageDialog(null,
						"Java 1.5 or higher required to run JFLAP!\n"
						+ "You appear to be running Java " + v + ".\n"
						+ "This program will now exit.");
				System.exit(0);
			}
		} catch (SecurityException e) {
			// Eh, that shouldn't happen.
		}
		
		// Apple is stupid.
		try {
			// Well, Apple WAS stupid...
			if (System.getProperty("os.name").startsWith("Mac OS")
					&& System.getProperty("java.specification.version").equals(
					"1.3"))
				System.setProperty("com.apple.hwaccel", "false");
		} catch (SecurityException e) {
			// Bleh.
		}
		// Sun is stupider.
		try {
			System.setProperty("java.util.prefs.syncInterval", "2000000");
		} catch (SecurityException e) {
			// Well, not key.
		}
		JFLAPUniverse.showMainMenu();
		if (args.length > 0) {
			if(args[0].equals("text")){
				
			}
			
			for (int i = 0; i < args.length; i++) {
//				Codec[] codecs = (Codec[]) JFLAPUniverse.CODEC_REGISTRY
//				.getDecoders().toArray(new Codec[0]);
//				try {
//					OpenAction.openFile(new File(args[i]), codecs);
//				} catch (ParseException e) {
//					System.err.println("Could not open " + args[i] + ": "
//							+ e.getMessage());
//				}
			}
		}
		//Makes it so that when a button is selected through tab or arrow keys,
		//hitting enter will trigger that one and NOT the "default" button.
		UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
		UIManager.put("OptionPane.cancelButtonMnemonic", "" + KeyEvent.VK_C);
		loadPreferences();
	}
	
	/**
	 * This method loads from the preferences file, if one exists.
	 */
	private static void loadPreferences() {
//		Profile current = Universe.curProfile;
//		String path = "";
//		try {
//			path = new File(".").getCanonicalPath();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		path = path + "/jflapPreferences.xml";
//		current.pathToFile = path;
//
//		if(new File(path).exists()){
//			File file = new File(path);
//			DocumentBuilderFactory factory = DocumentBuilderFactory
//			.newInstance();
//			DocumentBuilder builder;
//			try {
//				builder = factory.newDocumentBuilder(); Document doc;
//				try {
//					doc = builder.parse(file);
//					
//					//Set the empty string constant
//					Node parent = doc.getDocumentElement()
//					   .getElementsByTagName(current.EMPTY_STRING_NAME).item(0);
//					if (parent!=null) {
//						String empty = parent.getTextContent();
//						if(empty.equals(current.lambdaText)) 
//							current.setEmptyString(current.lambda);
//						else if(empty.equals(current.epsilonText)) 
//							current.setEmptyString(current.epsilon);
//					}
//					
//					//Then set the Turing final state constant
//					parent = doc.getDocumentElement()
//					   .getElementsByTagName(current.TURING_FINAL_NAME).item(0);
//					if (parent!=null) {
//						String turingFinal = parent.getTextContent();
//						if (turingFinal.equals("true"))
//							current.setTransitionsFromTuringFinalStateAllowed(true);
//                        else
//							current.setTransitionsFromTuringFinalStateAllowed(false);
//					}
//
//                    //set the Turing Acceptance ways.
//					parent = doc.getDocumentElement()
//					   .getElementsByTagName(current.ACCEPT_FINAL_STATE).item(0);
//					if (parent!=null) {
//						String acceptFinal = parent.getTextContent();
//						if (acceptFinal.equals("true"))
//							current.setAcceptByFinalState(true);
//                        else
//							current.setAcceptByFinalState(false);
//					}
//
//					parent = doc.getDocumentElement()
//					   .getElementsByTagName(current.ACCEPT_HALT).item(0);
//					if (parent!=null) {
//						String acceptHalt = parent.getTextContent();
//						if (acceptHalt.equals("true"))
//							current.setAcceptByHalting(true);
//                        else
//							current.setAcceptByHalting(false);
//
//					}
//
//                    //set the AllowStay option
//					parent = doc.getDocumentElement()
//					   .getElementsByTagName(current.ALLOW_STAY).item(0);
//					if (parent!=null) {
//						String allowStay = parent.getTextContent();
//						if (allowStay.equals("true"))
//							current.setAllowStay(true);
//                        else
//							current.setAllowStay(false);
//					}
//                    
//                    //Now set the Undo amount
//					parent = doc.getDocumentElement()
//					   .getElementsByTagName(current.UNDO_AMOUNT_NAME).item(0);
//					if (parent!=null) {
//                        String number = parent.getTextContent();
//                        current.setNumUndo(Integer.parseInt(number));
//					}
//
//				} catch (SAXException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}				
//			} catch (ParserConfigurationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}		
	}
}
