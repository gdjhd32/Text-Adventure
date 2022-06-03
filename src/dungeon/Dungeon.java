package dungeon;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import gui.Render;
import main.Main;

public class Dungeon {

	public enum Direction {
		North, South, West, East;
	}

	private Room[] dungeon;
	private Room currentRoom;
	private final Render RENDER;
	private final Main PARENT;

	public Dungeon(Main parent, Render render) {
		PARENT = parent;
		RENDER = render;
		dungeon = readDungeon(new File("assets/Dungeon/Dungeon000.txt"));
		currentRoom = dungeon[0];
		changeRoom(Direction.North);
	}

	private void changeRoom(Direction dir) {
		Room tmpRoom = currentRoom;
		switch (dir) {
		case North:
			if (currentRoom.NEXT_ROOMS[0] > -1)
				currentRoom = dungeon[currentRoom.NEXT_ROOMS[0]];
			break;
		case South:
			if (currentRoom.NEXT_ROOMS[1] > -1)
				currentRoom = dungeon[currentRoom.NEXT_ROOMS[1]];
			break;
		case West:
			if (currentRoom.NEXT_ROOMS[2] > -1)
				currentRoom = dungeon[currentRoom.NEXT_ROOMS[2]];
			break;
		case East:
			if (currentRoom.NEXT_ROOMS[3] > -1)
				currentRoom = dungeon[currentRoom.NEXT_ROOMS[3]];
			break;
		default:
			break;
		}
		if (tmpRoom.equals(currentRoom)) {
			RENDER.println("There is no room in this direction!");
			return;
		}
		RENDER.println("You enter another room. \n " + currentRoom.DECRIPTION);
		if (currentRoom.ENEMY != null) {
			PARENT.initFight(currentRoom.ENEMY);
		}
	}

	private Room[] readDungeon(File file) {
		Room[] dungeon = null;
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		int roomCount = 0;
		while (scanner.hasNext())
			if (!scanner.nextLine().equals("//"))
				roomCount++;

		scanner.close();

		dungeon = new Room[roomCount];

		scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		String[] lineArr;
		String line;
		while (scanner.hasNext()) {
			line = scanner.nextLine();
			if (!line.substring(0, 2).equals("//")) {
				lineArr = line.split("; ");
				dungeon[Integer.parseInt(lineArr[0])] = new Room(lineArr[1], lineArr[2],
						lineArr[3].substring(1, lineArr[3].length() - 1).split(", ")[0].equals("null") ? null
								: lineArr[3].substring(1, lineArr[3].length() - 1).split(", "),
						lineArr[4].equals("null") ? null : lineArr[4],
						lineArr[5].substring(1, lineArr[5].length() - 1).split(", ")[0].equals("null") ? -1
								: Integer.parseInt(lineArr[5].substring(1, lineArr[5].length() - 1).split(", ")[0]),
						lineArr[5].substring(1, lineArr[5].length() - 1).split(", ")[1].equals("null") ? -1
								: Integer.parseInt(lineArr[5].substring(1, lineArr[5].length() - 1).split(", ")[1]),
						lineArr[5].substring(1, lineArr[5].length() - 1).split(", ")[2].equals("null") ? -1
								: Integer.parseInt(lineArr[5].substring(1, lineArr[5].length() - 1).split(", ")[2]),
						lineArr[5].substring(1, lineArr[5].length() - 1).split(", ")[3].equals("null") ? -1
								: Integer.parseInt(lineArr[5].substring(1, lineArr[5].length() - 1).split(", ")[3]));
			}
		}

		scanner.close();
		return dungeon;
	}

	public class Room {

		public final String DECRIPTION, TYPE;
		public final String[] LOOT;
		public final String ENEMY;
		public final int[] NEXT_ROOMS = new int[4];

		public Room(String description, String type, String[] loot, String enemy, int roomNorth, int roomSouth,
				int roomWest, int roomEast) {
			DECRIPTION = description;
			TYPE = type;
			LOOT = loot;
			ENEMY = enemy;
			NEXT_ROOMS[0] = roomNorth;
			NEXT_ROOMS[1] = roomSouth;
			NEXT_ROOMS[2] = roomWest;
			NEXT_ROOMS[3] = roomEast;
		}

	}

}
