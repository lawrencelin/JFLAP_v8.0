package file.xml.formaldef.lsystem;

import java.util.List;

import model.grammar.Grammar;
import model.lsystem.LSystem;

import org.w3c.dom.Element;

import debug.JFLAPDebug;

import view.lsystem.helperclasses.Axiom;
import view.lsystem.helperclasses.ParameterMap;

import file.FileJFLAPException;
import file.xml.MetaTransducer;
import file.xml.XMLHelper;

/**
 * Transducer specific to LSystems. Doesn't extend FormalDefinitionTransducer
 * due to unneeded/mismatched methods.
 * 
 * @author Ian McMahon
 * 
 */
public class LSystemTransducer extends MetaTransducer<LSystem> {
	private static final String LSYSTEM_TAG = "lsystem";
	private AxiomTransducer axTrans = new AxiomTransducer();
	private ParameterMapTransducer mapTrans = new ParameterMapTransducer();

	@Override
	public String getTag() {
		return LSYSTEM_TAG;
	}

	@Override
	public LSystem fromStructureRoot(Element root) {
		LSystem system = super.fromStructureRoot(root);

		Axiom axiom = axTrans.fromStructureRoot(root);
		if (axiom != null)
			system.setAxiom(axiom);

		List<Element> paramMap = XMLHelper.getChildrenWithTag(root,
				PARAMETER_MAP_TAG);

		if (paramMap != null && !paramMap.isEmpty()) {
			ParameterMap parameters = mapTrans.fromStructureRoot(paramMap
					.get(0));
			if (parameters != null)
				system.setParameters(parameters);
		}
		return system;

	}

	@Override
	public LSystem buildStructure(Object[] subComp) {
		Grammar g = retrieveTarget(Grammar.class, subComp);
		return new LSystem(new Axiom(), g, new ParameterMap());
	}

	@Override
	public Object[] getConstituentComponents(LSystem structure) {
		Object[] components = new Object[3];
		components[0] = new Axiom(structure.getAxiom());
		components[1] = structure.getGrammar();
		components[2] = structure.getParameters();
		return components;
	}

}
