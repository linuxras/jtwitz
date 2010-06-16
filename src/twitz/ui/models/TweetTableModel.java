/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui.models;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import twitter4j.Status;

/**
 *
 * @author mistik1
 */
public class TweetTableModel extends AbstractTableModel {

	protected Vector<Vector<Status>> tableData = new Vector<Vector<Status>>();

	protected Vector columns = new Vector();

	public TweetTableModel() {

	}

	@Override
	public Class getColumnClass(int col)
	{
		return Status.class;
	}
	
	public Vector<Vector<Status>> getDataVector()
	{
		return tableData;
	}

	public void setDataVector(Vector<Vector<Status>> data, Vector columnData)
	{
		tableData = (data != null) ? data : new Vector<Vector<Status>>();
		columns = (columnData != null) ? columnData : new Vector();
		fireTableStructureChanged();
	}

	public int getRowCount()
	{
		return tableData.size();
	}

	public int getColumnCount()
	{
		return columns.size();
	}

	public Status getValueAt(int rowIndex, int columnIndex)
	{
		Vector<Status> row = tableData.elementAt(rowIndex);
		return row.elementAt(columnIndex);
	}

	public void setValueAt(Status s, int row, int column)
	{
		Vector<Status> r = tableData.elementAt(row);
		r.setElementAt(s, column);
		fireTableCellUpdated(row, column);
	}

	public boolean isCellEditable()
	{
		return false;
	}

	public void addRow(Vector<Status> data)
	{
		insertRow(getRowCount(), data);
	}

	public void insertRow(int row, Vector<Status> data)
	{
		tableData.insertElementAt(data, row);
		fireTableRowsInserted(row, row);
	}

	public void removeRow(int row)
	{
		tableData.removeElementAt(row);
		fireTableRowsDeleted(row, row);
	}

	//These column methods are not really needed for tweets but i'll implement
	// as a just in case method :)
	public void setColumnIdentifiers(Vector columnId)
	{
		setDataVector(tableData, columnId);
	}

	public void addColumn(Object columnName) {
        addColumn(columnName, (Vector)null);
    }

	public void addColumn(Object columnName, Vector<Status> columnData) {
        columns.addElement(columnName);
		if (columnData != null)
		{
			int columnSize = columnData.size();
			if (columnSize > getRowCount())
			{
				tableData.setSize(columnSize);
			}
			justifyRows(0, getRowCount());
			int newColumn = getColumnCount() - 1;
			for (int i = 0; i < columnSize; i++)
			{
				Vector<Status> row = tableData.elementAt(i);
				row.setElementAt(columnData.elementAt(i), newColumn);
			}
		}
		else
		{
			justifyRows(0, getRowCount());
		}

        fireTableStructureChanged();
    }

	/*
	 * This method is borrowed from the DefaultTableModel implementation
	 */
	private void justifyRows(int from, int to)
	{
		tableData.setSize(getRowCount());

		for (int i = from; i < to; i++)
		{
			if (tableData.elementAt(i) == null)
			{
				tableData.setElementAt(new Vector<Status>(), i);
			}
			((Vector<Status>) tableData.elementAt(i)).setSize(getColumnCount());
		}
	}

}
