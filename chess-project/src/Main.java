import chess.ChessController;
import chess.Controller;
import chess.ChessView;
import chess.views.console.ConsoleView;
import chess.views.gui.GUIView;

public class Main {
    public static void main(String[] args) {
        ChessController c = new Controller();
        ChessView v = new GUIView(c);
        //ChessView v = new ConsoleView(c);
        c.start(v);
    }
}