package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;

public class Knight extends Piece{

    public Knight(PlayerColor color) {
        super(color, PieceType.KNIGHT);
    }

    public boolean canMove(int x,int y){
        return Math.abs(x*y) == 2;
    }

    //Knight is the only piece that can ignore pieces on its way (It jumps above pieces)
}
