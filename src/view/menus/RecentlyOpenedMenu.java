package view.menus;

import java.io.File;
import java.util.List;

import javax.swing.JMenu;

import universe.preferences.JFLAPPreferences;
import universe.preferences.PreferenceChangeListener;
import view.action.QuickOpenAction;

public class RecentlyOpenedMenu extends JMenu implements PreferenceChangeListener{

	public RecentlyOpenedMenu(){
		super("Open recent...");
		JFLAPPreferences.addChangeListener(this);
		updateToFiles(JFLAPPreferences.getRecentlyOpenedFiles());
	}
	
	@Override
	public void preferenceChanged(String pref, Object val) {
		if (pref.equals(JFLAPPreferences.RECENT_CHANGED)){
			List<File> list = (List<File>) val;
			updateToFiles(list.toArray(new File[0]));
			
			
		}
	}

	private void updateToFiles(File[] array) {
		this.removeAll();
		for(File f: array){
			this.add(new QuickOpenAction(f));
		}
	}

}
