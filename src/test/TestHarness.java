package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public abstract class TestHarness {

	private JTextArea myArea;
	
	public TestHarness(){
		myArea = setUpDisplay();
		runTest();
	}
	
	public abstract void runTest();

	public void outPrintln(String s) {
		myArea.setForeground(Color.BLACK);
		myArea.append(s + "\n");
	}

	public void errPrintln(String str) {
		myArea.setForeground(Color.red);
		myArea.append(str +"\n");
		
	}

	private JTextArea setUpDisplay() {
		JFrame frame = new JFrame(getTestName());
		JTextArea area = new JTextArea();
		JScrollPane panel = new JScrollPane(area);
		panel.setPreferredSize(new Dimension(500, 300));

		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		return area;
	}

	public abstract String getTestName();
}
