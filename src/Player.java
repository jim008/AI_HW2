/**
 * Jimmy C Lu jcl2182
 * 
 * This class keeps data for the player in sokoban.
 */
public class Player {
	public char symbol;
	public int x, y,dx, dy, nextX, nextY, pushX, pushY;

	public Player(char s){
		symbol = s;
	}
	
	public void setNext(int dx, int dy){
		this.dx = dx;
		this.dy = dy;
		nextX = x + dx;
		nextY = y + dy;
		pushX = nextX + dx;
		pushY = nextY + dy;
	}
	
	public void update(){
		x = nextX;
		y = nextY;
		nextX = pushX;
		nextY = pushY;
		pushX += dx;
		pushY += dy;
	}
	
	public boolean isOnSol(){
		return symbol == '+';
	}
}
