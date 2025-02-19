package GameMap;

import Characters.Character;

import java.util.ArrayList;
import java.util.Random;

public class Grid extends ArrayList<ArrayList<Cell>> {
    private final int rows;
    private final int cols;
    private static Character player;
    private Cell playerCell;

    // Constructors
    private Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    private Grid(int rows, int cols, Character player) {
        this.rows = rows;
        this.cols = cols;
        initializeGrid();
    }

    // Getter
    public Cell getPlayerCell() {
        return playerCell;
    }

    // Method for generating a game map with random rows and columns
    public static Grid generateGameMap(Character player) {
        Random random = new Random();
        int rows = 4 + random.nextInt(7);
        int cols = 4 + random.nextInt(7);

        return new Grid(rows, cols, player);
    }

    // Method to initialize the game map (60% with a random number of enemies and sanctuaries)
    private void initializeGrid() {
        Random random = new Random();
        int totalCells = rows * cols;
        int maxOccupiedCells = (int) (totalCells * 0.6);
        int occupiedCells;
        int i, j;

        // Initialize all cells with VOID type
        for (i = 0; i < rows; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            for(j = 0; j < cols; j++) {
                row.add(new Cell(i, j, CellEntityType.VOID));

            }
            add(row);
        }

        // Place the player on the map at a random position
        int playerX = random.nextInt(rows);
        int playerY = random.nextInt(cols);
        playerCell = get(playerX).get(playerY);
        playerCell.setType(CellEntityType.PLAYER);

        // Place the minimum number of objects
        placeObject(CellEntityType.SANCTUARY, 2);
        placeObject(CellEntityType.ENEMY, 4);
        placeObject(CellEntityType.FPORTAL, 1);
        occupiedCells = 8;

        // Randomize the remaining number of enemies and sanctuaries
        int maxEnemies = random.nextInt(totalCells - occupiedCells) + 4;
        int maxSanctuaries = Math.min(maxEnemies / 2, (maxOccupiedCells - occupiedCells)/3);
        int cntSanctuaries = 2;
        int cntEnemies = 4;

        // Place the remaining entities
        while (occupiedCells < maxOccupiedCells && (cntSanctuaries < maxSanctuaries || cntEnemies < maxEnemies)) {
            if (cntSanctuaries < maxSanctuaries) {
                placeObject(CellEntityType.SANCTUARY, 1);
                cntSanctuaries++;
                occupiedCells++;
            }

            if (occupiedCells < maxOccupiedCells && cntEnemies < maxEnemies) {
                placeObject(CellEntityType.ENEMY, 1);
                cntEnemies++;
                occupiedCells++;
            }
        }
    }

    // Method to generate a hardcoded map for testing
    public static Grid harcodedMap(Character player) {
        int i, j;
        int rows = 5;
        int cols = 5;

        Grid gridCells = new Grid(rows, cols);
        gridCells.playerCell = null;

        // Initialize all cells with VOID type
        for (i = 0; i < rows; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            for(j = 0; j < cols; j++) {
                row.add(new Cell(i, j, CellEntityType.VOID));

            }
            gridCells.add(row);
        }

        // Set the player position on the first cell
        gridCells.getFirst().getFirst().setType(CellEntityType.PLAYER);
        gridCells.playerCell = gridCells.getFirst().getFirst();

        // Place sanctuaries at specific positions
        Cell sanctuary = gridCells.getFirst().get(3);
        sanctuary.setType(CellEntityType.SANCTUARY);
        sanctuary = gridCells.get(1).get(3);
        sanctuary.setType(CellEntityType.SANCTUARY);
        sanctuary = gridCells.get(2).getFirst();
        sanctuary.setType(CellEntityType.SANCTUARY);
        sanctuary = gridCells.get(4).get(3);
        sanctuary.setType(CellEntityType.SANCTUARY);

        // Place an enemy at a fixed position
        Cell enemy = gridCells.get(3).getLast();
        enemy.setType(CellEntityType.ENEMY);

        // Place a portal at a fixed position
        Cell portal = gridCells.get(4).getLast();
        portal.setType(CellEntityType.FPORTAL);

        return gridCells;
    }

    // Method to place objects randomly on the map
    private void placeObject(CellEntityType type, int count) {
        Random random = new Random();
        int cnt = 0;
        int attemps = 0;
        int maxAttemps = rows * cols * 10;

        // Attempts to place the objects within the grid
        while (cnt < count && attemps < maxAttemps) {
            int x = random.nextInt(rows);
            int y = random.nextInt(cols);
            Cell cell = get(x).get(y);
            if(cell.getType() == CellEntityType.VOID) {
                cell.setType(type);
                cnt++;
            }
            attemps++;
        }

        // Throws an exception if objects couldn't be placed
        if (cnt < count) {
            throw new IllegalStateException("Not enough space to place " + count + " entities");
        }
    }

    // Method to display the board
    public void displayGrid() {
        for (ArrayList<Cell> row : this) {
            for (Cell cell : row) {
                if (!cell.isVisited() && cell.getType() != CellEntityType.PLAYER) {
                    System.out.print("N ");
                } else {
                    System.out.print(cell.getType().name().charAt(0) + " ");
                }
            }
            System.out.println();
        }
    }

    // Method to move the player up by one row
    public Cell goNorth() throws ImpossibleMoveException {
         return movePlayer(-1, 0);
    }

    // Method to move the player down by one row
    public Cell goSouth() throws ImpossibleMoveException {
        return movePlayer(1, 0);
    }

    // Method to move the player left by one column
    public Cell goWest() throws ImpossibleMoveException {
        return movePlayer(0, -1);
    }

    // Method to move the player right by one column
    public Cell goEast() throws ImpossibleMoveException {
        return movePlayer(0, 1);
    }

    // Method to move the player on the board
    private Cell movePlayer(int x, int y) throws ImpossibleMoveException {
        int newX = playerCell.getX() + x;
        int newY = playerCell.getY() + y;

        // Checks if the new position is within bounds
        if (newX < 0 || newX >= rows || newY < 0 || newY >= cols) {
            throw new ImpossibleMoveException("Oh no, an invisible wall!\nLooks like you'll need to pick another direction.");
        }

        return get(newX).get(newY);
    }

    // Method to reset visited status for all cells
    public void resetVisited() {
        for (ArrayList<Cell> row : this) {
            for (Cell cell : row) {
                cell.setVisited(false);
            }
        }
        if (playerCell != null) {
            playerCell.setVisited(false);
        }
    }

    // Method to finalize the player's move
    public void finalizeMove(Cell target) {
        playerCell.markAsVisited();
        playerCell.setType(CellEntityType.VOID);
        playerCell = target;
        playerCell.setType(CellEntityType.PLAYER);
    }
}
