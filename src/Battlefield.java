
public class Battlefield {

	private Player player1;
	private Player player2;
	
	public Battlefield() {
		this(null, null);
	}
	
	public Battlefield(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
	}
}
