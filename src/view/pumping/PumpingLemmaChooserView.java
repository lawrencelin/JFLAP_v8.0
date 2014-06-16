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





package view.pumping;

import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import view.EditingPanel;
import view.environment.JFLAPEnvironment;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import oldnewstuff.main.JFLAPplet;

import debug.JFLAPDebug;

import model.pumping.ContextFreePumpingLemma;
import model.pumping.PumpingLemma;
import model.pumping.RegularPumpingLemma;
import model.undo.UndoKeeper;


/**
 * A <code>PumpingLemmaChooserPane</code> is the intermediate pane that allows
 * the user to select which pumping lemma they wish to see.
 * 
 * @author Jinghui Lim
 *
 */
public class PumpingLemmaChooserView extends EditingPanel 
{
    private static final Dimension DEFAULT_REG_SIZE = new Dimension(650, 640);
	private static final Dimension DEFAULT_CF_SIZE = new Dimension(750, 700);
	/**
     * The list of pumping lemmas to choose from.
     */
    PumpingLemmaChooser myChooser;
    /**
     * Radio Buttons that determine who goes first.
     */
    JRadioButton humanButton, computerButton;
    
    /**
     * Constructs a <code>PumpingLemmaChooserPane</code> associated with a
     * {@link gui.pumping.PumpingLemmaChooser} and an 
     * {@link gui.environment.Environment}.
     * 
     * @param plc the associated <code>PumpingLemmaChooser</code> 
     * @param env the associated <code>Environment</code>
     */
    public PumpingLemmaChooserView(PumpingLemmaChooser plc)
    {
    	super(new UndoKeeper(), false);
        super.setLayout(new BorderLayout());
        myChooser = plc;
        init();
    }
    
    public PumpingLemmaChooserView(RegPumpingLemmaChooser reg){
    	this((PumpingLemmaChooser) reg);
    	setMaximumSize(DEFAULT_REG_SIZE);
    	setPreferredSize(DEFAULT_REG_SIZE);
    }
    
    public PumpingLemmaChooserView(CFPumpingLemmaChooser cf){
    	this((PumpingLemmaChooser) cf);
    	setMaximumSize(DEFAULT_CF_SIZE);
    	setPreferredSize(DEFAULT_CF_SIZE);
    }
    
    /**
     * Initializes the chooser pane.
     */
    private void init()
    {
    	JPanel listPanel = new JPanel();
    	listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
    	listPanel.setBorder(BorderFactory.createTitledBorder("Then select a lemma."));
    	
    	add(initRadioButtonPanel(), BorderLayout.NORTH);
        for(int i = 0; i < myChooser.size(); i++)
            listPanel.add(addPumpingLemma(i));
        add(listPanel, BorderLayout.CENTER);
    }
    
    /**
     * Initiates the panel where the user, through radio buttons, decides whether
     * he/she or the computer will go first.
     */
    private JPanel initRadioButtonPanel(){    	
    	ButtonGroup group = new ButtonGroup();
    	JPanel buttonPanel;
    	buttonPanel = new JPanel(new BorderLayout());
    	buttonPanel.setBorder(BorderFactory.createTitledBorder("First choose who makes the first move."));
    	
    	humanButton = new JRadioButton("You go first");
    	computerButton = new JRadioButton("Computer goes first");    	
    	group.add(humanButton);
    	group.add(computerButton);
    	humanButton.setSelected(true);
    	buttonPanel.add(humanButton, BorderLayout.WEST);
    	buttonPanel.add(computerButton, BorderLayout.CENTER);
    	return buttonPanel;
    }
    
    /**
     * Creates a panel for the pumping lemma at index <code>i</code> in the 
     * pumping lemma chooser.
     * 
     * @param i the position of the pumping lemma we wish to add
     * @return a <code>JPanel</code> representing the pumping lemma
     */
    private JPanel addPumpingLemma(int i)
    {
        PumpingLemma lemma = myChooser.get(i);
        JPanel panel = new JPanel(new BorderLayout());
        JEditorPane ep = new JEditorPane("text/html", "<html><body align=center><b><i>L</i> = {" + 
                lemma.getHTMLTitle() + "}</b></body></html>");
        
        ep.setBackground(this.getBackground());
        ep.setDisabledTextColor(Color.BLACK);
        ep.setEnabled(false);
        panel.add(ep, BorderLayout.CENTER);

        PumpingLemmaChooseButton button = new PumpingLemmaChooseButton(myChooser.get(i), i);

        panel.add(button, BorderLayout.EAST);
        panel.setBorder(BorderFactory.createEtchedBorder());
        
        return panel;
    }
    
    /**
     * A <code>PumpingLemmaChooseButton</code> is a <code>JButton</code>
     * that opens a {@link PumpingLemmaInputView} for its associated
     * {@link pumping.PumpingLemma}.
     * 
     * @author Jinghui Lim
     *
     */
    private class PumpingLemmaChooseButton extends MagnifiableButton
    {
        /**
         * The pumping lemma the button should start.
         */
        private PumpingLemma myLemma;
        private int myIndex;
        
        /**
         * Constructs a <code>PumpingLemmaChooseButton</code> that opens
         * a pumping lemma in the environment when it is clicked.
         *  
         * @param pl the pumping lemma it should open
         * @param env the environment it should open the pumping lemma in
         */
        public PumpingLemmaChooseButton(PumpingLemma pl, int index)
        {
            super("Select", JFLAPPreferences.getDefaultTextSize());
            myLemma = pl;
            myIndex = index;
            
            this.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {   myChooser.reset(myIndex);
                        myChooser.setCurrent(myIndex);
                        PumpingLemmaInputView inputView = null;   //this value should change
                        if (humanButton.isSelected()) {
                        	if(myLemma instanceof RegularPumpingLemma)
                        		inputView = new HumanRegPumpingLemmaInputView((RegularPumpingLemma)myLemma);
                        	else if(myLemma instanceof ContextFreePumpingLemma)
                        		inputView = new HumanCFPumpingLemmaInputView((ContextFreePumpingLemma)myLemma);
                        }                        
                        else if (computerButton.isSelected()) {
                        	if(myLemma instanceof RegularPumpingLemma)
                        		inputView = new CompRegPumpingLemmaInputView((RegularPumpingLemma)myLemma);
                        	else if(myLemma instanceof ContextFreePumpingLemma)
                        		inputView = new CompCFPumpingLemmaInputView((ContextFreePumpingLemma)myLemma);
                        }
                        JFLAPUniverse.getActiveEnvironment().addSelectedComponent(inputView);
                    }
                });
        }
    }
    
    public void setComputerFirst(){
    		computerButton.doClick();
    }
    
    @Override
    public String getName() {
    	return "Pumping Lemma";
    }
}
