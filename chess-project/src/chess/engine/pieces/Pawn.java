package chess.engine.pieces;

import chess.PlayerColor;

public class Pawn extends Piece{
    public Pawn(PlayerColor color) {
        super(color, 0, 1, true, new int[]{0});
    }

    //Pawn is special, maxY only applies in positives, not negatives, can't go backwards!
    //First Move : maxX = 0, maxY = 1, excludeZero = true, validMoveRatio = {0}
    //For eating : maxX = 1, maxY = 1, excludeZero = true, validMoveRatio = {1}
    //  The pawn can't eat in front him :(
    //En Passant : My fucking god, how are we gonna do that, some ideas :
    //  Requirement #1 : Pawn must be on y = 4 if Black and 5 if White
    //      (Considering White starts on the bottom, bottom row being 1 while top row is 8)
    //          => Brings a question : Should y = 0(1 on Chess board) be the top row or bottom row
    //              Since the board has 1 on the bottom, let's use 0(1) = bottom row
    //  Requirement #2 : There must be a Pawn next to it
    //  Requirement #3 : Pawn next to it must not have been there last turn
    //  Requirement #4 : Pawn next to it must not have moved more than once

    //A pawn can be promoted if he reaches y = 0 or 7 (1 or 8 on the Chess Board), no need to check for color
    //Available promotions : Every piece except King and Pawn (obviously)

    // Une autre idée pour en passant :
    // Un pion qui avance de 2, avance uniquement de 1 mais affiché comme ayant avancé de 2
    //      (il bouge ensuite à la place où il est affiché s'il n'a pas été mangé)
    // Le problème avec cette approche est que :
    //  1) Le pion va bloquer l'accès et passage par la case où il se situe
    //  2) Il doit pouvoir être mangé sur la case où il est affiché et bloquer aussi le passage via cette case
}
