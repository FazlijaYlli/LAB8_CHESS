package chess;

public class Controller implements ChessController{
    @Override
    public void start(ChessView view) {
        view.startView();
    }

    @Override
    public boolean move(int fromX, int fromY, int toX, int toY) {
        return false;
    }

    @Override
    public void newGame() {

    }
}
