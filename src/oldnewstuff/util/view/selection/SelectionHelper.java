package oldnewstuff.util.view.selection;


public class SelectionHelper {

	public static int setAllISelected(boolean b, ISelectable[] selectables) {
		int i = 0;
		for (ISelectable s: selectables){
			if (s.isSelected() != b){
				s.setSelected(b);
				i++;
			}
		}
		return i;
	}

}
