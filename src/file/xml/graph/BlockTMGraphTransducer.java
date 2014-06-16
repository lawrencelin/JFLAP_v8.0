package file.xml.graph;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.automata.State;
import model.automata.Transition;
import model.automata.turing.TuringMachine;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.automata.turing.buildingblock.UpdatingBlock;
import model.graph.BlockTMGraph;
import model.graph.TransitionGraph;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import debug.JFLAPDebug;
import file.xml.StructureTransducer;
import file.xml.TransducerFactory;
import file.xml.XMLHelper;
import file.xml.XMLTransducer;

public class BlockTMGraphTransducer extends TransitionGraphTransducer {

	private static final String SUBGRAPH_TAG = "subgraph";

	@Override
	public String getTag() {
		return BLOCK_GRAPH_TAG;
	}
	
	@Override
	public BlockTMGraph fromStructureRoot(
			Element root) {
		TransitionGraph<?> graph = super.fromStructureRoot(root);
		BlockTuringMachine machine = (BlockTuringMachine) graph.getAutomaton();
		BlockTMGraph blockGraph = new BlockTMGraph(machine);
		
		List<Element> sub_elems = XMLHelper.getChildrenWithTag(root, SUBGRAPH_TAG);
		Map<Integer, TransitionGraph> graphMap = new HashMap<Integer, TransitionGraph>();
		
		for (Element block_elem : sub_elems){
			Element id_elem = XMLHelper.getChildrenWithTag(block_elem, STATE_TAG).get(0);
			Element graph_elem = XMLHelper.getChildrenWithTag(block_elem, STRUCTURE_TAG).get(0);
			
			String id = XMLHelper.containedText(id_elem);
			String type = StructureTransducer.retrieveTypeTag(graph_elem);
			
			XMLTransducer<TransitionGraph> subTrans = TransducerFactory.getTransducerForTag(type);
			TransitionGraph subGraph = subTrans.fromStructureRoot(graph_elem);
			graphMap.put(Integer.parseInt(id), subGraph);
		}
		
		BlockSet blocks = machine.getStates();
		BlockSet copy = blocks.copy();
		
		for (State s : copy) {
			Block b = (Block) s;	
			blockGraph.moveVertex(s, graph.pointForVertex(s));
			
			blockGraph.setGraph(b, graphMap.get(b.getID()));
		}
		
		for (Transition<?> t : machine.getTransitions()) {
			State from = t.getFromState(), to = t.getToState();
			Point2D edge = graph.getControlPt(from, to);
			blockGraph.setControlPt(edge, from, to);
		}
		return blockGraph;
	}
	
	@Override
	public Element appendComponentsToRoot(Document doc,
			TransitionGraph<? extends Transition<?>> graph, Element root) {
		BlockTMGraph blockGraph = (BlockTMGraph) graph;
		BlockTuringMachine auto = blockGraph.getAutomaton();
		
		root = super.appendComponentsToRoot(doc, graph, root);
		
		for(State s : auto.getStates()){
			Block b = (Block) s;
			TransitionGraph bGraph = blockGraph.getGraph(b);

			Element sub_elem = XMLHelper.createElement(doc, SUBGRAPH_TAG, null, null);
			XMLTransducer<TransitionGraph> trans = TransducerFactory.getTransducerForStructure(bGraph);
			
			sub_elem.appendChild(XMLHelper.createElement(doc, STATE_TAG, b.getID(), null));
			sub_elem.appendChild(trans.toXMLTree(doc, bGraph));
			root.appendChild(sub_elem);
		}
		
		return root;
	}
}
