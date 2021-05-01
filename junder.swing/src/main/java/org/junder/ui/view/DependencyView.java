package org.junder.ui.view;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.junder.ui.model.AppModel;
import org.junder.ui.model.ModelEvent;
import org.junder.widgets.CellInfoWidget;
import org.junder.widgets.MatrixWidget;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

/**
 * Container for the elements of the matrix view
 */
@SuppressWarnings("deprecation")
public final class DependencyView extends JPanel {

	private static final long serialVersionUID = -3064817230659716282L;
	private MatrixWidget matrixWidget;
	private final Observer modelObserver = new Observer() {

		@Override
		public void update(Observable o, Object arg) {
			if (arg == ModelEvent.MatrixLoaded) {
				matrixWidget.setModel(AppModel.instance.document.getModel());
				cellInfoWidget.setModel(AppModel.instance.document);
			} else if (arg == ModelEvent.SelectionChanged) {
				matrixWidget.showSelection();
				cellInfoWidget.refeshModelView();
			}
		}
	};
	private CellInfoWidget cellInfoWidget;

	/**
	 * Create the panel.
	 */
	public DependencyView() {
		setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.8);
		splitPane.setOneTouchExpandable(true);
		add(splitPane, BorderLayout.CENTER);

		matrixWidget = new MatrixWidget();
		splitPane.setLeftComponent(matrixWidget);
		cellInfoWidget = new CellInfoWidget();
		AppModel.instance.document.addObserver(modelObserver);
		splitPane.setRightComponent(cellInfoWidget);
	}

	public MatrixWidget getMatrixWidget() {
		return matrixWidget;
	}
}
