package file.xml.formaldef.components.states;

import model.automata.State;
import model.automata.transducers.Transducer;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TuringMachine;
import model.automata.turing.buildingblock.Block;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import debug.JFLAPDebug;

import file.xml.StructureTransducer;
import file.xml.TransducerFactory;
import file.xml.XMLHelper;
import file.xml.XMLTransducer;
import file.xml.formaldef.automata.MultiTapeTMTransducer;

public class BlockTransducer extends StateTransducer {

	@Override
	public State fromStructureRoot(Element root) {
		State s = super.fromStructureRoot(root);
		Element TMroot = getTMRoot(root);
		XMLTransducer transducer = StructureTransducer.getStructureTransducer(TMroot);
//		JFLAPDebug.print(XMLHelper.containedText(((Element)TMroot.getParentNode()).getElementsByTagName(NAME_TAG).item(0)));
		TuringMachine tm = 
				(TuringMachine) transducer.fromStructureRoot(TMroot);
//		JFLAPDebug.print(tm);
		return new Block(tm, s.getName(), s.getID());
	}

	private Element getTMRoot(Element root) {
		return XMLHelper.getChildrenWithTag(root, STRUCTURE_TAG).get(0);
	}

	@Override
	public Element toXMLTree(Document doc, State item) {
		Block b = (Block) item;
		Element parent = super.toXMLTree(doc, item);
		TuringMachine tm = b.getTuringMachine();
		XMLTransducer transducer = TransducerFactory.getTransducerForStructure(tm);
		Element tmRoot = transducer.toXMLTree(doc, tm);
		parent.appendChild(tmRoot);
		return parent;
	}

	@Override
	public String getTag() {
		return BLOCK_TAG;
	}
	
}
