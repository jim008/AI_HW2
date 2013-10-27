/**
 * Jimmy C Lu jcl2182
 * AI Project 2
 * Prof. Voris
 * 
 * This class tests various search algorithms to find a solution to Sokoban.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Sokoban {
	private String file;
	private final String[] FILENAMES = { "test.txt", "test2.txt", "test3.txt" };

	public static void main(String[] args) {
		Sokoban sokoban = new Sokoban();
		sokoban.start();
	}

	public void start() {
		int option = getUserOption();
		for (int i = 0; i < FILENAMES.length; i++)
			run(FILENAMES[i], option);

	}

	/*
	 * Prompts the user for the correct option
	 */
	private int getUserOption() {
		int opt = 0;

		String out = "Please enter a number for which search to use (8 to quit):\n"
				+ "1: Breadth First Search \n"
				+ "2: Depth First Search \n"
				+ "3: Uniform Cost Search \n"
				+ "4: Greedy Best First Search (Goal distance based) \n"
				+ "5: A* Search(Goal distance based\n"
				+ "6: Greedy Best First Search (Push box preferred) \n"
				+ "7: A* Search (Push box preferred) \n";
		System.out.println(out);
		Scanner scan = new Scanner(System.in);
		opt = scan.nextInt();
		scan.close();
		return opt;
	}

	/*
	 * Sets the map based on the text file string.
	 * 
	 * Runs the search based on the option selected.
	 */
	public void run(String f, int option) {
		try {
			file = f;
			setGameMap();
			map.printMap();
			if (option == 1) {
				BreadthFirstSearch bfs = new BreadthFirstSearch(map);
				System.out.println("Breadth First Search \n");
				bfs.printResult();
			} else if (option == 2) {
				DepthFirstSearch dfs = new DepthFirstSearch(map);
				System.out.println("Depth First Search \n");
				dfs.printResult();
			} else if (option == 3) {
				UniformedCostSearch ucs = new UniformedCostSearch(map);
				System.out.println("Uniform Cost Search \n");
				ucs.printResult();
			} else if (option == 4) {
				GreedyBestFirstSearch gbfs = new GreedyBestFirstSearch(map);
				System.out
						.println("Greedy Best First Search (Distance to goal) \n");
				gbfs.printResult();
			} else if (option == 5) {
				AStarSearch ass = new AStarSearch(map);
				System.out.println("A Star Search (Distance to Goal)");
				ass.printResult();
			} else if (option == 6) {
				GreedyBestFirstSearch gbfs = new GreedyBestFirstSearch(map, 2);
				System.out
						.println("Greedy Best First Search (Pushbox Preferred) \n");
				gbfs.printResult();
			} else if (option == 7) {
				AStarSearch ass = new AStarSearch(map, 2);
				System.out.println("A Star Search (Pushbox Preferred)");
				ass.printResult();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("file opening error");
		}
	}

	/*
	 * Sets the game map based on the get file method.
	 * tries to fill any empty spaces with '#' so there is
	 * no hallow walls for dead state calculations.
	 */
	private GameMap map;

	private void setGameMap() throws IOException {
		String data = getFileData(file);

		map = new GameMap(width, height);
		Scanner scan = new Scanner(data);

		for (int y = 0; y < height; y++) {
			String line = scan.nextLine();
			for (int x = 0; x < width; x++) {
				if (y == 0 || y == (height - 1) || x == 0 || x == (width - 1))
					map.add(x, y, '#');
				else if (x < line.length())
					map.add(x, y, line.charAt(x));
				else
					map.add(x, y, '#');
			}
		}

		scan.close();
	}

	/*
	 * Reads the file and puts it into a string.
	 * 
	 * returns the file data in String form.
	 */
	private int width, height;

	private String getFileData(String f) throws IOException {
		String data = "";
		width = 0;
		height = 0;
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		while ((line = br.readLine()) != null) {
			data += line + "\n";
			if (width < line.length())
				width = line.length();
			height++;
		}
		br.close();

		return data;
	}
}
