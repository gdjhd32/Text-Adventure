package fighting;

import fighting.Fighter.Direction;
import fighting.Fighter.Position;
import gui.Render;
import main.Main;

public class CombatSystem {

	private final Fighter PLAYER;
	private final Fighter ENEMY;
	private final Render RENDER;

	public CombatSystem(Render render, Fighter player, Fighter enemy) {
		PLAYER = player;
		ENEMY = enemy;
		RENDER = render;
	}
	
	

	public void movePlayer(Direction dir) {
		if (dir == Direction.Up) {
			switch (PLAYER.getPosition()) {
			case LeftCenter:
				PLAYER.setPosition(Position.LeftUp);
				break;
			case MiddleCenter:
				PLAYER.setPosition(Position.MiddleUp);
				break;
			case RightCenter:
				PLAYER.setPosition(Position.RightUp);
				break;
			case LeftDown:
				PLAYER.setPosition(Position.LeftCenter);
				break;
			case MiddleDown:
				PLAYER.setPosition(Position.MiddleCenter);
				break;
			case RightDown:
				PLAYER.setPosition(Position.RightCenter);
				break;
			default:
				RENDER.println("You did not move!");
				return;
			}
			RENDER.println("You moved to " + PLAYER.getPosition().name() + "!");
			return;
		}
		if (dir == Direction.Down) {
			switch (PLAYER.getPosition()) {
			case LeftUp:
				PLAYER.setPosition(Position.LeftCenter);
				break;
			case MiddleUp:
				PLAYER.setPosition(Position.MiddleCenter);
				break;
			case RightUp:
				PLAYER.setPosition(Position.RightCenter);
				break;
			case LeftCenter:
				PLAYER.setPosition(Position.LeftDown);
				break;
			case MiddleCenter:
				PLAYER.setPosition(Position.MiddleDown);
				break;
			case RightCenter:
				PLAYER.setPosition(Position.RightDown);
				break;
			default:
				RENDER.println("You did not move!");
				return;
			}
			RENDER.println("You moved to " + PLAYER.getPosition().name() + "!");
			return;
		}
		if (dir == Direction.Left) {
			switch (PLAYER.getPosition()) {
			case MiddleUp:
				PLAYER.setPosition(Position.LeftUp);
				break;
			case RightUp:
				PLAYER.setPosition(Position.MiddleUp);
				break;
			case MiddleCenter:
				PLAYER.setPosition(Position.LeftCenter);
				break;
			case RightCenter:
				PLAYER.setPosition(Position.MiddleCenter);
				break;
			case MiddleDown:
				PLAYER.setPosition(Position.LeftDown);
				break;
			case RightDown:
				PLAYER.setPosition(Position.MiddleDown);
				break;
			default:
				RENDER.println("You did not move!");
				return;
			}
			RENDER.println("You moved to " + PLAYER.getPosition().name() + "!");
			return;
		}
		if (dir == Direction.Right) {
			switch (PLAYER.getPosition()) {
			case LeftUp:
				PLAYER.setPosition(Position.MiddleUp);
				break;
			case MiddleUp:
				PLAYER.setPosition(Position.RightUp);
				break;
			case LeftCenter:
				PLAYER.setPosition(Position.MiddleCenter);
				break;
			case MiddleCenter:
				PLAYER.setPosition(Position.RightCenter);
				break;
			case LeftDown:
				PLAYER.setPosition(Position.MiddleDown);
				break;
			case MiddleDown:
				PLAYER.setPosition(Position.RightDown);
				break;
			default:
				RENDER.println("You did not move!");
				return;
			}
			RENDER.println("You moved to " + PLAYER.getPosition().name() + "!");
			return;
		}
	}
	
	

}
