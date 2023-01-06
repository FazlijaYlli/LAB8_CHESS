package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class Rook extends CastlingPiece {

    /**
     * Create a Rook piece
     *
     * @param color White or Black piece
     */
    public Rook(PlayerColor color) {
        super(color, PieceType.ROOK);
    }

    /**
     * Check if the (x,y) relative coordinate is a valid move for the Rook
     *
     * @param x relative x coordinate
     * @param y relative y coordinate
     * @return true if the Rook can move there
     */
    public boolean canMove(int x, int y) {
        return x + y != 0 && x * y == 0;
    }
}
