package model.symbols.symbolizer;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.management.RuntimeErrorException;

import universe.preferences.JFLAPPreferences;
import util.UtilFunctions;

import debug.JFLAPDebug;

import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class AdvancedSymbolizer extends SimpleSymbolizer {

	
	private FormalDefinition myDef;

	public AdvancedSymbolizer(FormalDefinition fd){
		myDef=fd;
	}

	@Override
	public Alphabet[] getParsingAlphabets() {
		return myDef.getParsingAlphabets();
	}

	@Override
	public Symbol createSymbol(String s) {
		return myDef.createSymbol(s);
	}

	@Override
	public boolean isValidSymbol(String in) {
		return myDef.isValidSymbol(in);
	}
	
}

