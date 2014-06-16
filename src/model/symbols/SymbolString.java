package model.symbols;


import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JOptionPane;



import debug.JFLAPDebug;



import universe.preferences.JFLAPPreferences;
import util.Copyable;
import util.UtilFunctions;

import model.algorithms.testinput.parse.ParserException;
import model.formaldef.FormalDefinition;
import model.formaldef.UsesSymbols;
import model.formaldef.components.Settable;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.regex.OperatorAlphabet;
import model.regex.RegularExpression;
import model.regex.operators.UnionOperator;
import model.symbols.symbolizer.Symbolizers;





public class SymbolString extends LinkedList<Symbol> implements Comparable<SymbolString>, 
Copyable,
Settable<SymbolString>{

//	public SymbolString(String in, FormalDefinition def){
//		super();
//		this.addAll(SymbolString.createFromDefinition(in, def));
//	}

	public SymbolString() {
		super();
	}

	public SymbolString(Symbol ... symbols) {
		super();
		for (Symbol s: symbols)
			this.add(s);
	}

	public SymbolString(SymbolString subList) {
		super(subList);
	}

	public <T extends Symbol> Set<T> getSymbolsOfClass(Class<T> clazz) {
		Set<T> results = new TreeSet<T>();
		for (Symbol s: this){
			if (clazz.isAssignableFrom(s.getClass()))
				results.add((T) s);
		}

		return results;
	}

	public SymbolString concat(SymbolString sym) {
		this.addAll(sym);
		return this;
	}

	public SymbolString reverse() {
		SymbolString reverse = new SymbolString();
		for (Symbol s: this)
			reverse.addFirst(s);
		return reverse;
	}

	public int indexOfSubSymbolString(SymbolString o) {
		if (o.isEmpty()) return 0;
		for (int i = 0; i <= this.size()-o.size(); i++){
			SymbolString sub = this.subList(i, i+o.size());
			if (sub.equals(o))
				return i;
		}
		return -1;
	}
	public boolean startsWith(SymbolString label) {
		return this.indexOfSubSymbolString(label) == 0;
	}

	/**
	 * THIS IS NOT THE SAME AS this.toString().length 
	 * in the case of an empty string or delimited string
	 * @return
	 */
	public int stringLength() {
		int length = 0;
		for (Symbol s: this){
			length += s.length();
		}
		return length;
	}

	public boolean endsWith(SymbolString ss) {
		int start = this.size()-ss.size();
		return this.subList(start).equals(ss);
	}

	public boolean endsWith(Symbol s) {
		return this.getLast().equals(s);
	}

	public SymbolString subList(int i) {
		return (SymbolString) this.subList(i, this.size());
	}

	@Override
	public SymbolString subList(int start, int end){
		return new SymbolString(super.subList(start, end).toArray(new Symbol[0]));
	}

	public SymbolString[] splitOnIndex(int position) {
		return new SymbolString[]{this.subList(0, position), this.subList(position)};
	}

	public String toString(){
		if (this.isEmpty()) 
			return JFLAPPreferences.getEmptyString();
		return UtilFunctions.createDelimitedString(this, 
				JFLAPPreferences.getSymbolStringDelimiter());
	}

	public boolean equals(Object o){
		if (o instanceof SymbolString)
			return this.compareTo((SymbolString) o) == 0;		
		if (o instanceof Symbol)
			return this.size() == 1 && this.getFirst() == o;
		return false;
	}

	@Override
	public int hashCode() {
		int code = 0;
		for (Symbol s: this)
			code += s.hashCode();
		return code;
	}

	@Override
	public SymbolString clone() {
		return this.copy();
	}


	@Override
	public SymbolString copy() {
		SymbolString string = new SymbolString();
		for (Symbol s: this)
			string.add(s.copy());
		return string;
	}

	@Override
	public int compareTo(SymbolString o) {
		Iterator<Symbol> me = this.iterator(),
				other = o.iterator();
		while(me.hasNext() && other.hasNext()){
			Symbol sMe = me.next(),
					sOther = other.next();

			if(sMe.compareTo(sOther) != 0)
				return sMe.compareTo(sOther);
		}

		if (!me.hasNext() && other.hasNext())
			return -1;
		if (me.hasNext() && !other.hasNext())
			return 1;

		return 0;
	}

	@Override
	public int indexOf(Object other){
		if (other instanceof Symbol)
			return super.indexOf(other);
		return indexOfSubSymbolString((SymbolString) other);

	}


//	public static SymbolString createFromDefinition(String in,
//			FormalDefinition def) {
//		return createFromDefinition(in, def.getAlphabets().toArray(new Alphabet[0]));
//
//	}

//	public static SymbolString createFromDefinition(String in,
//			Alphabet ... alphs) {
//
//		if (in == null ||in.length() == 0 || 
//				in == JFLAPPreferences.getEmptyStringSymbol()) 
//			return new SymbolString();
//
//		in = removeDelimiters(in);
//		ArrayList<SymbolString> options = new ArrayList<SymbolString>();
//
//		for (int i = in.length(); i > 0; i--){
//			SymbolString symbols = new SymbolString();
//			String temp = in.substring(0,i);
//			for (Alphabet alph: alphs){
//				if (alph.containsSymbolWithString(temp)){
//					symbols.add(alph.getByString(temp));
//					symbols.addAll(createFromDefinition(in.substring(i), alphs));
//					break;
//				}
//			}
//			if(symbols.stringLength() == in.length()){
//				return symbols;
//			}
//			else if(!symbols.isEmpty())
//				options.add(symbols);
//		}
//		SymbolString max = new SymbolString();
//		for (SymbolString s: options){
//			if (max.stringLength() < s.stringLength())
//				max = s;
//		}
//		return max;
//	}
//
//	public static SymbolString createFromString(String in, String delimiter) {
//		if (delimiter == null)
//			return toSingleCharSymbols(in);
//		return toMultiCharSymbols(in, delimiter, JFLAPPreferences.getVariableGrouping());
//	}

	private static SymbolString toMultiCharSymbols(String input, 
			String delimiter,
			GroupingPair gp) {

		SymbolString toReturn = new SymbolString();
		boolean buildingVar = false;
		for (String in: input.split(delimiter)){
			int paren = 0;
			String cur = "";
			for (int i = 0; i< in.length(); i++){
				Character s = in.charAt(i);
				if ((s.equals(gp.getCloseGroup()) && !buildingVar) || 
						(s.equals(gp.getOpenGroup()) && buildingVar)){
					throw new ParserException("The grouping in the input string " +
								in + " is not properly balanced.");
				}
				else if (s.equals(gp.getOpenGroup())){
					buildingVar = true;
					if (!cur.isEmpty())
						toReturn.add(new Terminal(cur));
					cur = s + "";
				}else if(s.equals(gp.getCloseGroup())){
					buildingVar = false;
					cur += s + "";
					if (cur.length() == 2)
						throw new ParserException("You may not create an empty Variable.");
					toReturn.add(new Variable(cur));
					cur = "";
				}
				else
					cur += s + "";
			}
			if (buildingVar)
				throw new ParserException("The grouping in the input string " +
						in + " is not properly balanced.");
			if (!cur.isEmpty())
				toReturn.add(new Terminal(cur));
		}
		return toReturn;
	}

	public static boolean isVariable(String s) {
		GroupingPair pair = JFLAPPreferences.getDefaultGrouping();
		return pair.isGrouped(s);
	}

	public static SymbolString toSingleCharSymbols(String in) {
		SymbolString s = new SymbolString();
		in = in.replaceAll(" ", "");
		for (Character c: in.toCharArray()){
			if (Character.isUpperCase(c))
				s.add(new Variable(c + ""));
			else
				s.add(new Terminal(c + ""));
		}
		return s;
	}

//	public static SymbolString createForMode(String in) {
//		String delimiter = null;
//		if (JFLAPPreferences.isUserDefinedMode())
//			delimiter = JFLAPPreferences.getSymbolStringDelimiter();
//		return createFromString(in, delimiter);
//	}
//
//	public static boolean checkAndSpawnError(String error, String in, Alphabet ... alphs){
//		if (!SymbolString.canBeParsed(in, alphs)){
//			JOptionPane.showMessageDialog(null, 
//					error,
//					"Bad Input",
//					JOptionPane.ERROR_MESSAGE);
//			return false;
//		}
//		return true;
//	}

	public static SymbolString concat(SymbolString ... strings) {
		SymbolString concat = new SymbolString();
		for (SymbolString ss: strings)
			concat.concat(ss);
		return concat;
	}

//	public static boolean canBeParsed(String input, FormalDefinition def) {
//		return canBeParsed(input, def.getAlphabets().toArray(new Alphabet[0]));
//	}
//
//	public static boolean canBeParsed(String input, Alphabet ... alphs) {
//		return SymbolString.isEmpty(input) || 
//				removeDelimiters(createFromDefinition(input, alphs).toString()).equals(removeDelimiters(input));
//	}

	private static String removeDelimiters(String input) {
		return input.replaceAll(JFLAPPreferences.getSymbolStringDelimiter(), "");
	}

	public static boolean isEmpty(String input) {
		return input.length() == 0 || input.equals(JFLAPPreferences.getEmptyString());
	}

	public SymbolString replace(int i, Symbol ... replaceWith) {
		return this.replace(i, new SymbolString(replaceWith));

	}

	public SymbolString replace(int i, SymbolString replaceWith) {
		return this.replace(i,i+1, replaceWith);
	}

	public int indexOf(SymbolString e, int start) {
		SymbolString temp = this.subList(start);
		return start + temp.indexOf(e);
	}

	public SymbolString replace(int start, int end, SymbolString rhs) {

		if (start < 0 || end > this.size() || end < start){
			throw new RuntimeException("Start index " + start + " does not work" +
					" with end index "+end);
		}
		SymbolString toreturn = new SymbolString(this);
		for(int i = start; i< end; i++){
			toreturn.remove(start);
		}
		toreturn.addAll(start, rhs);
		return toreturn;
	}

	public SymbolString replace(int start, int end, Symbol ... rhs) {
		return this.replace(start, end, new SymbolString(rhs));
	}

	public SymbolString replaceAll(Symbol toReplace, Symbol ... replaceWith) {
		return this.replaceAll(toReplace, new SymbolString(replaceWith));
	}

	public SymbolString replaceAll(Symbol toReplace, SymbolString replaceWith) {
		SymbolString toReturn = new SymbolString(this);
		for (int i = 0; i < toReturn.size(); i++){
			int oldSize = toReturn.size();
			toReturn = toReturn.replace(toReplace, replaceWith);
			if( oldSize != toReturn.size()){
				i += replaceWith.size()-1;
			}
		}
		return toReturn;
	}

	public SymbolString replace(Symbol toReplace, SymbolString replaceWith) {
		int index = this.indexOf(toReplace);
		if (index < 0)
			return new SymbolString(this);
		return replace(index, replaceWith);
	}

	public String toNondelimitedString() {
		return UtilFunctions.createDelimitedString(this, "");
	}

	public boolean containsAny(Symbol ... symbols) {
		for (Symbol s: symbols){
			if (this.contains(s))
				return true;
		}
		return false;
	}

	public boolean startsWith(Symbol ... symbols) {
		return this.startsWith(new SymbolString(symbols));
	}

	public boolean removeEach(Symbol s) {
		boolean changed = false;
		while(remove(s)) changed = true;
		return changed;
	}

	@Override
	public boolean setTo(SymbolString other) {
		return setTo(other.toArray(new Symbol[0]));
	}

	public boolean setTo(Symbol ... symbols) {
		this.clear();
		return addAll(symbols);
	}

	public boolean addAll(Symbol ... symbols) {
		return addAll(new SymbolString(symbols));
	}

	public SymbolString[] split(Symbol ... array) {
		List<SymbolString> split = new ArrayList<SymbolString>();
		SymbolString copy = this.copy();
		int index = 0;
		SymbolString delimiter = new SymbolString(array);
		while((index = copy.indexOf(delimiter)) != -1){
			SymbolString[] innerSplit =  copy.splitOnIndex(index);
			split.add(innerSplit[0]);
			copy = innerSplit[1].tailList(delimiter.size());
		}
		split.add(copy);
		return split.toArray(new SymbolString[0]);
	}

	public SymbolString tailList(int i) {
		return this.subList(i, this.size());
	}


}
