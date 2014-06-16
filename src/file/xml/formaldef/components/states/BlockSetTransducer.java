package file.xml.formaldef.components.states;

import model.automata.StateSet;
import model.automata.turing.buildingblock.BlockSet;

public class BlockSetTransducer extends StateSetTransducer {

	
	@Override
	public StateTransducer createStateTransducer() {
		return new BlockTransducer();
	}
	
	@Override
	public BlockSet createEmptyComponent() {
		return new BlockSet();
	}
	
	@Override
	public String getTag() {
		return BLOC_SET_TAG;
	}
}
