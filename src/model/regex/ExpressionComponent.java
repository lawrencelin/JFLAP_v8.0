package model.regex;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.event.ChangeEvent;

import debug.JFLAPDebug;

import errors.BooleanWrapper;
import model.automata.InputAlphabet;
import model.change.events.ExpressionChangedEvent;
import model.formaldef.UsesSymbols;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.regex.operators.Operator;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class ExpressionComponent extends FormalDefinitionComponent implements UsesSymbols{

	
	private SymbolString myExpression;

	public ExpressionComponent(Symbol ... symbols){
		myExpression = new SymbolString(symbols);
	}
	
	public ExpressionComponent(){
		myExpression = null;
	}
	
	public ExpressionComponent(SymbolString exp) {
		this (exp.toArray(new Symbol[0]));
	}

	public boolean setTo(SymbolString exp){
		ChangeEvent e = new ExpressionChangedEvent(this, myExpression, exp);
		if (myExpression == null) myExpression = new SymbolString();
		if(myExpression.equals(exp))
			return false;
		boolean changed = myExpression.setTo(exp);
		distributeChange(e);
		return changed;
	}
	
	public SymbolString getExpression(){
		return myExpression == null ? null : new SymbolString(myExpression);
	}
	
	@Override
	public String getDescriptionName() {
		return "Expression";
	}

	@Override
	public String getDescription() {
		return "The Expression component of a Regular Expression definition";
	}

	@Override
	public Character getCharacterAbbr() {
		return 'E';
	}

	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(myExpression != null, "The expression is not yet set.");
	}

	@Override
	public ExpressionComponent copy() {
		return new ExpressionComponent(myExpression);
	}

	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		if ( myExpression != null){
			if (a instanceof InputAlphabet){
				Set<Symbol> symbols = new TreeSet<Symbol>(myExpression);
				for (Symbol s:  symbols.toArray(new Symbol[0])){
					if (s  instanceof Operator)
						symbols.remove(s);
				}
				return symbols;
			}else if(a instanceof OperatorAlphabet){
				Set<Symbol> symbols = new TreeSet<Symbol>(myExpression);
				for (Symbol s:  symbols.toArray(new Symbol[0])){
					if (!(s instanceof Operator))
						symbols.remove(s);
				}
				return symbols;
			}
		}
		return new TreeSet<Symbol>();
	}

	@Override
	public boolean applySymbolMod(String from, String to) {
		boolean changed = false;
		for (Symbol s: myExpression){
			if (s.getString() == from){
				s.setString(to);
				changed = true;
			}
		}
		distributeChanged();
		return changed;
	}

	@Override
	public boolean purgeOfSymbols(Alphabet a, Collection<Symbol> s) {
		if (myExpression != null && 
				(a instanceof InputAlphabet || a instanceof OperatorAlphabet)){
			return myExpression.removeAll(s);
		}
		return false;
	}

	@Override
	public void clear() {
		myExpression = null;
	}

	@Override
	public String toString() {
		return getDescriptionName() + ": " + myExpression.toString();
	}
}
