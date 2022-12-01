package chess.engine.pieces;

import chess.PlayerColor;

public class Knight extends Piece{
    public Knight(PlayerColor color) {
        super(color, 2, 2, true, new int[]{2});
    }

    //Knight is the only piece that can ignore pieces on its way (It jumps above pieces)
}
