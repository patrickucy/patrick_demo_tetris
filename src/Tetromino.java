import java.util.Arrays;
import java.util.Random;


// because you will only use its subclasses
public abstract class Tetromino {
    // you need this property to be used by subclasses
    protected Cell[] cells = new Cell[4];
    // rotation states
    protected State[] states;

    protected int index = 10000;

    protected class State {
        int row0, column0, row1, column1, row2, column2, row3, column3;

        public State(int row0, int column0, int row1, int column1, int row2,
                     int column2, int row3, int column3) {
            super();
            this.row0 = row0;
            this.column0 = column0;
            this.row1 = row1;
            this.column1 = column1;
            this.row2 = row2;
            this.column2 = column2;
            this.row3 = row3;
            this.column3 = column3;
        }

    }

    public void rotateClockwise() {
        // 1) get current axis
        // 2) get next state
        // 3) using + to implement data transformation
        Cell o = cells[0];
        int row = o.getRow();
        int column = o.getColumn();
        index++;
        State s = states[index % states.length];
        cells[1].setRow(row + s.row1);
        cells[1].setColumn(column + s.column1);
        cells[2].setRow(row + s.row2);
        cells[2].setColumn(column + s.column2);
        cells[3].setRow(row + s.row3);
        cells[3].setColumn(column + s.column3);
    }

    public void rotateAntiClockwise() {

        Cell o = cells[0];
        int row = o.getRow();
        int column = o.getColumn();
        index--; // go to previous state, then
        State s = states[index % states.length];
        cells[1].setRow(row + s.row1);
        cells[1].setColumn(column + s.column1);
        cells[2].setRow(row + s.row2);
        cells[2].setColumn(column + s.column2);
        cells[3].setRow(row + s.row3);
        cells[3].setColumn(column + s.column3);
    }


    /**
     * here you define this constructor as private
     * the object will never call this method
     * in other word, you can only call your initialization by calling "randomTetromino"
     */
    private Tetromino() {

    }


    /**
     * Tetromino is the shape, it consists cells
     * the methods below are just telling every cells to move as a whole structure
     */
    public void softDrop() {
        for (int i = 0; i < cells.length; i++) {
            cells[i].drop();
        }
    }

    public void hardDrop() {
        for (int i = 0; i < cells.length; i++) {
            cells[i].hardDrop();
        }
    }

    public void moveUp() {
        for (int i = 0; i < cells.length; i++) {
            cells[i].moveUp();
        }
    }

    public void moveUpHard() {
        for (int i = 0; i < cells.length; i++) {
            cells[i].moveUpHard();
        }
    }

    public void moveLeft() {
        for (int i = 0; i < cells.length; i++) {
            cells[i].moveLeft();
        }
    }

    public void moveRight() {
        for (int i = 0; i < cells.length; i++) {
            cells[i].moveRight();
        }
    }

    /**
     * using this factory method to create a Tetromino instance
     * in other words,
     */
    public static Tetromino randomOne() {
        Random r = new Random();
        int type = r.nextInt(7);
        switch (type) {
            // there is a compile error here
            // because static method can never use "this" pointer
            // hoverever, you sitll wanna use it like this
            // then you have to declare your method as "private static class"
            case 0:
                return new T();
            case 1:
                return new I();
            case 2:
                return new S();
            case 3:
                return new O();
            case 4:
                return new L();
            case 5:
                return new J();
            case 6:
                return new Z();

        }
        return null;
    }

    // using static class to make them as part of the "Tetromino" class -> fullly encapsulation
    private static class T extends Tetromino { // full encapsulation
        public T() {
            // cell[0] is always the rotation center
            cells[0] = new Cell(0, 4, Tetris.T);
            cells[1] = new Cell(0, 3, Tetris.T);
            cells[2] = new Cell(0, 5, Tetris.T);
            cells[3] = new Cell(1, 4, Tetris.T);
            states = new State[4];
            states[0] = new State(0, 0, 0, -1, 0, 1, 1, 0);
            states[1] = new State(0, 0, -1, 0, 1, 0, 0, -1);
            states[2] = new State(0, 0, 0, 1, 0, -1, -1, 0);
            states[3] = new State(0, 0, 1, 0, -1, 0, 0, 1);
        }
    }

    private static class I extends Tetromino { // full encapsulation
        public I() {
            cells[0] = new Cell(0, 4, Tetris.I);
            cells[1] = new Cell(0, 3, Tetris.I);
            cells[2] = new Cell(0, 5, Tetris.I);
            cells[3] = new Cell(0, 6, Tetris.I);
            states = new State[]{
                    new State(0, 0, 0, 1, 0, -1, 0, -2),
                    new State(0, 0, -1, 0, 1, 0, 2, 0)
            };
        }
    }

    private static class S extends Tetromino { // full encapsulation
        public S() {
            cells[0] = new Cell(0, 4, Tetris.S);
            cells[1] = new Cell(0, 3, Tetris.S);
            cells[2] = new Cell(1, 4, Tetris.S);
            cells[3] = new Cell(1, 5, Tetris.S);
            states = new State[]{
                    new State(0, 0, 0, 1, 1, -1, 1, 0),
                    new State(0, 0, -1, 0, 1, 1, 0, 1)
            };
        }
    }

    private static class O extends Tetromino { // full encapsulation
        public O() {
            cells[0] = new Cell(0, 4, Tetris.O);
            cells[1] = new Cell(0, 5, Tetris.O);
            cells[2] = new Cell(1, 4, Tetris.O);
            cells[3] = new Cell(1, 5, Tetris.O);
            states = new State[]{
                    new State(0, 0, 0, 1, 1, 0, 1, 1),
                    new State(0, 0, 0, 1, 1, 0, 1, 1)
            };
        }
    }

    private static class L extends Tetromino { // full encapsulation
        public L() {
            cells[0] = new Cell(0, 4, Tetris.L);
            cells[1] = new Cell(0, 3, Tetris.L);
            cells[2] = new Cell(0, 5, Tetris.L);
            cells[3] = new Cell(1, 3, Tetris.L);
            states = new State[]{
                    new State(0, 0, 0, -1, 0, 1, 1, -1),
                    new State(0, 0, -1, 0, 1, 0, -1, -1),
                    new State(0, 0, 0, 1, 0, -1, -1, 1),
                    new State(0, 0, 1, 0, -1, 0, 1, 1),

            };
        }
    }

    private static class J extends Tetromino { // full encapsulation
        public J() {
            cells[0] = new Cell(0, 4, Tetris.J);
            cells[1] = new Cell(0, 3, Tetris.J);
            cells[2] = new Cell(0, 5, Tetris.J);
            cells[3] = new Cell(1, 5, Tetris.J);
            states = new State[]{
                    new State(0, 0, 0, -1, 0, 1, 1, 1),
                    new State(0, 0, -1, 0, 1, 0, 1, -1),
                    new State(0, 0, 0, 1, 0, -1, -1, -1),
                    new State(0, 0, 1, 0, -1, 0, -1, 1),

            };
        }
    }

    private static class Z extends Tetromino { // full encapsulation
        public Z() {
            cells[0] = new Cell(1, 4, Tetris.Z);
            cells[1] = new Cell(0, 3, Tetris.Z);
            cells[2] = new Cell(0, 4, Tetris.Z);
            cells[3] = new Cell(1, 5, Tetris.Z);
            states = new State[]{
                    new State(0, 0, -1, -1, -1, 0, 0, 1),
                    new State(0, 0, -1, 1, 0, 1, 1, 0)
            };
        }
    }

    @Override
    public String toString() {
        return "Tetromino [cells=" + Arrays.toString(cells) + "]";
    }


}
