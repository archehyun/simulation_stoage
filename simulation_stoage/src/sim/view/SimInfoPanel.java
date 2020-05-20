package sim.view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import sim.model.impl.stoage.block.BlockManager;

public class SimInfoPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	//ATCJobManager atcManager2 = CrossOverJobManager.getInstance();



	EquipTable equipTable[];



	public SimInfoPanel() {

		createView();

		//atcManager2.addMonitor(equipTable);
	}

	private void createView() {

		this.setLayout(new GridLayout(0, 1));
		equipTable = new EquipTable[3];
		for (int i = 0; i < BlockManager.block; i++) {
			equipTable[i] = new EquipTable(i);
			add(equipTable[i]);
		}
		this.setPreferredSize(new Dimension(260, 100));

	}
	/*
		private JComponent buildInfo() {

			lblBlockID = new JLabel();

			lblUtil = new JLabel();

			lblATCSpeed = new JLabel();

			lblHoistSpeed = new JLabel();

			JPanel pnMain = new JPanel(new GridLayout(0, 1));

			JPanel pnInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));

			pnInfo.setBorder(BorderFactory.createTitledBorder("Block"));

			pnInfo.add(new JLabel("Block ID"));
			pnInfo.add(lblBlockID);
			pnInfo.add(new JLabel("Utilization"));
			pnInfo.add(lblBlockID);

			JPanel pnATC = new JPanel(new FlowLayout(FlowLayout.LEFT));

			pnATC.setBorder(BorderFactory.createTitledBorder("ATC"));

			pnATC.add(new JLabel("ATC Speed"));
			pnATC.add(lblATCSpeed);
			pnATC.add(new JLabel("Hoist Speed"));
			pnATC.add(lblHoistSpeed);

			pnMain.add(pnInfo);
			pnMain.add(pnATC);

			return pnMain;

		}*/

}
