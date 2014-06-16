package view.action.lsystem;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.security.KeyStore;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import model.lsystem.LSystemException;
import model.lsystem.LSystem;

import universe.JFLAPUniverse;
import util.JFLAPConstants;
import view.environment.JFLAPEnvironment;
import view.lsystem.LSystemRenderView;
import view.lsystem.LSystemInputView;

/**
 * Creates a LSystemRenderView from the LSystem of a given LSystemInputView.
 * 
 * @author Ian McMahon
 *
 */
public class LSystemRenderAction extends AbstractAction {
	private LSystemInputView myView;
	
	public LSystemRenderAction(LSystemInputView view){
		super("Render L-System");
		myView = view;
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D, JFLAPConstants.MAIN_MENU_MASK));
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		LSystem l = myView.getLSystem();
		
		if(l == null)
			return;
		if(l.getAxiom().size() == 0)
			throw new LSystemException("The axiom must have one or more symbols.");
		
		
		LSystemRenderView render = new LSystemRenderView(l);
		JFLAPEnvironment environ = JFLAPUniverse.getActiveEnvironment();
		environ.addSelectedComponent(render);
	}

}
