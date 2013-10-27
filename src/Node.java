/**
 * Jimmy C Lu jcl2182
 * 
 * This class generates a node for the sokoban game.
 * 
 * It also contains the methods to move the objects.
 */

public class Node {
	public boolean isVisited;
	public Node prev, up, down, left, right;

	public GameMap gameMap;
	private Player player;
	public char trans;

	/*
	 * Constructor copies the map
	 */
	public Node(GameMap m) {
		gameMap = m.getCopy();
		player = gameMap.getPlayer();
		prev = null;
		isVisited = false;
		cost = 1;
	}

	/*
	 * Getter for checking if map is solved.
	 */
	public boolean isSolved() {
		return gameMap.isSolved();
	}

	/*
	 * Based on the change in direction value, sets the next move variable.
	 */
	public void setTrans(int dx, int dy) {
		if (dx == -1) {
			trans = 'l';
		} else if (dx == 1)
			trans = 'r';
		else if (dy == -1)
			trans = 'u';
		else if (dy == 1)
			trans = 'd';
	}

	/*
	 * Move is based on the change in x or y.
	 * 
	 * Sets the player's next square value based on dx and dy
	 */
	public boolean move(int dx, int dy) {
		setTrans(dx, dy);
		try {
			player.setNext(dx, dy);
		} catch (NullPointerException e) {
			return false;
		}
		boolean isMoved = false;

		if (gameMap.isDead()) {
			return false;
		}

		// If it's just a space then move the player else
		// push an object if it's not a wall or object without anything
		// behind the object.
		if (gameMap.get(player.nextX, player.nextY) == ' ') {
			movePlayer();
			isMoved = true;
		} else if ((gameMap.get(player.nextX, player.nextY) == '*' || gameMap
				.get(player.nextX, player.nextY) == '$')
				&& gameMap.get(player.pushX, player.pushY) != '#') {
			moveBox();
			movePlayer();
			isMoved = true;
		}

		if (isMoved) {
			player.update();
		}
		return isMoved;
	}

	// ---------------------Getters for heuristics-----------------------
	private int cost;

	public int getUniformCost() {
		return cost;
	}

	public int getGreedyCost(int h) {
		if (h == 1)
			return gameMap.getMinDistance(player.x, player.y);
		else
			return gameMap.getMinBoxDistance(player.x, player.y);
	}

	public int getACost(int h) {
		if (cost == 2 && h == 2)
			cost = 10;
		else if (h == 2)
			cost = 0;
		if (h == 1)
			return gameMap.getMinDistance(player.x, player.y) - cost;
		else
			return gameMap.getMinDistance(player.x, player.y)
					+ gameMap.getMinBoxDistance(player.x, player.y) - cost;
	}

	// ----------------------Getters for heuristics--------------------------

	
	/*
	 * Moves the player
	 */
	private void movePlayer() {
		moveObject(player.nextX, player.nextY, player.x, player.y, '@', '+');
	}

	/*
	 * Moves box
	 */
	private void moveBox() {
		if (gameMap.get(player.pushX, player.pushY) == ' '
				|| gameMap.get(player.pushX, player.pushY) == '.') {
			cost = 2;
			moveObject(player.pushX, player.pushY, player.nextX, player.nextY,
					'$', '*');
		}
	}

	/*
	 * Moves the object, checks what the next square is and set it based on
	 * what is passed in.
	 */
	private void moveObject(int nX, int nY, int x, int y, char sym, char onGoal) {
		if (gameMap.get(nX, nY) == ' ') {
			gameMap.set(nX, nY, sym);
		} else if (gameMap.get(nX, nY) == '.')
			gameMap.set(nX, nY, onGoal);

		if (gameMap.get(x, y) == sym)
			gameMap.set(x, y, ' ');
		else if (gameMap.get(x, y) == onGoal)
			gameMap.set(x, y, '.');
	}

}
