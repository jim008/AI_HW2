/**
 * Jimmy C Lu jcl2182
 * 
 * This class is used to keep track of the game maps
 * and return useful information from the map.
 * 
 * Private class Square for coordinates 
 */

import java.util.ArrayList;

public class GameMap {
	private char[][] mapData;
	private Player player;
	private ArrayList<Square> solutionList, boxList;

	/*
	 * Constructor takes in the dimensions of the map for loop purposes.
	 */
	private int width, height;

	public GameMap(int w, int h) {
		mapData = new char[w][h];
		solutionList = new ArrayList<Square>();
		boxList = new ArrayList<Square>();
		this.width = w;
		this.height = h;
	}

	/*
	 * returns the value based on coordinates
	 */
	public char get(int x, int y) {
		return mapData[x][y];
	}

	/*
	 * setter for map
	 */
	public void set(int x, int y, char d) {
		mapData[x][y] = d;
	}

	/*
	 * As data is added to the map, also sets where the player, box, solutions
	 * are located.
	 * 
	 * Does not save if the box is on goal.
	 */
	public void add(int x, int y, char data) {
		mapData[x][y] = data;
		if (data == '@' || data == '+') {
			player = new Player(data);
			player.x = x;
			player.y = y;
		}
		if (data == '.' || data == '+') {
			Square sol = new Square();
			sol.x = x;
			sol.y = y;

			solutionList.add(sol);
		}

		if (data == '$') {
			Square box = new Square();
			box.x = x;
			box.y = y;
			boxList.add(box);
		}
	}

	/*
	 * Method to check if maps are equal
	 */

	public boolean isEquals(GameMap in) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (in.get(x, y) != mapData[x][y])
					return false;
			}
		}
		return true;
	}

	/*
	 * Using the solution list created as data was added, checks to see if it's
	 * still a dot or +
	 */
	public boolean isSolved() {
		for (Square sol : solutionList) {
			if (mapData[sol.x][sol.y] == '.' || mapData[sol.x][sol.y] == '+') {
				return false;
			}
		}
		return true;
	}

	/*
	 * Used for heuristic functions, returns minimum or closest distance between
	 * a solution(without box) and the passed in coordinates.
	 */
	public int getMinDistance(int x, int y) {
		int dist = Integer.MAX_VALUE;

		for (Square comp : solutionList) {
			int temp = (comp.x * comp.x) + (comp.y * comp.y);
			temp = (int) Math.sqrt(temp);
			if (temp < dist)
				dist = temp;
		}
		return dist;
	}

	/*
	 * Used for heuristic functions, returns minimum or closest distance between
	 * a box and the passed in coordinates.
	 */
	public int getMinBoxDistance(int x, int y) {
		int dist = Integer.MAX_VALUE;

		for (Square comp : boxList) {
			int temp = (comp.x * comp.x) + (comp.y * comp.y);
			temp = (int) Math.sqrt(temp);
			if (temp < dist)
				dist = temp;
		}
		return dist;
	}

	/*
	 * Getter for the player on map
	 */
	public Player getPlayer() {
		return player;
	}

	/*
	 * prints out the map to system.
	 */
	public void printMap() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++)
				System.out.print(mapData[x][y]);
			System.out.println();
		}
	}

	/*
	 * Creates a copy of the map
	 */
	public GameMap getCopy() {
		GameMap copy = new GameMap(width, height);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				copy.add(x, y, mapData[x][y]);
			}
		}

		return copy;
	}

	/*
	 * Square is used for creating lists of solutions and boxes
	 */
	private class Square {
		public int x, y;
	}

	/*
	 * Checks if the box is dead by first seeing if it's against a wall without
	 * any possibility of moving out. Then it checks if there's a solution in
	 * the path against the wall.
	 */
	public boolean isDead() {
		boolean isDead = false;
		for (Square box : boxList) {
			isDead = isDeadVertical(box, -1) || isDeadVertical(box, 1)
					|| isDeadHorizontal(box, -1) || isDeadHorizontal(box, 1);
			if (isDead) {
				break;
			}
		}
		return isDead;
	}

	private boolean isDeadVertical(Square box, int dx) {
		for (int i = 0; i < height; i++) {
			if (mapData[box.x + dx][i] != '#') {
				return false;
			}

			if (mapData[box.x][i] == '.' || mapData[box.x][i] == '+')
				return false;
		}
		return true;
	}

	private boolean isDeadHorizontal(Square box, int dy) {
		for (int i = 0; i < width; i++) {
			if (mapData[i][box.y + dy] != '#') {
				return false;
			}

			if (mapData[i][box.y] == '.' || mapData[i][box.y] == '+')
				return false;
		}
		return true;
	}
}
