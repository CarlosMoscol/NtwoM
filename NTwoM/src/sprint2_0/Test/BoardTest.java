package sprint2_0.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


import static org.junit.Assert.*;

import org.junit.Test;

import sprint1_0.Product.Board_GUI;
import sprint1_0.Product.GameException;
import sprint1_0.Product.Position;
import sprint1_0.Product.Token;

public class BoardTest {

	@Test
	public void testConstructor() {
		Board_GUI board = new Board_GUI();
		try {
			assertEquals(0, board.getNumberOfPiecesOfPlayer(Token.PLAYER_1));
			assertEquals(0, board.getNumberOfPiecesOfPlayer(Token.PLAYER_2));
			assertTrue(board.getNumTotalPiecesPlaced() == 0);
			assertTrue(board.positionIsAvailable(0));
			assertNotNull(board.getMillCombination(0));
		} catch(GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Test
	public void testPieces() {
		Board_GUI board = new Board_GUI();
		try {
			assertTrue(board.getNumTotalPiecesPlaced() == 0);
			board.incNumTotalPiecesPlaced();
			board.incNumPiecesOfPlayer(Token.PLAYER_1);
			assertFalse(board.getNumTotalPiecesPlaced() == 0);
			assertEquals(1, board.getNumberOfPiecesOfPlayer(Token.PLAYER_1));
			board.incNumPiecesOfPlayer(Token.NO_PLAYER);
			fail("The previous method should have sent an exception");
		} catch (GameException e) {
			// expected exception, do nothing
		}
	}
	
	@Test
	public void testMillCombinations() {
		Board_GUI board = new Board_GUI();
		try {
			Position[] millRow = board.getMillCombination(0);
			assertNotNull(millRow);
			assertEquals(0, millRow[0].getPositionIndex());
			assertEquals(1, millRow[1].getPositionIndex());
			assertEquals(2, millRow[2].getPositionIndex());
		} catch (GameException e) {
			e.printStackTrace();
		}
	}

}
