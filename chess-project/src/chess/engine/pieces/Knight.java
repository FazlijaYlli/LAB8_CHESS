package chess.engine.pieces;

import chess.PieceType;
import chess.PlayerColor;
import chess.engine.Move;
import chess.engine.MoveType;
import chess.engine.Position;

import java.util.ArrayList;

public class Knight extends Piece{

    static final ArrayList<Move> moves;

    static {
        moves = new ArrayList<>();
        for (int i = 1; i < 3; ++i) {
            moves.add(new Move(new Position(i, 3 - i), MoveType.BOTH));
            moves.add(new Move(new Position(-i, i - 3), MoveType.BOTH));
        }
    }

    public Knight(PlayerColor color) {
        super(color, PieceType.KNIGHT);
    }

    @Override
    public ArrayList<Move> getMoves() {
        return moves;
    }

    //Knight is the only piece that can ignore pieces on its way (It jumps above pieces)
}
