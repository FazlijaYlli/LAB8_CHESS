package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;
public class Queen extends Piece{


    public Queen(PlayerColor color) {
        super(color, PieceType.QUEEN);
    }

    public boolean canMove(int x, int y){
        return (x+y != 0 && x*y == 0) //Rook
                || (y != 0 && Math.abs(x) == Math.abs(y)); //Bishop
    }

}
