package org.junder.widgets.model;

/**
 * A matrix with integer cell values.  
 */
public interface MatrixModel {

	/**
	 * The number of columns for this matrix.
	 * @return a number larger or equal to zero
	 */
	int getColumnCount();

	/**
	 * The number of rows for this matrix.
	 * @return a number larger or equal to zero
	 */
	int getRowCount();

	
	/**
	 * If a value zero or less it is hidden
	 * @param c ranges from 0 to {@link #getColumnCount()} - 1
	 * @param r ranges from 0 to {@link #getRowCount()} - 1
	 * @return
	 */
	int getCellValue(int c, int r);

	
	/**
	 * The name of the row at index i
	 * @param i ranges from 0 to {@link #getSize()} - 1
	 * @return
	 */
	String getRowName(int i);
	
	
	/**
	 * The name of the column at index i
	 * @param i ranges from 0 to {@link #getSize()} - 1
	 * @return
	 */
	String getColumnName(int i);
	
	MatrixSelection getManager();

}