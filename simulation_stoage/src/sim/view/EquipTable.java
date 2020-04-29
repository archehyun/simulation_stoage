package sim.view;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import sim.model.SimEvent;

public class EquipTable extends JTable implements IFMonitor{
	
	
	static Object[] columnNames = {"ID", "Name",  "Gender"  }  ;

	static Object[][] rowData  = new Object[][]  {
	    {new Integer(1), "Tom", "Male"  },
	    {new Integer(2), "Jane", "Female"}
	};
	SimpleTableModel model;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public EquipTable() {
		model = new SimpleTableModel();
		this.setModel(model);
	}

	@Override
	public void updateMonitor(SimEvent message) {
		// TODO Auto-generated method stub
		
	}
	
	class SimpleTableModel extends AbstractTableModel {
		  private Object[][] data = {};
		  private String[] columnNames = { "ID", "Name", "Gender" };
		  private Class[] columnClass = { Integer.class, String.class, String.class };
		  private Object[][] rowData = new Object[][] {
		      { new Integer(1), "Tom", "Male" },
		      { new Integer(2), "Jack", "Female" } };

		  public SimpleTableModel() {
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
