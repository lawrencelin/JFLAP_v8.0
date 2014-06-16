package file.xml.graph;

/**
 * Subclass of NoteMapTransducer with only the tag name changed for differentiating between State Labels and Notes.
 * @author Ian McMahon
 *
 */
public class StateLabelMapTransducer extends NoteMapTransducer{

	
	@Override
	public String getTag() {
		return STATE_LABELS;
	}
}
