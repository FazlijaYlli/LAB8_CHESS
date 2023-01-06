package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class Pawn extends CountingMovePiece {

    /**
     * Create a Pawn piece
     *
     * @param color White or Black piece
     */
    public Pawn(PlayerColor color) {
        super(color, PieceType.PAWN);
    }

    /**
     * Check if the (x,y) relative coordinate is a valid move for the Pawn
     *
     * @param x relative x coordinate
     * @param y relative y coordinate
     * @return true if the Pawn can move there
     */
    public boolean canMove(int x, int y) {
        if (getNbMoves() == 0 && y == (getColor() == PlayerColor.WHITE ? 2 : -2) && x == 0) {
            return true;
        } else {
            return y == (getColor() == PlayerColor.WHITE ? 1 : -1) && x == 0;
        }
    }

    /**
     * Check if the (x,y) relative coordinate is a valid attack for the Pawn
     *
     * @param x relative x coordinate
     * @param y relative y coordinate
     * @return true if the Pawn can attack there
     */
    public boolean canAttack(int x, int y) {
        return y == (getColor() == PlayerColor.WHITE ? 1 : -1) && Math.abs(x) == 1;
    }
}
