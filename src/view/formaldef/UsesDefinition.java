package view.formaldef;

import model.formaldef.FormalDefinition;

public interface UsesDefinition<T extends FormalDefinition> {
	
	public T getDefinition();
	
	public void setDefintion(T def);

}
