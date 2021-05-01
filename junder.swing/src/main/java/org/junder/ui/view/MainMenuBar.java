package org.junder.ui.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.junder.ui.model.Document;
import org.junder.ui.model.ModelEvent;
import org.junder.widgets.model.Direction;

@SuppressWarnings("deprecation")
public final class MainMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	private final Observer docObserver = new Observer() {

		@Override
		public void update(Observable o, Object t) {
			if (ModelEvent.MatrixLoaded.equals(t)) {
				// navigate.setEnabled(true);
				backMenu.setEnabled(Document.class.cast(o).getModel().getParent().isPresent());
			}
		}
	};
	private JMenuItem backMenu;

	public MainMenuBar(Document doc) {
		JMenu navigate = new JMenu("Navigate");
		// using VIM-like bindings: https://hea-www.harvard.edu/~fine/Tech/vi.html
		navigate.add(navItem(doc, Direction.Up, KeyEvent.VK_W));
		navigate.add(navItem(doc, Direction.Left, KeyEvent.VK_A));
		navigate.add(navItem(doc, Direction.Down, KeyEvent.VK_S));
		navigate.add(navItem(doc, Direction.Right, KeyEvent.VK_D));
		navigate.add(navItem(doc, Direction.Next, KeyEvent.VK_G));
		navigate.add(navItem(doc, Direction.Previous, KeyEvent.VK_F));
		JMenu view = new JMenu("View");
		view.add(menuItem((e) -> {
			doc.makeRowMatrix();
		}, "Row Sub Matrix", KeyEvent.VK_R));
		view.add(menuItem((e) -> {
			doc.makeColumnMatrix();
		}, "Column Sub Matrix", KeyEvent.VK_C));
		view.add(backMenu = menuItem((e) -> {
			doc.gotoParentMatrix();
		}, "Back to Parent Matrix", KeyEvent.VK_B));
		add(view);
		add(navigate);
		doc.addObserver(docObserver);
	}

	private static JMenuItem navItem(Document doc, Direction dir, int keyEvent) {
		return menuItem((e) -> {
			doc.select(dir);
		}, "Go " + dir.toString(), keyEvent);
	}

	public static JMenuItem menuItem(ActionListener action, String text, int keyEvent) {
		JMenuItem r = new JMenuItem(text);
		r.addActionListener(action);
		r.setAccelerator(KeyStroke.getKeyStroke(keyEvent, ActionEvent.ALT_MASK));
		return r;
	}

}
