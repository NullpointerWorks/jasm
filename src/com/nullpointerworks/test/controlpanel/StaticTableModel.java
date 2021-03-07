package com.nullpointerworks.test.controlpanel;

import javax.swing.table.DefaultTableModel;

public class StaticTableModel extends DefaultTableModel 
{
	private static final long serialVersionUID = -828323646501490661L;
	
	/*
	 * no cell can be edited
	 */
	@Override
	public boolean isCellEditable(int row, int column) 
    {
    	return false;
    }
	
	/*
	 * returns the class of the data object for the column.
	 * if they contain images, it will appear in the table
	 */
	@Override
	public Class<?> getColumnClass(int column)
    {
        return getValueAt(0, column).getClass();
    }
}
