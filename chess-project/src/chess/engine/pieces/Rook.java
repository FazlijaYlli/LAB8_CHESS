package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class Rook extends CastlingPiece{
    public Rook(PlayerColor color) {
        super(color, PieceType.ROOK);
    }

    //First Move : Castle is a King move, no need to check on the Rook side
}
