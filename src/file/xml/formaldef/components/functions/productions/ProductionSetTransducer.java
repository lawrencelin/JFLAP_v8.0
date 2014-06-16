package file.xml.formaldef.components.functions.productions;

import model.formaldef.components.SetComponent;
import model.grammar.Production;
import model.grammar.ProductionSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.formaldef.components.functions.FunctionSetTransducer;

import util.Copyable;

public class ProductionSetTransducer extends FunctionSetTransducer<Production> {


	public ProductionSetTransducer(ProductionTransducer trans) {
		super(trans);
	}

	@Override
	public String getTag() {
		return PROD_SET_TAG;
	}

	@Override
	public ProductionSet createEmptyComponent() {
		return new ProductionSet();
	}


}
