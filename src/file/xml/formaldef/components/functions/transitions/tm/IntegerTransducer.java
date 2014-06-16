package file.xml.formaldef.components.functions.transitions.tm;

import file.xml.XMLTransducer;
import file.xml.formaldef.components.SingleNodeTransducer;

public class IntegerTransducer extends SingleNodeTransducer<Integer> {

	public IntegerTransducer(String tag) {
		super(tag);
	}

	@Override
	public Object extractData(Integer structure) {
		return structure;
	}

	@Override
	public Integer createInstance(String text) {
		return Integer.valueOf(text);
	}


}
