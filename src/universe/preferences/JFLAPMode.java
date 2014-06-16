package universe.preferences;

import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;

public enum JFLAPMode {

	DEFAULT("Default"),
	MULTI_CHAR_DEFAULT("Multi-Char Default"),
	CUSTOM("Custom");
	
	private String string;
	
	JFLAPMode(String string){
		this.string = string;
	}
	
	@Override
	public String toString() {
		return this.string + " mode";
	}
	
	public static JFLAPMode intuitMode(FormalDefinition def){
		
		if (def.usingGrouping()) return JFLAPMode.CUSTOM;
		
		for (Alphabet alph: def.getAlphabets()){
			for (Symbol s: alph){
				if (s.getString().length() > 1)
					return JFLAPMode.MULTI_CHAR_DEFAULT;
			}
		}

		return JFLAPMode.DEFAULT;
	}

	public static JFLAPMode getMode(String modeString) {
		for (JFLAPMode mode: JFLAPMode.values())
			if (mode.toString() == modeString) return mode;
		return null;
	}
}
