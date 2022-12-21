package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.Move;
import chess.engine.MoveType;
import chess.engine.Position;

import java.util.ArrayList;

public class Queen extends Piece{

    static final ArrayList<Move> moves;

    static {
        moves = new ArrayList<>();
        moves.addAll(Rook.moves);
        moves.addAll(Bishop.moves);
    }

    public Queen(PlayerColor color) {
        super(color, PieceType.QUEEN);
    }

    @Override
    public ArrayList<Move> getMoves() {
        return moves;
    }

    //Queens always start on X = 3 (4 on Chess board)
}
