package chess;

import chess.engine.Move;
import chess.engine.MoveType;
import chess.engine.Position;
import chess.engine.pieces.*;

import java.util.ArrayList;
import java.lang.Math;

public class Controller implements ChessController {

    Piece[][] board;
    ChessView view;

    @Override
    public void start(ChessView view) {
        this.view = view;
        view.startView();
    }

    @Override
    public boolean move(int fromX, int fromY, int toX, int toY) {

        if (toX == fromX && toY == fromY)
            return false;

        // Si emplacement fro est vide
        if (board[fromY][fromX] == null)
            return false;

        int relativeX = toX - fromX;
        int relativeY = toY - fromY;

        // Si emplacement to est vide
        if (board[toY][toX] == null){
            if(!board[fromY][fromX].canMove(relativeX,relativeY))
                return false;
        }
        // Si emplacement to est occupé
        else {
            // Si est pièce de même couleur
            if(board[fromY][fromX].getColor() == board[toY][toX].getColor())
                return false;
            // si ne peut pas se deplacer sur cette case
            if(!board[fromY][fromX].canAttack(relativeX,relativeY))
                return false;
        }

        // si la pièce doit vérifer les collision ou non
        if (board[fromY][fromX].isCollisionable()){

            int directionX = relativeX == 0 ? 0: relativeX / Math.abs(relativeX);
            int directionY = relativeY == 0 ? 0: relativeY / Math.abs(relativeY);

            int x = fromX, y = fromY;
            while (!(x == toX && y == toY)){
                x += directionX;
                y += directionY;
                if (board[y][x] != null)
                    return false;
            }
        }

        // déplacement de la pièce
        view.removePiece(toX, toY);
        view.putPiece(board[fromY][fromX].getType(), board[fromY][fromX].getColor(), toX, toY);
        view.removePiece(fromX, fromY);
        board[toY][toX] = board[fromY][fromX];
        board[fromY][fromX] = null;
        return true;

    }

    @Override
    public void newGame() {
        board = new Piece[8][8];

        // Back Pieces

        for (int i = 0; i < 4; ++i) {
            PlayerColor currentColor = i % 2 == 0 ? PlayerColor.WHITE : PlayerColor.BLACK;
            board[7 * (i % 2)][7 * (i > 1 ? 1 : 0)] = new Rook(currentColor);
            board[7 * (i % 2)][1 + 5 * (i > 1 ? 1 : 0)] = new Knight(currentColor);
            board[7 * (i % 2)][2 + 3 * (i > 1 ? 1 : 0)] = new Bishop(currentColor);
            if (i > 1) {
                board[7 * (i % 2)][3] = new Queen(currentColor);
            }
            else {
                board[7 * (i % 2)][4] = new King(currentColor);
            }
        }

        // Pawns

        for (int i = 0; i < 16; ++i) {
            PlayerColor currentColor = i % 2 == 0 ? PlayerColor.WHITE : PlayerColor.BLACK;
            board[1 + 5 * (i % 2)][i % 8 - (i % 2) + i / 8] = new Pawn(currentColor);
        }

        // Put pieces on view to be visible

        for (int line = 0; line < 8; ++line) {
            for (int column = 0; column < 8; ++column) {
                if (board[line][column] != null) {
                    view.putPiece(board[line][column].getType(), board[line][column].getColor(), column, line);
                }
            }
        }
    }
}
