package chess.engine.pieces;

import chess.PlayerColor;
import chess.engine.MoveType;
import chess.engine.Position;

import java.util.ArrayList;

public abstract class Piece {
    // TODO : Compléter les attributs pour premier mouvement.
    private final PlayerColor color;
    private final int maxX;
    private final int maxY;
    private final boolean excludeZero;
    // TODO : trouver meilleur nom.
    private final int[] validMoveRatio;

    public Piece(PlayerColor color, int maxX, int maxY, boolean excludeZero, int[] validMoveRatio) {
        this.color = color;
        this.maxX = maxX;
        this.maxY = maxY;
        this.excludeZero = excludeZero;
        this.validMoveRatio = validMoveRatio;
    }

    public void move(int x, int y) {
        if (x == 0 && y == 0) {
            throw new IllegalArgumentException("Not moving isn't a move!");
        }
    }


    //When a Check occurs, only moves authorized should be :
    //  - Blocking the check with any piece (expect King obviously)
    //  - Evading the check with the King
    //  - If no move is available -> Checkmate

    //How to check for a Stalemate :
    //  - Player currently playing has not received a check and has no moves available (Pat Stalemate)
    //      So we actually have to return a value (probably boolean) to say if moves are available
    //      -> Function to check what moves are available ? Could be used to highlight authorized moves when a piece is selected
    //      And then also used for Stalemate check
    //  - Following configuration :
    //      2 Kings
    //      2 Kings and 1 Bishop
    //      2 Kings and 1 Knight
    //      2 Kings and 2 Bishops (Bishops must on same color squares)
    //          How to check if two pieces are on the same color square :
    //              -For each piece, add their x and y coordinates
    //              -If their (x+y) % 2 result is equal, they're on the same color square

    //Fonctions d'une pièce :
    //  - Pouvoir retourner les positions où elle peut se déplacer
    //  - Pouvoir savoir s'il y a une pièce sur la case où elle se dirige
    //	    (peu importe la couleur de la pièce)
    //	    [retourner la pièce peu être utile]
    //
    //La classe Piece devrait possèder un tableau statique contenant toutes les pièces
    //	(représente le board)
    //La classe Piece devrait possèder un tableau statique des cases attaqués
    //	(valeur de la case = nbBlancsQuiAttenquent * 100 + nbNoirsQuiAttaquent)
    //      (La formule, c'est juste pour utiliser 1 seul tableau de cases attaquées)
}
