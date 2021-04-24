package org.junder.widgets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import org.junder.widgets.model.CellPoint;
import org.junder.widgets.model.MatrixModel;

/**
 * Provides a view of the columns, rows and cells of a matrix. 
 * It makes use of an underlying {@link MatrixModel}.
 */
public final class MatrixWidget extends JScrollPane {
	private static final long serialVersionUID = 1L;
	public static final int HeaderGutter = 5;
	private ComponentAdapter listener = new ComponentAdapter() {
		public void componentResized(java.awt.event.ComponentEvent e) {
			updateViewSizes();
		};
	};
	
	public MatrixWidget() {
		super(new MatrixPanel());
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		setRowHeaderView(new RowHeader());
		setColumnHeaderView(new ColumnHeader());
		// set the scroll model to redraw every time the pane scrolls
		getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		addComponentListener(listener);
	}
	
	public void setModel(MatrixModel m) {
		getData().reset(m, getGraphics());
		getPanel().setPreferredSize(new Dimension(getData().viewWidth(),getData().viewHeight()));
		updateViewSizes();
		getPanel().repaint();
		getColumnHeader().repaint();
		getRowHeader().repaint();
	}
	
	private MatrixPanel getPanel() {
		return (MatrixPanel) getViewport().getView();
	}
	
	
	private Component getRowPanel() {
		return getRowHeader().getView();
	}
	
	private Component getColumnPanel() {
		return  getColumnHeader().getView();
	}
	
	private MatrixViewModel getData() {
		return getPanel().data;
	}
	
	private void updateViewSizes() {
		final int pixelHeight = Math.min(getSize().height/2, getData().getHeaderSize().height); 
		final int pixelWidth = Math.min(getSize().width/2, getData().getHeaderSize().width); 
		getRowPanel().setPreferredSize(new Dimension(pixelWidth, getData().viewWidth()));
		getColumnPanel().setPreferredSize(new Dimension(getData().viewHeight(), pixelHeight));					
		getRowPanel().revalidate();
		getColumnPanel().revalidate();
		getViewport().revalidate();
	}
	
	private static class BasePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		BasePanel() {
			super();
			setFont(UIManager.getFont("Menu.font"));		
		}
	}
	
	private final static class MatrixPanel extends BasePanel {
		private static final long serialVersionUID = -1993664873369229330L;		
		private final MatrixViewModel data = new MatrixViewModel();
		
		public MatrixPanel() {
			setFocusable(true);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					data.onClick(e.getX(),e.getY());
					repaint();
				}
			});
		}
		
		@Override
		protected void paintComponent(Graphics g) {			
			super.paintComponent(g);
			if (!data.hasCells())
				return;
			// redraw only cells that are visible in the clip bounds
			final Rectangle b = g.getClipBounds();
			final Point p = b.getLocation();
			final int cellSize = data.getCellSize();
			final Point at = new Point(
					Math.max(0, p.x / cellSize - 1), 
					Math.max(0, p.y / cellSize - 1));
			final Point bottomRight = new Point(
					Math.min(data.model.getColumnCount(), at.x + b.width / cellSize + 3),
					Math.min(data.model.getRowCount(), at.y + b.height / cellSize + 3));
			// draw grid
			FontMetrics metrics = g.getFontMetrics();
			final int hOffset =  (cellSize - metrics.getHeight())/2;
			Optional<CellPoint> selected = data.model.getManager().getSelectedCell();
			for (int c = at.x;c < bottomRight.x; c++) { 
				g.drawLine(c*cellSize, at.y*cellSize, c*cellSize, (bottomRight.y)*cellSize);
				for (int r = at.y;r < bottomRight.y; r++) {
					if (c == at.x)
						g.drawLine(at.x*cellSize,r*cellSize,(bottomRight.x)*cellSize, r*cellSize);
					Integer v = data.model.getCellValue(c,r);
					if (v > 0) {
						String s = v.toString();
						final int wOffset = (cellSize - metrics.stringWidth(s)) / 2;
						g.setColor(Color.yellow);
						g.fillRect(c*cellSize+1, r*cellSize+1, cellSize-2, cellSize-2);
						g.setColor(Color.black);
						g.drawString(s, c*cellSize + wOffset, (r+1)*cellSize - hOffset);
					}
					if (r == c) {
						g.drawLine(c*cellSize, r*cellSize, (c+1)*cellSize, (r+1)*cellSize);
					}
				}
				if (selected.isPresent()) {
					g.setColor(Color.red);
					g.drawRect(
							selected.get().col*cellSize+1, 
							selected.get().row*cellSize+1, 
							cellSize-2, cellSize-2);
					g.setColor(Color.black);						
				}
			}
		}
	}
	
	private class RowHeader extends BasePanel {
		private static final long serialVersionUID = 1L;

		public RowHeader() {
			setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (!getData().hasCells())
				return;
			// redraw only rows that are visible in the clip bounds
			final Rectangle b = g.getClipBounds();
			final Point p = b.getLocation();
			final int cellSize = getData().getCellSize();
			final int headerSize = Math.min(getBounds().width, getData().getHeaderSize().width);
			final int startY = Math.max(0, p.y / cellSize - 1);
			final int endY = Math.min(getData().model.getRowCount(), startY + b.height / cellSize + 3);
			// draw grid and header
			FontMetrics metrics = g.getFontMetrics();
			final int hOffset =  (cellSize - metrics.getHeight())/2;
			g.setColor(Color.black);
			for (int r = startY;r < endY; r++) {
				g.drawLine(0,r*cellSize,headerSize, r*cellSize);
				String v = getData().model.getRowName(r);
				int wOffset = metrics.stringWidth(v) + HeaderGutter;
				g.drawString(v, headerSize - wOffset, (r+1)*cellSize - hOffset);
			}
		}
	}
	
	private class ColumnHeader extends BasePanel {
		private static final long serialVersionUID = 1L;

		public ColumnHeader() {
			setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		}

		@Override
		protected void paintComponent(Graphics go) {
			super.paintComponent(go);
			if (!getData().hasCells())
				return;
			Graphics2D g = (Graphics2D) go;
			// redraw only columns that are visible in the clip bounds
			final Rectangle b = g.getClipBounds();
			final Point p = b.getLocation();
			final int cellSize = getData().getCellSize();
			final int headerSize = Math.min(getBounds().height, getData().getHeaderSize().height);
			final int startX = Math.max(0, p.x / cellSize - 1);
			final int endX = Math.min(getData().model.getColumnCount(), startX + b.width / cellSize + 3);
			// draw grid and header
			FontMetrics metrics = g.getFontMetrics();
			final int wOffset =  cellSize/2  - metrics.getHeight()/2;
			AffineTransform transform = new AffineTransform();
			transform.rotate(Math.PI/2);
			Font rotatedFont = getFont().deriveFont(transform);
			Font oldFont = g.getFont();
			g.setFont(rotatedFont);
			for (int c = startX;c < endX; c++) {
				g.drawLine(c*cellSize,0,c*cellSize,headerSize);
				String v = getData().model.getColumnName(c);
				int hOffset = metrics.stringWidth(v) + HeaderGutter;
				g.drawString(v, c*cellSize + wOffset,headerSize - hOffset);
			}
			g.setFont(oldFont);
		}
	}


	static private final class MatrixViewModel {
		private int cellSize = 0;
		private Dimension headerSize = new Dimension(0,0);
		MatrixModel model;
		
		public int getCellSize() {
			if (cellSize == -1) 
				throw new IllegalStateException("cellSize not calculated yet");
			return cellSize;
		}
		
		public void onClick(int x, int y) {
			model.getManager().setSelectedCell(
					new CellPoint(x / getCellSize(), y / getCellSize()));
		}

		private int cellViewSize(boolean width) {
			if (hasCells())
				return (width?
						  model.getColumnCount()
						  : model.getRowCount()) * getCellSize();
			else
				return 0;
		}
		
		public int viewWidth() {return cellViewSize(true);}
		
		public int viewHeight() {return cellViewSize(false);}

		public boolean hasCells() {
			return (model != null && model.getRowCount() > 0);
		}

		public Dimension getHeaderSize() {
			return headerSize;
		}
		
		public void reset(MatrixModel m, Graphics graphics) {
			model = m;
			FontMetrics metrics = graphics.getFontMetrics();
			headerSize = new Dimension(
					metrics.stringWidth(longestColumnName() + "W"),
				    metrics.stringWidth(longestRowName() + "W"));
			cellSize = metrics.stringWidth("0000");			
		}
		
		private String longestColumnName() {
			String result = ""; 			
			if (model == null)
				return result;
			for (int i = 0; i < model.getColumnCount(); i++) {
				final String s = model.getColumnName(i);
				if (s.length() > result.length())
					result = s;
			}
			return result;
		}

		private String longestRowName() {
			String result = ""; 			
			if (model == null)
				return result;
			for (int i = 0; i < model.getRowCount(); i++) {
				final String s = model.getRowName(i);
				if (s.length() > result.length())
					result = s;
			}
			return result;
		}
				
	}

	public void showSelection() {
		Optional<CellPoint> selected = getData().model.getManager().getSelectedCell();
		if (selected.isPresent()) 
			getPanel().scrollRectToVisible(selected.get().rectangle(getData().cellSize));
		getPanel().repaint();
	}
}
