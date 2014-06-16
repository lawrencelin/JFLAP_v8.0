package oldnewstuff.util.view.selection;

public abstract class SelectableObject implements ISelectable {

	public boolean amSelected,
				   amSelectable;
	
	public SelectableObject() {
		this(false,false);
	}
	
	public SelectableObject (boolean selected){
		this (selected, false);
	}
	
	public SelectableObject (boolean selected, boolean selectable){
		amSelectable = selectable;
		amSelected = selected;
	}
	
	@Override
	public void setSelected(boolean b) {
		amSelected = b;
	}
	
	@Override
	public boolean isSelected() {
		return amSelected;
	}

	@Override
	public boolean isSelectable() {
		return amSelectable;
	}

	@Override
	public void setSelectable(boolean b) {
		amSelectable = b;
	}

}
