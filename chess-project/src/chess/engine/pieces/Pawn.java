package chess.engine.pieces;

import chess.PlayerColor;

public class Pawn extends Piece{
    public Pawn(PlayerColor color) {
        super(color, 1, 0, true, new int[]{0});
    }
}
