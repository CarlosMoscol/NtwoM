package sprint2_0.Product;
import java.util.Random;

public abstract class BotPlayer extends Player {
	protected Random rand;
	public int numberOfMoves = 0; // TODO TESTING
	public int movesThatRemove = 0;
	private static String[] randomNames = { "Albert Einstein",
		"Stephen Hawking", "Sheldon Cooper", "Dr.House", "Michael Jackson",
		"Michael Bay", "Mark Zuckerberg", "Alfred Hitchcock", "Amy Whinehouse",
		"Angelina Jolie", "Arnold Schwarzenegger", "Barak Obama", "Batman",
		"David Beckham", "Bruce Willis", "Charlie Chaplin", "Clint Eastwood",
		"Conan O' Brien", "Condoleezza Rice", "Charles Darwin", "Dexter Morgan",
		"Frodo", "Sauron", "George W Bush", "Hannibal", "Harrison Ford", "Harry Potter",
		"John Locke", "Johnny Depp", "John Wayne", "Karl Marx", "Larry King", "Leonardo Dicaprio",
		"Manny Pacquiao", "Marilyn Manson", "Matt Damon", "Meryl Streep", "Mr Bean",
		"Paris Hilton", "Prince Charles", "Quentin Tarantino", "Robert Pattinson",
		"Samuel L. Jackson", "Simon Cowell", "Snoop Lion", "Spielberg", "Steven Seagal",
		"Terminator", "Tom Cruise", "Will Smith", "Nelson Mandela", "Iron Man", "Hulk", "Thor",
		"Loki", "Captain America", "Black Widow", "Phil Coulson"};
	
	public BotPlayer (Token player, int numPiecesPerPlayer) throws GameException {
		super (player, numPiecesPerPlayer);
		rand = new Random();
		setName();
	}
	
	private void setName() {
		name = randomNames[rand.nextInt(randomNames.length)];
		System.out.println("El nombre del Bot es: "+name);
	}

	@Override
	public boolean isAI() {
		return true;
	}
	
	public abstract int getIndexToPlacePiece(Board_GUI gameBoard);
	
	public abstract int getIndexToRemovePieceOfOpponent(Board_GUI gameBoard);
	
	public abstract Move getPieceMove(Board_GUI gameBoard, int gamePhase) throws GameException;
}
