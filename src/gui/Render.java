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

import fighting.Fighter;
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

	private final Fighter PLAYER;
	private Fighter enemy;
	private boolean fighting;

	public Render(Main parent, String[] validKeys, Fighter player, Fighter emptyEnemy) {
		super("Text-Adventure");
		VALID_KEYS = validKeys;
		isPressed = new boolean[VALID_KEYS.length];
		PARENT = parent;
		PLAYER = player;
		enemy = emptyEnemy;
		fighting = false;
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

		playerName = new Label(PLAYER.NAME);
		playerName.setBounds(0, 0, leftSideSize + 2, 20);
		playerName.setBackground(backgroundColor);
		playerName.setForeground(labelColor);
		playerName.setAlignment(Label.CENTER);
		add(playerName);

		playerHP = new Label("HP: " + PLAYER.getCurrentHp() + " / " + PLAYER.getMaxHp());
		playerHP.setBounds(0, 20, leftSideSize + 2, 20);
		playerHP.setBackground(backgroundColor);
		playerHP.setForeground(labelColor);
		add(playerHP);

		playerStr = new Label("STR: " + PLAYER.getStr());
		playerStr.setBounds(0, 40, leftSideSize + 2, 20);
		playerStr.setBackground(backgroundColor);
		playerStr.setForeground(labelColor);
		add(playerStr);

		playerAg = new Label("AG: " + PLAYER.getAg());
		playerAg.setBounds(0, 60, leftSideSize + 2, 20);
		playerAg.setBackground(backgroundColor);
		playerAg.setForeground(labelColor);
		add(playerAg);

		playerWeapon = new Label("Weapon: None");
		playerWeapon.setBounds(0, 80, leftSideSize + 2, 20);
		playerWeapon.setBackground(backgroundColor);
		playerWeapon.setForeground(labelColor);
		add(playerWeapon);

		playerAtk = new Label("ATK: " + PLAYER.getWeapon().atk());
		playerAtk.setBounds(0, 100, leftSideSize + 2, 20);
		playerAtk.setBackground(backgroundColor);
		playerAtk.setForeground(labelColor);
		add(playerAtk);

		playerArmor = new Label("Armor: None");
		playerArmor.setBounds(0, 120, leftSideSize + 2, 20);
		playerArmor.setBackground(backgroundColor);
		playerArmor.setForeground(labelColor);
		add(playerArmor);

		playerDef = new Label("DEF: " + PLAYER.getArmor().getCurrentDef() + " / " + PLAYER.getArmor().getMaxDef());
		playerDef.setBounds(0, 140, leftSideSize + 2, 20);
		playerDef.setBackground(backgroundColor);
		playerDef.setForeground(labelColor);
		add(playerDef);

		backgroundLeft = new Panel();
		backgroundLeft.setBackground(new Color(5, 5, 5));
		backgroundLeft.setBounds(0, 0, leftSideSize + 2, getHeight());
		backgroundLeft.setFocusable(false);
		add(backgroundLeft);

		enemyName = new Label(enemy.NAME);
		enemyName.setBounds(leftSideSize + textAreaWidth - 26, 0, rightSideSize, 20);
		enemyName.setBackground(backgroundColor);
		enemyName.setForeground(labelColor);
		enemyName.setAlignment(Label.CENTER);
		enemyName.setVisible(false);
		add(enemyName);

		enemyHP = new Label("HP: " + enemy.getCurrentHp() + " / " + enemy.getMaxHp());
		enemyHP.setBounds(leftSideSize + textAreaWidth - 26, 20, rightSideSize, 20);
		enemyHP.setBackground(backgroundColor);
		enemyHP.setForeground(labelColor);
		enemyHP.setVisible(false);
		add(enemyHP);

		enemyStr = new Label("STR: " + enemy.getStr());
		enemyStr.setBounds(leftSideSize + textAreaWidth - 26, 40, rightSideSize, 20);
		enemyStr.setBackground(backgroundColor);
		enemyStr.setForeground(labelColor);
		enemyStr.setVisible(false);
		add(enemyStr);

		enemyAg = new Label("AG: " + enemy.getAg());
		enemyAg.setBounds(leftSideSize + textAreaWidth - 26, 60, rightSideSize, 20);
		enemyAg.setBackground(backgroundColor);
		enemyAg.setForeground(labelColor);
		enemyAg.setVisible(false);
		add(enemyAg);

		enemyWeapon = new Label("Weapon: None");
		enemyWeapon.setBounds(leftSideSize + textAreaWidth - 26, 80, rightSideSize, 20);
		enemyWeapon.setBackground(backgroundColor);
		enemyWeapon.setForeground(labelColor);
		enemyWeapon.setVisible(false);
		add(enemyWeapon);

		enemyAtk = new Label("ATK: " + enemy.getWeapon().atk());
		enemyAtk.setBounds(leftSideSize + textAreaWidth - 26, 100, rightSideSize, 20);
		enemyAtk.setBackground(backgroundColor);
		enemyAtk.setForeground(labelColor);
		enemyAtk.setVisible(false);
		add(enemyAtk);

		enemyArmor = new Label("Armor: None");
		enemyArmor.setBounds(leftSideSize + textAreaWidth - 26, 120, rightSideSize, 20);
		enemyArmor.setBackground(backgroundColor);
		enemyArmor.setForeground(labelColor);
		enemyArmor.setVisible(false);
		add(enemyArmor);

		enemyDef = new Label("DEF: " + enemy.getArmor().getCurrentDef() + " / " + enemy.getArmor().getMaxDef());
		enemyDef.setBounds(leftSideSize + textAreaWidth - 26, 140, rightSideSize, 20);
		enemyDef.setBackground(backgroundColor);
		enemyDef.setForeground(labelColor);
		enemyDef.setVisible(false);
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
		output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		output.setEditable(false);
		output.setFocusable(false);
		add(output);
	}

	public void println(String arg) {
		if (output.getText().length() == 0) {
			output.append(arg);
			return;
		}
		output.append("\n" + arg);
	}

	public void changeLast(String arg) {
		if (output.getText().length() == 0) {
			output.append(arg);
			return;
		}
		int lastSectionLength = output.getText().split("\n")[output.getText().split("\n").length - 1].length();
		if (output.getText().length() == lastSectionLength) {
			output.setText(arg);
			return;
		}
		output.replaceRange(arg, output.getText().length() - lastSectionLength, output.getText().length());
	}

	public void refreshEnemyName() {
		enemyName.setText(enemy.NAME);
	}

	public void refreshStatLabel() {
		playerHP.setText("HP: " + PLAYER.getCurrentHp() + " / " + PLAYER.getMaxHp());
		playerStr.setText("STR: " + PLAYER.getStr());
		playerAg.setText("AG: " + PLAYER.getAg());
		enemyHP.setText("HP: " + enemy.getCurrentHp() + " / " + enemy.getMaxHp());
		enemyStr.setText("STR: " + enemy.getStr());
		enemyAg.setText("AG: " + enemy.getAg());
	}

	public void refreshWeaponLabel() {
		if (PLAYER.getWeapon() != null) {
			playerWeapon.setText("Weapon: " + PLAYER.getWeapon().name());
			playerAtk.setText("ATK: " + PLAYER.getWeapon().atk());
		}
		if (enemy.getWeapon() != null) {
			enemyWeapon.setText("Weapon: " + enemy.getWeapon().name());
			enemyAtk.setText("ATK: " + PLAYER.getWeapon().atk());
		}
	}

	public void refreshArmorLabel() {
		if (PLAYER.getArmor() != null) {
			playerArmor.setText("Armor: " + PLAYER.getArmor().getName());
			playerDef.setText("DEF: " + PLAYER.getArmor().getCurrentDef() + " / " + PLAYER.getArmor().getMaxDef());
		}
		if (enemy.getArmor() != null) {
			enemyArmor.setText("Armor: " + enemy.getArmor().getName());
			enemyDef.setText("DEF: " + enemy.getArmor().getCurrentDef() + " / " + enemy.getArmor().getMaxDef());
		}
	}

	public void startFight(Fighter enemy) {
		fighting = true;
		this.enemy = enemy;
		refreshEnemyName();
		refreshStatLabel();
		refreshWeaponLabel();
		refreshArmorLabel();
		enemyName.setVisible(true);
		enemyHP.setVisible(true);
		enemyStr.setVisible(true);
		enemyAg.setVisible(true);
		enemyWeapon.setVisible(true);
		enemyAtk.setVisible(true);
		enemyArmor.setVisible(true);
		enemyDef.setVisible(true);
	}

	public void endFight() {
		fighting = false;
		enemyName.setVisible(false);
		enemyHP.setVisible(false);
		enemyStr.setVisible(false);
		enemyAg.setVisible(false);
		enemyWeapon.setVisible(false);
		enemyAtk.setVisible(false);
		enemyArmor.setVisible(false);
		enemyDef.setVisible(false);
	}
	
	public boolean isFighting() {
		return fighting;
	}

}
