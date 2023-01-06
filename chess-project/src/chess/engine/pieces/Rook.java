package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class Rook extends CastlingPiece {

    public Rook(PlayerColor color) {
        super(color, PieceType.ROOK);
    }

    public boolean canMove(int x, int y){
        return x+y != 0 && x*y == 0;
    }
}
