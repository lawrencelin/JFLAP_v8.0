/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */




package util.view.magnify;

import java.awt.Font;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;






/**
 * A JSlider that adjusts the size of JTable cells and the font.
* @author Jonathan Su
*/

public class SizeSlider extends JSlider{

    static final int MIN = 1;
    static final int MAX = 100;
    static final int INIT = 50;
    static final String TABLE_SIZE_TITLE = "Table Text Size";
    
    private Set<Magnifiable> myTargets;
	
	public SizeSlider(Magnifiable ... targets) {
		super(MIN, MAX, INIT); 
	    this.addChangeListener(new SliderListener());
	    setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), TABLE_SIZE_TITLE));
	    myTargets = new HashSet<Magnifiable>(Arrays.asList(targets));
	}


	class SliderListener implements ChangeListener {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                double frac = source.getValue()/(double)MAX;
                for (Magnifiable m: myTargets)
                	m.setMagnification(frac);
            }
      }


	public void addListener(Magnifiable t) {
		myTargets.add(t);
	}


	public void distributeMagnification() {
		this.fireStateChanged();
	}
}
