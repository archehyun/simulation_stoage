package sim.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import sim.model.SimEvent;
import sim.model.SimModelManager;
import sim.model.stoage.atc.ATCJobManager;
import sim.model.stoage.atc.SimATC;
import sim.model.stoage.block.BlockManager;
import sim.model.stoage.jobmanager.JobManager;

/**
 * @author ¹ÚÃ¢Çö
 *
 */
public class SimMain {
	
	SimViewATC obj[];	
	SimViewBlock block[];
	
	SimMap canvas = new SimMap();
	
	SimModelManager atcManager = ATCJobManager.getInstance();
	JobManager jobManager = JobManager.getInstance();	
	BlockManager blockManager = BlockManager.getInstance();
	JFrame frame;
	private SimView txfArea;	
	
	public void init()
	{
		for(int i=0;i<BlockManager.block;i++)
		{
			SimATC atc1 = new SimATC("atc"+i,i);
			
			atcManager.addSimModel(atc1);
		}
		
		blockManager.init();		
		
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
			block[i] = new SimViewBlock(i, i*90+75,30);
			canvas.addDrawObject(block[i]);
		}
		
		
		obj = new SimViewATC[BlockManager.block];
		
		for(int i=0;i<obj.length;i++)
		{
			obj[i] = new SimViewATC(i,i*90+75, 15);
			canvas.addDrawObject(obj[i]);
		}
		
		
		
		
		SimEvent event = new SimEvent(0);
		event.setEventMessage("simstart");
		
		jobManager.append(event);		
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
		
		EquipTable equipTable = new EquipTable();
		
		frame = new JFrame();
		
		JPanel pnMain = new JPanel(new BorderLayout());
		
		txfArea = new SimView();
		
		JTabbedPane pane = new JTabbedPane();
		pane.addTab("Map", canvas);
		pane.addTab("Log", txfArea);		
		
		JPanel pnControl = new JPanel(new BorderLayout());
		
		JTextField txfInput = new JTextField();
		
		pnControl.add(txfInput);
		
		pnControl.add(getButtons(),BorderLayout.EAST);
		
		
		pnMain.add(pane);
		pnMain.add(pnControl,BorderLayout.SOUTH);
		//pnMain.add(new JScrollPane(equipTable),BorderLayout.EAST);
		
		frame.getContentPane().add(pnMain);
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ViewUtil.center(frame);
		frame.setVisible(true);
	}
	
	public JComponent getButtons()
	{
		JPanel pnMain = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JButton butStart = new JButton("Start");
		butStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationStart();
			}
		});
		
		JButton butStop = new JButton("Stop");
		butStop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationStop();
			}
		});
		
		pnMain.add(butStart);
		pnMain.add(butStop);
		
		return pnMain;
	}
	
	

	public static void main(String[] args) {
		
		SimMain main = new SimMain();
		
		main.init();

	}

}
