/**
 * Jimmy C Lu jcl2182
 * HW2
 * 
 * This class uses a Greedy Best first search to find
 * a solution to a sokoban game.
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class GreedyBestFirstSearch {
	private GameMap gameMap;

	public GreedyBestFirstSearch(GameMap m) {
		gameMap = m;
		heuristic = 1;
	}

	/*
	 * Constructor to determine which heuristic to use
	 */
	private int heuristic;

	public GreedyBestFirstSearch(GameMap m, int h) {
		gameMap = m;
		heuristic = 2;
	}

	/*
	 * Very similar to the Uniform search method except it
	 * uses getGreedyCost to always move towards the goal.
	 * 
	 * Uses a priority queue with a custom comparator.
	 * Puts the set of created nodes into the queue then
	 * checks if the next move is solved in all possible directions
	 * if not then go back to the previous state and check the next move.
	 * 
	 * Uses a list of maps to check of the move is already explored.
	 */
	private int[] dx = { 0, 0, 1, -1 };
	private int[] dy = { 1, -1, 0, 0 };
	private final int DIRSIZE = 4;
	private long nodesGenerated, nodesRevisited;
	private ArrayList<GameMap> exploredList;

	public Node search() {
		Node curNode = new Node(gameMap);

		nodesGenerated = 0;
		nodesRevisited = 0;
		PriorityQueue<Node> queue = new PriorityQueue<Node>(1000,
				new Comparator<Node>() {
					public int compare(Node one, Node two) {
						if (one.getGreedyCost(heuristic) < two
								.getGreedyCost(heuristic))
							return -1;
						else if (one.getGreedyCost(heuristic) > two
								.getGreedyCost(heuristic)) {
							return 1;
						} else
							return 0;
					}
				});
		queue.add(curNode);
		exploredList = new ArrayList<GameMap>();

		if (curNode.isSolved())
			return curNode;

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

	/*
	 * Connects all the branches to the node.
	 */
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
	 * Uses isEquals from the Node class to compare the maps
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
	 * from the found node, uses prev to trace back the steps then
	 * uses a stack to reverse the order.
	 */
	private long fringeNodes;

	public void printResult() {
		fringeNodes = 0;
		long time = System.nanoTime();
		Node n = search();

		if (n == null) {
			System.out.println("Solution not found");
			return;
		}
		time = System.nanoTime() - time;
		time /= 1000;
		String result = "Solution: ";
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
	 * Print solution is used to check that the solution is correct by
	 * printing what the map looks like for each step.
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
	 * Not used since there is no fringe nodes.  Uses a 
	 * depth first traversal to find any unvisited nodes.
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
