package view.help;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import debug.JFLAPDebug;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import universe.JFLAPUniverse;
import universe.preferences.JFLAPMode;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;

public class PreferenceDialog extends JDialog {

	private static final int LS_ANGLE = 0, LS_DISTANCE = 1, LS_HUE = 2,
			LS_INCREMENT = 3, LS_WIDTH = 4;
	private static final char[] PARENS = new char[] { '(', ')' };
	private static final char[] SQUARE_BRACKETS = new char[] { '[', ']' };
	private static final char[] POINTY_BRACKETS = new char[] { '<', '>' };
	private static final char[] CURLY_BRACKETS = new char[] { '{', '}' };
	
	private JTabbedPane myTabbedPane;
	
	//Default buttons so user can reset prefs in one click
	private List<JRadioButton> buttonList;

	private SpinnerNumberModel bufferAmount;

	private JSpinner aSpin;
	private JSpinner dSpin;
	private JSpinner hSpin;
	private JSpinner iSpin;
	private JSpinner wSpin;

	public PreferenceDialog() {
		super(JFLAPUniverse.getActiveEnvironment(), "Preferences", true);
		buttonList = new ArrayList<JRadioButton>();
		myTabbedPane = new JTabbedPane();
		myTabbedPane.setPreferredSize(new Dimension(860, 270));
		
		add(myTabbedPane);
		init();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension boxSize = getPreferredSize();
		setLocation((screenSize.width - boxSize.width) >> 1,
				(screenSize.height - boxSize.height) >> 1);
		pack();
		setVisible(true);
		toFront();
	}

	public void init() {
		JPanel general = createGeneralPanel();
		JPanel color = createColorPanel();

		myTabbedPane.add(general);
		myTabbedPane.add(color);
	}

	private JPanel createGeneralPanel() {
		JPanel general = new JPanel(new BorderLayout());
		general.setName("General");

		JPanel west = new JPanel();
		west.setBorder(BorderFactory.createLineBorder(Color.black));
		west.setLayout(new BoxLayout(west, BoxLayout.Y_AXIS));

		JPanel mode = createModePanel();
		JPanel grouping = createGroupingPanel();
		JPanel buffer = createBufferPanel();
		JPanel cyk = createCKYPanel();
		JPanel symbol = createSymbolPanel();
		JPanel buttonPanel = createButtonPanel();

		west.add(mode);
		west.add(grouping);
		west.add(buffer);
		west.add(cyk);
		west.add(symbol);
		west.add(buttonPanel);

		JPanel center = createFormalDefPanel();
		center.setBorder(BorderFactory.createLineBorder(Color.black));

		general.add(west, BorderLayout.WEST);
		general.add(center, BorderLayout.CENTER);
		return general;
	}

	private JPanel createModePanel() {
		JPanel mode = new JPanel();
		mode.add(new JLabel("Default Mode: "));
		ButtonGroup modeGroup = new ButtonGroup();

		JRadioButton defaultMode = new JRadioButton(new ModeAction(JFLAPMode.DEFAULT));
		JRadioButton multiMode = new JRadioButton(new ModeAction(JFLAPMode.MULTI_CHAR_DEFAULT));
		JRadioButton customMode = new JRadioButton(new ModeAction(JFLAPMode.CUSTOM));

		mode.add(defaultMode);
		modeGroup.add(defaultMode);
		buttonList.add(defaultMode);
		mode.add(multiMode);
		modeGroup.add(multiMode);
		mode.add(customMode);
		modeGroup.add(customMode);

		JFLAPMode selected = JFLAPPreferences.getDefaultMode();
		if (selected == JFLAPMode.CUSTOM)
			customMode.doClick();
		else if (selected == JFLAPMode.MULTI_CHAR_DEFAULT)
			multiMode.doClick();
		else
			defaultMode.doClick();
		return mode;
	}
	
	private JPanel createGroupingPanel() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("Custom Mode Grouping Pair: "));
		ButtonGroup group = new ButtonGroup();

		JRadioButton paren = new JRadioButton(new GroupingAction(PARENS));
		JRadioButton squareBrackets = new JRadioButton(new GroupingAction(SQUARE_BRACKETS));
		JRadioButton pointyBrackets = new JRadioButton(new GroupingAction(POINTY_BRACKETS));
		JRadioButton curlyBrackets = new JRadioButton(new GroupingAction(CURLY_BRACKETS));
		
		panel.add(pointyBrackets);
		group.add(pointyBrackets);
		buttonList.add(pointyBrackets);
		panel.add(squareBrackets);
		group.add(squareBrackets);
		panel.add(paren);
		group.add(paren);		
		panel.add(curlyBrackets);
		group.add(curlyBrackets);
		
		GroupingPair selected = JFLAPPreferences.getDefaultGrouping();

		if (selected.equals(new GroupingPair(CURLY_BRACKETS[0], CURLY_BRACKETS[1])))
			curlyBrackets.doClick();
		else if (selected.equals(new GroupingPair(PARENS[0], PARENS[1])))
			paren.doClick();
		else if (selected.equals(new GroupingPair(SQUARE_BRACKETS[0], SQUARE_BRACKETS[1])))
			squareBrackets.doClick();
		else
			pointyBrackets.doClick();
		return panel;
	}

	private JPanel createBufferPanel() {
		JPanel tmBuffer = new JPanel();

		JLabel label = new JLabel("Turing Machine buffer length: ");
		bufferAmount = new SpinnerNumberModel(
				JFLAPPreferences.getDefaultTMBufferSize(), 1, 20, 1) {
			@Override
			public Object getValue() {
				int val = (Integer) super.getValue();
				JFLAPPreferences.setDefaultTMBufferSize(val);
				return val;
			}
		};
		JSpinner spinner = new JSpinner(bufferAmount);

		tmBuffer.add(label);
		tmBuffer.add(spinner);
		return tmBuffer;
	}

	private JPanel createCKYPanel() {
		JPanel cykPanel = new JPanel();
		cykPanel.add(new JLabel("CYK Table Direction: "));
		ButtonGroup cykGroup = new ButtonGroup();

		JRadioButton horizontal = new JRadioButton(
				new CYKDirectionAction(false));
		JRadioButton diagonal = new JRadioButton(new CYKDirectionAction(true));

		cykPanel.add(horizontal);
		cykGroup.add(horizontal);
		buttonList.add(horizontal);
		cykPanel.add(diagonal);
		cykGroup.add(diagonal);

		if (JFLAPPreferences.isCYKtableDiagonal())
			diagonal.doClick();
		else
			horizontal.doClick();

		return cykPanel;
	}

	private JPanel createSymbolPanel() {
		JPanel symbols = new JPanel(new GridLayout(2, 2));

		symbols.add(new JLabel("Empty String Symbol: "));
		symbols.add(createEmptySymbolOptions());
		symbols.add(new JLabel("Empty Set Symbol: "));
		symbols.add(createEmptySetOptions());

		return symbols;
	}

	private JPanel createEmptySymbolOptions() {
		JPanel panel = new JPanel();
		ButtonGroup group = new ButtonGroup();

		JRadioButton lambda = new JRadioButton(new LambdaPrefAction());
		JRadioButton epsilon = new JRadioButton(new EpsilonPrefAction());
		panel.add(lambda);
		group.add(lambda);
		buttonList.add(lambda);
		panel.add(epsilon);
		group.add(epsilon);
		

		if (JFLAPPreferences.getEmptyString().equals(JFLAPConstants.EPSILON))
			epsilon.doClick();			
		else
			lambda.doClick();

		return panel;
	}

	private JPanel createEmptySetOptions() {
		JPanel panel = new JPanel();
		ButtonGroup group = new ButtonGroup();

		JRadioButton defaultSet = new JRadioButton(new EmptySetPrefAction(
				JFLAPConstants.EMPTY_SET));
		JRadioButton lambda = new JRadioButton(new EmptySetPrefAction(
				JFLAPConstants.UPPER_LAMBDA));
		JRadioButton brackets = new JRadioButton(new EmptySetPrefAction("{}"));
		panel.add(defaultSet);
		group.add(defaultSet);
		buttonList.add(defaultSet);
		panel.add(lambda);
		group.add(lambda);
		panel.add(brackets);
		group.add(brackets);
		
		String emptySet = JFLAPPreferences.getEmptySetString();
		if (emptySet.equals("{}"))
			brackets.doClick();
		else if (emptySet.equals(JFLAPConstants.UPPER_LAMBDA))
			lambda.doClick();
		else
			defaultSet.doClick();
		return panel;
	}
	
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();JButton done = new JButton("Done");
		done.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				PreferenceDialog.this.dispose();
			}
		});
		
		JButton reset = new JButton("Reset Defaults");
		reset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
					for(JRadioButton b : buttonList)
						b.doClick();
					bufferAmount.setValue(JFLAPConstants.DEFAULT_TM_BUFFER);
					aSpin.getModel().setValue(JFLAPConstants.DEFAULT_LS_ANGLE);
					dSpin.getModel().setValue(JFLAPConstants.DEFAULT_LS_DISTANCE);
					hSpin.getModel().setValue(JFLAPConstants.DEFAULT_LS_HUE);
					iSpin.getModel().setValue(JFLAPConstants.DEFAULT_LS_INCREM);
					wSpin.getModel().setValue(JFLAPConstants.DEFAULT_LS_WIDTH);
			}
		});
		
		panel.add(done);
		panel.add(reset);
		return panel;
	}

	private JPanel createFormalDefPanel() {
		JPanel formal = new JPanel();
		GroupLayout layout = new GroupLayout(formal);
		formal.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		JLabel angle = createLSystemLabel("Angle Change"), distance = createLSystemLabel("Distance"), hue = createLSystemLabel("Hue Change"), inc = createLSystemLabel("Line Increment"), width = createLSystemLabel("Line Width"), 
				openClose = new JLabel("Regex Grouping: "), union = new JLabel(
				"Regex union symbol: ");

		aSpin = createLSystemSpinner(LS_ANGLE, JFLAPPreferences.getLSAngle());
		dSpin = createLSystemSpinner(LS_DISTANCE, JFLAPPreferences.getLSDistance());
		hSpin = createLSystemSpinner(LS_HUE, JFLAPPreferences.getLSHue());
		iSpin = createLSystemSpinner(LS_INCREMENT, JFLAPPreferences.getLSIncrement());
		wSpin = createLSystemSpinner(LS_WIDTH, JFLAPPreferences.getLSWidth());
		JPanel openCloseP = createOpenClosePanel(), unionP = createUnionPanel();

		layout.setHorizontalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(Alignment.LEADING)
								.addComponent(angle).addComponent(distance)
								.addComponent(hue).addComponent(inc)
								.addComponent(width).addComponent(openClose)
								.addComponent(union))
				.addGroup(
						layout.createParallelGroup(Alignment.LEADING)
								.addComponent(aSpin).addComponent(dSpin)
								.addComponent(hSpin).addComponent(iSpin)
								.addComponent(wSpin).addComponent(openCloseP)
								.addComponent(unionP)));
		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(angle).addComponent(aSpin))
				.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(distance).addComponent(dSpin))
				.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(hue).addComponent(hSpin))
				.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(inc).addComponent(iSpin))
				.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(width).addComponent(wSpin))
				.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(openClose).addComponent(openCloseP))
				.addGroup(
						layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(union).addComponent(unionP)));

		return formal;
	}

	private JLabel createLSystemLabel(String text) {
		return new JLabel("Default L-System " + text + ": ");
	}

	private JSpinner createLSystemSpinner(int type, double current) {
		SpinnerModel bufferAmount = new LSystemPrefSpinner(type, current);
		return new JSpinner(bufferAmount);
	}

	private JPanel createOpenClosePanel() {
		JPanel panel = new JPanel();
		ButtonGroup group = new ButtonGroup();

		JRadioButton paren = new JRadioButton(new OpenClosePrefAction(PARENS));
		JRadioButton squareBrackets = new JRadioButton(new OpenClosePrefAction(SQUARE_BRACKETS));
		JRadioButton pointyBrackets = new JRadioButton(new OpenClosePrefAction(POINTY_BRACKETS));
		JRadioButton curlyBrackets = new JRadioButton(new OpenClosePrefAction(CURLY_BRACKETS));

		panel.add(paren);
		group.add(paren);
		buttonList.add(paren);
		panel.add(squareBrackets);
		group.add(squareBrackets);
		panel.add(pointyBrackets);
		group.add(pointyBrackets);
		panel.add(curlyBrackets);
		group.add(curlyBrackets);

		String currentGroup = JFLAPPreferences
				.getCurrentRegExOpenGroup().getString();

		if (currentGroup.equals(CURLY_BRACKETS[0]))
			curlyBrackets.doClick();
		else if (currentGroup.equals(SQUARE_BRACKETS[0]))
			squareBrackets.doClick();
		else if (currentGroup.equals(POINTY_BRACKETS[0]))
			pointyBrackets.doClick();
		else
			paren.doClick();
		return panel;
	}

	private JPanel createUnionPanel() {
		JPanel panel = new JPanel();
		ButtonGroup group = new ButtonGroup();

		JRadioButton plus = new JRadioButton(new UnionPrefAction(
				JFLAPConstants.PLUS));
		JRadioButton bar = new JRadioButton(new UnionPrefAction(
				JFLAPConstants.BAR));
//		JRadioButton u = new JRadioButton(new UnionPrefAction(
//				JFLAPConstants.UNION));
//		JRadioButton logOr = new JRadioButton(new UnionPrefAction(
//				JFLAPConstants.LOGICAL_OR));
		panel.add(plus);
		group.add(plus);
		buttonList.add(plus);
		panel.add(bar);
		group.add(bar);
//		panel.add(u);
//		group.add(u);
//		panel.add(logOr);
//		group.add(logOr);

		String union = JFLAPPreferences.getUnionOperator().getString();
		if (union.equals(JFLAPConstants.PLUS))
			plus.doClick();
//		else if (union.equals(JFLAPConstants.BAR))
		else
			bar.doClick();
//		else if (union.equals(JFLAPConstants.UNION))
//			u.doClick();
//		else
//			logOr.doClick();
		return panel;
	}

	private JPanel createColorPanel() {
		JPanel color = new JPanel(new BorderLayout());
		color.setName("Color");
		
		final JColorChooser chooser = new JColorChooser();
		color.add(chooser, BorderLayout.WEST);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		
		final JRadioButton state = new JRadioButton("State Color"),
		selected = new JRadioButton("Selected State Color"),
		background = new JRadioButton("Background Color"), 
		cyk = new JRadioButton("CYK Highlight Color"),
		trans = new JRadioButton("Transition Color"),
		selTrans = new JRadioButton("Selected Transition Color");
		
		buttonPanel.add(state);
		buttonPanel.add(selected);
		buttonPanel.add(cyk);
		buttonPanel.add(background);
		buttonPanel.add(trans);
		buttonPanel.add(selTrans);
		
		JButton setColor = new JButton("Set Colors");
		setColor.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Color c = chooser.getColor();
				if(state.isSelected())
					JFLAPPreferences.setStateColor(c);
				if(selected.isSelected())
					JFLAPPreferences.setSelectedStateColor(c);
				if(background.isSelected())
					JFLAPPreferences.setBackgroundColor(c);
				if(cyk.isSelected())
					JFLAPPreferences.setCYKHighlight(c);
				if(trans.isSelected())
					JFLAPPreferences.setTransitionColor(c);
				if(selTrans.isSelected())
					JFLAPPreferences.setSelectedTransitionColor(c);
			}
		});
		buttonPanel.add(setColor);		
		
		JButton reset = new JButton("Reset Defaults");
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFLAPPreferences.resetColors();
			}
		});
		buttonPanel.add(reset);
		
		JButton done = new JButton("Done");
		done.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PreferenceDialog.this.dispose();
			}
		});
		buttonPanel.add(done);

		color.add(buttonPanel, BorderLayout.EAST);
		return color;
	}

	public JTabbedPane getTabbedPane() {
		return myTabbedPane;
	}

	private class ModeAction extends AbstractAction {

		private JFLAPMode mode;

		public ModeAction(JFLAPMode mode) {
			super(mode.toString());
			this.mode = mode;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFLAPPreferences.setDefaultMode(mode);
		}
	}
	
	private class GroupingAction extends AbstractAction {
		
		private GroupingPair myPair;

		public GroupingAction(char[] pair){
			super(pair[0]+" "+pair[1]);
			myPair = new GroupingPair(pair[0], pair[1]);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFLAPPreferences.setDefaultGrouping(myPair);
		}
	}

	private class CYKDirectionAction extends AbstractAction {

		private boolean diagonal;

		public CYKDirectionAction(boolean diagonal) {
			super(diagonal ? "Diagonal" : "Horizontal");
			this.diagonal = diagonal;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFLAPPreferences.setCYKDiagonal(diagonal);
		}

	}

	private class LambdaPrefAction extends AbstractAction {

		public LambdaPrefAction() {
			super(JFLAPConstants.LAMBDA);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFLAPPreferences.setEmptyString(JFLAPConstants.LAMBDA);
		}
	}

	private class EpsilonPrefAction extends AbstractAction {
		public EpsilonPrefAction() {
			super(JFLAPConstants.EPSILON);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFLAPPreferences.setEmptyString(JFLAPConstants.EPSILON);
		}
	}

	private class EmptySetPrefAction extends AbstractAction {
		private String myString;

		public EmptySetPrefAction(String empty) {
			super(empty);
			myString = empty;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFLAPPreferences.setEmptySetString(myString);
		}
	}

	private class LSystemPrefSpinner extends SpinnerNumberModel {
		private int myType;

		public LSystemPrefSpinner(int type, double current) {
			super(current, 0.1, 1000.0, 0.1);
			myType = type;
		}

		@Override
		public Object getValue() {
			double val = (Double) super.getValue();
			switch (myType) {
			case (LS_ANGLE):
				JFLAPPreferences.setLSAngle(val);
				break;
			case (LS_DISTANCE):
				JFLAPPreferences.setLSDistance(val);
				break;
			case (LS_HUE):
				JFLAPPreferences.setLSHue(val);
				break;
			case (LS_INCREMENT):
				JFLAPPreferences.setLSIncrement(val);
				break;
			case (LS_WIDTH):
				JFLAPPreferences.setLSWidth(val);
				break;
			}
			return val;
		}
	}

	private class OpenClosePrefAction extends AbstractAction {

		private char[] myPair;

		public OpenClosePrefAction(char[] pair) {
			super(pair[0]+""+pair[1]);
			myPair = pair;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFLAPPreferences.setRegexGrouping(new String[]{""+myPair[0], ""+myPair[1]});
		}
		
	}

	private class UnionPrefAction extends AbstractAction {
		private String myUnion;

		public UnionPrefAction(String union) {
			super(union);
			myUnion = union;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFLAPPreferences.setUnionOperator(myUnion);
		}
	}
}
