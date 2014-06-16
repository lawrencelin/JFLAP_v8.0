package model.languages;

import java.util.Comparator;

import model.symbols.SymbolString;

public class SetComparator implements Comparator<SymbolString> {

	@Override
	public int compare(SymbolString o1, SymbolString o2) {
		if(o1.size()==o2.size()){
			return o1.compareTo(o2);
		}
		return o1.size()-o2.size();
	}

}
