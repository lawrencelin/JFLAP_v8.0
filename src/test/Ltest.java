package test;

import java.util.HashMap;
import java.util.Map;

import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.Variable;
import model.lsystem.CommandAlphabet;
import model.lsystem.Expander;
import model.lsystem.LSystem;
import model.lsystem.commands.LSystemCommand;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import debug.JFLAPDebug;

public class Ltest {
	public static void main (String[] args){
		Grammar gram = new Grammar();
		ProductionSet prods = gram.getProductionSet();
		Variable zero = new Variable("0"), one = new Variable("1"), hash = new Variable("#"),
				right = new Variable(">"), left = new Variable("<");
		CommandAlphabet com = new CommandAlphabet();
		LSystemCommand minus = com.getLeftYaw(), g = com.getDraw(),
				plus = com.getRightYaw(), push = com.getPush(), pop = com.getPop();
		
		
		
		prods.add(new Production(new Symbol[]{zero, minus, left}, left));
		prods.add(new Production(new Symbol[]{zero, right, hash}, left));
		prods.add(new Production(new Symbol[]{zero, right, minus}, minus));
		prods.add(new Production(new Symbol[]{one, hash, left}, right));
		prods.add(new Production(new Symbol[]{one, minus, left}, minus));
		prods.add(new Production(new Symbol[]{one, right, minus}, right));
		SymbolString axiom = new SymbolString(hash, right, minus, minus, minus, minus, minus, hash);
		Map<String, String> parameters = new HashMap<String, String>();
		LSystem lsys = new LSystem(axiom, gram, parameters);
		
		Expander ex = new Expander(lsys);
		
		for(int i=0; i<10; i++)
		JFLAPDebug.print(ex.expansionForLevel(i));
	}
}
