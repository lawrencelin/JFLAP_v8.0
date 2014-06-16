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

package view.lsystem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import model.lsystem.Expander;
import model.lsystem.LSystem;
import model.symbols.SymbolString;
import view.help.ImageDisplayComponent;
import view.lsystem.helperclasses.ParameterMap;

/**
 * The view has the interface to render an L-system.
 * 
 * @author Thomas Finley, Ian McMahon
 */

public class LSystemRenderView extends JPanel {
	
	private static final Dimension LSYSTEM_RENDER_SIZE = new Dimension(600, 650);
	
	private LSystem lsystem;
	private Expander expander;
	private Renderer renderer;
	private ImageDisplayComponent imageDisplay;
	private JTextField expansionDisplay;
	private JProgressBar progressBar;
	private Action displayAction;
	private SpinnerNumberModel spinnerModel;
	private SpinnerNumberModel pitchModel;
	private SpinnerNumberModel rollModel;
	private SpinnerNumberModel yawModel;

	public LSystemRenderView(LSystem lsystem) {
		super(new BorderLayout());
		initView(lsystem);
		initListeners();

		// We can't edit the expansion.
		expansionDisplay.setEditable(false);

		// The user has to be able to change the recursion depth.
		JSpinner spinner = new JSpinner(spinnerModel);

		// Lay out the top Panel.
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(spinner, BorderLayout.EAST);
		topPanel.add(expansionDisplay, BorderLayout.CENTER);
		topPanel.add(progressBar, BorderLayout.WEST);
		add(topPanel, BorderLayout.NORTH);

		// Now, for the angle at which the damn thing is viewed...
		JSpinner s1 = new JSpinner(pitchModel), s2 = new JSpinner(rollModel), s3 = new JSpinner(
				yawModel);

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(new JLabel("Pitch"));
		bottomPanel.add(s1);
		bottomPanel.add(new JLabel("Roll"));
		bottomPanel.add(s2);
		bottomPanel.add(new JLabel("Yaw"));
		bottomPanel.add(s3);

		JScrollPane scroller = new JScrollPane(imageDisplay);
		add(scroller, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

		
		setPreferredSize(LSYSTEM_RENDER_SIZE);
		// Finally, set the initial display.
		updateDisplay();
		
	}
	
	@Override
	public String getName() {
		return "L-S Render";
	}
	
	public ImageDisplayComponent getDisplayComponent(){
		return imageDisplay;
	}

	/**
	 * Initializes the private fields of the view.
	 * 
	 * @param lsystem
	 *            L-System to initialize based on.
	 */
	private void initView(LSystem lsystem) {
		this.lsystem = lsystem;
		expander = new Expander(lsystem);
		renderer = new Renderer();
		imageDisplay = new ImageDisplayComponent();
		expansionDisplay = new JTextField();
		progressBar = new JProgressBar(0, 1);
		displayAction = new AbstractAction("Redisplay") {
			public void actionPerformed(ActionEvent e) {
				updateDisplay();
				displayAction.setEnabled(false);
			}
		};
		spinnerModel = new SpinnerNumberModel(0, 0, 200, 1);
		pitchModel = new SpinnerNumberModel(0, 0, 359, 15);
		rollModel = new SpinnerNumberModel(0, 0, 359, 15);
		yawModel = new SpinnerNumberModel(0, 0, 359, 15);
	}

	/**
	 * Initialize ChangeListeners for spinners.
	 */
	private void initListeners() {
		spinnerModel.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateDisplay();
			}
		});
		ChangeListener c = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateDisplay();
			}
		};
		pitchModel.addChangeListener(c);
		rollModel.addChangeListener(c);
		yawModel.addChangeListener(c);
	}

	/**
	 * Updates the display.Graphics2D;
	 */
	public void updateDisplay() {
		int recursionDepth = spinnerModel.getNumber().intValue();
		final SymbolString expansion = expander
				.expansionForLevel(recursionDepth);

		progressBar.setMaximum(expansion.size() * 2);
		imageDisplay.setImage(null);

		final javax.swing.Timer t = new javax.swing.Timer(30,
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int i = renderer.getCompletedSymbols() - 1;
						progressBar.setValue(i);
						progressBar.repaint();
					}
				});

		final Thread drawThread = new Thread() {
			public void run() {
				if (expansion.size() < 100) {
					String expansionString = expansion.toString();
					expansionDisplay.setText(expansionString);
				} else
					expansionDisplay.setText("Expansion contains "+expansion.size()+ " Symbols!");
				t.start();

				Image image = printComponent();

				imageDisplay.setImage(image);
				t.stop();
				imageDisplay.repaint();
				imageDisplay.revalidate();
				progressBar.setValue(progressBar.getMaximum());
			}
		};
		drawThread.start();
	}

	/**
	 * Prints the current displayed L-system.
	 */
	private Image printComponent() {
		int recursionDepth = spinnerModel.getNumber().intValue();
		SymbolString expansion = expander.expansionForLevel(recursionDepth);
		
		// Now, set the display.
		ParameterMap parameters = lsystem.getParameters();
		Matrix m = new Matrix();
		double pitch = pitchModel.getNumber().doubleValue(),
				roll = rollModel.getNumber().doubleValue(), 
				yaw = yawModel.getNumber().doubleValue();
		
		m.pitch(pitch);
		m.roll(roll);
		m.yaw(yaw);
		return renderer.render(expansion, parameters, m, null, new Point());
	}
}
