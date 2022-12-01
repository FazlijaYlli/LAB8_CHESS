package chess.engine.pieces;

import chess.PlayerColor;

public class Bishop extends Piece{
    public Bishop(PlayerColor color) {
        super(color, 7, 7, true, new int[]{1, 4, 9, 16, 25, 36, 49});
    }
}
