package chess.engine.pieces;

import chess.PlayerColor;
import chess.PieceType;

public abstract class CastlingPiece extends Piece {
    boolean hasMoved;


    public CastlingPiece(PlayerColor color, PieceType type) {
        super(color, type);
        this.hasMoved = false;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}