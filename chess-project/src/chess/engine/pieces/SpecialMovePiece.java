package chess.engine.pieces;

import chess.PlayerColor;
import chess.PieceType;

public abstract class SpecialMovePiece extends Piece {
    boolean hasMoved;

    public SpecialMovePiece(PlayerColor color, PieceType type) {
        super(color, type);
        this.hasMoved = false;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }
}
