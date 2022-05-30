package sprint2_0.Product;

public class SomeOneBotPlayer extends BotPlayer{

	public SomeOneBotPlayer(Token player, int numPiecesPerPlayer) throws GameException {
		super(player, numPiecesPerPlayer);
	}

	@Override
	public int getIndexToPlacePiece(Board_GUI gameBoard) {
		while(true) {
			int index = rand.nextInt(Board_GUI.NUM_POSITIONS_OF_BOARD);
			try {
				if(!gameBoard.getPosition(index).isOccupied()) {
					return index;
				}
			} catch (GameException e) {
				e.printStackTrace();
			}
		}
	}

}

