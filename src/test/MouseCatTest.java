package test;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.algorithms.conversion.gramtoauto.CFGtoPDAConverterLL;
import model.algorithms.testinput.simulate.AutoSimulator;
import model.algorithms.testinput.simulate.SingleInputSimulator;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Terminal;
import model.grammar.TerminalAlphabet;
import model.grammar.Variable;
import model.grammar.VariableAlphabet;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;

public class MouseCatTest {

	
	private static final String[] VAR_STRINGS = {"List", "Statement", "Direction"};
	private static final String[] TERM_STRINGS = {"size", "int", "begin", ";", "cat", "var", "mouse", "hole",
													"move", "clockwise", "repeat", "end", "north", "south",
														"east", "west"};
	private static JTextArea myArea;

	public static void main(String[] args) throws FileNotFoundException {
		myArea = setUpDisplay();
		//Build the grammar
		Grammar g = buildMouseCatGrammar();
		OutPrintln(g.toString());
		
		
		//convert the grammar to a PDA
		CFGtoPDAConverterLL converter = new CFGtoPDAConverterLL(g);
		converter.stepToCompletion();
		PushdownAutomaton pda = converter.getConvertedAutomaton();
		OutPrintln(pda.toString());
		
		//read input in from a test program, initialize simulator
		SymbolString input = Symbolizers.symbolize(createInput(),pda);
		AutoSimulator sim = new AutoSimulator(pda, SingleInputSimulator.DEFAULT);
		sim.beginSimulation(input);
		
		//run simulator
		boolean accepted = sim.getNextAccept() != null;
		OutPrintln("Accepted: " + accepted);
		
		
	}

	private static String createInput() throws FileNotFoundException {
		String string = "";
		File f = new File("src/test/jzg.mc");
		Scanner scan = new Scanner(f);
		while (scan.hasNextLine()){
			string += scan.nextLine();
		}
		return string;
	}

	private static Grammar buildMouseCatGrammar() {
		Grammar g = new Grammar();
		
		GroupingPair gp = new GroupingPair('<', '>');
		
		g.setVariableGrouping(gp);
		
		Variable program = new Variable(gp.creatGroupedString("Program"));
		Variable list = new Variable(gp.creatGroupedString("List"));
		Variable statement = new Variable(gp.creatGroupedString("Statement"));
		Variable direction = new Variable(gp.creatGroupedString("Direction"));

		g.getVariables().addAll(new Variable[]{program, list, statement, direction});
		g.getStartVariable().setString(program.toString());
		
		VariableAlphabet vars = g.getVariables();
		
		for (String s: VAR_STRINGS){
			vars.add(new Variable(gp.creatGroupedString(s)));
		}
		
		Terminal size = new Terminal("size");
		Terminal integer = new Terminal("int");
		Terminal cat = new Terminal("cat");
		Terminal var = new Terminal("var");
		Terminal begin = new Terminal("begin");
		Terminal halt = new Terminal("halt");
		Terminal mouse = new Terminal("mouse");
		Terminal hole = new Terminal("hole");
		Terminal move = new Terminal("move");
		Terminal clockwise = new Terminal("clockwise");
		Terminal repeat = new Terminal("repeat");
		Terminal end = new Terminal("end");
		Terminal north = new Terminal("north");
		Terminal south = new Terminal("south");
		Terminal east = new Terminal("east");
		Terminal west = new Terminal("west");
		Terminal eol = new Terminal(";");
		
		Terminal[] terms = new Terminal[]{size, cat, var, begin, halt, mouse, 
				hole, move, clockwise, repeat, end, north, south, east, west};
		
		g.getTerminals().addAll(Arrays.asList(terms));
		
		
		createAndAddProduction(g, program, size, integer, integer, begin, list, halt );
		createAndAddProduction(g, statement, eol);
		createAndAddProduction(g, list, statement, eol);
		createAndAddProduction(g, statement, cat, var, integer, integer, direction);
		createAndAddProduction(g, statement, mouse, var, integer, integer, direction);
		createAndAddProduction(g, statement, hole,  integer, integer);
		createAndAddProduction(g, statement, move, var);
		createAndAddProduction(g, statement, move, var, integer);
		createAndAddProduction(g, statement, clockwise, var);
		createAndAddProduction(g, statement, repeat, integer, list, end);
		createAndAddProduction(g, direction, north);
		createAndAddProduction(g, direction, south);
		createAndAddProduction(g, direction, east);
		createAndAddProduction(g, direction, west);
		return g;
	}

	private static void createAndAddProduction(Grammar g, Variable var, Symbol ... symbols) {
		Production p = new Production(new SymbolString(var), new SymbolString(symbols));
		g.getProductionSet().add(p);
		
	}
	
	
	private static void OutPrintln(String s) {
		myArea.setForeground(Color.BLACK);
		myArea.append(s + "\n");
	}

	private static void ErrPrintln(String str) {
		myArea.setForeground(Color.red);
		myArea.append(str +"\n");
		
	}

	private static JTextArea setUpDisplay() {
		JFrame frame = new JFrame("JFLAP Test Print!");
		JTextArea area = new JTextArea();
		JScrollPane panel = new JScrollPane(area);

		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		return area;
	}
}
