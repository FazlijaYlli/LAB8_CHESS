package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class Bishop extends Piece{



    public Bishop(PlayerColor color) {
        super(color, PieceType.BISHOP);
    }

    public boolean canMove(int x, int y){
        return y != 0 && Math.abs(x/y) == 1;
    }
}
