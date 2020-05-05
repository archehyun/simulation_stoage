package sim.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sim.model.SimEvent;
import sim.model.stoage.atc.ATCJobManager;
import sim.model.stoage.atc.LandSideATC;
import sim.model.stoage.atc.SeaSideATC;
import sim.model.stoage.atc.SimATC;
import sim.model.stoage.block.BlockManager;
import sim.model.stoage.jobmanager.JobManager;

/**
 * @author  占쏙옙창占쏙옙
 *
 */
public class SimMain {

	SimViewATC obj[][];
	SimViewBlock block[];

	SimMap canvas = new SimMap();

	ATCJobManager atcManager = ATCJobManager.getInstance();
	JobManager jobManager = JobManager.getInstance();
	BlockManager blockManager = BlockManager.getInstance();
	JFrame frame;
	private SimView txfArea;

	EquipTable equipTable;

	public void init()
	{
		for(int i=0;i<BlockManager.block;i++)
		{
			SimATC atc1 = new SeaSideATC("atc_sea" + i, i + 100);

			atc1.setInitLocation(0, 0);
			SimATC atc2 = new LandSideATC("atc_land" + i, i + 200);
			atc2.setInitLocation(0, 25);

			atcManager.addSimModel(atc1);
			atcManager.addSimModel(atc2);
		}

		blockManager.blockInit();

		atcManager.addMonitor(equipTable);

		atcManager.addMonitor(txfArea);
		jobManager.addMonitor(txfArea);
	}

	/**
	 *
	 */
	public void simulationStart()
	{
		block = new SimViewBlock[BlockManager.block];

		for(int i=0;i<BlockManager.block;i++)
		{
			block[i] = new SimViewBlock(i, i * BlockManager.BLOCK_GAP + BlockManager.magin, BlockManager.magin);
			canvas.addDrawObject(block[i]);
		}


		for (int i = 0; i < BlockManager.block; i++) {

			int atcID = i + 100;
			int x = i * 90 + BlockManager.magin;
			canvas.addDrawObject(new SimViewATC(atcID, i, x, BlockManager.magin));

			atcID = i + 200;
			canvas.addDrawObject(new SimViewATC(atcID, i, x, BlockManager.magin));
		}

		atcManager.simStart();
	}

	/**
	 *
	 */
	public void simulationStop()
	{
		SimEvent event = new SimEvent(0);
		event.setEventMessage("simstop");
		jobManager.append(event);
		atcManager.simStop();
	}

	public SimMain() {

		frame = new JFrame();

		JPanel pnMain = new JPanel(new BorderLayout());

		txfArea = new SimView();

		JTabbedPane pane = new JTabbedPane();

		equipTable = new EquipTable();

		JPanel pnCenter = new JPanel(new BorderLayout());
		pnCenter.add(canvas);
		JScrollPane comp = new JScrollPane(equipTable);
		comp.setPreferredSize(new Dimension(260, 100));
		pnCenter.add(comp, BorderLayout.EAST);
		pane.addTab("Map", pnCenter);
		//		pane.addTab("table",);

		pane.addTab("Log", txfArea);

		JPanel pnControl = new JPanel(new BorderLayout());

		JTextField txfInput = new JTextField();

		pnControl.add(txfInput);

		pnControl.add(getButtons(),BorderLayout.EAST);

		pnMain.add(pane);
		pnMain.add(pnControl,BorderLayout.SOUTH);

		frame.getContentPane().add(pnMain);

		frame.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentResized(ComponentEvent e) {
				JFrame frame=(JFrame) e.getComponent();
				System.out.println("frmae:" + frame.getSize());

				float rate = frame.getSize().width / 250;

				BlockManager.blockRate = rate;

			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
		});

		frame.setSize(900, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ViewUtil.center(frame);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
	}

	public JComponent getButtons()
	{
		JPanel pnMain = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JToggleButton butStart = new JToggleButton();
		butStart.setMnemonic('S');

		//JMenuItem�� �⑥��� 異�媛�
   /*     file_New_txt.setAccelerator(KeyStroke.getKeyStroke
                          ('N',InputEvent.CTRL_MASK)); //Ctrl+N
*/



		butStart.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				AbstractButton abstractButton = (AbstractButton) e.getSource();
				abstractButton.getModel();

				if (butStart.isSelected()) {
					abstractButton.setText("Stop");
					simulationStart();
				} else {
					abstractButton.setText("Start");
					simulationStop();
				}

			}
		});

		butStart.setText("Start");

		JButton butAuto = new JButton("Auto");

		butAuto.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SimEvent event = new SimEvent(0);
				event.setEventMessage("simstart");

				jobManager.append(event);

			}
		});

		JButton butNext = new JButton("Next");

		butNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SimEvent event = new SimEvent(0);
				event.setEventMessage("simstart");

				jobManager.putOrder();

			}
		});
		pnMain.add(butStart);
		pnMain.add(butAuto);
		pnMain.add(butNext);

		return pnMain;
	}



	public static void main(String[] args) {

		SimMain main = new SimMain();

		main.init();

	}

}
