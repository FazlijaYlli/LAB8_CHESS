package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class King extends CastlingPiece {

    /**
     * Create a King piece
     *
     * @param color White or Black piece
     */
    public King(PlayerColor color) {
        super(color, PieceType.KING);
    }

    /**
     * Check if the (x,y) relative coordinate is a valid move for the King
     *
     * @param x relative x coordinate
     * @param y relative y coordinate
     * @return true if the King can move there
     */
    public boolean canMove(int x, int y) {
        return (Math.abs(x) == 1 || Math.abs(y) == 1) && Math.abs(x * y) < 2;
    }
}
