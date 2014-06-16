package file.xml.formaldef.components.functions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import debug.JFLAPDebug;

import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import file.xml.BasicTransducer;
import file.xml.XMLTransducer;
import file.xml.TransducerFactory;
import file.xml.XMLHelper;
import file.xml.formaldef.components.functions.transitions.tm.BlockTransitionTransducer;
import file.xml.formaldef.components.symbols.SymbolStringTransducer;

public abstract class FunctionTransducer<T extends LanguageFunction> extends BasicTransducer<T> {

	
	private Alphabet[] myAlphs;
	
	public FunctionTransducer(Alphabet ... alphs){
		myAlphs = alphs;
	}
	
	@Override
	public T fromStructureRoot(Element root) {
		List<Element> eleChildren = XMLHelper.getElementChildren(root);
		Map<String, Object> valueMap = new HashMap<String, Object>();
		for (Element e: eleChildren){
			String tag = e.getTagName();
			Alphabet[] alphs = 
					FunctionAlphabetFactory.discerneAlphabets(tag, myAlphs);
			XMLTransducer trans;
			if (alphs.length > 0){
				trans = getSymbolStringTransducer(tag, alphs);
			}
			else{
				trans = getTransducerForTag(tag);
			}
			valueMap.put(tag, trans.fromStructureRoot(e));			
		}
		T func = createFunction(valueMap);
		return func;
	}

	public XMLTransducer getSymbolStringTransducer(String tag, Alphabet[] alphs) {
		return new SymbolStringTransducer(tag, alphs);
	}

	public XMLTransducer getTransducerForTag(String tag) {
		return TransducerFactory.getTransducerForTag(tag);
	}

	public abstract T createFunction(Map<String, Object> valueMap);
	
	@Override
	public Element toXMLTree(Document doc, T structure) {
		Map<String, Object> tagToValue = createTagToValueMap(structure);
		Element root = XMLHelper.createElement(doc, getTag(), null, null);
		for (Entry<String, Object> e: tagToValue.entrySet()){
			String tag = e.getKey();
			Object value = e.getValue();
			XMLTransducer trans = getTransducerForTag(tag);
			if (trans == null){
				trans = new SymbolStringTransducer(e.getKey());
				value = new SymbolString((Symbol[]) value);
			}
			root.appendChild(trans.toXMLTree(doc, value));
		}
		return root;
	}
	public abstract Map<String, Object> createTagToValueMap(T structure);

}
