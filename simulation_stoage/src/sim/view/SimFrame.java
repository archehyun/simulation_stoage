package sim.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.commom.UnparserableCommandException;
import sim.model.impl.stoage.manager.ATCJobManager;
import sim.view.framework.SimCanvas;

public class SimFrame extends JFrame implements ActionListener {

	protected static Logger logger = Logger.getLogger(SimFrame.class.getName());

	//ATCManager atcManager = ATCManager.getInstance();

	//JobManager jobManager = JobManager.getInstance();

	OptionDialog optionDialog;

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private SimView txfArea;

	private SimInfoPanel infoPanel;

	private JCheckBox bOX;

	SimMain simMain = new SimMain();

	SimCanvas canvas = new SimCanvas(simMain);

	private JToggleButton butStart;

	private JMenuItem startMenuItem;

	private JMenuItem stopMenuItem;

	private JButton butAuto;

	private JTree tree;

	private JCheckBox cbxCount;

	private JButton butNext;

	public SimFrame() {
		logger.debug("log4j:logger.debug()");

		simMain.setCanvas(canvas);

		this.setTitle("Stoage Simualtion");

		JPanel pnMain = new JPanel(new BorderLayout());

		txfArea = new SimView();

		infoPanel = new SimInfoPanel();

		JTabbedPane pane = new JTabbedPane();

		JPanel pnCenter = new JPanel(new BorderLayout());

		pnCenter.add(canvas);

		simMain.setCanvas(canvas);

		pnCenter.add(infoPanel, BorderLayout.EAST);
		pane.addTab("Map", pnCenter);
		//		pane.addTab("table",);

		pane.addTab("Log", txfArea);


		//pnControl.add(getButtons(), BorderLayout.EAST);

		pnMain.add(pane);

		JPanel pnEquipList = buildEquipList();

		pnMain.add(pnEquipList, BorderLayout.WEST);
		pnMain.add(getButtons(), BorderLayout.NORTH);
		pnMain.add(buildControl(), BorderLayout.SOUTH);

		getContentPane().add(pnMain);

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				JFrame frame = (JFrame) e.getComponent();

				float wrate = frame.getSize().width / 250;
				float hrate = frame.getSize().height / 250;
				float rate = frame.getSize().height / 250;
				BlockManager.blockRate = rate;
				BlockManager.blockWRate = wrate;
				BlockManager.blockHRate = hrate;

			}
		});


		this.setJMenuBar(createMenuBar());
		setSize(800, 625);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ViewUtil.center(this);
		setVisible(true);
		setAlwaysOnTop(true);

		//atcManager2.addMonitor(txfArea);
		//jobManager.addMonitor(txfArea);
	}

	private DefaultMutableTreeNode builtTreeNode(Node root) {
		DefaultMutableTreeNode dmtNode;

		dmtNode = new DefaultMutableTreeNode(root.getNodeName());
		NodeList nodeList = root.getChildNodes();
		for (int count = 0; count < nodeList.getLength(); count++) {
			Node tempNode = nodeList.item(count);

			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				if (tempNode.hasChildNodes()) {
					dmtNode.add(builtTreeNode(tempNode));
				}
			}
		}
		return dmtNode;
	}

	/**
	 * @return
	 */
	private JPanel buildEquipList() {
		JPanel pnEquipList = new JPanel(new BorderLayout());

		pnEquipList.setBorder(BorderFactory.createTitledBorder("Equip List"));
		pnEquipList.setPreferredSize(new Dimension(200, 200));
		tree = new JTree(builtTreeNode(simMain.getRoot()));


		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}

		JButton butReload = new JButton("Reload");
		butReload.addActionListener(this);
		pnEquipList.add(tree);
		pnEquipList.add(butReload, BorderLayout.SOUTH);

		return pnEquipList;
	}

	private JPanel buildControl() {
		JPanel pnControl = new JPanel(new BorderLayout());

		JTextField txfInput = new JTextField();


		txfInput.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					JTextField tf = (JTextField) e.getSource();
					//System.out.println(tf.getText() + ":enter");

					try {
						if (tf.getText().equals("?")) {
							tf.setText("W-I-15-1-1");
							return;
						} else {

						}
						simMain.putCommand(tf.getText());
					} catch (UnparserableCommandException e1) {
						JOptionPane.showMessageDialog(SimFrame.this, "command error");
					} catch (ArrayIndexOutOfBoundsException e2) {

						JOptionPane.showMessageDialog(SimFrame.this, "index error");

					}
				}
			}
		});
		JButton butSend = new JButton("Send");
		pnControl.add(txfInput);
		pnControl.add(butSend, BorderLayout.EAST);
		return pnControl;
	}

	private JMenuBar createMenuBar()
	{
		JMenuBar bar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setMnemonic('x');
		exitMenuItem.addActionListener(this);
		fileMenu.add(exitMenuItem);
		JMenu simulationMenu = new JMenu("Simulation");
		simulationMenu.setMnemonic('S');
		startMenuItem = new JMenuItem("Start");

		startMenuItem.addActionListener(this);

		simulationMenu.add(startMenuItem);
		stopMenuItem = new JMenuItem("Stop");
		stopMenuItem.setEnabled(false);
		stopMenuItem.addActionListener(this);
		simulationMenu.add(stopMenuItem);

		JMenu optionMenu = new JMenu("Options");
		optionMenu.setMnemonic('O');

		JMenuItem atcOptionMenu = new JMenuItem("Option");
		atcOptionMenu.addActionListener(this);

		optionMenu.add(atcOptionMenu);

		bar.add(fileMenu);
		bar.add(simulationMenu);
		bar.add(optionMenu);
		return bar;
	}

	/**
	 * @return
	 */
	public JComponent getButtons() {

		JPanel pnMain = new JPanel(new BorderLayout());
		JPanel pnLeftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));

		bOX = new JCheckBox("Init");
		bOX.setSelected(true);

		pnLeftButtons.add(bOX);

		cbxCount = new JCheckBox("count");
		cbxCount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.setCountView(cbxCount.isSelected());

			}
		});

		pnLeftButtons.add(cbxCount);

		butStart = new JToggleButton();

		//JMenuItem
		/*     file_New_txt.setAccelerator(KeyStroke.getKeyStroke
		                  ('N',InputEvent.CTRL_MASK)); //Ctrl+N
		 */

		butStart.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				AbstractButton abstractButton = (AbstractButton) e.getSource();
				abstractButton.getModel();

				if (abstractButton.isSelected()) {

					abstractButton.setText("Stop");
					butAuto.setEnabled(true);
					butNext.setEnabled(true);
					simMain.clear();

					simMain.createInit();
					simMain.simulationStart();
				} else {
					abstractButton.setText("Start");

					butAuto.setEnabled(false);
					butNext.setEnabled(false);
					simMain.simulationStop();
					simMain.clear();
				}
			}
		});


		butStart.setText("Start");
		butStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (bOX.isSelected()) {
					simMain.blockInit();
				}
			}
		});

		butAuto = new JButton("Auto");
		butAuto.setMnemonic('A');

		butAuto.setEnabled(false);

		butAuto.addActionListener(this);

		butNext = new JButton("Next");
		butNext.setMnemonic('N');

		butNext.setEnabled(false);

		butNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SimEvent event = new SimEvent(0);
				event.setEventMessage("simstart");

				simMain.putOrder();

			}
		});

		pnLeftButtons.add(butStart);
		pnLeftButtons.add(butAuto);
		pnLeftButtons.add(butNext);

		JPanel pnRightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton butOption = new JButton("Option");

		butOption.addActionListener(this);

		JButton butClear = new JButton("Clear");
		butClear.setEnabled(false);
		butClear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				new OptionDialog();
			}
		});

		pnRightButtons.add(butOption);
		pnRightButtons.add(butClear);

		pnMain.add(pnLeftButtons);
		pnMain.add(pnRightButtons, BorderLayout.EAST);

		return pnMain;
	}

	public static void main(String[] args) {

		try {
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

		new SimFrame();

	}

	class OptionDialog extends JDialog implements ActionListener {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		private JTextField txfATCSpeed;

		public OptionDialog() {

			createAndView();

		}

		private void createAndView() {

			this.setTitle("ATC Update");
			JPanel pnMain = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JButton butSave = new JButton("Save");
			butSave.setMnemonic('S');
			butSave.addActionListener(OptionDialog.this);
			txfATCSpeed = new JTextField(10);
			pnMain.add(new JLabel("ATC Speed: "));
			pnMain.add(txfATCSpeed);
			pnMain.add(butSave);

			this.setModal(true);

			txfATCSpeed.setText(String.valueOf(ATCJobManager.SPEED));
			this.getContentPane().add(pnMain);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.pack();
			this.setResizable(false);
			this.setLocationRelativeTo(SimFrame.this);
			this.setAlwaysOnTop(true);
			setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();
			if (command.equals("Save")) {


				String strSpeed = txfATCSpeed.getText();

				try {

					float speed = Float.parseFloat(strSpeed);

					simMain.updateATCSpeed(speed);

					setVisible(false);
					this.dispose();

				} catch (NumberFormatException ee) {
					txfATCSpeed.setText(String.valueOf(ATCJobManager.SPEED));

					JOptionPane.showMessageDialog(this, "Only Integer");
				}



			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Option")) {
			new OptionDialog();
		}
		else if (command.equals("Auto")) {
			SimEvent event = new SimEvent(0);
			event.setEventMessage("simstart");

			simMain.append(event);

			butAuto.setEnabled(false);
			butNext.setEnabled(false);
		}
		else if (command.equals("Start")) {
			if (bOX.isSelected()) {
				simMain.blockInit();
			}
			butStart.setSelected(true);
			butNext.setEnabled(true);
			JMenuItem item = (JMenuItem) e.getSource();
			item.setEnabled(false);
			stopMenuItem.setEnabled(true);

			infoPanel.updateView();

		} else if (command.equals("Stop")) {
			butStart.setSelected(false);
			JMenuItem item = (JMenuItem) e.getSource();
			item.setEnabled(false);
			startMenuItem.setEnabled(true);
		}
		else if (command.equals("Exit")) {
			System.exit(1);
		}
		else if (command.equals("Reload")) {
			try {
				simMain.parse();


				tree.setModel(new MyTreeMode(builtTreeNode(simMain.getRoot())));
				for (int i = 0; i < tree.getRowCount(); i++) {
					tree.expandRow(i);
				}
			} catch (SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}


	class MyTreeMode extends DefaultTreeModel {

		public MyTreeMode(TreeNode root) {
			super(root);
		}

	}

}
