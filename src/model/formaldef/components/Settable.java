package model.formaldef.components;

import model.formaldef.Describable;

public interface Settable<T>{

	public abstract boolean setTo(T other);

}