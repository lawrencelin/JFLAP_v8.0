package util.view.thinscroller;

import javax.swing.JScrollPane;





public class ThinScrollBarScrollPane extends JScrollPane{


	
	public ThinScrollBarScrollPane(int bar_height, int vsbPolicy, int hsbPolicy){
		super(vsbPolicy, hsbPolicy);
		this.setHorizontalScrollBar(new ThinScrollBar(ScrollBar.HORIZONTAL, bar_height));
		this.setVerticalScrollBar(new ThinScrollBar(ScrollBar.VERTICAL, bar_height));

	}

	
}
