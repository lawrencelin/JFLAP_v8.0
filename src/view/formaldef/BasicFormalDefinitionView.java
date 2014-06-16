package view.formaldef;

import javax.swing.JComponent;

import model.formaldef.FormalDefinition;
import model.undo.UndoKeeper;

public abstract class BasicFormalDefinitionView<T extends FormalDefinition> extends FormalDefinitionView<T,T> {

	public BasicFormalDefinitionView(T model, UndoKeeper keeper,
			boolean editable) {
		super(model, keeper, editable);
	}

	@Override
	public T getDefinition() {
		return getModel();
	}

}
