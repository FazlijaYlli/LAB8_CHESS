package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class Knight extends Piece{
    public Knight(PlayerColor color) {
        super(color, PieceType.KNIGHT);
    }

    //Knight is the only piece that can ignore pieces on its way (It jumps above pieces)
}
