package oldnewstuff.view;

import java.io.File;
import java.util.Map;

import javax.swing.JPanel;


public interface Saveable<T> {

	public T getObjectToSave();
	
	public boolean shouldBeSaved();
	
	public Map<String, Object> getSaveParameters();

}
