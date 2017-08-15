
public class Player {

	private String name;
	private int wins;
	
	public Player() {
		this("", 0);
	}
	
	public Player(String name, int wins) {
		this.name = name;
		this.wins = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public int getWins() {
		return wins;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setWins(int wins) {
		this.wins = wins;
	}
	
	public String toString() {
		return name + ": " + wins + " wins";
	}
	
}
