package util.view.magnify;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import universe.preferences.JFLAPPreferences;

public class MagnifiableTable extends JTable implements Magnifiable {

	public MagnifiableTable(){
		super();
	}
	
	public MagnifiableTable(TableModel model){
		super(model);
	}
	
	
	@Override
	public void setMagnification(double mag) {
		float size = (float) (mag*JFLAPPreferences.getDefaultTextSize());
        this.setFont(this.getFont().deriveFont(size));
        this.setRowHeight((int) (size+10));
	}
}
