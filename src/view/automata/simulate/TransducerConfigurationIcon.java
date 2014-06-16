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




package view.automata.simulate;

import java.awt.Component;
import java.awt.Graphics2D;

import model.algorithms.testinput.simulate.configurations.InputOutputConfiguration;
import model.algorithms.testinput.simulate.configurations.MealyConfiguration;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.OutputFunction;
import model.automata.transducers.Transducer;
import model.symbols.SymbolString;

/**
 * 
 * @author Jinghui Lim
 *
 */
public class TransducerConfigurationIcon<T extends Transducer<S>, S extends OutputFunction<S>> extends ConfigurationIcon<T, FSATransition>
{
    public TransducerConfigurationIcon(InputOutputConfiguration<T, S> configuration)
    {
        super(configuration);
    }

    public void paintConfiguration(Component c, Graphics2D g, int width, int height)
    {
        super.paintConfiguration(c, g, width, height);
        InputOutputConfiguration<T, S> config = (InputOutputConfiguration<T, S>) getConfiguration();
        // Draw the torn tape with the rest of the input.
        SymbolString symbols = config.getPrimaryString();
		int primary = config.getPrimaryPosition();
		
        Torn.paintSymbolString((Graphics2D)g, symbols,
                 RIGHT_STATE.x+5.0f,
                 ((float)super.getIconHeight())*0.5f, 
                 Torn.MIDDLE, width-RIGHT_STATE.x-5.0f,
                 false, true, primary);
        // Draw the stack.
        Torn.paintSymbolString((Graphics2D)g, config.getOutput(),
                 BELOW_STATE.x, BELOW_STATE.y + 5.0f,
                 Torn.TOP, getIconWidth(), false, true, -1);
    }
}
