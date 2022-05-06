package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.Main;

@SuppressWarnings("serial")
public class Render extends JFrame {

	private final String[] VALID_KEYS;
	private final boolean[] isPressed;

	private TextArea output;
	private JPanel backgroundLeft, backgroundRight;
	private final Main PARENT;

	public Render(Main parent, String[] validKeys) {
		super("Text-Adventure");
		VALID_KEYS = validKeys;
		isPressed = new boolean[VALID_KEYS.length];
		PARENT = parent;
		initWindow();
	}

	private void initWindow() {
		setLayout(null);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		for (int i = 0; i < VALID_KEYS.length; i++) {
			isPressed[i] = false;
		}

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				for (int i = 0; i < VALID_KEYS.length; i++)
					if (VALID_KEYS[i].contains(e.getKeyChar() + ""))
						isPressed[i] = false;
			}

			@Override
			public void keyPressed(KeyEvent e) {
				for (int i = 0; i < VALID_KEYS.length; i++)
					if (VALID_KEYS[i].contains(e.getKeyChar() + "") && !isPressed[i]) {
						isPressed[i] = true;
						PARENT.keyPressed(e.getKeyChar() + "");
					}
			}
		});

		setBounds(100, 100, 540, 600);
		setResizable(false);
		setVisible(true);

		backgroundLeft = new JPanel();
		backgroundLeft.setBackground(new Color(5, 5, 5));
		backgroundLeft.setBounds(0, 0, 137, getHeight());
		add(backgroundLeft);
		
		backgroundRight = new JPanel();
		backgroundRight.setBackground(new Color(5, 5, 5));
		backgroundRight.setBounds(386, 0, 138, getHeight());
		add(backgroundRight);
		
		output = new TextArea();
		output.setBounds(135, -2, 270, getHeight() - 18);
		output.setBackground(new Color(0, 0, 0));
		output.setForeground(new Color(0, 255, 0));
		output.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 12));
		output.setEditable(false);
		output.setFocusable(false);
		add(output);
	}

	public void println(String arg) {
		if (output.getText().length() == 0) {
			output.append(" " + arg);
			return;
		}
		output.append("\n " + arg);
	}

	public void changeLast(String arg) {
		if (output.getText().length() == 0) {
			output.append(" " + arg);
			return;
		}
		int lastSectionLength = output.getText().split("\n")[output.getText().split("\n").length - 1].length();
		if (output.getText().length() == lastSectionLength) {
			output.setText(" " + arg);
			return;
		}
		output.replaceRange(" " + arg, output.getText().length() - lastSectionLength, output.getText().length());
	}

}
