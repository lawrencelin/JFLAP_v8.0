package oldnewstuff.util.view.selection;


public interface ISelector {

	public abstract void select(ISelectable component);

	public abstract void clearSelection();

	public abstract ISelectable getSelected();

	public abstract void selectNext();
	
	public abstract void selectPrevious();
	
	public abstract void selectAll();

}