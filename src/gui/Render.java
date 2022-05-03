package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.desktop.ScreenSleepEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import main.Main;

@SuppressWarnings("serial")
public class Render extends JFrame {

	private String[] VALID_KEYS;
	private boolean[] isPressed;

	private TextArea output;
	private Main parent;

	private KeyThread keyThread;

	public Render(Main parent, String[] validKeys) {
		super("Text-Adventure");
		VALID_KEYS = validKeys;
		isPressed = new boolean[VALID_KEYS.length];
		this.parent = parent;
		initWindow();
		keyThread = new KeyThread(validKeys);
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
						parent.keyPressed(keyThread.controll(e.getKeyChar() + ""));
						isPressed[i] = true;
						return;
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

	private class KeyThread {

		private String[] validKeys;
		private String lastKey;
		private WaitingThread wThread;
		boolean keyCombination;

		public KeyThread(String[] validKeys) {
			super();
			this.validKeys = validKeys;
			keyCombination = true;
		}

		public String controll(String key) {
			if (lastKey != null && keyCombination) {
				for (int i = 0; i < validKeys.length; i++) {
					if (validKeys[i].contains(lastKey) && validKeys[i].contains(key)) {
						lastKey = key;
						keyCombination = true;
						wThread = new WaitingThread(50, this);
						wThread.start();
						return validKeys[i];
					}
				}
				return "";
			}
			lastKey = key;
			keyCombination = true;
			wThread = new WaitingThread(100, this);
			wThread.start();
			
			for (int i = 0; i < validKeys.length; i++)
				if (validKeys[i].equals(lastKey))
					return lastKey;
			return "";
		}

		private class WaitingThread extends Thread {

			private int waitingTime;
			private KeyThread parent;

			public WaitingThread(int time, KeyThread parent) {
				waitingTime = time;
				this.parent = parent;
			}

			public void sleep() {
				try {
					Thread.sleep(waitingTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				parent.keyCombination = false;
			}
			
			@Override
			public void run() {
				sleep();
			}
		}

	}

}