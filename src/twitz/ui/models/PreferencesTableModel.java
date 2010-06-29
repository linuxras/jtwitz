/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package twitz.ui.models;

import java.io.Serializable;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author mistik1
 */
public class PreferencesTableModel extends AbstractTableModel implements Serializable {

	Vector<Vector<String>> data = new Vector<Vector<String>>();
	Vector<String> headers = new Vector<String>();

	public PreferencesTableModel(Vector<Vector<String>> dataVector, Vector<String> headers)
	{
		this.data = dataVector;
		this.headers = headers;
	}

	public int getRowCount()
	{
		return data.size();
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public int getColumnCount()
	{
		return headers.size();
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Vector row = data.elementAt(rowIndex);
		return row.elementAt(columnIndex);
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void addRow(Vector<String> rowData) {
		insertRow(getRowCount(), rowData);
	}

	public void insertRow(int row, Vector<String> rowData) {
		data.insertElementAt(rowData, row);
		fireTableRowsInserted(row, row);
	}
}
