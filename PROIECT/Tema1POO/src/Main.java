
import Login.Account;

import java.util.ArrayList;

public class Main{
    public static void main(String[] args) {
        Game game = new Game();

        try {
            ArrayList<Account> accounts = JsonInput.deserializeAccounts();
            game.getAccounts().addAll(accounts);
            game.run();
        } catch (Exception e) {
            System.out.println("Error initializing the game: " + e.getMessage());
        }
    }
}