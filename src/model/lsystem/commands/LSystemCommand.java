package model.lsystem.commands;

import model.formaldef.Describable;
import model.grammar.Terminal;

/**
 * Superclass for all symbols used as commands when rendering
 * L-Systems
 * 
 * @author Ian McMahon
 *
 */
public abstract class LSystemCommand extends Terminal implements Describable {

	public LSystemCommand(String s) {
		super(s);
	}

}
