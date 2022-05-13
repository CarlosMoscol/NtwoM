package sprint1_0.Product;

public class HumanPlayer extends Player {
		
    	public HumanPlayer(String name, Token player, int numPiecesPerPlayer) throws GameException {
    		super(player, numPiecesPerPlayer);
    		this.name = name;
    	}

    	@Override
    	public boolean isAI() {
    		return false;
    	}
}
