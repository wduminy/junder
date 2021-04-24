package org.junder.ui.model;

import java.util.Observable;
import java.util.Optional;

import org.junder.structure.JavaPackage;
import org.junder.structure.ProgramStructure;
import org.junder.widgets.model.CellInfoModel;
import org.junder.widgets.model.CellPoint;
import org.junder.widgets.model.Direction;
import org.junder.widgets.model.MatrixSelection;

/**
 * An observer receives instance of {@link ModelEvent} 
 */
public class Document extends Observable implements MatrixSelection, CellInfoModel {

	private AdjacencyModel model;
	private Optional<CellPoint> selectedCell = Optional.empty();

	public AdjacencyModel getModel() {
		return model;
	}

	public void setStructure(ProgramStructure s) {
		model = new AdjacencyModel(this,s.getPackages());
		notifyChange(ModelEvent.MatrixLoaded);
		setSelectedCell(CellPoint.Origin);
	}

	private void notifyChange(ModelEvent e) {
		setChanged();
		notifyObservers(e);
	}

	@Override
	public Optional<CellPoint> getSelectedCell() {
		return selectedCell;
	}

	@Override
	public void setSelectedCell(CellPoint v) {
		if (v.inRange(model.getColumnCount()-1,model.getRowCount()-1)) {
			selectedCell = Optional.of(v);
			notifyChange(ModelEvent.SelectionChanged);
		}
	}

	@Override
	public JavaPackage infoFrom() {
		return model.getPackage(selectedCell.get().row);
	}

	@Override
	public JavaPackage infoTo() {
		return model.getPackage(selectedCell.get().col);
	}

	@Override
	public boolean hasInfo() {
		return selectedCell.isPresent();
	}

	public String statusText() {
		if (model == null)
			return "";
		else {
			int v = model.cycleUsingCount();
			if (v > 0)
				return Integer.toString(v) + " usings in cycles";
			else
				return "";
		}
	}

	public void select(Direction d) {
		CellPoint oldCell = getSelectedCell().orElse(CellPoint.Origin);
		switch (d) {
			case Left:
				setSelectedCell(oldCell.left());
				break;
			case Right:
				setSelectedCell(oldCell.right(model.getColumnCount()-1));
				break;
			case Up:
				setSelectedCell(oldCell.up());
				break;
			case Down:
				setSelectedCell(oldCell.down(model.getRowCount()-1));
				break;
			case Next:
				setSelectedCell(nextWithValue(oldCell));
				break;
			case Previous:
				setSelectedCell(previousWithValue(oldCell));
				break;
		}
	}

	private CellPoint nextWithValue(CellPoint from) {
		final CellPoint last = new CellPoint(model.getColumnCount()-1, model.getRowCount()-1);
		CellPoint at = from.next(last.col,last.row);
		while (!at.equals(last)) {
			if (model.getCellValue(at.col, at.row) > 0)
				return at;
			at = at.next(last.col,last.row);
		}
		return at;
	}

	private CellPoint previousWithValue(CellPoint from) {
		final int lastColumn = model.getColumnCount()-1;
		CellPoint at = from.previous(lastColumn);
		while (!at.equals(CellPoint.Origin)) {
			if (model.getCellValue(at.col, at.row) > 0)
				return at;
			at = at.previous(lastColumn);
		}
		return at;
	}

	public void makeRowMatrix() {
		createChildMatrix(getSelectedCell().get().row);
	}

	public void makeColumnMatrix() {
		createChildMatrix(getSelectedCell().get().col);
	}
	
	private void createChildMatrix(int index) {
		JavaPackage p = model.getPackage(index);
		model = new AdjacencyModel(this,getModel().linkedPackages(p),model);
		selectedCell = Optional.empty();
		notifyChange(ModelEvent.MatrixLoaded);
	}
	
	
	public void gotoParentMatrix() {
		model = getModel().getParent().get();
		selectedCell = Optional.empty();
		notifyChange(ModelEvent.MatrixLoaded);	
	}

}
