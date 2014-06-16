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

package view.action.file.imagesave;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import universe.JFLAPUniverse;
import view.automata.views.AutomatonView;
import view.environment.JFLAPEnvironment;
import view.help.ImageDisplayComponent;
import view.lsystem.LSystemRenderView;
import file.FileJFLAPException;
import file.BasicFileChooser;

/**
 * This utility was created to factor out the massive amounts of common code in
 * the four Graphics saving action classes.
 * 
 * @author Ian McMahon
 */
public class SaveImageUtility {

	/**
	 * Saves view as the specified format and provides a file filter with the given description.
	 * 
	 * @param view Component to be saved as an image (if LSystem or Automata, will save the entire picture)
	 * @param description File Filter Description
	 * @param format Various acceptable formats for the FileChooser to recognize (will save any "invalid" filenames as format[0]
	 */
	public static void saveImage(Component view, String description,
			String... format) {
		JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();
		view = getCorrectComponent(view);
		
		BufferedImage bimg = new BufferedImage(view.getWidth(),
				view.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bimg.createGraphics();		
		paintView(view, g);


		FileFilter img = new FileNameExtensionFilter(description, format);
		BasicFileChooser chooser = new BasicFileChooser(img);
		chooser.addChoosableFileFilter(img);
		chooser.addChoosableFileFilter(new AcceptAllFileFilter());
		
		int n = chooser.showSaveDialog(env);
		if (n != JFileChooser.APPROVE_OPTION)
			return;

		while (n == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();

			if (!new FileNameExtensionFilter(description, format).accept(file)) // only append if the chosen name is not acceptable
				file = new File(file.getAbsolutePath() + "." + format[0]);
			if (file.exists()) {
				int confirm = env.showConfirmDialog("File exists. Overwrite file?");

				if (confirm == JOptionPane.NO_OPTION) {
					n = chooser.showSaveDialog(env);
					continue;
				}
			}
			try {
				ImageIO.write(bimg, format[0], file);
				return;
			} catch (IOException ioe) {
				throw new FileJFLAPException("Save failed with error:\n"
						+ ioe.getMessage() + "\n" + "Save failed");
			}
		}
	}

	/**
	 * Will paint view or, in special cases, the graphic image of view to g to be saved as
	 * a BufferedImage.
	 * @param view Component to be painted.
	 * @param g Graphic to be painted on.
	 */
	private static void paintView(Component view, Graphics2D g) {
		if(view instanceof ImageDisplayComponent)	
			((ImageDisplayComponent) view).paintImage(g);
		else
			view.paint(g);
		g.setColor(Color.gray);
		g.draw(new Rectangle2D.Double(0, 0,
				view.getWidth() - 2, view.getHeight() - 1));
		
	}

	/**
	 * Replaces view with its image component if necessary (LSystem or Automata) to provide
	 * proper dimensions to the Graphics.
	 * @param view Component to be verified.
	 * @return view or view's internal element to be saved.
	 */
	private static Component getCorrectComponent(Component view) {
		// This does need to be implemented (correctly) to save images of
		// automata, I think it should with LSystems too. 
		// if (view instanceof EditingPanel){
		// view = ((EditorPane)view).getAutomatonPane();
		// }
		if(view instanceof LSystemRenderView)	
			return ((LSystemRenderView) view).getDisplayComponent();
		if(view instanceof AutomatonView)
			return ((AutomatonView) view).getCentralPanel();
		return view;
	}

}

/**
 * 
 * Java 6 has this, but not previous versions of java, so I'm writing it here.
 * 
 * @author Henry
 */

class FileNameExtensionFilter extends FileFilter {
	String[] myAcceptedFormats;
	String myDescription;

	public FileNameExtensionFilter(String description, String... formats) {
		myDescription = description;
		myAcceptedFormats = formats;
	}

	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		for (int i = 0; i < myAcceptedFormats.length; i++)
			if (f.getName().endsWith("." + myAcceptedFormats[i]))
				return true;
		return false;
	}

	public String getDescription() {
		return myDescription;
	}
}

class AcceptAllFileFilter extends FileFilter {
	public boolean accept(File f) {
		return true;
	}

	public String getDescription() {
		return "All files";
	}
}
