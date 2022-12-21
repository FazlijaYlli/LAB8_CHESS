package chess.engine.pieces;

import chess.PlayerColor;
import chess.PieceType;
import chess.engine.Move;

public abstract class CastlingPiece extends Piece {
    boolean hasMoved;

    public CastlingPiece(PlayerColor color, PieceType type) {
        super(color, type);
    }


}
