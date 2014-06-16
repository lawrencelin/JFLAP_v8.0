package model.undo;

public interface IUndoRedo {

	public boolean undo();
	
	public boolean redo();
	
	public String getName();
	
}
