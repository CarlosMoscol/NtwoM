package sprint2_0.Product;
import com.Log;

public class State_Board {
    static public final int NUM_PIECES_PER_PLAYER = 9;
    static public final int PLACING_PHASE = 1;
    static public final int MOVING_PHASE = 2;
    static public final int FLYING_PHASE = 3;

    static public final int INVALID_SRC_POS = -1;
    static public final int UNAVAILABLE_POS = -2;
    static public final int INVALID_MOVE = -3;
    static public final int VALID_MOVE = 0;

    static protected final int MIN_NUM_PIECES = 2;

    protected Board_GUI gameBoard;
    protected int gamePhase;

    public State_Board() {
        gameBoard = new Board_GUI();
        gamePhase = State_Board.PLACING_PHASE;
    }

    public int getCurrentGamePhase() {
        return gamePhase;
    }

    public Board_GUI getGameBoard() {
        return gameBoard;
    }

    public Token getPlayerInBoardPosition(int boardPosition) {
        try {
            return gameBoard.getPosition(boardPosition).getPlayerOccupyingIt();
        } catch (GameException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return Token.NO_PLAYER;
    }

    public boolean positionIsAvailable(int boardIndex) throws GameException {
        return gameBoard.positionIsAvailable(boardIndex);
    }

    public boolean validMove(int currentPositionIndex, int nextPositionIndex) throws GameException {
        Position currentPos = gameBoard.getPosition(currentPositionIndex);
        if(currentPos.isAdjacentToThis(nextPositionIndex) && !gameBoard.getPosition(nextPositionIndex).isOccupied()) {
            return true;
        }
        return false;
    }

    public int movePieceFromTo(int srcIndex, int destIndex, Token player) throws GameException {
        if(positionHasPieceOfPlayer(srcIndex, player)) {
            if(positionIsAvailable(destIndex)) {
                System.out.println("Number of pieces: "+gameBoard.getNumberOfPiecesOfPlayer(player));
                if(validMove(srcIndex, destIndex) || (gameBoard.getNumberOfPiecesOfPlayer(player) == State_Board.MIN_NUM_PIECES + 1)) {
                    gameBoard.getPosition(srcIndex).setAsUnoccupied();
                    gameBoard.getPosition(destIndex).setAsOccupied(player);
                    return State_Board.VALID_MOVE;
                } else {
                    return State_Board.INVALID_MOVE;
                }
            } else {
                return State_Board.UNAVAILABLE_POS;
            }
        } else {
            return State_Board.INVALID_SRC_POS;
        }
    }

    public boolean placePieceOfPlayer(int boardPosIndex, Token player) throws GameException {
        if(gameBoard.positionIsAvailable(boardPosIndex)) {
            gameBoard.getPosition(boardPosIndex).setAsOccupied(player);
            gameBoard.incNumPiecesOfPlayer(player);
            if(gameBoard.incNumTotalPiecesPlaced() == (NUM_PIECES_PER_PLAYER * 2)) {
                gamePhase = State_Board.MOVING_PHASE;
            }
            return true;
        }
        return false;
    }

    public boolean madeAMill(int dest, Token player) throws GameException {
        int maxNumPlayerPiecesInRow = 0;
        for(int i = 0; i < Board_GUI.NUM_MILL_COMBINATIONS; i++) {
            Position[] row = gameBoard.getMillCombination(i);
            for(int j = 0; j < Board_GUI.NUM_POSITIONS_IN_EACH_MILL; j++) {
                if(row[j].getPositionIndex() == dest) {
                    int playerPiecesInThisRow = numPiecesFromPlayerInRow(row, player);
                    if(playerPiecesInThisRow > maxNumPlayerPiecesInRow) {
                        maxNumPlayerPiecesInRow = playerPiecesInThisRow;
                    }
                }
            }
        }
        return (maxNumPlayerPiecesInRow == Board_GUI.NUM_POSITIONS_IN_EACH_MILL);
    }

    private int numPiecesFromPlayerInRow(Position[] pos, Token player) {
        int counter = 0;
        for(int i = 0; i < pos.length; i++) {
            if(pos[i].getPlayerOccupyingIt() == player) {
                counter++;
            }
        }
        return counter;
    }

    public boolean positionHasPieceOfPlayer(int boardIndex, Token player) throws GameException {
        return (gameBoard.getPosition(boardIndex).getPlayerOccupyingIt() == player);
    }

    public void printGameBoard() {
        gameBoard.printBoard();
    }

    public boolean removePiece(int boardIndex, Token player) throws GameException {
        if(!gameBoard.positionIsAvailable(boardIndex) && positionHasPieceOfPlayer(boardIndex, player)) {
            gameBoard.getPosition(boardIndex).setAsUnoccupied();
            gameBoard.decNumPiecesOfPlayer(player);
            if(gamePhase == State_Board.MOVING_PHASE && gameBoard.getNumberOfPiecesOfPlayer(player) == (State_Board.MIN_NUM_PIECES+1)) {
                gamePhase = State_Board.FLYING_PHASE;
                Log.info("New game phase is: "+gamePhase);
            }
            return true;
        }
        return false;
    }

    public Player getPlayer() {
        return null;
    }

    public boolean isTheGameOver() {
        try {
            if(gameBoard.getNumberOfPiecesOfPlayer(Token.PLAYER_1) == State_Board.MIN_NUM_PIECES
                    || gameBoard.getNumberOfPiecesOfPlayer(Token.PLAYER_2) == State_Board.MIN_NUM_PIECES) {
                return true;
            } else {
                boolean p1HasValidMove = false, p2HasValidMove = false;
                Token player;

                // Reviza si cada jugador tiene al menos un mvimiento valido
                for(int i = 0; i < Board_GUI.NUM_POSITIONS_OF_BOARD; i++) {
                    Position position = gameBoard.getPosition(i);
                    if((player = position.getPlayerOccupyingIt()) != Token.NO_PLAYER) {
                        int[] adjacent = position.getAdjacentPositionsIndexes();
                        for(int j = 0; j < adjacent.length; j++) {
                            Position adjacentPos = gameBoard.getPosition(adjacent[j]);
                            if(!adjacentPos.isOccupied()) {
                                if(!p1HasValidMove) { //Debera cambiar solo si el boleano es falso
                                    p1HasValidMove = (player == Token.PLAYER_1);
                                }
                                if(!p2HasValidMove) {
                                    p2HasValidMove = (player == Token.PLAYER_2);
                                }
                                break;
                            }
                        }
                    }
                    if(p1HasValidMove && p2HasValidMove) {
                        return false;
                    }
                }
            }
        } catch (GameException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return true;
    }
}
