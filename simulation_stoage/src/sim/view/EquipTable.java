package sim.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.block.Block;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.manager.ATCManager;
import sim.view.framework.IFMonitor;

public class EquipTable extends JPanel implements IFMonitor {

	private int blockID;

	JTable table;

	ATCManager manager = ATCManager.getInstance();

	BlockManager blockManager = BlockManager.getInstance();

	static Object[] columnNames = { "ATCID", "Busy", "Bay", "Row", "Count" };

	static Object[][] rowData  = new Object[][]  {
			{ new Integer(1), true, "Tom", "Male" }, { new Integer(2), true, "Jane", "Female" }
	};
	SimpleTableModel model;

	private JLabel lblBlockID;

	private JLabel lblUtil;

	private JLabel lblATCSpeed;

	private JLabel lblHoistSpeed;

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	public EquipTable(int blockID) {

		this.blockID = blockID;
		manager.getATCManager(blockID).addMonitor(this);

		//		manager.addMonitor(this);
		blockManager.addMonitor(this);

		table = new JTable();
		model = new SimpleTableModel();
		table.setModel(model);

		// Tweak the appearance of the table by manipulating its column model
		TableColumnModel colmodel = table.getColumnModel();

		// Set column widths
		colmodel.getColumn(0).setPreferredWidth(150);
		colmodel.getColumn(0).setPreferredWidth(50);
		colmodel.getColumn(1).setPreferredWidth(50);
		colmodel.getColumn(2).setPreferredWidth(50);
		colmodel.getColumn(3).setPreferredWidth(50);

		this.setLayout(new BorderLayout());

		/*	lblBlockID = new JLabel();

			lblBlockID.setText("Block:" + blockID);

			lblUtil = new JLabel();
		 */
		JScrollPane comp = new JScrollPane(table);

		this.add(comp);

		this.add(buildInfo(), BorderLayout.NORTH);

		//	this.setPreferredSize(new Dimension(260, 100));

	}



	@Override
	public void updateMonitor(SimEvent message) {

		String type = (String) message.get("type");
		if (type == null)
			return;
		if (type.equals("atc")) {
			SimATC atc = (SimATC) message.get("item");

			if (atc != null) {
				try {
					model.update(atc);

					table.tableChanged(new TableModelEvent(model));
					repaint();

					table.setModel(model);


				} catch (Exception e) {

					e.printStackTrace();

				}
			}
		}


		if (type != null && type.equals("block")) {

			Block blocks[] = (Block[]) message.get("blocks");

			try {
			int blockContainerCount = blocks[blockID].getContainerCount();

			int totalSlot = blocks[blockID].getTotalSlot();

			float persent = (float) blockContainerCount / (float) totalSlot * 100;


			lblUtil.setText(blockContainerCount + "/" + totalSlot + "(" + String.format("%.1f", persent) + "%)");
			} catch (Exception e) {
				lblUtil.setText(e.getMessage());
			}
		}
	}

	class SimpleTableModel extends AbstractTableModel {
		private Object[][] data = {};

		private String[] columnNames = { "ATCID", "Busy", "Row", "Bay", "Count" };

		private Class[] columnClass = { Integer.class, Boolean.class, String.class, String.class, Integer.class };

		private Object[][] rowData = {};

			public SimpleTableModel() {
			}

		public void update(SimATC atcItem) {

			for (int i = 0; i < rowData.length; i++) {
				if (rowData[i][0].equals(atcItem.getSimName())) {
					rowData[i][0] = atcItem.getSimName();
					rowData[i][1] = atcItem.isIdle();
					rowData[i][2] = atcItem.getX();
					rowData[i][3] = atcItem.getY();
					rowData[i][4] = atcItem.getWorkCount();
					return;
				}
			}
			Object[][] temp = new Object[rowData.length + 1][5];
			for (int i = 0; i < rowData.length; i++) {
				temp[i] = rowData[i];
			}
			temp[rowData.length][0] = atcItem.getSimName();
			temp[rowData.length][1] = atcItem.isIdle();
			temp[rowData.length][2] = atcItem.getX();
			temp[rowData.length][3] = atcItem.getY();
			temp[rowData.length][4] = atcItem.getWorkCount();
			rowData = temp;

		}

			public void update(List li) {

			List blockATCli = new LinkedList<>();

			for (int i = 0; i < li.size(); i++) {

				SimATC atcItem = (SimATC) li.get(i);
				if (atcItem.getBlockID() == blockID) {
					blockATCli.add(atcItem);
				}
				}
			rowData = new Object[blockATCli.size()][5];

			for (int i = 0; i < blockATCli.size(); i++) {

				SimATC atcItem = (SimATC) blockATCli.get(i);

				rowData[i][0] = atcItem.getSimName();
				rowData[i][1] = atcItem.isIdle();
				rowData[i][2] = atcItem.getX();
				rowData[i][3] = atcItem.getY();
				rowData[i][4] = atcItem.getWorkCount();

				//System.out.println("atc x:" + atcItem.getX());
			}

			}

			@Override
			public int getRowCount() {
				return rowData.length;
			}

			@Override
			public int getColumnCount() {
				return columnNames.length;
			}

			@Override
			public String getColumnName(int columnIndex) {
				return columnNames[columnIndex];
			}

			@Override
			public Class getColumnClass(int columnIndex) {
				return columnClass[columnIndex];
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				boolean isEditable = true;
				if (columnIndex == 0) {
					isEditable = false; // Make the ID column non-editable
				}
				return isEditable;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				return rowData[rowIndex][columnIndex];
			}

			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				rowData[rowIndex][columnIndex] = aValue;
			}
	}

	private JComponent buildInfo() {

		lblBlockID = new JLabel();

		lblUtil = new JLabel();

		lblATCSpeed = new JLabel();

		lblHoistSpeed = new JLabel();

		JPanel pnMain = new JPanel(new GridLayout(0, 1));

		JPanel pnInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));

		pnInfo.setBorder(BorderFactory.createTitledBorder("Block " + blockID));

		/*pnInfo.add(new JLabel("Block ID"));
		pnInfo.add(lblBlockID);*/
		pnInfo.add(new JLabel("Utilization"));
		pnInfo.add(lblUtil);

		JPanel pnATC = new JPanel(new FlowLayout(FlowLayout.LEFT));

		pnATC.setBorder(BorderFactory.createTitledBorder("ATC"));

		pnATC.add(new JLabel("ATC Speed"));
		pnATC.add(lblATCSpeed);
		pnATC.add(new JLabel("Hoist Speed"));
		pnATC.add(lblHoistSpeed);

		pnMain.add(pnInfo);
		pnMain.add(pnATC);

		//lblBlockID.setText(blockID + "��");

		return pnMain;

	}

	public void updateView() {

		model = new SimpleTableModel();

		model.update(ATCManager.getInstance().getATCManager(blockID).getATCs());
		table.setModel(model);
		table.updateUI();


	}

}
