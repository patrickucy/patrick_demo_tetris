import java.awt.image.BufferedImage;

public class Cell {
    private int row;
    private int column;
    private BufferedImage image;  // when you don't know what to import, you hover cursor on those red spots

    public Cell(int row, int column, BufferedImage image) {
        super();
        this.row = row;
        this.column = column;
        this.image = image;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return row + "," + column;
    }


    public void drop() {
        this.row++;
    }

    public void hardDrop() {
        this.row += 2;
    }

    public void moveUpHard() {
        this.row -= 2;
    }

    public void moveUp() {
        this.row--;
    }

    public void moveLeft() {
        this.column--;
    }

    public void moveRight() {
        this.column++;
    }
}
