package model.formaldef.components.alphabets.grouping;


import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;


public class DelimiterModeOptionMenu {

	private static final String NONE = "None";

	public static GroupingPair createAndSetGrouping(IGrouping grouper) {
		ArrayList<GroupingPair> allGroups = 
				new ArrayList<GroupingPair>(SpecialSymbolFactory.getAllGroupingOptions());
		String s = (String)JOptionPane.showInputDialog(
                null,
                "Select which grouping you would like to use with your " + 
                		grouper.toString().split(":")[0] + ":", //TODO: this is a bad hack to get the name
                "Grouping Options",
                JOptionPane.PLAIN_MESSAGE,
                null,
                convertToString(allGroups),
                NONE);
		if (s == null) return null;
		for (GroupingPair gp: allGroups){
			if (s.equals(gp.toShortString())){
				grouper.setGrouping(gp);
				return gp;
			}
		}
		
		//they have seleced none
		return null;
		
		
	}

	private static String[] convertToString(ArrayList<GroupingPair> allGroups) {
		String[] groups = new String[allGroups.size()+1];
		groups[0] = NONE;
		int i = 1;
		
		for (GroupingPair gp : allGroups){
			groups[i++] = gp.toShortString();
		}
		
		return groups;
	}

}
