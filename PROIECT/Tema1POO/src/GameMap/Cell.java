package GameMap;

public class Cell {
    private final int x, y;
    private CellEntityType type;
    private boolean visited;

    // Constructor
    public Cell(int x, int y, CellEntityType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.visited = false;
    }

    //Getters + Setters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CellEntityType getType() {
        return type;
    }

    public void setType(CellEntityType type) {
        this.type = type;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void markAsVisited() {
        visited = true;
    }
}
