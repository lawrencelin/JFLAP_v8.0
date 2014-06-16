package view.action.automata;

import java.awt.event.ActionEvent;

import view.automata.views.FSAView;

public class MinimizeDFAAction extends AutomatonAction{

	public MinimizeDFAAction(FSAView view) {
		super("Minimize DFA", view);
		this.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
