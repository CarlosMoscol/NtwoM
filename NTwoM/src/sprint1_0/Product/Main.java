package sprint1_0.Product;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.Log;
public class Main {
	public State_Board game;
	public BufferedReader input;
	public static final int MAX_MOVES = 150;
	public static int totalMoves = 0;

	public static void main(String []args) throws Exception, GameException {
		System.out.println("Iniciando NtwoM...");
		Log.set(Log.LEVEL_ERROR);
		Main maingame = new Main();
		maingame.input = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("(L)OCAL or (N)ETWORK?"); 
		String userInput = maingame.input.readLine();
		userInput = userInput.toUpperCase();
			
		if(userInput.compareTo("LOCAL") == 0 || userInput.compareTo("L") == 0) {
			maingame.createLanGame(9);
		} else if(userInput.compareTo("") == 0 || userInput.compareTo("N") == 0) {
			System.out.println("Aun no implementado");
		} else {
			System.out.println("Comando Desconocido");
			System.exit(-1);
		}
	}
	
	public void createLanGame(int minimaxDepth) throws IOException, GameException {
		int numberGames = 0, fixedNumberGames = 0, numberMoves = 0, draws = 0, p1Wins = 0, p2Wins = 0;
		String userInput = "";
		boolean bothCPU = true;
		Player p1 = null, p2=null;
		p1 = new HumanPlayer("Carlitos", Token.PLAYER_1, State_Board.NUM_PIECES_PER_PLAYER);
		p2 = new HumanPlayer("Kencito", Token.PLAYER_2, State_Board.NUM_PIECES_PER_PLAYER);
		System.out.println ("Tablero v0.1 NTwoM: (P1 y P2 predefinidos)");
		
		
		if(bothCPU) {
			System.out.println("Numero de partidas a jugar: ");
			userInput = input.readLine();
			numberGames = Integer.parseInt(userInput.toUpperCase());
			fixedNumberGames = numberGames;
		} else {
			numberGames = 1;
		}
		game = new LanGame();
		((LanGame)game).setPlayers(p1, p2);
		
		long gamesStart = System.nanoTime();
		while(numberGames > 0) {
			if((numberGames-- % 50) == 0){
				System.out.println("Games left: "+numberGames);
			}
			
			while(game.getCurrentGamePhase() == State_Board.PLACING_PHASE) {

				while(true) {
					Player p = ((LanGame)game).getCurrentTurnPlayer();
					int boardIndex;

					if(p.isAI()) {
						long startTime = System.nanoTime();
						boardIndex = ((BotPlayer)p).getIndexToPlacePiece(game.gameBoard);
						long endTime = System.nanoTime();
						Log.warn("Number of moves: "+((BotPlayer)p).numberOfMoves);
						Log.warn("Moves that removed: "+((BotPlayer)p).movesThatRemove);
						Log.warn("It took: "+ (endTime - startTime)/1000000+" miliseconds");

					} else {
						game.printGameBoard();
						System.out.println(p.getName()+" coloca una pieza en la posicion (0 - 23): ");
						userInput = input.readLine();
						userInput = userInput.toUpperCase();
						boardIndex = Integer.parseInt(userInput);
					}

					if(game.placePieceOfPlayer(boardIndex, p.getPlayerToken())) {
						numberMoves++; // TODO testing
						totalMoves++;
						p.raiseNumPiecesOnBoard();

						if(game.madeAMill(boardIndex, p.getPlayerToken())) {
							Token opponentPlayer = (p.getPlayerToken() == Token.PLAYER_1) ? Token.PLAYER_2 : Token.PLAYER_1;

							while(true) {
								if(p.isAI()){
									boardIndex = ((BotPlayer)p).getIndexToRemovePieceOfOpponent(game.gameBoard);
									System.out.println(p.getName()+" removes opponent piece on "+boardIndex);
								} else {
									game.printGameBoard();
									System.out.println("You made a mill. You can remove a piece of your oponent: ");
									userInput = input.readLine();
									userInput = userInput.toUpperCase();
									boardIndex = Integer.parseInt(userInput);
								}
								if(game.removePiece(boardIndex, opponentPlayer)) {
									break;
								} else {
									System.out.println("You can't remove a piece from there. Try again");
								}
							}
						}
						((LanGame)game).updateCurrentTurnPlayer();
						break;
					} else {
						System.out.println("You can't place a piece there. Try again");
					}
				}
			}

			System.out.println("The pieces are all placed. Starting the fun part... ");
			while(!game.isTheGameOver() && numberMoves < Main.MAX_MOVES) {

				while(true) {
					System.out.println("Number of moves made: "+numberMoves);
					Player p = ((LanGame)game).getCurrentTurnPlayer();
					int srcIndex, destIndex;
					Move move = null;

					if(p.isAI()) {
						long startTime = System.nanoTime();
						move = ((BotPlayer)p).getPieceMove(game.gameBoard, game.getCurrentGamePhase());
						long endTime = System.nanoTime();
						game.printGameBoard();

						System.out.println("Number of moves: "+((BotPlayer)p).numberOfMoves);
											System.out.println("Moves that removed: "+((BotPlayer)p).movesThatRemove);
						System.out.println("It took: "+ (endTime - startTime)/1000000+" miliseconds");
						srcIndex = move.srcIndex;
						destIndex = move.destIndex;
						System.out.println(p.getName()+" moved piece from "+srcIndex+" to "+destIndex);
					} else {
						game.printGameBoard();
						System.out.println(p.getName()+" it's your turn. Input PIECE_POS:PIECE_DEST");
						userInput = input.readLine();
						userInput = userInput.toUpperCase();
						String[] positions = userInput.split(":");
						srcIndex = Integer.parseInt(positions[0]);
						destIndex = Integer.parseInt(positions[1]);
						System.out.println("Move piece from "+srcIndex+" to "+destIndex);
					}

					int result;
					if((result = game.movePieceFromTo(srcIndex, destIndex, p.getPlayerToken())) == State_Board.VALID_MOVE) {
						numberMoves++; // TODO testing
						totalMoves++;
						if(game.madeAMill(destIndex, p.getPlayerToken())) {
							Token opponentPlayerToken = (p.getPlayerToken() == Token.PLAYER_1) ? Token.PLAYER_2 : Token.PLAYER_1;
							int boardIndex;

							while(true) {
								if(p.isAI()){
									boardIndex = move.removePieceOnIndex;
									System.out.println(p.getName()+" removes opponent piece on "+boardIndex);
								} else {
									System.out.println("You made a mill! You can remove a piece of your oponent: ");
									userInput = input.readLine();
									userInput = userInput.toUpperCase();
									boardIndex = Integer.parseInt(userInput);
								}
								if(game.removePiece(boardIndex, opponentPlayerToken)) {
									break;
								} else {
									System.out.println("It couldn't be done! Try again.");
								}
							}
						}


						if(game.isTheGameOver() || numberMoves >= MAX_MOVES) {
							game.printGameBoard();
							break;
						}
						((LanGame)game).updateCurrentTurnPlayer();
					} else {
						System.out.println("Invalid move. Error code: "+result);
					}
				}
			}

			if(!game.isTheGameOver()) {
				System.out.println("Draw!");
				draws++;
			} else {
				System.out.println("Game over. Player "+((LanGame)game).getCurrentTurnPlayer().getPlayerToken()+" Won");
				if(((LanGame)game).getCurrentTurnPlayer().getPlayerToken() == Token.PLAYER_1) {
					p1Wins++;
				} else {
					p2Wins++;
				}
			}
			numberMoves = 0;
			game = new LanGame();
			p1.reset();
			p2.reset();
			((LanGame)game).setPlayers(p1, p2);
		}
		long gamesEnd = System.nanoTime();
		System.out.println(fixedNumberGames+" Juegos completados en: "+ (gamesEnd - gamesStart)/1000000000+" seconds");
		System.out.println("Average number of ply: "+(totalMoves/fixedNumberGames));
		System.out.println("Draws: "+draws+" ("+((float)draws/fixedNumberGames)*100+"%)");
		System.out.println("P1 Wins: "+p1Wins+" ("+((float)p1Wins/fixedNumberGames)*100+"%)");
		System.out.println("P2 Wins: "+p2Wins+" ("+((float)p2Wins/fixedNumberGames)*100+"%)");
	}

}
