package view.formaldef.componentpanel;

import view.formaldef.componentpanel.alphabets.AlphabetBar;
import view.formaldef.componentpanel.alphabets.SpecialSymbolPanel;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.SpecialSymbol;
import model.undo.UndoKeeper;

public class ComponentPanelFactory {

	public static DefinitionComponentPanel createForComponent(FormalDefinitionComponent comp,
																UndoKeeper keeper, boolean editable) {
		if (comp instanceof Alphabet)
			return new AlphabetBar<Alphabet>((Alphabet) comp, keeper, editable);
		else if (comp instanceof SpecialSymbol)
			return new SpecialSymbolPanel((SpecialSymbol) comp, keeper, editable);
		return null;
	}

}
