//storage class for the three players/buttons indexs for drawing lines

import javafx.geometry.Point2D;

public class TrioIndex {

    int [] topIndex;
    int [] bottomIndex;
    int [] middleIndex;

    public TrioIndex(int tx, int ty, int bx, int by, int mx, int my) {
        topIndex = new int[2];
        bottomIndex = new int[2];
        middleIndex = new int[2];

        topIndex[0] = tx;
        topIndex[1] = ty;

        bottomIndex[0] = bx;
        bottomIndex[1] = by;

        middleIndex[0] = mx;
        middleIndex[1] = my;
    }

    public Point2D[] getTrioP2D(TournamentView view) {
         Point2D[] tP2D = {view.getScenePos(topIndex[0],topIndex[1]),
                         view.getScenePos(bottomIndex[0],bottomIndex[1]),
                         view.getScenePos(middleIndex[0],middleIndex[1])};

         return tP2D;
    }
}
