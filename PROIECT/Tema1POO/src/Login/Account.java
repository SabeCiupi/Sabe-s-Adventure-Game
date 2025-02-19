package Login;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import Characters.Character;

public class Account {
    private final Information information;
    private final ArrayList<Character> characters;
    private int gamesPlayed;

    // Constructor
    public Account(ArrayList<Character> characters, int gamesPlayed, Information information) {
        this.information = information;
        this.characters = characters;
        this.gamesPlayed = 0;
    }

    // Method to add a character -> future feature
    public void addCharacter(Character character) {
        characters.add(character);
    }

    // Method to increase the number of games played
    public void incGamesPlayed() {
        gamesPlayed++;
    }

    // Getters
    public Information getInformation() {
        return information;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    // Method to display info account
    @Override
    public String toString() {
        return "Account: " + information + "\nGames Played: " + gamesPlayed + "\nCharacters: " + characters.size();
    }

    // Inner class to store confidential information
    public static class Information {
        private final Credentials credentials;
        private final TreeSet<String> favGames;
        private final String name;
        private final String country;

        // Constructor
        public Information(Credentials credentials, SortedSet<String> favGames, String name, String country) {
            this.credentials = credentials;
            this.favGames = new TreeSet<>();
            this.name = name;
            this.country = country;
        }

        // Getters
        public Credentials getCredentials() {
            return credentials;
        }

        public TreeSet<String> getFavGames() {
            return favGames;
        }

        // Method to add a favourite game for the player -> future feature
        public void addFavGame(String game) {
            favGames.add(game);
        }

        // Method to display the person's information
        @Override
        public String toString() {
            return "Name: " + name + "\nCountry: " + country + "\nFavorites: " + favGames;
        }

    }
}
