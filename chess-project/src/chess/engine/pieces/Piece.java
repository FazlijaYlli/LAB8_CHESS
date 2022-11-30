package chess.engine.pieces;
import chess.PlayerColor;

import java.util.ArrayList;

public abstract class Piece {
    // TODO : Compléter les attributs pour premier mouvement.
    private PlayerColor color;
    private int maxX;
    private int maxY;
    private boolean exludeZero;
    // TODO : trouver meilleur nom.
    private ArrayList<Integer> validMoveRatio;

    public Piece(PlayerColor color) {
        this.color = color;
    }
}
