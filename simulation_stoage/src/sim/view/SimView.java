package sim.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import sim.model.core.SimEvent;
import sim.view.framework.IFMonitor;

public class SimView extends JPanel implements IFMonitor{
	
	SimTextArea textArea;
	
	SimTextArea textArea2;

	/**
	 * 
	 */
	
	JPanel pnInfo;
	
	JLabel lblBlockCount;
	
	
	
	private static final long serialVersionUID = 1L;

	public SimView() {
		
		this.setLayout(new BorderLayout());
		textArea = new SimTextArea(25, 20);
		textArea2 = new SimTextArea(25, 20);
		
		pnInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		lblBlockCount = new JLabel();
		
		pnInfo.add(new JLabel("block"));
		pnInfo.add(lblBlockCount);
		GridLayout layout = new GridLayout(1,2);
		JPanel pnLog = new JPanel(layout);
		
		pnLog.add(new JScrollPane(textArea));
		pnLog.add(new JScrollPane(textArea2));
		
		this.add(pnLog);
		this.add(pnInfo,BorderLayout.SOUTH);
	}

	@Override
	public synchronized void updateMonitor(SimEvent message) {
		if(message.getSimName().equals("jobManager"))
		{
			lblBlockCount.setText(message.getEventMessage());
		}
		else if(message.getEventMessage().startsWith("atc"))
		{
			textArea2.setText(message.getEventMessage());
		}
		else 
		{
			textArea.setText(message.getEventMessage());
		}
	}
	
	class SimTextArea extends JTextArea
	{
		
		String simName;
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		List<String> list;;
		public SimTextArea(int i, int j) {
			super(i,j);
			
			list = new LinkedList<String>();
		}
		
		public void setText(String message)
		{
			if(list.size()>2000)
			{
				list.remove(0);
			}
			list.add(message);
			
			StringBuffer buf = new StringBuffer();		
			
			Iterator<String> iter = list.iterator();
			while(iter.hasNext())
			{
				buf.append(iter.next()+"\n");	
			}
			super.setText(buf.toString());
			
			setCaretPosition( getText().length()-1);
		}
	}

}
