package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import main.Main;

@SuppressWarnings("serial")
public class Render extends JFrame {

	private final String[] VALID_KEYS;
	private final boolean[] isPressed;

	private TextArea output;
	private Panel backgroundLeft, backgroundRight;
	private Label playerName;
	private Label playerHP, playerStr, playerAg, playerWeapon, playerAtk, playerArmor, playerDef;
	private Label enemyName;
	private Label enemyHP, enemyStr, enemyAg, enemyWeapon, enemyAtk, enemyArmor, enemyDef;
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

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				for (int i = 0; i < VALID_KEYS.length; i++)
					if (VALID_KEYS[i].contains(e.getKeyChar() + ""))
						isPressed[i] = false;
			}

			@Override
			public void keyPressed(KeyEvent e) {
				for (int i = 0; i < VALID_KEYS.length; i++)
					if (VALID_KEYS[i].equals(e.getKeyChar() + "") && !isPressed[i]) {
						isPressed[i] = true;
						PARENT.keyPressed(e.getKeyChar() + "");
					}
			}
		});

		setBounds(100, 100, 660, 600);
		setResizable(false);
		setVisible(true);

		int textAreaWidth = (int) (getWidth() / 2.5), leftSideSize = ((getWidth() - textAreaWidth) / 2) + 4,
				rightSideSize = getWidth() - (leftSideSize + textAreaWidth - 10);
		Color labelColor = new Color(26, 183, 189), backgroundColor = new Color(5, 5, 5);

		playerName = new Label(PARENT.getPlayer().NAME);
		playerName.setBounds(0, 0, leftSideSize + 2, 20);
		playerName.setBackground(backgroundColor);
		playerName.setForeground(labelColor);
		playerName.setAlignment(Label.CENTER);
		add(playerName);

		playerHP = new Label("HP: " + PARENT.getPlayer().getCurrentHp() + " / " + PARENT.getPlayer().getMaxHp());
		playerHP.setBounds(0, 20, leftSideSize + 2, 20);
		playerHP.setBackground(backgroundColor);
		playerHP.setForeground(labelColor);
		add(playerHP);

		playerStr = new Label("STR: " + PARENT.getPlayer().getStr());
		playerStr.setBounds(0, 40, leftSideSize + 2, 20);
		playerStr.setBackground(backgroundColor);
		playerStr.setForeground(labelColor);
		add(playerStr);

		playerAg = new Label("AG: " + PARENT.getPlayer().getAg());
		playerAg.setBounds(0, 60, leftSideSize + 2, 20);
		playerAg.setBackground(backgroundColor);
		playerAg.setForeground(labelColor);
		add(playerAg);

		playerWeapon = new Label("Weapon: None");
		playerWeapon.setBounds(0, 80, leftSideSize + 2, 20);
		playerWeapon.setBackground(backgroundColor);
		playerWeapon.setForeground(labelColor);
		add(playerWeapon);

		playerAtk = new Label("ATK: " + PARENT.getPlayer().getAtk());
		playerAtk.setBounds(0, 100, leftSideSize + 2, 20);
		playerAtk.setBackground(backgroundColor);
		playerAtk.setForeground(labelColor);
		add(playerAtk);

		playerArmor = new Label("Armor: None");
		playerArmor.setBounds(0, 120, leftSideSize + 2, 20);
		playerArmor.setBackground(backgroundColor);
		playerArmor.setForeground(labelColor);
		add(playerArmor);

		playerDef = new Label("DEF: " + PARENT.getPlayer().getCurrentDef() + " / " + PARENT.getPlayer().getMaxDef());
		playerDef.setBounds(0, 140, leftSideSize + 2, 20);
		playerDef.setBackground(backgroundColor);
		playerDef.setForeground(labelColor);
		add(playerDef);

		backgroundLeft = new Panel();
		backgroundLeft.setBackground(new Color(5, 5, 5));
		backgroundLeft.setBounds(0, 0, leftSideSize + 2, getHeight());
		backgroundLeft.setFocusable(false);
		add(backgroundLeft);

		enemyName = new Label(PARENT.getEnemy().NAME);
		enemyName.setBounds(leftSideSize + textAreaWidth - 26, 0, rightSideSize, 20);
		enemyName.setBackground(backgroundColor);
		enemyName.setForeground(labelColor);
		enemyName.setAlignment(Label.CENTER);
		add(enemyName);

		enemyHP = new Label("HP: " + PARENT.getEnemy().getCurrentHp() + " / " + PARENT.getEnemy().getMaxHp());
		enemyHP.setBounds(leftSideSize + textAreaWidth - 26, 20, rightSideSize, 20);
		enemyHP.setBackground(backgroundColor);
		enemyHP.setForeground(labelColor);
		add(enemyHP);

		enemyStr = new Label("STR: " + PARENT.getEnemy().getStr());
		enemyStr.setBounds(leftSideSize + textAreaWidth - 26, 40, rightSideSize, 20);
		enemyStr.setBackground(backgroundColor);
		enemyStr.setForeground(labelColor);
		add(enemyStr);

		enemyAg = new Label("AG: " + PARENT.getEnemy().getAg());
		enemyAg.setBounds(leftSideSize + textAreaWidth - 26, 60, rightSideSize, 20);
		enemyAg.setBackground(backgroundColor);
		enemyAg.setForeground(labelColor);
		add(enemyAg);

		enemyWeapon = new Label("Weapon: None");
		enemyWeapon.setBounds(leftSideSize + textAreaWidth - 26, 80, rightSideSize, 20);
		enemyWeapon.setBackground(backgroundColor);
		enemyWeapon.setForeground(labelColor);
		add(enemyWeapon);

		enemyAtk = new Label("ATK: " + PARENT.getEnemy().getAtk());
		enemyAtk.setBounds(leftSideSize + textAreaWidth - 26, 100, rightSideSize, 20);
		enemyAtk.setBackground(backgroundColor);
		enemyAtk.setForeground(labelColor);
		add(enemyAtk);

		enemyArmor = new Label("Armor: None");
		enemyArmor.setBounds(leftSideSize + textAreaWidth - 26, 120, rightSideSize, 20);
		enemyArmor.setBackground(backgroundColor);
		enemyArmor.setForeground(labelColor);
		add(enemyArmor);

		enemyDef = new Label("DEF: " + PARENT.getEnemy().getCurrentDef() + " / " + PARENT.getEnemy().getMaxDef());
		enemyDef.setBounds(leftSideSize + textAreaWidth - 26, 140, rightSideSize, 20);
		enemyDef.setBackground(backgroundColor);
		enemyDef.setForeground(labelColor);
		add(enemyDef);

		backgroundRight = new Panel();
		backgroundRight.setBackground(new Color(5, 5, 5));
		backgroundRight.setBounds(leftSideSize + textAreaWidth - 26, 0, rightSideSize, getHeight());
		backgroundRight.setFocusable(false);
		add(backgroundRight);

		output = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		output.setBounds(leftSideSize, -2, textAreaWidth, getHeight() - 34);
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

	public void refreshChangeableLabel() {
		playerHP.setText("HP: " + PARENT.getPlayer().getCurrentHp() + " / " + PARENT.getPlayer().getMaxHp());
		playerDef.setText("DEF: " + PARENT.getPlayer().getCurrentDef() + " / " + PARENT.getPlayer().getMaxDef());
		enemyHP.setText("HP: " + PARENT.getEnemy().getCurrentHp() + " / " + PARENT.getEnemy().getMaxHp());
		enemyDef.setText("DEF: " + PARENT.getEnemy().getCurrentDef() + " / " + PARENT.getEnemy().getMaxDef());
	}

	public void refreshWeaponLabel() {
		if (PARENT.getPlayer().getWeapon() != null) {
			playerWeapon.setText("Weapon: " + PARENT.getPlayer().getWeapon().NAME);
			playerAtk.setText("ATK: " + PARENT.getPlayer().getAtk());
		}
		if (PARENT.getEnemy().getWeapon() != null) {
			enemyWeapon.setText("Weapon: " + PARENT.getEnemy().getWeapon().NAME);
			enemyAtk.setText("ATK: " + PARENT.getPlayer().getAtk());
		}
	}

	public void refreshArmorLabel() {
		if (PARENT.getPlayer().getArmor() != null) {
			playerArmor.setText("Armor: " + PARENT.getPlayer().getArmor().name());
			playerDef.setText("DEF: " + PARENT.getPlayer().getCurrentDef() + " / " + PARENT.getPlayer().getMaxDef());
		}
		if (PARENT.getEnemy().getArmor() != null) {
			enemyArmor.setText("Armor: " + PARENT.getEnemy().getArmor().name());
			enemyDef.setText("DEF: " + PARENT.getEnemy().getCurrentDef() + " / " + PARENT.getEnemy().getMaxDef());
		}
	}

}
