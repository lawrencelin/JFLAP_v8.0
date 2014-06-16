package file;

import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;

import file.xml.XMLCodec;

public class BasicFileChooser extends JFileChooser {

	public BasicFileChooser(FileFilter filter){
		super(System.getProperties().getProperty("user.dir"));
		this.setFileFilter(filter);
	}
}
