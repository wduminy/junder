package org.junder.widgets.model;

import java.awt.Rectangle;

public final class CellPoint {
	public static final CellPoint Origin = new CellPoint(0,0);
	public final int col;
	public final int row;
	public CellPoint(int c, int r) {
		col = c;
		row = r;
	}
	
	public boolean isAt(int c, int r) {
		return col == c && row == r;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CellPoint other = (CellPoint) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "("+col+"," +row+")";
	}

	public CellPoint left() {
		if (col > 0)
			return new CellPoint(col-1, row);
		else
			return this;
	}

	public CellPoint right(int lastCol) {
		if (col < lastCol)
			return new CellPoint(col + 1, row);
		else
			return this;
	}

	public CellPoint up() {
		if (row > 0)
			return new CellPoint(col, row - 1);
		else
			return this;
	}

	public CellPoint down(int lastRow) {
		if (row < lastRow)
			return new CellPoint(col, row + 1);
		else
			return this;
	}

	public Rectangle rectangle(int cellSize) {
		return new Rectangle(col*cellSize,row*cellSize,cellSize,cellSize);
	}

	public CellPoint next(int lastCol, int lastRow) {
		CellPoint r = right(lastCol);
		if (r == this) {
			r = down(lastRow);
			if (r != this) 
				r = new CellPoint(0, r.row);
		}
		return r;
	}

	public CellPoint previous(int lastColumn) {
		CellPoint r = left();
		if (r == this) {
			r = up();
			if (r != this) 
				r = new CellPoint(lastColumn, r.row);
		}
		return r;
	}

	public boolean inRange(int lastCol, int lastRow) {
		return col <= lastCol && row <= lastRow;
	}
}
