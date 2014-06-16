package model.automata.turing.buildingblock.library;

import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.buildingblock.Block;

public class BlockLibrary {

	public static final String FINAL = "Final";
	public static final String MOVE = "Move";
	public static final String UNTIL = "Until";
	public static final String SHIFT = "Shift";
	public static final String START = "Start";
	public static final String WRITE = "Write";
	public static final String UNDSCR = "_";
	public static final String NOT = "Not";
	public static final String DUPLICATE = "Duplicate";
	public static final String COPY = "Copy";



	public static Block createFromName(String name, TapeAlphabet alph, int id) {
		String[] parts = name.split(UNDSCR);

		//FinalBlock
		if (parts[0].equals(FINAL) && parts.length == 1){
			return new HaltBlock(id);
		}
		//StartBlock
		else if(parts[0].equals(START) && parts.length == 1){
			return new StartBlock(id);
		}
		//MoveBlock family
		else if(parts[0].equals(MOVE) && isMove(parts[1])){
			
			//MoveBlock
			if (parts.length == 2){
				return new MoveBlock(toMove(parts[1]), alph,  id);
			}
			//MoveUntilBlock
			else if(parts[2].equals(UNTIL) &&
					alph.containsSymbolWithString(parts[3])
					&& parts.length == 4){
				return new MoveUntilBlock(toMove(parts[1]), 
						alph.getSymbolForString(parts[3]), alph,  id);

			}
			//MoveUntilNotBlock
			else if( parts[2].equals(UNTIL) && 
					parts[3].equals(NOT) && 
					alph.containsSymbolWithString(parts[4]) &&
					parts.length == 5){
				return new MoveUntilNotBlock(toMove(parts[1]), 
						alph.getSymbolForString(parts[4]), alph,  id);
	
			}
		}
		//ShiftBlock family
		else if(parts[0].equals(SHIFT) && 
				isMove(parts[1])){
			//ShiftBlock
			if(parts.length == 2){
				return new ShiftBlock(toMove(parts[1]), alph,  id);
			}
			//SingleShiftBlock
			else if (parts.length == 3 && 
					alph.containsSymbolWithString(parts[2])){
				return new SingleShiftBlock(alph.getSymbolForString(parts[2]), 
						toMove(parts[1]), alph,  id);
			}

		}
		//WriteBlock
		else if(parts[0].equals(WRITE) && parts.length == 2 &&
				alph.containsSymbolWithString(parts[1])){
			return new WriteBlock(alph.getSymbolForString(parts[1]), alph,  id);

		}
		else if (parts[0].equals(COPY) && parts.length == 1)
			return new CopyBlock(alph,  id);
		return null;
	}



	private static TuringMachineMove toMove(String string) {
		if (string.length() > 1) return null;
		for (TuringMachineMove move: TuringMachineMove.values()){
			if (move.char_abbr == string.charAt(0))
				return move;
		}
		return null;
	}



	private static boolean isMove(String string) {
		return toMove(string) != null;
	}




}
