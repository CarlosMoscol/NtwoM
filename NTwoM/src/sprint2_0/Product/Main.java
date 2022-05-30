package sprint2_0.Product;
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
		
		System.out.println("Para iniciar el juego Escriba (L) o LOCAL"); 
		String userInput = maingame.input.readLine();
		userInput = userInput.toUpperCase();
			
		if(userInput.compareTo("LOCAL") == 0 || userInput.compareTo("L") == 0) {
			maingame.createLanGame(9);
		} else {
			System.out.println("Comando Desconocido");
			System.exit(-1);
		}
	}
	
	public void createLanGame(int minimaxDepth) throws IOException, GameException {
		
		/*
		int numberGames = 0, fixedNumberGames = 0, numberMoves = 0, draws = 0, p1Wins = 0, p2Wins = 0;
		String userInput = "";
		boolean bothCPU = true;
		Player p1 = null, p2=null;
		p1 = new HumanPlayer("Carlitos(X)", Token.PLAYER_1, State_Board.NUM_PIECES_PER_PLAYER);
		p2 = new HumanPlayer("Kencito(O)", Token.PLAYER_2, State_Board.NUM_PIECES_PER_PLAYER);
		System.out.println ("Tablero v0.1 NTwoM: (P1 y P2 predefinidos)");
		*/
		//Seleccion de jugadores
		Player p1 = null, p2 = null;
		boolean bothCPU = true;
		int numberGames = 0, fixedNumberGames = 0, numberMoves = 0, draws = 0, p1Wins = 0, p2Wins = 0;
		
		
		System.out.println("Jugador 1: (H)UMANO or (B)OT?");
		String userInput = input.readLine();
		userInput = userInput.toUpperCase();
		
		if(userInput.compareTo("HUMANO") == 0 || userInput.compareTo("H") == 0) {
			p1 = new HumanPlayer("KENCALU", Token.PLAYER_1, State_Board.NUM_PIECES_PER_PLAYER);
			bothCPU = false;
		} else if(userInput.compareTo("BOT") == 0 || userInput.compareTo("B") == 0) {
			p1 = new SomeOneBotPlayer(Token.PLAYER_1,State_Board.NUM_PIECES_PER_PLAYER);
		} else {
			System.out.println("Comando desconocido");
			System.exit(-1);
		}
		
		System.out.println("Jugador 2: (H)UMANO or (B)OT?");
		userInput = input.readLine();
		userInput = userInput.toUpperCase();
		
		if(userInput.compareTo("HUMANO") == 0 || userInput.compareTo("H") == 0) {
			p2 = new HumanPlayer("KENCALU_CO", Token.PLAYER_2, State_Board.NUM_PIECES_PER_PLAYER);
			bothCPU = false;
		} else if(userInput.compareTo("BOT") == 0 || userInput.compareTo("B") == 0) {
			p2 = new SomeOneBotPlayer(Token.PLAYER_2,State_Board.NUM_PIECES_PER_PLAYER);
		} else {
			System.out.println("Comando desconocido");
			System.exit(-1);
		}
		
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
				System.out.println("Juegos restantes: "+numberGames);
			}
			
			while(game.getCurrentGamePhase() == State_Board.PLACING_PHASE) {

				while(true) {
					Player p = ((LanGame)game).getCurrentTurnPlayer();
					int boardIndex;

					if(p.isAI()) {
						long startTime = System.nanoTime();
						boardIndex = ((BotPlayer)p).getIndexToPlacePiece(game.gameBoard);
						long endTime = System.nanoTime();
						Log.warn("Numero de movimientos: "+((BotPlayer)p).numberOfMoves);
						Log.warn("Piezas removidas: "+((BotPlayer)p).movesThatRemove);
						Log.warn("La partida demoro: "+ (endTime - startTime)/1000000+" milisegundos");

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
									System.out.println(p.getName()+" remover la pieza del openente "+boardIndex);
								} else {
									game.printGameBoard();
									System.out.println("Hiciste un molino !!!. Puedes quitar una pieza de tu oponente: ");
									userInput = input.readLine();
									userInput = userInput.toUpperCase();
									boardIndex = Integer.parseInt(userInput);
								}
								if(game.removePiece(boardIndex, opponentPlayer)) {
									break;
								} else {
									System.out.println("No se puede colocar la pieza alli. Intentar otra vez");
								}
							}
						}
						((LanGame)game).updateCurrentTurnPlayer();
						break;
					} else {
						System.out.println("No se puede colocar la pieza alli. Intentar otra vez");
					}
				}
			}

			System.out.println("Todas lapiezas estan colocadas en el tablero. Comenzando la parte divertida... ");
			while(!game.isTheGameOver() && numberMoves < Main.MAX_MOVES) {

				while(true) {
					System.out.println("Numero de movimientos realizados: "+numberMoves);
					Player p = ((LanGame)game).getCurrentTurnPlayer();
					int srcIndex, destIndex;
					Move move = null;

					if(p.isAI()) {
						long startTime = System.nanoTime();
						move = ((BotPlayer)p).getPieceMove(game.gameBoard, game.getCurrentGamePhase());
						long endTime = System.nanoTime();
						game.printGameBoard();

						System.out.println("Numero de movimiento: "+((BotPlayer)p).numberOfMoves);
											System.out.println("Piezas que fueron eliminadas: "+((BotPlayer)p).movesThatRemove);
						System.out.println("La partida demoro: "+ (endTime - startTime)/1000000+" milisegundos");
						srcIndex = move.srcIndex;
						destIndex = move.destIndex;
						System.out.println(p.getName()+" mover pieza desde "+srcIndex+" a "+destIndex);
					} else {
						game.printGameBoard();
						System.out.println(p.getName()+" es tu turno. Ingresa POS_INI:POS_DEST \n");
						userInput = input.readLine();
						userInput = userInput.toUpperCase();
						String[] positions = userInput.split(":");
						srcIndex = Integer.parseInt(positions[0]);
						destIndex = Integer.parseInt(positions[1]);
						System.out.println("Mover pieza "+srcIndex+" de "+destIndex);
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
									System.out.println(p.getName()+" quita la pieza del oponente en "+boardIndex);
								} else {
									System.out.println("Hiciste un molino !!!. Puedes quitar una pieza de tu oponente: ");
									userInput = input.readLine();
									userInput = userInput.toUpperCase();
									boardIndex = Integer.parseInt(userInput);
								}
								if(game.removePiece(boardIndex, opponentPlayerToken)) {
									break;
								} else {
									System.out.println("No se pudo hacer! Intentar otra vez.");
								}
							}
						}


						if(game.isTheGameOver() || numberMoves >= MAX_MOVES) {
							game.printGameBoard();
							break;
						}
						((LanGame)game).updateCurrentTurnPlayer();
					} else {
						System.out.println("Movimiento invalido. Codigo de error: "+result);
					}
				}
			}

			if(!game.isTheGameOver()) {
				System.out.println("Empate!");
				draws++;
			} else {
				System.out.println("Juego terminado. Jugador "+((LanGame)game).getCurrentTurnPlayer().getPlayerToken()+" Gano");
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
		System.out.println(fixedNumberGames+" Juegos completados en: "+ (gamesEnd - gamesStart)/1000000000+" segundos");
		System.out.println("Numero promedio de movimientos: "+(totalMoves/fixedNumberGames));
		System.out.println("Empates: "+draws+" ("+((float)draws/fixedNumberGames)*100+"%)");
		System.out.println("J1 Gana: "+p1Wins+" ("+((float)p1Wins/fixedNumberGames)*100+"%)");
		System.out.println("J2 Gana: "+p2Wins+" ("+((float)p2Wins/fixedNumberGames)*100+"%)");
	}

}
