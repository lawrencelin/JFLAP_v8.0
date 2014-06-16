package oldnewstuff.view.menus.old.creation;


import java.util.Arrays;
import java.util.List;

import javax.swing.JMenuItem;

import oldview.menus.PrimaryMenuCreator;

import jflap.actions.LSystemDisplayAction;
import jflap.controller.JFLAPController;




public class LSystemMenuCreator extends PrimaryMenuCreator {

	@Override
	public List<JMenuItem> getInputMenuComponents() {
		return Arrays.asList(new JMenuItem[]{new JMenuItem( new LSystemDisplayAction())});
	}

	@Override
	public List<JMenuItem> getModifyMenuComponents() {
		return null;
	}

	@Override
	public List<JMenuItem> getTestMenuComponents() {
		return null;
	}

	@Override
	public List<JMenuItem> getConvertMenuComponents() {
		return null;
	}

}
