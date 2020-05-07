package sim.view;

import java.util.List;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import sim.model.SimEvent;
import sim.model.stoage.atc.SimATC;

public class EquipTable extends JTable implements IFMonitor{


	static Object[] columnNames = { "ATCID", "Busy", "Bay", "Row", "Count" };

	static Object[][] rowData  = new Object[][]  {
			{ new Integer(1), true, "Tom", "Male" }, { new Integer(2), true, "Jane", "Female" }
	};
	SimpleTableModel model;


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	public EquipTable() {
		model = new SimpleTableModel();
		this.setModel(model);

		// Tweak the appearance of the table by manipulating its column model
		TableColumnModel colmodel = getColumnModel();

		// Set column widths
		colmodel.getColumn(0).setPreferredWidth(100);
		colmodel.getColumn(0).setPreferredWidth(50);
		colmodel.getColumn(1).setPreferredWidth(25);
		colmodel.getColumn(2).setPreferredWidth(25);
		colmodel.getColumn(3).setPreferredWidth(50);

	}

	@Override
	public void updateMonitor(SimEvent message) {
		List li = (List) message.get("atc");

		if (li == null)
		{
			return;
		}

		try {
			model.update(li);

			tableChanged(new TableModelEvent(model));
			repaint();

			this.setModel(model);


		} catch (Exception e) {

			e.printStackTrace();

		}


	}

	class SimpleTableModel extends AbstractTableModel {
		private Object[][] data = {};

		private String[] columnNames = { "ATCID", "Busy", "Bay", "Row", "Count" };

		private Class[] columnClass = { Integer.class, Boolean.class, String.class, String.class, Integer.class };
		private Object[][] rowData = new Object[][] {
				{ new Integer(1), true, "Tom", "Male", 1 }, { new Integer(2), true, "Jack", "Female", 1 } };

			public SimpleTableModel() {
			}

			public void update(List li) {

			rowData = new Object[li.size()][5];

			for (int i = 0; i < li.size(); i++) {
				SimATC atcItem = (SimATC) li.get(i);
				rowData[i][0] = atcItem.getSimName();
				rowData[i][1] = atcItem.isIdle();
				rowData[i][2] = atcItem.getX();
				rowData[i][3] = atcItem.getY();
				rowData[i][4] = atcItem.getWorkCount();
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


}
