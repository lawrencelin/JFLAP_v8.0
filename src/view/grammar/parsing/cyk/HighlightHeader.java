package view.grammar.parsing.cyk;

import java.awt.Color;

import model.symbols.Symbol;

/**
 * Helper class used in the rendering of headers (as the current framework
 * has issues with negative numbers, which headers contain as their row).
 * 
 * @author Ian McMahon
 *
 */
public class HighlightHeader {
	private Color highlight;
	private Symbol name;
	
	public HighlightHeader(Symbol name){
		this.name = name;
	}
	
	public Color getHighlight(){
		return highlight;
	}
	
	public void setHightlight(Color highlight){
		this.highlight = highlight;
	}
	
	public String toString(){
		return name.toString();
	}
	
}
