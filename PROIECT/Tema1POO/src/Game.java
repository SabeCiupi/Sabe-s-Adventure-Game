import Characters.Enemy;
import Characters.Spell;
import GameMap.Cell;
import GameMap.CellEntityType;
import GameMap.Grid;
import GameMap.ImpossibleMoveException;
import Login.Account;
import Login.Credentials;
import Characters.Character;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private final ArrayList<Account> accounts;
    private Grid grid;
    private Character current;

    // Constructor
    public Game() {
        accounts = new ArrayList<>();
    }

    //Getters
    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    // Method to authenticate in the account
    private Account authentificate(Scanner scanner) {
        // Prompts the user to enter their email and password
        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        // Search the credentials in the json file
        for (Account account : accounts) {
            Credentials creds = account.getInformation().getCredentials();
            if (creds.getEmail().equals(email) && creds.getPassword().equals(password)) {
                System.out.println("Welcome back, little hero! Let's begin the adventure! :D\n");
                return account;
            }
        }
        System.out.println("Invalid credentials!");
        return null;
    }

    // Method for selecting the character from the account
    private Character select(Account account, Scanner scanner) {
        ArrayList<Character> characters = account.getCharacters();
        int i;

        // Display the list of available characters
        for (i = 0; i < characters.size(); i++) {
            System.out.println(i + ": " + characters.get(i).toString());
        }

        // Choose one by entering its number
        int choice = -1;
        while (choice < 0 || choice >= characters.size()) {
            System.out.println("Enter your hero number: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        return  characters.get(choice);
    }

    // Method of the game's logic
    public void run() {
        Scanner scanner = new Scanner(System.in);
        int cntLvl = 1;

        // Authentication using a loop until the credentials are correct
        System.out.println("Login");
        Account account = null;
        while (account == null) {
            account = authentificate(scanner);
        }
        System.out.println(account + "\n");

        while (true) {
            System.out.println("Choose your hero! :D");
            current = select(account, scanner);

            System.out.println("Generating game map...");
            grid = Grid.harcodedMap(current);

            // Game logic loop
            boolean gameRunning = true;
            while (gameRunning) {
                System.out.println("\nGame Level: " + cntLvl + "\nCurrent Grid:");

                grid.displayGrid();

                // Getting the current cell
                Cell currentCell = grid.getPlayerCell();

                // Choosing a direction(w/a/s/d), info(i), changing character(p) or exiting the game(m)
                System.out.println("Choose your move :)");
                String move = scanner.nextLine().toLowerCase();

                    try {
                        Cell target = null;
                        switch (move) {
                            case "w" -> target = grid.goNorth();
                            case "s" -> target = grid.goSouth();
                            case "a" -> target = grid.goWest();
                            case "d" -> target = grid.goEast();
                            case "i" -> {
                                System.out.println("Character Stats: " + current);
                                continue;
                            }
                            case "p" -> {
                                System.out.println("Exiting to select a new character...");
                                gameRunning = false;
                            }
                            case "m" -> {
                                System.out.println("Thank you for playing! See you next time!");
                                return;
                            }
                            default -> System.out.println("Invalid direction! Use w/s/a/d.");
                        }

                        // Verifying the type of the cell to handle the action correctly
                        if (target != null) {
                            switch (target.getType()) {
                                case ENEMY -> {
                                    System.out.println("Oh my! An enemy appeared! Time to show your courage! >:3");
                                    boolean battleWon = battle(target);

                                    // Verify if the game is lost to reset it; otherwise regenerate health and mana
                                    if (!battleWon) {
                                        System.out.println("Game Over. You’ll win next time, little hero!\n");
                                        gameRunning = false;
                                        cntLvl = 1;
                                        resetCharacter(current);
                                    } else {
                                        current.regenerateHealth(Math.min(current.getCurrentHealth()*2, current.getMaxHealth()));
                                        current.regenerateMana(current.getMaxMana() - current.getCurrentMana());
                                        current.levelUp();
                                    }
                                }
                                case SANCTUARY -> {
                                    System.out.println("You’ve discovered a cute little sanctuary! Recharge your energy, little hero! ^^");
                                    current.regenerateHealth((int) (Math.random() * current.getMaxHealth()));
                                    current.regenerateMana((int) (Math.random() * current.getMaxMana()));
                                    currentCell.setType(CellEntityType.VOID);
                                    int xp = current.getExperience();
                                    current.setExperience(xp + 5);
                                    current.levelUp();
                                }
                                case FPORTAL -> {
                                    System.out.println("You found a portal! Let’s jump in and explore the next level! :3");
                                    account.incGamesPlayed();
                                    int xp = current.getExperience();
                                    current.setExperience(xp + cntLvl * 5);
                                    current.levelUp();
                                    cntLvl++;
                                    current.regenerateHealth(current.getMaxHealth());
                                    current.regenerateMana(current.getMaxMana());
                                    grid = Grid.generateGameMap(current);
                                    grid.resetVisited();
                                    continue;
                                }
                                case VOID ->
                                    System.out.println("You’re in a safe zone! Let’s pick your next step!");
                            }

                            if (gameRunning) {
                                grid.finalizeMove(target);
                            }
                        }
                    } catch (ImpossibleMoveException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }

    // Method to simulate the battle
    private boolean battle(Cell enemyCell) {
        // Creates a new enemy with random stats and a list of abilities
        Enemy enemy = new Enemy();
        enemy.setAbilities(enemy.generateRandomAbilities());

        // Creates a random list of abilities for the hero
        current.setAbilities(current.generateRandomAbilities());

        System.out.println("Battle starts! Enemy stats: " + enemy);

        // The battle ends when either the main character or the enemy has no health left
        while (current.getCurrentHealth() > 0 && enemy.getCurrentHealth() > 0) {
            // Chosing if the player wants to attack or use an ability
            System.out.println("Your move: Attack(F) or Ability(E)");
            Scanner scanner = new Scanner(System.in);
            String action = scanner.nextLine().toLowerCase();
            System.out.println();

            switch (action) {
                case "f" -> {
                    enemy.receiveDamage(current.getDamage());
                }
                case "e" -> {
                    if (current.getAbilities() != null && !current.getAbilities().isEmpty()) {
                        // Display the abilities
                        System.out.println("Choose an ability:");
                        for(int i = 0; i < current.getAbilities().size(); i++) {
                            Spell spell = current.getAbilities().get(i);
                            System.out.println(i + ": " + spell);
                        }

                        // Choosing the ability
                        int choice = -1;
                        while (choice < 0 || choice >= current.getAbilities().size()) {
                            System.out.println("Enter the ability number: ");
                            try {
                                choice = Integer.parseInt(scanner.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Please enter a valid number.");
                            }
                        }

                        // Verify if the ability can be used
                        Spell selected = current.getAbilities().get(choice);
                        boolean canUse = current.useAbility(selected, enemy);

                        // Apply the damage
                        if (canUse) {
                            current.setCurrentMana(current.getCurrentMana() - selected.getManaCost());
                            int abilityDamage = selected.getDamage();
                            int normalDamage = current.getDamage();
                            int totalDamage = abilityDamage + normalDamage;
                            enemy.receiveDamage(totalDamage);
                        }

                        // Removes the ability
                        current.getAbilities().remove(selected);
                    } else {
                        System.out.println("No abilities available! :(");
                    }
                }
                default -> System.out.println("Invalid action! :(");
            }

            // If the hero is alive, the enemy attacks
            if (enemy.getCurrentHealth() > 0) {
                enemy.attack(current);
            }
        }

        // If the hero survived, they will receive random xp
        if (current.getCurrentHealth() > 0) {
            int xp = calcExp(enemy.getMaxHealth(), enemy.getDamage());
            System.out.println("Enemy defeated! You did it! :D\n");
            System.out.println("You earned " + xp + " XP!");
            current.setExperience(current.getExperience() + xp);
        }
        return current.getCurrentHealth() > 0;
    }

    // Method to reset the character's experience and level
    private void resetCharacter(Character character) {
        character.setExperience(0);
        character.setLevel(1);
        character.regenerateHealth(character.getMaxHealth());
        character.regenerateMana(character.getMaxMana());
    }

    // Method to calculate xp based on enemy health and damage
    private int calcExp(int eHealth, int eDamage) {
        return (int) ((eHealth * 0.05) + (eDamage * 0.45));
    }
}
