package file.xml.graph;

import java.awt.geom.Point2D;
import java.util.Map;

import model.automata.Automaton;
import model.automata.Transition;
import model.graph.BlockTMGraph;
import model.graph.TransitionGraph;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.StructureTransducer;
import file.xml.XMLHelper;

/**
 * Transducer for encoding the data of an AutomatonEditorPanel, which includes a
 * TransitionGraph and possibly a Collection of Notes and State labels.
 * 
 * @author Ian McMahon
 * 
 */
public class AutomatonEditorTransducer extends StructureTransducer<AutomatonEditorData> {

	private TransitionGraphTransducer graphTrans = new TransitionGraphTransducer();
	private NoteMapTransducer noteTrans = new NoteMapTransducer();
	private StateLabelMapTransducer labelTrans = new StateLabelMapTransducer();

	@Override
	public String getTag() {
		return EDITOR_PANEL_TAG;
	}

	@Override
	public AutomatonEditorData fromStructureRoot(Element root) {
		Element graph_elem = XMLHelper.getChildArray(root, STRUCTURE_TAG).get(0);
		graphTrans = (TransitionGraphTransducer) StructureTransducer.getStructureTransducer(graph_elem);
		TransitionGraph graph = graphTrans.fromStructureRoot(graph_elem);

		Element note_elem = XMLHelper.getChildrenWithTag(root, NOTE_MAP_TAG).get(0);
		Map<Point2D, String> notes = noteTrans.fromStructureRoot(note_elem);

		Element label_elem = XMLHelper.getChildrenWithTag(root, STATE_LABELS).get(0);
		Map<Point2D, String> labels = labelTrans.fromStructureRoot(label_elem);

		return new AutomatonEditorData(graph, labels, notes);
	}

	@Override
	public Element appendComponentsToRoot(Document doc, AutomatonEditorData editorData, Element root) {
		TransitionGraph graph = editorData.getGraph();
		graphTrans = graph instanceof BlockTMGraph ? new BlockTMGraphTransducer() : new TransitionGraphTransducer();
		
		Map<Point2D, String> labels = editorData.getLabels();
		Map<Point2D, String> notes = editorData.getNotes();

		root.appendChild(graphTrans.toXMLTree(doc, graph));
		root.appendChild(labelTrans.toXMLTree(doc, labels));
		root.appendChild(noteTrans.toXMLTree(doc, notes));

		return root;
	}

}
