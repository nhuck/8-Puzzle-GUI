// Course:          CS4242
// Student name:    Noah Huck
// Assignment #:    #1
// Due Date:        02/12/2020

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;

/**
 * GUI Class for the 8 Puzzle A* Problem
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private final ButtonGroup gridButtons = new ButtonGroup();
	private JTextField man1;
	private JTextField man2;
	private JTextField man3;
	private JTextField man4;
	private JTextField man5;
	private JTextField man6;
	private JTextField man7;
	private JTextField man8;
	private JTextField man9;
	private JTextArea solutionSpace;
	private JTextField closedNodes;
	private JTextField moveCount;

	JPanel gridPanel = new JPanel();

	private int[] localGrid = { 1, 2, 3, 8, 0, 4, 7, 6, 5 };
	private int[] test = new int[9];

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			new AStar().command();
		} else {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						MainFrame frame = new MainFrame();
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 951, 633);
		setTitle("8 Puzzle");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		this.setResizable(false);

		Box verticalBox = Box.createVerticalBox();
		contentPane.add(verticalBox);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		splitPane.setDividerLocation(0.6678);
		verticalBox.add(splitPane);

		gridPanel.setPreferredSize(new Dimension(470, 380));
		splitPane.setLeftComponent(gridPanel);

		JPanel controls = new JPanel();
		controls.setPreferredSize(new Dimension(470, 10));
		gridPanel.setPreferredSize(new Dimension(180, 380));
		splitPane.setRightComponent(controls);
		controls.setLayout(null);

		JLabel lblClosedNodes = new JLabel("Closed Nodes:");
		lblClosedNodes.setBounds(44, 299, 112, 35);

		JLabel lblMoves = new JLabel("# of Moves:");
		lblMoves.setBounds(54, 344, 78, 35);

		controls.add(lblClosedNodes);
		controls.add(lblMoves);

		closedNodes = new JTextField();
		closedNodes.setEditable(false);
		closedNodes.setLocation(145, 299);
		closedNodes.setSize(78, 35);
		moveCount = new JTextField();
		moveCount.setEditable(false);
		moveCount.setLocation(145, 344);
		moveCount.setSize(68, 35);
		controls.add(closedNodes);
		controls.add(moveCount);

		JButton manEntry = new JButton("Manual Entry");
		manEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (testValid())
					changeGrid();
			}
		});
		manEntry.setBounds(44, 164, 169, 35);
		controls.add(manEntry);

		JButton reset = new JButton("Reset to Solved");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		reset.setBounds(44, 209, 169, 35);
		controls.add(reset);

		JButton genMoves = new JButton("Generate Answer");
		genMoves.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generate();
			}
		});
		genMoves.setBounds(44, 254, 169, 35);
		controls.add(genMoves);

		man1 = new JTextField();
		man1.setBounds(44, 21, 38, 35);
		controls.add(man1);

		man2 = new JTextField();
		man2.setBounds(103, 21, 38, 35);
		controls.add(man2);

		man3 = new JTextField();
		man3.setBounds(161, 21, 38, 35);
		controls.add(man3);

		man4 = new JTextField();
		man4.setBounds(44, 72, 38, 35);
		controls.add(man4);

		man5 = new JTextField();
		man5.setBounds(103, 72, 38, 35);
		controls.add(man5);

		man6 = new JTextField();
		man6.setBounds(161, 72, 38, 35);
		controls.add(man6);

		man7 = new JTextField();
		man7.setBounds(44, 119, 38, 35);
		controls.add(man7);

		man8 = new JTextField();
		man8.setBounds(103, 119, 38, 35);
		controls.add(man8);

		man9 = new JTextField();
		man9.setBounds(161, 119, 38, 35);
		controls.add(man9);

		GridBagLayout gbl_gridPanel = new GridBagLayout();
		gbl_gridPanel.columnWidths = new int[] { 146, 146, 146 };
		gbl_gridPanel.rowHeights = new int[] { 146, 146, 146 };
		gbl_gridPanel.columnWeights = new double[] { 0.0, 0.0, 0.0 };
		gbl_gridPanel.rowWeights = new double[] { 0.0, 0.0, 0.0 };
		gridPanel.setLayout(gbl_gridPanel);

		addButtons();

		JPanel panel_1 = new JPanel();
		panel_1.setSize(new Dimension(700, 100));
		panel_1.setPreferredSize(new Dimension(700, 100));
		verticalBox.add(panel_1);
		panel_1.setLayout(null);

		JLabel solutionLabel = new JLabel("Solution:");
		solutionLabel.setBounds(0, 0, 63, 109);

		solutionLabel.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(solutionLabel);

		solutionSpace = new JTextArea();
		solutionSpace.setBounds(68, 0, 859, 109);
		solutionSpace.setEditable(false);
		solutionSpace.setPreferredSize(new Dimension(650, 60));
		panel_1.add(solutionSpace);

	}

	/**
	 * addButtons() - Grid button creation separated so they can be redrawn as
	 * needed. Location to place the buttons is found using <code>locate()</code>
	 * and <b>localGrid</b>
	 */
	private void addButtons() {
		JButton btn1 = new JButton("1");
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonPressed(1);
			}
		});
		btn1.setFocusable(false);
		btn1.setMaximumSize(new Dimension(37, 37));
		btn1.setMinimumSize(new Dimension(37, 37));
		btn1.setPreferredSize(new Dimension(37, 37));
		btn1.setFont(new Font("Tahoma", Font.PLAIN, 24));
		gridButtons.add(btn1);
		GridBagConstraints gbc_btn1 = new GridBagConstraints();
		gbc_btn1.fill = GridBagConstraints.BOTH;
		gbc_btn1.insets = new Insets(0, 0, 5, 5);
		gbc_btn1.gridx = locate(1) % 3;
		gbc_btn1.gridy = locate(1) / 3;
		gridPanel.add(btn1, gbc_btn1);

		JButton btn2 = new JButton("2");
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonPressed(2);
			}
		});
		btn2.setFocusable(false);
		btn2.setMaximumSize(new Dimension(37, 37));
		btn2.setMinimumSize(new Dimension(37, 37));
		btn2.setPreferredSize(new Dimension(37, 37));
		btn2.setFont(new Font("Tahoma", Font.PLAIN, 24));
		gridButtons.add(btn2);
		GridBagConstraints gbc_btn2 = new GridBagConstraints();
		gbc_btn2.fill = GridBagConstraints.BOTH;
		gbc_btn2.insets = new Insets(0, 0, 5, 5);
		gbc_btn2.gridx = locate(2) % 3;
		gbc_btn2.gridy = locate(2) / 3;
		gridPanel.add(btn2, gbc_btn2);

		JButton btn3 = new JButton("3");
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonPressed(3);
			}
		});
		btn3.setFocusable(false);
		btn3.setMaximumSize(new Dimension(37, 37));
		btn3.setMinimumSize(new Dimension(37, 37));
		btn3.setPreferredSize(new Dimension(37, 37));
		btn3.setFont(new Font("Tahoma", Font.PLAIN, 24));
		gridButtons.add(btn3);
		GridBagConstraints gbc_btn3 = new GridBagConstraints();
		gbc_btn3.fill = GridBagConstraints.BOTH;
		gbc_btn3.insets = new Insets(0, 0, 5, 0);
		gbc_btn3.gridx = locate(3) % 3;
		gbc_btn3.gridy = locate(3) / 3;
		gridPanel.add(btn3, gbc_btn3);

		JButton btn8 = new JButton("8");
		btn8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonPressed(8);
			}
		});
		btn8.setFocusable(false);
		btn8.setMaximumSize(new Dimension(37, 37));
		btn8.setMinimumSize(new Dimension(37, 37));
		btn8.setPreferredSize(new Dimension(37, 37));
		btn8.setFont(new Font("Tahoma", Font.PLAIN, 24));
		gridButtons.add(btn8);
		GridBagConstraints gbc_btn8 = new GridBagConstraints();
		gbc_btn8.fill = GridBagConstraints.BOTH;
		gbc_btn8.insets = new Insets(0, 0, 5, 5);
		gbc_btn8.gridx = locate(8) % 3;
		gbc_btn8.gridy = locate(8) / 3;
		gridPanel.add(btn8, gbc_btn8);

		JButton btn0 = new JButton("0");
		btn0.setFocusable(false);
		btn0.setMaximumSize(new Dimension(37, 37));
		btn0.setMinimumSize(new Dimension(37, 37));
		btn0.setPreferredSize(new Dimension(37, 37));
		btn0.setVisible(false);
		btn0.setEnabled(false);
		btn0.setBorderPainted(false);
		btn0.setFont(new Font("Tahoma", Font.PLAIN, 24));
		gridButtons.add(btn0);
		GridBagConstraints gbc_btn0 = new GridBagConstraints();
		gbc_btn0.fill = GridBagConstraints.BOTH;
		gbc_btn0.insets = new Insets(0, 0, 5, 5);
		gbc_btn0.gridx = locate(0) % 3;
		gbc_btn0.gridy = locate(0) / 3;
		gridPanel.add(btn0, gbc_btn0);

		JButton btn4 = new JButton("4");
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonPressed(4);
			}
		});
		btn4.setFocusable(false);
		btn4.setMaximumSize(new Dimension(37, 37));
		btn4.setMinimumSize(new Dimension(37, 37));
		btn4.setPreferredSize(new Dimension(37, 37));
		btn4.setFont(new Font("Tahoma", Font.PLAIN, 24));
		gridButtons.add(btn4);
		GridBagConstraints gbc_btn4 = new GridBagConstraints();
		gbc_btn4.fill = GridBagConstraints.BOTH;
		gbc_btn4.insets = new Insets(0, 0, 5, 0);
		gbc_btn4.gridx = locate(4) % 3;
		gbc_btn4.gridy = locate(4) / 3;
		gridPanel.add(btn4, gbc_btn4);

		JButton btn7 = new JButton("7");
		btn7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonPressed(7);
			}
		});
		btn7.setFocusable(false);
		btn7.setMaximumSize(new Dimension(37, 37));
		btn7.setMinimumSize(new Dimension(37, 37));
		btn7.setPreferredSize(new Dimension(37, 37));
		btn7.setFont(new Font("Tahoma", Font.PLAIN, 24));
		gridButtons.add(btn7);
		GridBagConstraints gbc_btn7 = new GridBagConstraints();
		gbc_btn7.fill = GridBagConstraints.BOTH;
		gbc_btn7.insets = new Insets(0, 0, 0, 5);
		gbc_btn7.gridx = locate(7) % 3;
		gbc_btn7.gridy = locate(7) / 3;
		gridPanel.add(btn7, gbc_btn7);

		JButton btn6 = new JButton("6");
		btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonPressed(6);
			}
		});
		btn6.setFocusable(false);
		btn6.setMaximumSize(new Dimension(37, 37));
		btn6.setMinimumSize(new Dimension(37, 37));
		btn6.setPreferredSize(new Dimension(37, 37));
		btn6.setFont(new Font("Tahoma", Font.PLAIN, 24));
		gridButtons.add(btn6);
		GridBagConstraints gbc_btn6 = new GridBagConstraints();
		gbc_btn6.fill = GridBagConstraints.BOTH;
		gbc_btn6.insets = new Insets(0, 0, 0, 5);
		gbc_btn6.gridx = locate(6) % 3;
		gbc_btn6.gridy = locate(6) / 3;
		gridPanel.add(btn6, gbc_btn6);

		JButton btn5 = new JButton("5");
		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonPressed(5);
			}
		});
		btn5.setFocusable(false);
		btn5.setMaximumSize(new Dimension(37, 37));
		btn5.setMinimumSize(new Dimension(37, 37));
		btn5.setPreferredSize(new Dimension(37, 37));
		btn5.setFont(new Font("Tahoma", Font.PLAIN, 24));
		gridButtons.add(btn5);
		GridBagConstraints gbc_btn5 = new GridBagConstraints();
		gbc_btn5.fill = GridBagConstraints.BOTH;
		gbc_btn5.gridx = locate(5) % 3;
		gbc_btn5.gridy = locate(5) / 3;
		gridPanel.add(btn5, gbc_btn5);
	}

	/**
	 * generate() - uses <code>AStar</code> class to fill text areas with relevant
	 * information.
	 */
	private void generate() {
		int[][] input = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				input[i][j] = localGrid[3 * i + j];
			}
		}
		if (new Node(input, 0, null, 0).H == 0)
			return;
		AStar a = new AStar(input);
		String[] out = a.moves(a.doStar());
		String toBox = "";
		for (String s : out) {
			toBox = toBox.concat(s);
		}
		solutionSpace.setText(toBox);
		closedNodes.setText(String.valueOf(a.getClosedNodesSize()));
		moveCount.setText(String.valueOf(out.length));
	}

	/**
	 * reset() - sets <b>localGrid</b> to the solution state and repaints the grid
	 * buttons
	 */
	private void reset() {
		localGrid[0] = 1;
		localGrid[1] = 2;
		localGrid[2] = 3;
		localGrid[3] = 8;
		localGrid[4] = 0;
		localGrid[5] = 4;
		localGrid[6] = 7;
		localGrid[7] = 6;
		localGrid[8] = 5;
		repaintGrid();
	}

	/**
	 * buttonPressed(int) - checks if button is next to the blank. If adjacent to
	 * blank, the button pushed is swapped with the blank
	 * 
	 * @param btn - the value of the button that was pressed
	 */
	public void buttonPressed(int btn) {
		int zeroPos = locate(0);
		int btnPos = locate(btn);

		// Same Row OR same column
		if (btnPos / 3 == zeroPos / 3) {
			if (Math.abs(btnPos - zeroPos) == 1)
				swapButtons(zeroPos, btnPos);
			else
				return;
		} else if (btnPos % 3 == zeroPos % 3) {
			if (Math.abs(btnPos - zeroPos) == 3)
				swapButtons(zeroPos, btnPos);
			else
				return;
		}

	}

	/**
	 * swapButtons(int, int) - swaps two values in <b>localGrid</b> and repaints the
	 * grid buttons
	 * 
	 * @param a - the index in <b>localGrid</b> of the first number to be swapped
	 * @param b - the index in <b>localGrid</b> of the second number to be swapped
	 */
	private void swapButtons(int a, int b) {
		int temp = localGrid[a];
		localGrid[a] = localGrid[b];
		localGrid[b] = temp;
		repaintGrid();
	}

	/**
	 * testValid() - Uses the values in the grid of textfields to determine if they
	 * form a legitimate puzzle case. Values are parsed as integers and assessed for
	 * duplicates, out of range number, number of inversions.
	 * 
	 * @return - true if values form a valid puzzle, false if values are invalid
	 */
	private boolean testValid() {
		int inversions = 0;
		try {
			absorbTest();
		} catch (Exception e) {
			doAlert("Please ONLY enter integers in EVERY box.");
			return false;
		}
		// checks for out of range numbers
		for (int i : test) {
			if (i > 8 || i < 0) {
				doAlert("Numbers must range from 0 to 8, try again.");
				return false;
			}
		}

		// check for repeating numbers:
		int zeros = 0;
		for (int i = 0; i < 9; i++) {
			if (test[Math.abs(test[i])] >= 0)
				test[Math.abs(test[i])] = -test[Math.abs(test[i])];
			else {
				doAlert("Repeating Numbers Exist, try again.");
				return false;
			}
			if (test[i] == 0)
				zeros++;
		}
		if (zeros > 1) {
			doAlert("Repeating Numbers Exist, try again.");
			return false;
		}
		absorbTest();

		// checks for solvability
		for (int i = 0; i < 9; ++i) {
			if (test[i] == 0)
				continue;
			for (int j = i + 1; j < 9; ++j) {
				if (test[j] != 0 && test[i] > test[j])
					inversions++;
			}
		}
		if (inversions % 2 == 0)
			doAlert("This puzzle is not solvable.");
		return (inversions % 2 != 0);
	}

	/**
	 * Gives an alert to the user when attempting to enter an invalid puzzle.
	 * 
	 * @param s - Message to be communicated.
	 */
	private void doAlert(String s) {
		JOptionPane.showConfirmDialog(this, s, "Invalid Puzzle", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
				null);
	}

	/**
	 * absorbTest() - parses testfield grid for integers and stores them in
	 * <b>int[]</b> <code>test</code>
	 */
	private void absorbTest() {
		test[0] = Integer.parseInt(man1.getText());
		test[1] = Integer.parseInt(man2.getText());
		test[2] = Integer.parseInt(man3.getText());
		test[3] = Integer.parseInt(man4.getText());
		test[4] = Integer.parseInt(man5.getText());
		test[5] = Integer.parseInt(man6.getText());
		test[6] = Integer.parseInt(man7.getText());
		test[7] = Integer.parseInt(man8.getText());
		test[8] = Integer.parseInt(man9.getText());
	}

	/**
	 * changeGrid() - called to change button grid to match the textfield grid
	 */
	private void changeGrid() {
		localGrid = Arrays.copyOf(test, test.length);
		repaintGrid();
	}

	/**
	 * repaintGrid() - when the state of the <b>int[]</b> <code>localGrid</code>
	 * changes, this method is called to repaint the buttons in the button grid
	 */
	private void repaintGrid() {
		gridPanel.removeAll();
		gridPanel.setVisible(false);
		addButtons();
		gridPanel.setVisible(true);
	}

	/**
	 * locate(int) - called to locate values within the <b>localGrid</b>
	 * 
	 * @param n - value to be located
	 * @return int - index of <b>n</b> in <b>localGrid</b>
	 */
	private int locate(int n) {
		for (int i = 0; i < 9; i++) {
			if (localGrid[i] == n)
				return i;
		}
		return -1;
	}
}
