package view.formaldef.componentpanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.text.JTextComponent;

import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import util.view.thinscroller.ThinScrollBarScrollPane;


public abstract class SetComponentBar<T> extends ThinScrollBarScrollPane implements JFLAPConstants{

	private JToolBar myBar;
	private Color myHighlight;
    
	public SetComponentBar(Color highlight) {
		super(4, VERTICAL_SCROLLBAR_NEVER, HORIZONTAL_SCROLLBAR_AS_NEEDED);
		myBar = new JToolBar();
		myBar.setFloatable(false);
		myHighlight = highlight;
		this.setViewportView(myBar);
	}
	
	public void setTo(T ...items){
		myBar.removeAll();
		this.add(items);
	}
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if (myBar == null) return;
		for (Component c: myBar.getComponents())
			c.setFont(font);
	}
	
	
	public void add(T ...toAdd){
		for (T item: toAdd){
			myBar.add(setUpBox(item));
		}
	}
	
	public boolean highlightBoxes(T ... items){
		for (T item : items){
			ItemBox box = this.getBoxForItem(item);
			if (box == null) return false;
			box.highlight();
		}
		return true;
	}

	private ItemBox getBoxForItem(T item) {
		for (Component comp : this.getComponents()){
			if (comp instanceof SetComponentBar.ItemBox &&
					((SetComponentBar.ItemBox) comp).getItem().equals(item)){
				return (ItemBox) comp;
			}
		}
		
		return null;
	}

	public void clearHighlights() {
		for (Component comp : this.getComponents()){
			if (comp instanceof SetComponentBar.ItemBox){
				((SetComponentBar.ItemBox) comp).dehighlight();
			}
		}
	}

	
	private ItemBox setUpBox(T item) {
		ItemBox box = new ItemBox(item);
		box.setFocusable(false);
		box.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				doClickResponse(((ItemBox) e.getSource()).getItem(), e);
			}
		});
		box.setFont(this.getFont());
		return box;
	}

	public void removeAllBoxes(){
		myBar.removeAll();
	}
	
	public abstract void doClickResponse(T item, MouseEvent e);

	private class ItemBox extends JButton{

		private T myItem;

		public ItemBox(T item) {
			myItem = item;
			this.setOpaque(true);
			this.setText(myItem.toString());
		}
		
		public void dehighlight() {
			this.setBackground(JFLAPPreferences.getBackgroundColor());
		}

		public void highlight(){
			this.setBackground(myHighlight);
		}
		
		public T getItem(){
			return myItem;
		}
		
	}
	
}
