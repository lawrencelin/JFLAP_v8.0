package view.undoing;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import model.undo.UndoKeeperListener;
import util.view.ActionLinkedButton;
import debug.JFLAPDebug;

public abstract class UndoRelatedButton extends ActionLinkedButton implements UndoKeeperListener {

	private ImageIcon myIcon;
	private boolean amUsingIcon;


	public UndoRelatedButton(UndoRelatedAction a, boolean useIcon) {
		super(a);
		amUsingIcon = useIcon;
		a.getKeeper().addUndoListener(this);
	}


	public abstract String getIconFilename();
	
	@Override
	public String getText() {
		return amUsingIcon ? "" : super.getText();
	}

	@Override
	public Icon getIcon() {
		if (!amUsingIcon)
			return null;
		if (myIcon == null){
			URL url = getClass().getResource(getIconFilename());
			Image img = Toolkit.getDefaultToolkit().getImage(url);
			myIcon = new ImageIcon(img);
		}
		return myIcon;
	}

	@Override
	public void keeperStateChanged() {
		updateEnabled();
	}
	
}
