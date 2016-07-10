import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


/**
 * this is the game class
 */
public class Tetris extends JPanel {
    // all these properties are components of Tetris
    private int score;
    private int lines;
    public static final int ROWS = 20;
    public static final int COLUMNS = 10;

    private Cell[][] wall = new Cell[ROWS][COLUMNS];


    // However all these properties are depended on Tetris
    // and this is why these properties don't need to initialize right now
    // current Tetromino
    private Tetromino tetromino;
    private Tetromino nextOne;


    /**
     * propertyies for game itself
     */
    private boolean pause;
    private boolean finish;
    private Timer timer;

    //images materials
    // you just have one background
    public static BufferedImage background;
    public static BufferedImage gameOver;
    public static BufferedImage I;
    public static BufferedImage T;
    public static BufferedImage S;
    public static BufferedImage J;
    public static BufferedImage L;
    public static BufferedImage O;
    public static BufferedImage Z;

    static { // use static block to load static images resources

        // exception handling
        try {
            // since you don't have image resources, this is the best you can do
            // its meaning is reading an image file from current package, and its name is "tetris.png"
            // all these images files must be put into the same package
            background = ImageIO.read(Tetris.class.getResource("tetris.png"));
//			gameOver = ImageIO.read(Tetris.class.getResource("game-over.png"));
            I = ImageIO.read(Tetris.class.getResource("I.png"));
            T = ImageIO.read(Tetris.class.getResource("T.png"));
            S = ImageIO.read(Tetris.class.getResource("S.png"));
            J = ImageIO.read(Tetris.class.getResource("J.png"));
            L = ImageIO.read(Tetris.class.getResource("L.png"));
            O = ImageIO.read(Tetris.class.getResource("O.png"));
            Z = ImageIO.read(Tetris.class.getResource("Z.png"));
            gameOver = ImageIO.read(Tetris.class.getResource("gameOver.png"));

//			...

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * paint the content of a 2-D array at the panel
     * if no cell in rect then we draw a black grid,
     * have cell we paint the exact cell at that spot
     */
    private void paintWall(Graphics g) {


        /** Approach #0*/
//		for (int row = 0; row < ROWS; row++) {
//			for (int column = 0; column < COLUMNS; column++) {
//				Cell cell = wall[row][column];
//				int x = column*CELL_SIZE + GRID_X_OFFSET;
//				int y = row*CELL_SIZE + GRID_Y_OFFSET;
//				if (cell == null){ // if we get nothing we draw an empty frame
//					g.setColor(new Color(Color.BLACK.hashCode()));
//					g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
//				}else{ // if we have rect object , we add an image
//					g.drawImage(cell.getImage(), x+CELL_X_OFFSET,y+CELL_Y_OFFSET,null);
//					// adding this line of code making the appearance more beautiful
//					g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
//				}
//			}
//		}


        /** Approach #1*/
        // because in java 2-D array is in fact a fake, it fundamentally is a 1-D array
        for (int row = 0; row < wall.length; row++) { // the length of row!
            Cell[] line = wall[row];
            for (int column = 0; column < line.length; column++) {
                Cell cell = line[column];
                int x = column * CELL_SIZE;
                int y = row * CELL_SIZE;
                if (cell == null) {
                    g.setColor(Color.black);
                    // grid visibality
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                } else {
                    g.drawImage(cell.getImage(), x + CELL_X_OFFSET, y + CELL_Y_OFFSET, null);
                    // adding this line of code making the appearance more beautiful
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    private void paintTetromino(Graphics g) {
        if (this.tetromino == null) {
            return;
        }

        Cell[] cells = this.tetromino.cells;
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            int x = CELL_SIZE * cell.getColumn();
            int y = CELL_SIZE * cell.getRow();
            g.drawImage(cell.getImage(), x + CELL_X_OFFSET, y + CELL_Y_OFFSET, null);
            g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
        }
    }


    private static final int NEXT_ONE_X_OFFSET = 280;
    private static final int NEXT_ONE_Y_OFFSET = 30;

    private void paintNextOne(Graphics g) {
        if (this.nextOne == null) {
            return;
        }
        // nextOne Image OFFSET
        g.translate(NEXT_ONE_X_OFFSET, NEXT_ONE_Y_OFFSET);
        Cell[] cells = this.nextOne.cells;
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            int x = CELL_SIZE * cell.getColumn();
            int y = CELL_SIZE * cell.getRow();
            g.drawImage(cell.getImage(), x + CELL_X_OFFSET, y + CELL_Y_OFFSET, null);
            g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
        }
    }

    private static final int FONT_SIZE = 30;
    private static final int FONT_COLOR = 0x667799;

    private void paintScore(Graphics g) {
        g.translate(5, 135);
        g.setColor(new Color(FONT_COLOR));
        Font font = this.getFont();
        font = new Font(font.getName(), Font.BOLD, FONT_SIZE);
        g.setFont(font);
        g.drawString("Score: " + this.score, 0, 0);
        g.translate(0, 56);
        g.drawString("Lines: " + this.lines, 0, 0);
        String str = "[P]Pause";
        if (pause) {
            str = "[C]Continue";
        }
        if (finish) {
            str = "[S]Restart";
        }
        g.translate(0, 56);
        g.drawString(str, 0, 0);
    }

    private static final int CELL_SIZE = 26;
    private static final int GRID_X_OFFSET = 22;
    private static final int GRID_Y_OFFSET = 10;
    private static final int CELL_X_OFFSET = 1;
    private static final int CELL_Y_OFFSET = 1;

    /**
     * if you wanna paint something, you have to override paint method in Tetris(JPanel) class
     */
    @Override
    public void paint(Graphics g) {
        // when you override paint(), the yellow background will be gone
        // think of "g" as a paint brush trying to paint something in a rectangle area in a panel
        g.drawImage(Tetris.background, 0, 0, null);

        // we can offset the coordinate system of g(paint brush)
        // of course you can mannually alter your x, y specifically in Approach #1
        g.translate(GRID_X_OFFSET, GRID_Y_OFFSET);

        this.paintWall(g);
        this.paintTetromino(g);
        this.paintNextOne(g);
        this.paintScore(g);
        g.translate(-300, -280);
        if (finish) {
            g.drawImage(gameOver, 0, 0, null);
        }
    }


    /**
     * Tetris class, add  "action()" to open your software
     */
    public void action() {
        startAction();


        /** HERE you can use "KeyAdapter", it's the subclass of "KeyListener",
         *  however, if you use "KeyAdapter" You will just need to override "keyPress()"
         *  method instead of overriding the three following methods
         *
         */
        KeyListener l = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
//				int key = e.getKeyCode();
//				System.out.println(key); // i don't really know why it always shows "0"
            }

            @Override
            public void keyReleased(KeyEvent e) {
//				int key = e.getKeyCode();
//				System.out.println(key);			
            }

            @Override
            public void keyPressed(KeyEvent e) {
//				long when = e.getWhen(); // in this project, we don't need when, here is just a demo of how powerful KeyEvent is
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_Q) {
                    System.exit(0); // kill java process
                }
                if (finish) {
                    if (key == KeyEvent.VK_S) {
                        startAction();
                        repaint();
                    }
                    return;
                }
                if (pause) {
                    if (key == KeyEvent.VK_C) {
                        continueAction();
                        repaint();
                    }
                    return;
                }

                switch (key) {
                    // ATTENTION HERE ! you can't use "this" pointer here
                    // the reason is that you are currently in KeyListener's internal anonymous subclass !! not class "Tetris"
                    case KeyEvent.VK_DOWN:
                        softDropAction();
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveRightAction();
                        break;
                    case KeyEvent.VK_LEFT:
                        moveLeftAction();
                        break;
                    case KeyEvent.VK_SPACE:
                        hardDropAction();
                        break;
                    case KeyEvent.VK_UP:
                        rotateClockwiseAction();
                        break;
                    case KeyEvent.VK_R:
                        rotateAntiClockwiseAction();
                        break;
                    case KeyEvent.VK_P:
                        pauseAction();
                        ;
                        break;


                }
                // ATTENTION HERE ! you can't use "this" pointer here
                // the reason is that you are currently in KeyListener's internal anonymous subclass !! not class "Tetris"
                repaint();
                // here it means call "paint()" as soon as possible, because after you alter your data, you have to update UI
            }


        };
        this.addKeyListener(l); // binding current panel with keyboard key events
        this.requestFocus(); // the same method as OC "setFirstRespond", ask the system to focus at current JPanel

    }


    /**
     * cheack current dropping tetromino if is overlapping with existing cells at the bottom
     */
    private boolean isCoincide() {
        Cell[] cells = tetromino.cells;
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            int row = cell.getRow();
            int column = cell.getColumn();
            if (row >= 0
                    && row < ROWS
                    && column >= 0
                    && column < COLUMNS
                    && wall[row][column] != null) {
                return true;
            }
        }
        return false;
    }

    private boolean isOutOfBounds() {
        Cell[] cells = this.tetromino.cells;
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            int column = cell.getColumn();
            int row = cell.getRow();
            if (column < 0 || column >= COLUMNS || row >= ROWS) {
                return true;
            }
        }
        return false;
    }

    /**
     * we add moveRight() method in Tetris class
     */
    public void moveRightAction() {
        this.tetromino.moveRight();
        if (this.isOutOfBounds() || this.isCoincide()) {
            this.tetromino.moveLeft();
            // why do you have to have this ? because sometimes, if you will be out of bounds after rotation
        }
    }

    public void moveLeftAction() {
        this.tetromino.moveLeft();
        if (this.isOutOfBounds() || this.isCoincide()) {
            this.tetromino.moveRight();
        }
    }


    public void hardDropAction() {
        softDropAction();
    }


    /*
     * there is a lot going in this method
     */
    public void softDropAction() {
        if (canDrop()) {
            tetromino.softDrop();
        } else {
            landToWall();
            destroyLines();
            checkGameOver();
            tetromino = nextOne;
            nextOne = Tetromino.randomOne();
        }
    }


    private boolean canDrop() {
        for (Cell cell : tetromino.cells) {
            if (cell.getRow() == ROWS - 1) {
                return false;
            }
        }

        for (Cell cell : tetromino.cells) {// augmented version for loop
            int row = cell.getRow();
            int column = cell.getColumn();
            if (row >= 0
                    && row < ROWS
                    && column >= 0
                    && column < COLUMNS
                    && wall[cell.getRow() + 1][cell.getColumn()] != null) {
                return false;
            }
        }
        return true;
    }


    public static final int[] SCORE_TABLE = {0, 1, 10, 50, 100};

    private void destroyLines() {

        int lines = 0;

        // this is not a simple destruction
        // you need to move the whole cells down by 1 cell
        for (int row = 0; row < wall.length; row++) {
            if (isFullCells(row)) {
                removeLine(row);
                lines++;
            }
        }
        this.lines += lines;
        this.score += SCORE_TABLE[lines];
    }

    private void removeLine(int row) {
        for (int i = row; i >= 1; i--) {
            // cp[i-1] ->[i]
            //copy of -> creating new array
            // array copy -> copy from original copy and delete the original one
            System.arraycopy(wall[i - 1], 0, wall[i], 0, COLUMNS);
        }
        Arrays.fill(wall[0], null);
    }

    private boolean isFullCells(int row) {
        Cell[] line = wall[row];
        for (Cell cell : line) {
            if (cell == null) {
                return false;
            }
        }
        return true;
    }

    private void landToWall() {
        // tetromino need to blend into the wall
        for (Cell cell : tetromino.cells) {// augmented version for loop
            wall[cell.getRow()][cell.getColumn()] = cell;
        }
    }


    /**
     * rotation actions
     */
    public void rotateClockwiseAction() {
        tetromino.rotateClockwise();
        if (isOutOfBounds() || isCoincide()) {
            tetromino.rotateAntiClockwise();
        }
    }

    public void rotateAntiClockwiseAction() {
        tetromino.rotateAntiClockwise();
        if (isOutOfBounds() || isCoincide()) {
            tetromino.rotateClockwise();
        }
    }

    /**
     * we add 2 more properties, a timer instance,
     * Tetris class
     */
    private void clearWall() {
        for (Cell[] line : wall) {
            Arrays.fill(line, null);
        }
    }


    public void startAction() {
        clearWall();
        pause = false;
        finish = false;
        tetromino = Tetromino.randomOne();
        nextOne = Tetromino.randomOne();
        lines = 0;
        score = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                softDropAction();
                repaint();
            }

        }, 700, 700);
    }


    public void pauseAction() {
        pause = true;
        timer.cancel(); // stop any task on such timer

    }


    public void continueAction() {
        pause = false;
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                softDropAction();
                repaint();
            }

        }, 700, 700);
    }


    public void checkGameOver() {
        if (wall[0][4] != null) {
            finish = true;
            timer.cancel();
        }
    }


    /**
     * we use main function to initialize our UI
     */
    public static void main(String[] args) {
        System.out.println("Game loaded!");

        // setting up the scene

        JFrame frame = new JFrame("Tetris by Changyou Yu");

        // because the tile bar has a height of 20 points
        frame.setSize(Tetris.background.getWidth(), Tetris.background.getHeight() + 20);
        frame.setLocationRelativeTo(null); // let our window to stay at the center of screen
//		frame.setUndecorated(true); // get rid of window frame
        frame.setResizable(false);


        // tetris inherited form JPanel
        Tetris director = new Tetris();
        director.setBackground(Color.YELLOW); // (0xffff00)in my point of view hashCode() is just the ID
        frame.add(director); // add Tetris(JPanel to current window

        // when close your window, it terminates the java program at the same time
        // in Mac OS, you might have the feeling how it works and the subtlety behind this setting
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);// this method will call paint() as soon as possible, there will be latency


        // the real game begins !
        director.action();
    }


}
