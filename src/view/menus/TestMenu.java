package view.menus;

import java.awt.Component;

import javax.swing.JMenu;

import view.action.automata.DFAEqualityAction;
import view.action.automata.HighlightEmptyTransAction;
import view.action.automata.HighlightNondeterminismAction;
import view.action.grammar.GrammarTypeTestAction;
import view.automata.views.AutomatonView;
import view.automata.views.FSAView;
import view.environment.JFLAPEnvironment;
import view.environment.TabChangeListener;
import view.environment.TabChangedEvent;
import view.grammar.GrammarView;

public class TestMenu extends JMenu implements TabChangeListener {

	public TestMenu(JFLAPEnvironment e) {
		super("Test");
		e.addTabListener(this);
		this.update(e.getCurrentView());
	}

	@Override
	public void tabChanged(TabChangedEvent e) {
		update(e.getCurrentView());
	}

	private void update(Component view) {
		this.removeAll();
		setVisible(false);

		if (!(view instanceof GrammarView || view instanceof AutomatonView))
			return;
		setVisible(true);

		if (view instanceof GrammarView) {
			GrammarView v = (GrammarView) view;
			 this.add(new GrammarTypeTestAction(v));
			 return;
		}
		AutomatonView v = (AutomatonView) view;
		
		if (view instanceof FSAView) {
			this.add(new DFAEqualityAction((FSAView) v));
		}
		this.add(new HighlightNondeterminismAction(v));
		this.add(new HighlightEmptyTransAction(v));
	}

}
