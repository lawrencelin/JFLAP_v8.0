package file;

import file.xml.XMLCodec;


public class XMLFileChooser extends BasicFileChooser {

	public XMLFileChooser(boolean save){
		super((save ? XMLCodec.getSaveFileFilter() : XMLCodec.getOpenFileFilter()));
	}
}
