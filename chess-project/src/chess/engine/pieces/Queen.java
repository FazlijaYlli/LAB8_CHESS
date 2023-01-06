package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class Queen extends Piece {

    /**
     * Create a Queen piece
     *
     * @param color White or Black piece
     */
    public Queen(PlayerColor color) {
        super(color, PieceType.QUEEN);
    }

    /**
     * Check if the (x,y) relative coordinate is a valid move for the Queen
     *
     * @param x relative x coordinate
     * @param y relative y coordinate
     * @return true if the Queen can move there
     */
    public boolean canMove(int x, int y) {
        return (x + y != 0 && x * y == 0) //Rook
                || (y != 0 && Math.abs(x) == Math.abs(y)); //Bishop
    }

}
