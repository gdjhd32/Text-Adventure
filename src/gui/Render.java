package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import main.Main;

@SuppressWarnings("serial")
public class Render extends JFrame {

	private String[] VALID_KEYS = { "w", "a", "s", "d" };
	private boolean[] isPressed = new boolean[VALID_KEYS.length];

	private TextArea output;
	private Main parent;

	public Render(Main parent) {
		super("Text-Adventure");
		this.parent = parent;
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
					if ((e.getKeyChar() + "").equals(VALID_KEYS[i]))
						isPressed[i] = false;
			}

			@Override
			public void keyPressed(KeyEvent e) {
				for (int i = 0; i < VALID_KEYS.length; i++)
					if ((e.getKeyChar() + "").equals(VALID_KEYS[i]) && !isPressed[i]) {
						isPressed[i] = true;
						parent.keyPressed(e.getKeyChar() + "");
					}
			}
		});

		setBounds(100, 100, 270, 600);
		setResizable(false);
		setVisible(true);

		output = new TextArea();
		output.setBounds(-2, -1, getWidth() - 12, getHeight() - 19);
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
