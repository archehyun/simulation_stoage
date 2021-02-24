package sim.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import sim.model.impl.stoage.block.BlockManager;

public class SimInfoPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	EquipTable equipTable[];



	public SimInfoPanel() {

	}

	private void createView() {
		this.setBackground(Color.black);
		this.setLayout(new GridLayout(0, 1));
		equipTable = new EquipTable[BlockManager.block];
		for (int i = 0; i < BlockManager.block; i++) {
			equipTable[i] = new EquipTable(i);
			add(equipTable[i]);
		}
		this.setPreferredSize(new Dimension(260, 100));

	}


	public void updateView() {
		createView();
	}



}
