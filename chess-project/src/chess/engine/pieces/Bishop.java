package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class Bishop extends Piece {

    /**
     * Create a Bishop piece
     *
     * @param color White or Black piece
     */
    public Bishop(PlayerColor color) {
        super(color, PieceType.BISHOP);
    }

    /**
     * @param x relative x coordinate
     * @param y relative y coordinate
     * @return true if the Bishop can move there
     */
    public boolean canMove(int x, int y) {
        return y != 0 && Math.abs(x) == Math.abs(y);
    }
}
