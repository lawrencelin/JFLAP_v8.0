package model.formaldef.components.alphabets;


import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

import debug.JFLAPDebug;

import errors.BooleanWrapper;

import model.automata.InputAlphabet;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.SetComponent;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.rules.AlphabetRule;
import model.formaldef.rules.GroupingRule;
import model.formaldef.rules.applied.BaseRule;
import model.symbols.Symbol;



public abstract class Alphabet extends SetComponent<Symbol>{



	private Set<AlphabetRule> myRules;

	public Alphabet(){
		myRules = new TreeSet<AlphabetRule>();
		this.addRules(new BaseRule());
	}




	@Override
	public boolean removeAll(Collection<? extends Object> symbols) {
		this.checkRules(AlphabetActionType.REMOVE, symbols.toArray(new Symbol[0]));
		return super.removeAll(symbols);
	}

	public Symbol getSymbolForString(String cur) {
		for (Symbol s: this){
			if (s.getString().equals(cur))
				return s;
		}
		return null;
	}
	

	public boolean equals(Object o){
		if (!(o instanceof Alphabet))
			return false;
		Alphabet other = (Alphabet) o;
		return Arrays.equals(super.toArray(), other.toArray());

	}

	public BooleanWrapper isComplete() {
		return new BooleanWrapper(!this.isEmpty(), "The " + this.toString() + 
				" is incomplete because it is empty.");
	}


	public boolean addAll(Symbol... symbols) {
		return this.addAll(Arrays.asList(symbols));
	}

	@Override
	public boolean addAll(Collection<? extends Symbol> c) {
		this.checkRules(AlphabetActionType.ADD, c.toArray(new Symbol[0]));
		return super.addAll(c);
	}

	public boolean containsSymbolWithString(String... strings) {
		for	(String s: strings){
			if (!this.contains(new Symbol(s))) 
				return false; 
		}
		return true;
	}


	public Set<Character> getUniqueCharacters() {
		Set<Character> chars = new TreeSet<Character>();
		for (Symbol s: this){
			for (char c : s.getString().toCharArray()){
				chars.add(c);
			}
		}
		return chars;
	}


	public Symbol getFirstSymbolContaining(char ... chars) {
		for (Symbol s: this){
			if (s.containsCharacters(chars))
				return s;
		}
		return null;
	}

	@Override
	public Alphabet clone() {

		try {
			Alphabet alph = this.getClass().newInstance();
			for (Symbol s: this)
				alph.add((Symbol) s.copy());
			for (AlphabetRule rule: this.getRules())
				alph.addRules(rule);
			return alph;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AlphabetException("Error cloning the alphabet");
		}

	}

	/**
	 * Returns the "index" of the item in this Alphabet.
	 * 
	 * @param variable
	 *            the variable to find the row for
	 * @return the index of the symbol
	 * 				-1 if the symbol is not in the ALphabet
	 */
	public int getIndex(Symbol sym) {
		int index = this.size()-1;
		for (Symbol s : this){
			if (s.equals(sym))
				break;
			index--;
		}
		return index;	
	}


	public String[] getSymbolStringArray() {
		String[] strings = new String[this.size()];
		Iterator<Symbol> iter = this.iterator();
		for (int i = 0; i < strings.length; i++){
			strings[i] = iter.next().getString();
		}
		return strings;
	}

	public <T extends GroupingRule> GroupingRule getRuleOfClass(Class<T> clz) {
		for (AlphabetRule rule : this.getRules()){
			if (clz.isAssignableFrom(rule.getClass()))
				return clz.cast(rule);
		}
		return null;
	}

	public abstract String getSymbolName();

	public AlphabetRule[] getRules() {
		return myRules.toArray(new AlphabetRule[0]);
	}

	@Override
	public int hashCode() {
		int hash = 1;
		for (Symbol s: this){
			hash *= s.hashCode();
		}
		return (1+hash);
	}

	public boolean removeRule(AlphabetRule rule){
		return myRules.remove(rule);
	}

	public boolean addRules(AlphabetRule ... rules){
		return myRules.addAll(Arrays.asList(rules));
	}

	public void checkRules(AlphabetActionType type, Symbol ... symbols) throws AlphabetException{
		for (Symbol s : symbols){
			for (AlphabetRule rule : myRules){
				BooleanWrapper bw = new BooleanWrapper(true);
				switch (type){
				case ADD: 
					bw = rule.canAdd(this, s); break;
				case REMOVE:
					bw = rule.canRemove(this, s); break;
				case MODIFY:
					bw = rule.canModify(this, s, symbols[1]);
				}
				if (bw.isError())
					throw new AlphabetException(bw.getMessage());

			}
			if (type == AlphabetActionType.MODIFY)
				break;
		}
	}

	public static boolean addCopiedSymbols(Alphabet alph,
			Symbol[] toAdd) {
		boolean converted = true;
		for (Symbol s: toAdd){
			converted = converted && alph.add(new Symbol(s.getString()));
		}
		return converted;
	}




}
