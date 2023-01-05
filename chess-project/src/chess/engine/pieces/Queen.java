package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;
public class Queen extends Piece{


    public Queen(PlayerColor color) {
        super(color, PieceType.QUEEN);
    }

    public boolean canMove(int x, int y){
        return (y != 0 && (Math.abs(x/y) == 1 || x == 0)) || x != 0;
    }
}
