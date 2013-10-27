/**
 * Jimmy C Lu jcl2182
 * HW 2
 * Very similar to the greedy best first search except it will also consider 
 * where the player is in respect to the box.
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class AStarSearch {
	private GameMap gameMap;

	public AStarSearch(GameMap m) {
		gameMap = m;
		heuristic = 1;
	}

	//This constructor is for heuristic options
	private int heuristic;

	public AStarSearch(GameMap m, int h) {
		gameMap = m;
		heuristic = h;
	}

	/*
	 * Similar to uniform cost search except the value is determined 
	 * by cost + heuristic distance function getAcost in Node class.
	 * 
	 * Explored list matches the map and if maps are exactly the same
	 * then it's a node that already has been visited.
	 * 
	 * Directional changes dx and dy are based on two arrays to determine
	 * where to add to subtract.
	 */
	private int[] dx = { 0, 0, 1, -1 };
	private int[] dy = { 1, -1, 0, 0 };
	private final int DIRSIZE = 4;
	private long nodesGenerated, nodesRevisited; //keeps track
	private ArrayList<GameMap> exploredList;

	public Node search() {
		Node curNode = new Node(gameMap);

		nodesGenerated = 0;
		nodesRevisited = 0;
		
		//Custom comparator created for the priority queue
		PriorityQueue<Node> queue = new PriorityQueue<Node>(1000,
				new Comparator<Node>() {
					public int compare(Node one, Node two) {
						if (one.getACost(heuristic) < two.getACost(heuristic))
							return 1;
						else if (one.getACost(heuristic) > two
								.getACost(heuristic)) {
							return -1;
						} else
							return 0;
					}
				});
		queue.add(curNode);
		exploredList = new ArrayList<GameMap>();

		if (curNode.isSolved())
			return curNode;

		//Explores all possible values.  Set branches connects everything
		//so that it can find fringe nodes later.  Prev is to collect
		//the answer.
		while (!queue.isEmpty()) {
			curNode = queue.poll();
			exploredList.add(curNode.gameMap);
			curNode.isVisited = true;
			for (int i = 0; i < DIRSIZE; i++) {
				Node n = new Node(curNode.gameMap);
				nodesGenerated++;
				if (n.move(dx[i], dy[i]) && !isExplored(n.gameMap)) {
					n.prev = curNode;
					setBranches(n, curNode, dx[i], dy[i]);
					if (n.isSolved())
						return n;
					queue.add(n);
				}
			}
		}
		return null;
	}

	//x and y is the direction variable to set the nodes.
	private void setBranches(Node n, Node curNode, int x, int y) {
		if (x == 1) {
			n.right = curNode;
		} else if (x == -1)
			n.left = curNode;
		else if (y == 1)
			n.down = curNode;
		else if (y == -1)
			n.up = curNode;
	}

	/*
	 * Compares the maps and if they are a match then
	 * it's definitely a node that's been explored.
	 * 
	 * Uses isEquals from Node class.
	 */
	private boolean isExplored(GameMap m) {
		if (exploredList == null)
			return false;

		for (GameMap compare : exploredList) {
			if (m.isEquals(compare)) {
				nodesRevisited++;
				return true;
			}
		}

		return false;
	}

	/*
	 * Main function that is called to run the algorithm and 
	 * prints out the result.
	 * 
	 * Keeps track of time with system nano time and converts 
	 * it to milliseconds.
	 * 
	 * Fringe nodes is not used by method is still there to
	 * to count any unvisited nodes.  Since this nodes doesn't
	 * visit explored nodes, there are none.
	 */
	private long fringeNodes;

	public void printResult() {
		fringeNodes = 0;
		long time = System.nanoTime();
		Node n = search();
		String result = "";
		if (n == null) {
			result += "Solution not found \n";
			return;
		}
		time = System.nanoTime() - time;
		time /= 1000;
		//Stack used to reverse the answer to correct order
		result += "Solution: \n";
		Stack<Character> s = new Stack<Character>();
		s.push(n.trans);
		while ((n = n.prev) != null) {
			s.push(n.trans);
		}

		String sol = "";
		while (!s.isEmpty()) {
			String out = s.pop() + " ";
			sol += out;
			result += out;
		}

		result += "\n" + "Nodes Generated: " + nodesGenerated + "\n";
		result += "Nodes revisited: " + nodesRevisited + "\n";
		result += "Explored nodes list: " + exploredList.size() + "\n";
		result += "Time: " + time + " milliseconds \n";
		System.out.println(result);
	}

	/*
	 * This function was to test to see if the solution is correct by showing
	 * the map for each step.
	 */
	private void printSolutions(String s) {
		s = s.trim();
		Scanner scan = new Scanner(s);
		Node n = new Node(gameMap);
		while (scan.hasNext()) {
			switch (scan.next().charAt(0)) {
			case 'r':
				n.move(1, 0);
				n.gameMap.printMap();
				break;
			case 'l':
				n.move(-1, 0);
				n.gameMap.printMap();
				break;
			case 'u':
				n.move(0, -1);
				n.gameMap.printMap();
				break;
			case 'd':
				n.move(0, 1);
				n.gameMap.printMap();
				break;
			}
		}
		scan.close();
	}

	/*
	 * Does a depth traversal to find fringe nodes.
	 */
	private void setFringeNodes(Node n) {
		if (!n.isVisited)
			fringeNodes++;
		if (n.up != null)
			setFringeNodes(n.up);
		if (n.left != null)
			setFringeNodes(n.left);
		if (n.down != null)
			setFringeNodes(n.down);
		if (n.right != null)
			setFringeNodes(n.right);
	}
}
