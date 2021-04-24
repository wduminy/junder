package org.junder.ui.model;

import java.util.Collection;
import java.util.Optional;

import org.junder.structure.AdjacencyStructure;
import org.junder.structure.JavaPackage;
import org.junder.widgets.model.MatrixModel;
import org.junder.widgets.model.MatrixSelection;
public final class AdjacencyModel extends AdjacencyStructure implements MatrixModel {
	private final MatrixSelection selectionManager;
	private final Optional<AdjacencyModel> parent;
	
	public AdjacencyModel(MatrixSelection m, Collection<? extends JavaPackage> list, AdjacencyModel theParent) {
		super(list);
		selectionManager = m;
		parent =  Optional.ofNullable(theParent);
	}
	
	public AdjacencyModel(MatrixSelection m, Collection<? extends JavaPackage> list) {
		this(m, list, null);
	}

	@Override
	public int getColumnCount() {
		return size();
	}

	@Override
	public int getRowCount() {
		return size();
	}

	@Override
	public int getCellValue(int c, int r) {
		return getPackage(r).countUsagesTo(getPackage(c));
	}

	@Override
	public String getColumnName(int i) {
		return getPackage(i).shortName();
	}

	@Override
	public String getRowName(int i) {
		return getPackage(i).shortName();
	}

	@Override
	public MatrixSelection getManager() {
		return selectionManager;
	}

	public Optional<AdjacencyModel> getParent() {
		return parent;
	}

}
