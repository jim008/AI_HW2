import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Sokoban {
	public final String FILE = "test.txt";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Sokoban game = new Sokoban();
		game.start();
	}

	public void start() {
		try {
			setData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String[] input;
	private int width, height;

	private void setData() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(FILE));
		height = Integer.parseInt(reader.readLine());
		input = new String[height];
		width = 0;
		for (int i = 0; i < height; i++) {
			input[i] = reader.readLine();
			if (input[i].length() > width)
				width = input[i].length();
		}
		reader.close();
		setNodes();
	}

	Node[][] nodes;
	Node start;
	int solutionsCount;
	private void setNodes() {
		solutionsCount = 0;
		nodes = new Node[height][width];
		setNodesData();
		setNodesLink();
		
		for(int i = 0; i < height; i++){
			for(int j = 0; j < input[i].length(); j++){
				System.out.print(nodes[i][j].data);
			}
			System.out.println();
		}
	}

	private void setNodesData(){
		for(int i = 0; i < height; i++){
			for(int j = 0; j < input[i].length(); j++){
				Node n = new Node();
				n.data = input[i].charAt(j);
				if(n.data == '@')
					start = n;
				if(n.data == '.')
					solutionsCount++;
				nodes[i][j] = n;
			}
		}
	}
	
	private void setNodesLink(){
		for(int i = 0; i < height; i++){
			for(int j = 0; j < input[i].length(); j++){
				Node n = nodes[i][j];
				if(i > 0 && nodes[i-1][j] != null){
					n.up = nodes[i-1][j];
					nodes[i-1][j].down = n;
				}
				
				if(j > 0){
					n.left = nodes[i][j-1];
					nodes[i][j-1].right = n;
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private class Node {
		public Node up, down, left, right, prev;
		public char data;

		public Node() {
			up = down = left = right = null;
			data = ' ';
		}
		

		public char peekLeft(){
			return left.data;
		}
		
		public char peekRight(){
			return right.data;
		}
		
		public char peekUp(){
			return up.data;
		}
		
		public char peekDown(){
			return down.data;
		}
	}
}
