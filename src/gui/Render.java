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

import fighting.Armor;
import fighting.Fighter;
import fighting.Inventory;
import fighting.Weapon;
import gui.Render.InventoryPanel.ItemType;
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
	private Label itemName, itemStat, itemType;
	private InventoryPanel inventoryPanel;

	private final Main PARENT;

	private final Fighter PLAYER;
	private Fighter enemy;
	private boolean fighting;

	private final Inventory INVENTORY;
	private boolean inventoryOpen;

	private final Font NORMAL_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);

	public Render(Main parent, String[] validKeys, Fighter player, Fighter emptyEnemy, Inventory inventory) {
		super("Text-Adventure");
		VALID_KEYS = validKeys;
		isPressed = new boolean[VALID_KEYS.length];
		PARENT = parent;
		PLAYER = player;
		enemy = emptyEnemy;
		fighting = false;
		inventoryOpen = false;
		INVENTORY = inventory;
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
		playerName.setFont(NORMAL_FONT);
		playerName.setBackground(backgroundColor);
		playerName.setForeground(labelColor);
		playerName.setAlignment(Label.CENTER);
		add(playerName);

		playerHP = new Label("HP: " + PLAYER.getCurrentHp() + " / " + PLAYER.getMaxHp());
		playerHP.setBounds(0, 20, leftSideSize + 2, 20);
		playerHP.setFont(NORMAL_FONT);
		playerHP.setBackground(backgroundColor);
		playerHP.setForeground(labelColor);
		add(playerHP);

		playerStr = new Label("STR: " + PLAYER.getStr());
		playerStr.setBounds(0, 40, leftSideSize + 2, 20);
		playerStr.setFont(NORMAL_FONT);
		playerStr.setBackground(backgroundColor);
		playerStr.setForeground(labelColor);
		add(playerStr);

		playerAg = new Label("AG: " + PLAYER.getAg());
		playerAg.setBounds(0, 60, leftSideSize + 2, 20);
		playerAg.setFont(NORMAL_FONT);
		playerAg.setBackground(backgroundColor);
		playerAg.setForeground(labelColor);
		add(playerAg);

		playerWeapon = new Label("Weapon: None");
		playerWeapon.setBounds(0, 80, leftSideSize + 2, 20);
		playerWeapon.setFont(NORMAL_FONT);
		playerWeapon.setBackground(backgroundColor);
		playerWeapon.setForeground(labelColor);
		add(playerWeapon);

		playerAtk = new Label("ATK: " + PLAYER.getWeapon().atk());
		playerAtk.setBounds(0, 100, leftSideSize + 2, 20);
		playerAtk.setFont(NORMAL_FONT);
		playerAtk.setBackground(backgroundColor);
		playerAtk.setForeground(labelColor);
		add(playerAtk);

		playerArmor = new Label("Armor: None");
		playerArmor.setBounds(0, 120, leftSideSize + 2, 20);
		playerArmor.setFont(NORMAL_FONT);
		playerArmor.setBackground(backgroundColor);
		playerArmor.setForeground(labelColor);
		add(playerArmor);

		playerDef = new Label("DEF: " + PLAYER.getArmor().getCurrentDef() + " / " + PLAYER.getArmor().getMaxDef());
		playerDef.setBounds(0, 140, leftSideSize + 2, 20);
		playerDef.setFont(NORMAL_FONT);
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
		enemyName.setFont(NORMAL_FONT);
		enemyName.setBackground(backgroundColor);
		enemyName.setForeground(labelColor);
		enemyName.setAlignment(Label.CENTER);
		enemyName.setVisible(false);
		add(enemyName);

		enemyHP = new Label("HP: " + enemy.getCurrentHp() + " / " + enemy.getMaxHp());
		enemyHP.setBounds(leftSideSize + textAreaWidth - 26, 20, rightSideSize, 20);
		enemyHP.setFont(NORMAL_FONT);
		enemyHP.setBackground(backgroundColor);
		enemyHP.setForeground(labelColor);
		enemyHP.setVisible(false);
		add(enemyHP);

		enemyStr = new Label("STR: " + enemy.getStr());
		enemyStr.setBounds(leftSideSize + textAreaWidth - 26, 40, rightSideSize, 20);
		enemyStr.setFont(NORMAL_FONT);
		enemyStr.setBackground(backgroundColor);
		enemyStr.setForeground(labelColor);
		enemyStr.setVisible(false);
		add(enemyStr);

		enemyAg = new Label("AG: " + enemy.getAg());
		enemyAg.setBounds(leftSideSize + textAreaWidth - 26, 60, rightSideSize, 20);
		enemyAg.setFont(NORMAL_FONT);
		enemyAg.setBackground(backgroundColor);
		enemyAg.setForeground(labelColor);
		enemyAg.setVisible(false);
		add(enemyAg);

		enemyWeapon = new Label("Weapon: None");
		enemyWeapon.setBounds(leftSideSize + textAreaWidth - 26, 80, rightSideSize, 20);
		enemyWeapon.setFont(NORMAL_FONT);
		enemyWeapon.setBackground(backgroundColor);
		enemyWeapon.setForeground(labelColor);
		enemyWeapon.setVisible(false);
		add(enemyWeapon);

		enemyAtk = new Label("ATK: " + enemy.getWeapon().atk());
		enemyAtk.setBounds(leftSideSize + textAreaWidth - 26, 100, rightSideSize, 20);
		enemyAtk.setFont(NORMAL_FONT);
		enemyAtk.setBackground(backgroundColor);
		enemyAtk.setForeground(labelColor);
		enemyAtk.setVisible(false);
		add(enemyAtk);

		enemyArmor = new Label("Armor: None");
		enemyArmor.setBounds(leftSideSize + textAreaWidth - 26, 120, rightSideSize, 20);
		enemyArmor.setFont(NORMAL_FONT);
		enemyArmor.setBackground(backgroundColor);
		enemyArmor.setForeground(labelColor);
		enemyArmor.setVisible(false);
		add(enemyArmor);

		enemyDef = new Label("DEF: " + enemy.getArmor().getCurrentDef() + " / " + enemy.getArmor().getMaxDef());
		enemyDef.setBounds(leftSideSize + textAreaWidth - 26, 140, rightSideSize, 20);
		enemyDef.setFont(NORMAL_FONT);
		enemyDef.setBackground(backgroundColor);
		enemyDef.setForeground(labelColor);
		enemyDef.setVisible(false);
		add(enemyDef);

		itemName = new Label("");
		itemName.setBounds(leftSideSize + textAreaWidth - 26, 0, rightSideSize, 20);
		itemName.setFont(NORMAL_FONT);
		itemName.setBackground(backgroundColor);
		itemName.setForeground(labelColor);
		itemName.setVisible(false);
		add(itemName);

		itemStat = new Label("");
		itemStat.setBounds(leftSideSize + textAreaWidth - 26, 20, rightSideSize, 20);
		itemStat.setFont(NORMAL_FONT);
		itemStat.setBackground(backgroundColor);
		itemStat.setForeground(labelColor);
		itemStat.setVisible(false);
		add(itemStat);

		itemType = new Label("");
		itemType.setBounds(leftSideSize + textAreaWidth - 26, 40, rightSideSize, 20);
		itemType.setFont(NORMAL_FONT);
		itemType.setBackground(backgroundColor);
		itemType.setForeground(labelColor);
		itemType.setVisible(false);
		add(itemType);

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

		inventoryPanel = new InventoryPanel(this, PARENT);
		inventoryPanel.setBounds(leftSideSize, 0, 238, getHeight());
		inventoryPanel.setBackground(new Color(0, 0, 0));
		inventoryPanel.addSection("Weapons", ItemType.Weapon);
		inventoryPanel.addSection("Armor", ItemType.Armor);
		inventoryPanel.setFocusable(false);
		inventoryPanel.setVisible(false);
		add(inventoryPanel);
	}

	public void println(String arg) {
		if (output.getText().length() == 0) {
			output.append("-> " + arg);
			return;
		}
		output.append("\n-> " + arg);
	}

	public void changeLast(String arg) {
		if (output.getText().length() == 0) {
			output.append("-> " + arg);
			return;
		}
		int lastSectionLength = output.getText().split("\n")[output.getText().split("\n").length - 1].length();
		if (output.getText().length() == lastSectionLength) {
			output.setText("-> " + arg);
			return;
		}
		output.replaceRange("-> " + arg, output.getText().length() - lastSectionLength, output.getText().length());
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

	public void openInventory() {
		inventoryOpen = true;
		output.setVisible(false);
		inventoryPanel.resetInventory();
		inventoryPanel.setVisible(true);
		itemName.setVisible(true);
		itemStat.setVisible(true);
		itemType.setVisible(true);
	}

	public void closeInventory() {
		inventoryOpen = false;
		output.setVisible(true);
		inventoryPanel.setVisible(false);
		itemName.setVisible(false);
		itemStat.setVisible(false);
		itemType.setVisible(false);
	}

	public boolean isInventoryOpen() {
		return inventoryOpen;
	}

	public void inventoryControll(String key) {
		inventoryPanel.inventoryControll(key);
	}

	public class InventoryPanel extends Panel {

		private SubPanelList subPanelList;
		private Label section;
		private final Render PARENT;
		private final Main MAIN;

		public enum ItemType {
			Weapon, Armor
		}

		public InventoryPanel(Render parent, Main main) {
			super();
			PARENT = parent;
			MAIN = main;
			subPanelList = new SubPanelList(this, PARENT);
			initComponents();
		}

		private void initComponents() {
			section = new Label("");
			section.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
			section.setBackground(new Color(0, 0, 0));
			section.setForeground(new Color(0, 255, 0));
			PARENT.add(section);
		}

		@Override
		public void setBounds(int x, int y, int width, int height) {
			super.setBounds(x, y, width, height);
			section.setBounds(x, y, getWidth(), 30);
		}

		public void addSection(String name, ItemType type) {
			subPanelList.add(name, type);
		}

		public void resetInventory() {
			subPanelList.setVisibleSubPanel(subPanelList.get(0));
			subPanelList.getVisibleSubPanel().showComponents();
			while (subPanelList.getVisibleSubPanel().next != null) {
				subPanelList.setVisibleSubPanel(subPanelList.getVisibleSubPanel().next);
				subPanelList.getVisibleSubPanel().hideComponents();
			}
			subPanelList.setVisibleSubPanel(subPanelList.get(0));
			if (subPanelList.getVisibleSubPanel().NAME.equals("Weapons")) {
				Weapon tmp = MAIN.getWeapon(subPanelList.getVisibleSubPanel().getCurrentItem());
				itemName.setText("Name: " + tmp.name());
				itemStat.setText("Atk: " + tmp.atk());
				itemType.setText("Type: " + tmp.type());
			}
			if (subPanelList.getVisibleSubPanel().NAME.equals("Armor")) {
				Armor tmp = MAIN.getArmor(subPanelList.getVisibleSubPanel().getCurrentItem());
				itemName.setText("Name: " + tmp.getName());
				itemStat.setText("Def: " + tmp.getMaxDef());
				itemType.setText("Material: " + tmp.getType());
			}
			section.setText(subPanelList.get(0).NAME);
			if (subPanelList.get(1) != null)
				section.setText("   " + section.getText() + " ->");
		}

		public void inventoryControll(String key) {
			if (key.equals("a")) {
				if (subPanelList.getVisibleSubPanel().getPrevious() != null) {
					changeSection(subPanelList.getVisibleSubPanel().ID - 1);
				}
				return;
			}
			if (key.equals("d")) {
				if (subPanelList.getVisibleSubPanel().getNext() != null) {
					changeSection(subPanelList.getVisibleSubPanel().ID + 1);
				}
				return;
			}
			if (key.equals("w")) {
				subPanelList.getVisibleSubPanel().moveUp();
				return;
			}
			if (key.equals("s")) {
				subPanelList.getVisibleSubPanel().moveDown();
				return;
			}
			if (key.equals("\n")) {
				if (subPanelList.getVisibleSubPanel().NAME.equals("Weapons")) {
					PLAYER.setWeapon(MAIN.getWeapon(subPanelList.getVisibleSubPanel().getCurrentItem()));
					refreshWeaponLabel();
				}
				if (subPanelList.getVisibleSubPanel().NAME.equals("Armor")) {
					PLAYER.setArmor(MAIN.getArmor(subPanelList.getVisibleSubPanel().getCurrentItem()));
					refreshArmorLabel();
				}
				return;
			}
		}

		public void changeSection(int id) {
			subPanelList.getVisibleSubPanel().hideComponents();
			subPanelList.setVisibleSubPanel(subPanelList.get(id));
			if (subPanelList.getVisibleSubPanel().NAME.equals("Weapons")) {
				Weapon tmp = MAIN.getWeapon(subPanelList.getVisibleSubPanel().getCurrentItem());
				itemName.setText("Name: " + tmp.name());
				itemStat.setText("Atk: " + tmp.atk());
				itemType.setText("Type: " + tmp.type());
			}
			if (subPanelList.getVisibleSubPanel().NAME.equals("Armor")) {
				Armor tmp = MAIN.getArmor(subPanelList.getVisibleSubPanel().getCurrentItem());
				itemName.setText("Name: " + tmp.getName());
				itemStat.setText("Def: " + tmp.getMaxDef());
				itemType.setText("Material: " + tmp.getType());
			}
			subPanelList.getVisibleSubPanel().showComponents();
			section.setText(subPanelList.getVisibleSubPanel().NAME);
			if (subPanelList.getVisibleSubPanel().getNext() != null)
				section.setText(section.getText() + " ->");
			if (subPanelList.getVisibleSubPanel().getPrevious() != null) {
				section.setText(" <- " + section.getText());
				return;
			}
			section.setText("   " + section.getText());
		}

		private class SubPanelList {

			private SubPanelElement first, current, visibleSubPanel;
			private final Panel PARENT;
			private final Render RENDER;

			public SubPanelList(Panel parent, Render render) {
				first = null;
				visibleSubPanel = null;
				PARENT = parent;
				RENDER = render;
			}

			public void add(String name, ItemType type) {
				if (first == null) {
					first = new SubPanelElement(name, type, 0, PARENT, RENDER);
					return;
				}
				current = first;
				while (current.getNext() != null)
					current = current.getNext();
				current.setNext(new SubPanelElement(name, type, current.ID + 1, PARENT, RENDER));
				current.getNext().setPrevious(current);
			}

			public SubPanelElement get(int i) {
				current = first;
				try {
					for (int j = 0; j < i; j++)
						current = current.getNext();
					return current;
				} catch (Exception e) {
					return null;
				}
			}

			public void setVisibleSubPanel(SubPanelElement spe) {
				visibleSubPanel = spe;
			}

			public SubPanelElement getVisibleSubPanel() {
				return visibleSubPanel;
			}

			private class SubPanelElement {

				public final String NAME;
				public final ItemType TYPE;
				public final int ID;
				private SubPanelElement next, previous;
				private Label[] labels;
				private int currentLabel;
				private final Panel PARENT;
				private final Render RENDER;
				private final Color ACTIVE = new Color(0, 255, 255), UNACTIVE = new Color(0, 255, 0);

				public SubPanelElement(String name, ItemType type, int id, Panel parent, Render render) {
					NAME = name;
					TYPE = type;
					ID = id;
					next = null;
					previous = null;
					PARENT = parent;
					RENDER = render;
					initComponents();
				}

				private void initComponents() {
					currentLabel = 0;
					int length = 0;
					if (TYPE.equals(ItemType.Weapon)) {
						while (INVENTORY.getWeapon(length) != null) {
							length++;
						}
						labels = new Label[length];
						for (int i = 0; i < labels.length; i++) {
							labels[i] = new Label(INVENTORY.getWeapon(i).name());
							labels[i].setBounds(PARENT.getX() + 5, PARENT.getY() + 30 + i * (12 + 5), PARENT.getWidth(),
									12);
							labels[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
							labels[i].setBackground(new Color(0, 0, 0));
							labels[i].setForeground(UNACTIVE);
							labels[i].setFocusable(false);
							labels[i].setVisible(false);
							RENDER.add(labels[i]);
						}
						return;
					}
					if (TYPE.equals(ItemType.Armor)) {
						while (INVENTORY.getArmor(length) != null) {
							length++;
						}
						labels = new Label[length];
						for (int i = 0; i < labels.length; i++) {
							labels[i] = new Label(INVENTORY.getArmor(i).getName());
							labels[i].setBounds(PARENT.getX() + 5, PARENT.getY() + 30 + i * (12 + 5), PARENT.getWidth(),
									12);
							labels[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
							labels[i].setBackground(new Color(0, 0, 0));
							labels[i].setForeground(UNACTIVE);
							labels[i].setFocusable(false);
							labels[i].setVisible(false);
							RENDER.add(labels[i]);
						}
						return;
					}
				}

				public void hideComponents() {
					for (int i = 0; i < labels.length; i++)
						labels[i].setVisible(false);
				}

				public void showComponents() {
					currentLabel = 0;
					for (int i = 0; i < labels.length; i++) {
						labels[i].setForeground(UNACTIVE);
						labels[i].setVisible(true);
					}
					labels[currentLabel].setForeground(ACTIVE);
				}

				public void moveUp() {
					if (currentLabel == 0)
						return;
					labels[currentLabel].setForeground(UNACTIVE);
					currentLabel--;
					labels[currentLabel].setForeground(ACTIVE);
					if (labels.length > 30)
						if (currentLabel > 15 && currentLabel < labels.length - 15)
							for (int i = 0; i < labels.length; i++)
								labels[i].setBounds(labels[i].getBounds().x, labels[i].getBounds().y + 17,
										labels[i].getBounds().width, 12);
					if (NAME.equals("Weapons")) {
						Weapon tmp = MAIN.getWeapon(getCurrentItem());
						itemName.setText("Name: " + tmp.name());
						itemStat.setText("Atk: " + tmp.atk());
						itemType.setText("Type: " + tmp.type());
					}
					if (NAME.equals("Armor")) {
						Armor tmp = MAIN.getArmor(getCurrentItem());
						itemName.setText("Name: " + tmp.getName());
						itemStat.setText("Def: " + tmp.getMaxDef());
						itemType.setText("Material: " + tmp.getType());
					}
				}

				public void moveDown() {
					if (currentLabel == labels.length - 1)
						return;
					labels[currentLabel].setForeground(UNACTIVE);
					currentLabel++;
					labels[currentLabel].setForeground(ACTIVE);
					if (labels.length > 30)
						if (currentLabel > 15 && currentLabel < labels.length - 15)
							for (int i = 0; i < labels.length; i++)
								labels[i].setBounds(labels[i].getBounds().x, labels[i].getBounds().y - 17,
										labels[i].getBounds().width, 12);
					if (NAME.equals("Weapons")) {
						Weapon tmp = MAIN.getWeapon(getCurrentItem());
						itemName.setText("Name: " + tmp.name());
						itemStat.setText("Atk: " + tmp.atk());
						itemType.setText("Type: " + tmp.type());
					}
					if (NAME.equals("Armor")) {
						Armor tmp = MAIN.getArmor(getCurrentItem());
						itemName.setText("Name: " + tmp.getName());
						itemStat.setText("Def: " + tmp.getMaxDef());
						itemType.setText("Material: " + tmp.getType());
					}
				}

				public String getCurrentItem() {
					return labels[currentLabel].getText();
				}

				public void setNext(SubPanelElement spe) {
					next = spe;
				}

				public SubPanelElement getNext() {
					return next;
				}

				public void setPrevious(SubPanelElement spe) {
					previous = spe;
				}

				public SubPanelElement getPrevious() {
					return previous;
				}

			}

		}
	}
}
