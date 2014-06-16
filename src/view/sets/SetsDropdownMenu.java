package view.sets;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import model.sets.AbstractSet;
import model.sets.num.CongruenceSet;
import model.sets.num.EvensSet;
import model.sets.num.MultiplesSet;
import model.sets.num.FibonacciSet;
import model.sets.num.OddsSet;
import model.sets.num.PrimesSet;

@SuppressWarnings({"serial", "rawtypes"})
public class SetsDropdownMenu extends JComboBox {

	private static Map<String, Class> myClassNamesMap;
	
	static {
		myClassNamesMap = new HashMap<String, Class>();
		myClassNamesMap.put("Fibonacci", FibonacciSet.class);
		myClassNamesMap.put("Prime Numbers", PrimesSet.class);
		myClassNamesMap.put("Even Numbers", EvensSet.class);
		myClassNamesMap.put("Odd Numbers", OddsSet.class);
		myClassNamesMap.put("Factors of Set", MultiplesSet.class);
		myClassNamesMap.put("Congruence Set", CongruenceSet.class);
	}

	public SetsDropdownMenu() {
		super(getNamesToArray());
	}
	
	private static String[] getNamesToArray() {
		String[] array = new String[myClassNamesMap.size()];
		int index = 0;
		Iterator iter = myClassNamesMap.keySet().iterator();
		while(iter.hasNext()) {
			array[index] = (String) iter.next();
			index++;
		}
		return array;
	}
	
	
//	public AbstractSet getSelectedSet() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException, NoSuchMethodException {
//		Class cl = myClassNamesMap.get((String) getSelectedItem());
//		
//		if (cl.equals(MultiplesSet.class)) {
//			int[] params = getParameters(new String[] {"factor"});
//			return (AbstractSet) cl.getDeclaredConstructor(int.class).newInstance(params[0]);
//		}
//		if (cl.equals(CongruenceSet.class)) {
//			int[] params = getParameters(new String[] {"dividend", "modulus"});
//			return (AbstractSet) cl.getDeclaredConstructor(int.class, int.class).newInstance(params[0], params[1]);
//		}
//		return (AbstractSet) cl.newInstance();
//	}
	
	public Class getSelectedSetClass() {
		return myClassNamesMap.get((String) getSelectedItem());
	}
	
	private int[] getParameters(String[] names) {
		int[] values = new int[names.length];
		for (int i = 0; i < names.length; i++) {
			values[i] = promptForParameter(names[i]);
		}
		return values;
		
	}
	
	private int promptForParameter(String name) {
		String ans = JOptionPane.showInputDialog("Enter value for " + name + ": ");
		int i;
		try {
			i = Integer.parseInt(ans);
		} catch (NumberFormatException e) {
			i = promptForParameter(name);
		}
		return i;
		
	}

}
