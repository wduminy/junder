package org.junder.widgets.model;

import java.util.Optional;

public interface MatrixSelection {
	Optional<CellPoint> getSelectedCell();
	void setSelectedCell(CellPoint v);
}
