package chess;

import chess.engine.Move;
import chess.engine.MoveType;
import chess.engine.Position;
import chess.engine.pieces.*;

import java.util.ArrayList;
import java.lang.Math;

public class Controller implements ChessController {

    Piece[][] board;
    int[][] attackBoard;
    ChessView view;

    private PlayerColor playerTurn = PlayerColor.WHITE;


    @Override
    public void start(ChessView view) {
        this.view = view;
        view.startView();
    }

    @Override
    public boolean move(int fromX, int fromY, int toX, int toY) {

        // Si emplacement from est vide
        if (board[fromY][fromX] == null)
            return false;

        // Si ce n'est pas le tour de ce joueur
        if (board[fromY][fromX].getColor() != playerTurn)
            return false;

        // Movements spéciaux
        if(board[fromY][fromX] instanceof SpecialMovePiece){
            if (board[fromY][fromX].getType() == PieceType.KING){
                if(castling(fromX,fromY,toX,toY))
                    return true;
            }

        }

        // Si on déplace la pièce sur elle même
        if (toX == fromX && toY == fromY)
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

            int x = fromX + directionX, y = fromY + directionY;
            while (!(x == toX && y == toY)){
                if (board[y][x] != null)
                    return false;
                x += directionX;
                y += directionY;
            }
        }

        // déplacement de la pièce
        view.removePiece(toX, toY);
        view.putPiece(board[fromY][fromX].getType(), playerTurn, toX, toY);
        view.removePiece(fromX, fromY);
        board[toY][toX] = board[fromY][fromX];
        board[fromY][fromX] = null;

        playerTurn = getOpponent();
        return true;

    }

    PlayerColor getOpponent(){
        return playerTurn == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
    }
    public boolean isCellAttacked(PlayerColor by, Position cell) {

        for (int line = 0; line < 8; ++line) {
            for (int column = 0; column < 8; ++column) {
                if (board[line][column] != null && board[line][column].getColor() == by &&
                        board[line][column].canAttack(cell.getY() - line, cell.getX() - column)) {
                    return true;
                }
            }
        }

        return false;
    }

    boolean castling(int fromX,int fromY, int toX, int toY){

        if (toY != fromY)
            return false;

        if (!(toX == 6 || toX == 1))
            return false;

        boolean bigCastle = toX == 1;
        int rookX = bigCastle ? 0 : 7;

        //check if pieces are castlelable and has already moved
        if (!(board[fromY][fromX] instanceof King
            && board[fromY][rookX] instanceof Rook)){
            if (((SpecialMovePiece) board[fromY][fromX]).getHasMoved()
                || ((SpecialMovePiece) board[fromY][rookX]).getHasMoved()){
                return false;
            }
            return false;
        }

        for (int x = fromX + (bigCastle ? -1 : 1); x != toX; x += bigCastle ? -1 : 1){
            if (!(board[fromY][x] == null && !isCellAttacked(getOpponent(),new Position(x,fromY))))
                return false;
        }

        // moving pieces
        //king
        view.putPiece(PieceType.KING, playerTurn, toX, toY);
        view.removePiece(fromX, fromY);

        //rook
        view.putPiece(PieceType.ROOK, playerTurn, bigCastle ? 2 : 5, toY);
        view.removePiece((bigCastle ? 0 : 7), fromY);

        //king
        board[toY][toX] = board[fromY][fromX];
        board[fromY][fromX] = null;

        //rook
        board[toY][bigCastle ? 2 : 5] = board[fromY][bigCastle ? 0 : 7];
        board[fromY][bigCastle ? 0 : 7] = null;

        playerTurn = getOpponent();
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

        // Setup attacks board
        for (int line = 0; line < 8; ++line) {
            for (int column = 0; column < 8; ++column) {
                Piece p = board[line][column];
                if (p != null) {

                }
            }
        }
    }
}
