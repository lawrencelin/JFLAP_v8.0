package view;

import java.awt.Component;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import model.automata.Automaton;
import model.automata.State;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.moore.MooreMachine;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.grammar.Grammar;
import model.graph.TransitionGraph;
import model.lsystem.LSystem;
import model.pumping.ContextFreePumpingLemma;
import model.pumping.PumpingLemma;
import model.pumping.RegularPumpingLemma;
import model.regex.RegularExpression;
import view.automata.Note;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.views.AutomatonView;
import view.automata.views.BlockTMView;
import view.automata.views.FSAView;
import view.automata.views.MealyView;
import view.automata.views.MooreView;
import view.automata.views.MultiTapeTMView;
import view.automata.views.PDAView;
import view.grammar.GrammarView;
import view.lsystem.LSystemInputView;
import view.pumping.CFPumpingLemmaChooser;
import view.pumping.CompCFPumpingLemmaInputView;
import view.pumping.CompRegPumpingLemmaInputView;
import view.pumping.HumanCFPumpingLemmaInputView;
import view.pumping.HumanRegPumpingLemmaInputView;
import view.pumping.PumpingLemmaChooserView;
import view.pumping.PumpingLemmaInputView;
import view.pumping.RegPumpingLemmaChooser;
import view.regex.RegexView;
import debug.JFLAPDebug;
import file.xml.XMLCodec;
import file.xml.graph.AutomatonEditorData;
//import view.sets.SetsView;

public class ViewFactory {

	private static Map<Class, Class<? extends Component>> myClassToComponent;

	static{
		myClassToComponent = new HashMap<Class, Class<? extends Component>>();
		myClassToComponent.put(Grammar.class, GrammarView.class);
		myClassToComponent.put(CFPumpingLemmaChooser.class, PumpingLemmaChooserView.class);
		myClassToComponent.put(RegPumpingLemmaChooser.class, PumpingLemmaChooserView.class);
		myClassToComponent.put(LSystem.class, LSystemInputView.class);
		myClassToComponent.put(FiniteStateAcceptor.class, FSAView.class);
		myClassToComponent.put(PushdownAutomaton.class, PDAView.class);
		myClassToComponent.put(MealyMachine.class, MealyView.class);
		myClassToComponent.put(MooreMachine.class, MooreView.class);
		myClassToComponent.put(MultiTapeTuringMachine.class, MultiTapeTMView.class);
		myClassToComponent.put(BlockTuringMachine.class, BlockTMView.class);
		myClassToComponent.put(RegularExpression.class, RegexView.class);
//		myClassToComponent.put(SetsManager.class, SetsView.class);

	}
	
	public static Component createView(File f) {
		return createView(new XMLCodec().decode(f));
	}

	public static Component createView(Object decode) {		
		if(decode instanceof PumpingLemma)
			return createPumpingLemmaView((PumpingLemma) decode);
		if(decode instanceof AutomatonEditorData)
			return createAutomataView((AutomatonEditorData) decode);
		Class argClass = decode.getClass();
		Class<? extends Component> viewClass = myClassToComponent.get(argClass);
//		JFLAPDebug.print(argClass.getGenericSuperclass()+" "+viewClass);
		try {
			return viewClass.getConstructor(argClass).newInstance(decode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}
	
	public static Component createPumpingLemmaView(PumpingLemma pl){
		String player = pl.getFirstPlayer();
		
		if(pl instanceof RegularPumpingLemma){            
            RegularPumpingLemma reg = (RegularPumpingLemma) pl;
            PumpingLemmaInputView inputPane;
            if (player.equals(PumpingLemma.COMPUTER))
            	inputPane = new CompRegPumpingLemmaInputView(reg);                    	
            else
            	inputPane = new HumanRegPumpingLemmaInputView(reg);
            inputPane.update();
            return inputPane;
		}
		
		//Context-Free Pumping Lemma
		
         ContextFreePumpingLemma cf = (ContextFreePumpingLemma) pl;
         PumpingLemmaInputView inputPane;            
         if (player.equals(PumpingLemma.COMPUTER))            	
         	inputPane = new CompCFPumpingLemmaInputView(cf);
         else
         	inputPane = new HumanCFPumpingLemmaInputView(cf);
         inputPane.update();
         return inputPane;
	}
	
	public static AutomatonView createAutomataView(AutomatonEditorData data){
		TransitionGraph graph = data.getGraph();
		Map<Point2D, String> labels = data.getLabels(), notes = data.getNotes();
		
		Automaton auto = graph.getAutomaton();
		AutomatonView view = (AutomatonView) createView(auto);
		AutomatonEditorPanel panel = (AutomatonEditorPanel) view.getCentralPanel();
		
		panel.setGraph(graph);
		
		for(Point2D p : notes.keySet()){
			Point basic = new Point((int) p.getX(), (int) p.getY());
			panel.addNote(new Note(panel, basic, notes.get(p)));
		}
		for(Point2D p : labels.keySet()){
			Point basic = new Point((int) p.getX(), (int) p.getY());
			panel.addStateLabel((State) graph.vertexForPoint(p), new Note(panel, basic), labels.get(p));
		}
		panel.stopAllEditing();
		return view;
	}

}
