/**
 * Jimmy C Lu jcl2182
 * hw2
 * 
 * This class does a uniform cost search for the sokoban game.
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

public class UniformedCostSearch {
	private GameMap gameMap;

	public UniformedCostSearch(GameMap m) {
		gameMap = m;
	}

	/*
	 * dx and dy are arrays to change directions so it can be used in a loop.
	 * 
	 * Uses a priorityQueue and custom comparator to determine what the lowest
	 * cost is.
	 * 
	 * Very similar to breadth first traversal except it first explores a node
	 * that's less cost.
	 * 
	 * The nodes are generated if it's not a wall or it's not a node inside the
	 * explored nodes list.
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
						if (one.getUniformCost() < two.getUniformCost())
							return -1;
						else if (one.getUniformCost() > two.getUniformCost()) {
							return 1;
						} else
							return 0;
					}
				});
		queue.add(curNode);
		exploredList = new ArrayList<GameMap>();

		if (curNode.isSolved())
			return curNode;

		//Continues the search until there's no moves to explore or
		//solution is found
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
	 * Sets the connections for node.
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
	 * Compares the maps to see if this node is correct.
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

		while (!s.isEmpty()) {
			result += s.pop() + " ";
		}

		result += "\n" + "Nodes Generated: " + nodesGenerated + "\n";
		result += "Nodes revisited: " + nodesRevisited + "\n";
		result += "Explored nodes list: " + exploredList.size() + "\n";
		result += "Time: " + time + " milliseconds \n";
		System.out.println(result);
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
