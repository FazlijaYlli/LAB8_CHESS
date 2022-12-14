package chess.engine.pieces;

import chess.PlayerColor;
import chess.PieceType;

public abstract class castlingPiece extends Piece {
    boolean hasMoved;

    public castlingPiece(PlayerColor color, PieceType type) {
        super(color, type);
    }
}
