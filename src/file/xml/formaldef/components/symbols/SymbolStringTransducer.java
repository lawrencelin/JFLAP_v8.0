package file.xml.formaldef.components.symbols;

import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.SimpleSymbolizer;
import model.symbols.symbolizer.Symbolizer;
import file.xml.formaldef.components.SingleNodeTransducer;

public class SymbolStringTransducer extends SingleNodeTransducer<SymbolString> {

	private Symbolizer mySymbolizer;

	public SymbolStringTransducer(String tag, Alphabet ... alphs){
		this(tag, new SimpleSymbolizer(alphs));
	}


	public SymbolStringTransducer(String tag, Symbolizer symbolizer) {
		super(tag);
		mySymbolizer = symbolizer;

	}


	public Object extractData(Symbol[] structure){
		return this.extractData(new SymbolString(structure));
	}
	
	@Override
	public Object extractData(SymbolString structure) {
		return structure.toNondelimitedString();
	}

	@Override
	public SymbolString createInstance(String text) {
		if (text==null) text="";
		return mySymbolizer.symbolize(text);
	}
	

}
