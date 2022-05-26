package automatCreator;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Render extends JFrame {

	private JPanel mainPanel, backgroundPanel;

	public Render() {
		super("AutomatonCreator");
		initWindow();
	}

	private void initWindow() {
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setBounds(200, 100, 500, 500);
		setVisible(true);
		setResizable(false);

		initComponents();
	}

	private void initComponents() {
		mainPanel = new JPanel();
		mainPanel.setBounds(10, 10, getWidth() - 16 - 20, getHeight() - 39 - 20);
		mainPanel.setLayout(null);

		backgroundPanel = new JPanel();
		backgroundPanel.setBounds(0, 0, 500, 500);
		backgroundPanel.setBackground(new Color(255, 0, 0));

		DraggingComponentsMouseListener backgroundMouseListener = new DraggingComponentsMouseListener(backgroundPanel,
				mainPanel);
		ResizingComponentMouseWheelListener backgroundMouseWheelListener = new ResizingComponentMouseWheelListener(
				backgroundPanel, mainPanel);

		backgroundPanel.addMouseListener(backgroundMouseListener);
		backgroundPanel.addMouseMotionListener(backgroundMouseListener);
		backgroundPanel.addMouseWheelListener(backgroundMouseWheelListener);
		backgroundPanel.setLayout(null);

		JPanel n = new JPanel();
		n.setBounds(0, 0, 50, 50);
		n.setBackground(new Color(0, 0, 0));

		DraggingComponentsMouseListener nMouseListener = new DraggingComponentsMouseListener(n, backgroundPanel);

		n.addMouseListener(nMouseListener);
		n.addMouseMotionListener(nMouseListener);
		n.setLayout(null);

		JPanel nn = new JPanel();
		nn.setBounds(0, 0, 50, 50);
		nn.setBackground(new Color(0, 0, 100));

		DraggingComponentsMouseListener nnMouseListener = new DraggingComponentsMouseListener(nn, backgroundPanel);

		nn.addMouseListener(nnMouseListener);
		nn.addMouseMotionListener(nnMouseListener);
		nn.setLayout(null);

		backgroundPanel.add(n);
		backgroundPanel.add(nn);
		mainPanel.add(backgroundPanel);
		add(mainPanel);
	}

	private class DraggingComponentsMouseListener implements MouseListener, MouseMotionListener {

		public final JPanel PARENT, PARENT_DISPLAYER;
		private int previousMouseX, previousMouseY, componentX, componentY;

		public DraggingComponentsMouseListener(JPanel parent, JPanel parentDisplayer) {
			PARENT = parent;
			PARENT_DISPLAYER = parentDisplayer;
			previousMouseX = -1;
			previousMouseY = -1;
			componentX = PARENT.getBounds().x;
			componentY = PARENT.getBounds().y;

		}

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			previousMouseX = -1;
			previousMouseY = -1;
		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			dragged(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {

		}

		private void dragged(MouseEvent e) {
			if (previousMouseX > -1 && previousMouseY > -1) {
				componentX += e.getXOnScreen() - previousMouseX;
				componentY += e.getYOnScreen() - previousMouseY;

				if (PARENT.getWidth() < PARENT_DISPLAYER.getWidth()) {
					if (componentX < 0)
						componentX = 0;
					if (componentX > PARENT_DISPLAYER.getWidth() - PARENT.getWidth())
						componentX = PARENT_DISPLAYER.getWidth() - PARENT.getWidth();
				}
				if (PARENT.getWidth() > PARENT_DISPLAYER.getBounds().width) {
					if (componentX > 0)
						componentX = 0;
					if (componentX < PARENT_DISPLAYER.getWidth() - PARENT.getWidth())
						componentX = PARENT_DISPLAYER.getWidth() - PARENT.getWidth();
				}
				if (PARENT.getHeight() < PARENT_DISPLAYER.getHeight()) {
					if (componentY < 0)
						componentY = 0;
					if (componentY > PARENT_DISPLAYER.getHeight() - PARENT.getHeight())
						componentY = PARENT_DISPLAYER.getHeight() - PARENT.getHeight();
				}
				if (PARENT.getHeight() > PARENT_DISPLAYER.getHeight()) {
					if (componentY > 0)
						componentY = 0;
					if (componentY < PARENT_DISPLAYER.getHeight() - PARENT.getHeight())
						componentY = PARENT_DISPLAYER.getHeight() - PARENT.getHeight();
				}

				PARENT.setBounds(componentX, componentY, PARENT.getWidth(), PARENT.getHeight());
			}

			previousMouseX = e.getXOnScreen();
			previousMouseY = e.getYOnScreen();
		}

		public void setComponentCoordiantes(int x, int y) {
			componentX = x;
			componentY = y;
		}

	}

	private class ResizingComponentMouseWheelListener implements MouseWheelListener {

		private JPanel PARENT, PARENT_DISPLAYER;
		private final double SCALE = 1.2;

		public ResizingComponentMouseWheelListener(JPanel parent, JPanel parentDisplayer) {
			PARENT = parent;
			PARENT_DISPLAYER = parentDisplayer;
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.getPreciseWheelRotation() < 0) {
				PARENT.setBounds(PARENT.getX(), PARENT.getY(), (int) (PARENT.getWidth() * SCALE),
						(int) (PARENT.getHeight() * SCALE));
				if (PARENT.getComponentCount() > 0)
					resizeChildComponents(PARENT, true);
			}

			if (e.getPreciseWheelRotation() > 0 && PARENT.getWidth() > PARENT_DISPLAYER.getWidth()
					&& !(PARENT.getWidth() / SCALE < PARENT_DISPLAYER.getWidth())
					&& PARENT.getHeight() > PARENT_DISPLAYER.getHeight()
					&& !(PARENT.getHeight() / SCALE < PARENT_DISPLAYER.getHeight())) {
				PARENT.setBounds(PARENT.getX(), PARENT.getY(), (int) (PARENT.getWidth() / SCALE),
						(int) (PARENT.getHeight() / SCALE));
				if (PARENT.getX() + PARENT.getWidth() < PARENT_DISPLAYER.getWidth())
					PARENT.setBounds(PARENT_DISPLAYER.getWidth() - PARENT.getWidth(), PARENT.getY(), PARENT.getWidth(),
							PARENT.getHeight());
				if (PARENT.getY() + PARENT.getHeight() < PARENT_DISPLAYER.getHeight())
					PARENT.setBounds(PARENT.getX(), PARENT_DISPLAYER.getHeight() - PARENT.getHeight(),
							PARENT.getWidth(), PARENT.getHeight());
				((DraggingComponentsMouseListener) PARENT.getMouseListeners()[0]).setComponentCoordiantes(PARENT.getX(),
						PARENT.getY());
				if (PARENT.getComponentCount() > 0)
					resizeChildComponents(PARENT, false);
			}
		}

		private void resizeChildComponents(JPanel c, boolean scaleUp) {
			if (scaleUp) {
				for (int i = 0; i < c.getComponentCount(); i++) {
					PARENT.getComponent(i).setBounds((int) (c.getComponent(i).getX() * SCALE),
							(int) (c.getComponent(i).getY() * SCALE), (int) (c.getComponent(i).getWidth() * SCALE),
							(int) (c.getComponent(i).getHeight() * SCALE));
					((DraggingComponentsMouseListener) c.getComponent(i).getMouseListeners()[0])
							.setComponentCoordiantes(c.getComponent(i).getX(), c.getComponent(i).getY());
					try {
						if (((JPanel) c.getComponent(i)).getComponentCount() > 0) {
							resizeChildComponents(((JPanel) c.getComponent(i)), scaleUp);
						}
					} catch (Exception e) {

					}

				}
				return;
			}
			for (int i = 0; i < PARENT.getComponentCount(); i++) {
				PARENT.getComponent(i).setBounds((int) (PARENT.getComponent(i).getX() / SCALE),
						(int) (PARENT.getComponent(i).getY() / SCALE),
						(int) (PARENT.getComponent(i).getWidth() / SCALE),
						(int) (PARENT.getComponent(i).getHeight() / SCALE));
				((DraggingComponentsMouseListener) PARENT.getComponent(i).getMouseListeners()[0])
						.setComponentCoordiantes(PARENT.getComponent(i).getX(), PARENT.getComponent(i).getY());
				try {
					if (((JPanel) c.getComponent(i)).getComponentCount() > 0) {
						resizeChildComponents(((JPanel) c.getComponent(i)), scaleUp);
					}
				} catch (Exception e) {

				}
			}

		}
	}

}
