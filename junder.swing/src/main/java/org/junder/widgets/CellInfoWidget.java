package org.junder.widgets;

import java.awt.Color;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.junder.structure.JavaClass;
import org.junder.widgets.model.CellInfoModel;

import net.miginfocom.swing.MigLayout;

public final class CellInfoWidget extends JPanel {
	private ClassListModel fromListModel = new ClassListModel();
	private ClassListModel toListModel = new ClassListModel();

	public CellInfoWidget() {
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		setLayout(new MigLayout("", "[grow][grow][]", "[][][][][][grow]"));

		JLabel lblPackage = new JLabel("Package:");
		add(lblPackage, "cell 0 0,alignx left");

		fromName = new JTextField();
		fromName.setEditable(false);
		add(fromName, "cell 0 1 2 1,growx");
		fromName.setColumns(10);

		JLabel lblUses = new JLabel("Uses Package:");
		add(lblUses, "cell 0 2,alignx left");

		toName = new JTextField();
		toName.setEditable(false);
		add(toName, "cell 0 3 2 1,growx");
		toName.setColumns(10);

		JLabel lblFromClass = new JLabel("Classes:");
		add(lblFromClass, "cell 0 4,alignx left");

		JLabel lblTo = new JLabel("Uses classes:");
		add(lblTo, "cell 1 4");

		JList<JavaClass> fromList = new JList<JavaClass>(fromListModel);
		add(fromList, "cell 0 5,grow");

		JList<JavaClass> toList = new JList<JavaClass>(toListModel);
		add(toList, "cell 1 5,grow");

	}

	private static final long serialVersionUID = -2993813099973958716L;
	private JTextField fromName;
	private JTextField toName;
	private CellInfoModel model;

	public void setModel(CellInfoModel v) {
		model = v;
		refeshModelView();
	}

	public void refeshModelView() {
		clearContents();
		if (model.hasInfo())
			showContents();
		else
			clearContents();
	}

	private void showContents() {
		fromName.setText(model.infoFrom().getName());
		toName.setText(model.infoTo().getName());
		fromListModel.clear();
		model.infoFrom().classesUsedOf(
				model.infoTo()).forEach(fromListModel::insertSort);
		toListModel.clear();
		model.infoTo().classedUsedBy(
				model.infoFrom()).forEach(toListModel::insertSort);
	}

	private void clearContents() {
		fromName.setText("");
		toName.setText("");
		fromListModel.clear();
		toListModel.clear();
	}

	private static final class ClassListModel extends DefaultListModel<JavaClass> {
		private static final long serialVersionUID = 604528198570724704L;
		void insertSort(JavaClass e) {
			int pos = 0;
			String name = e.toString();
			while (pos < getSize()) {
				if ( String.CASE_INSENSITIVE_ORDER.compare(getElementAt(pos).toString(), name) > 0) {
					insertElementAt(e, pos);
					return;
				}
				pos++;
			}
			addElement(e);
		}
	}

}
