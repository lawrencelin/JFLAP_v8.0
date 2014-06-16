package file;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import util.JFLAPConstants;

public class TxtFileFilter extends FileFilter {

	@Override
	public String getDescription() {
		return "Text files (.txt)";
	}

	@Override
	public boolean accept(File f) {
		return f.getName().endsWith(".txt")
				|| f.isDirectory();

	}
}
