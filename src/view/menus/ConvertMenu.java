package view.menus;

import java.awt.Component;

import javax.swing.JMenu;

import view.action.automata.CombineAutomataAction;
import view.action.automata.DFAtoREAction;
import view.action.automata.FSAtoRegGrammarAction;
import view.action.automata.MinimizeDFAAction;
import view.action.automata.NFAtoDFAAction;
import view.action.automata.PDAtoCFGAction;
import view.action.automata.StayOptionRemoveAction;
import view.action.automata.TMtoUnrestrictedGrammarAction;
import view.action.automata.TrapStateAction;
import view.action.grammar.conversion.CFGtoPDALLAction;
import view.action.grammar.conversion.CFGtoPDALRAction;
import view.action.grammar.conversion.CNFTransformAction;
import view.action.grammar.conversion.RegGrammarToFSAAction;
import view.action.regex.REtoFAAction;
import view.automata.views.AutomatonView;
import view.automata.views.FSAView;
import view.automata.views.MultiTapeTMView;
import view.automata.views.PDAView;
import view.environment.JFLAPEnvironment;
import view.environment.TabChangeListener;
import view.environment.TabChangedEvent;
import view.grammar.GrammarView;
import view.regex.RegexView;

public class ConvertMenu extends JMenu implements TabChangeListener {

	public ConvertMenu(JFLAPEnvironment e) {
		super("Convert");
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
		
		if(!(view instanceof GrammarView || view instanceof AutomatonView
				|| view instanceof RegexView
				))
			return;
		setVisible(true);
		
		if(view instanceof GrammarView){
			GrammarView v = (GrammarView) view;
			this.add(new CFGtoPDALLAction(v));
			this.add(new CFGtoPDALRAction(v));
			this.add(new RegGrammarToFSAAction(v));
			this.add(new CNFTransformAction(v));
		}
		
		if(view instanceof AutomatonView){
			AutomatonView v = (AutomatonView) view;
			if(view instanceof FSAView){
				this.add(new NFAtoDFAAction((FSAView) v));
				this.add(new FSAtoRegGrammarAction((FSAView) v));
				this.add(new DFAtoREAction((FSAView) v));
				this.add(new TrapStateAction((FSAView) v));
				this.add(new MinimizeDFAAction((FSAView) v));
			}
			if(view instanceof PDAView)
				this.add(new PDAtoCFGAction((PDAView) v));
			if(view instanceof MultiTapeTMView){
				MultiTapeTMView mtv = (MultiTapeTMView) v;
				this.add(new TMtoUnrestrictedGrammarAction(mtv));
				
				if(mtv.getDefinition().getNumTapes() == 1)
					this.add(new StayOptionRemoveAction(mtv));
			}
			this.add(new CombineAutomataAction(v));
		}
		
		if(view instanceof RegexView){
			this.add(new REtoFAAction((RegexView) view));
		}
	}
}
