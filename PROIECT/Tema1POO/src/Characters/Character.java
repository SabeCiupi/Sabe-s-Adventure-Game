package Characters;

import java.util.Random;

public abstract class Character extends Entity{
    private final String name;
    private int level;
    private int experience;
    private int strength;
    private int dexterity;
    private int charisma;

    // Constructor
    public Character(String name, int maxHealth, int maxMana, int strength, int dexterity, int charisma,
                     boolean isImmuneToFire, boolean isImmuneToIce, boolean isImmuneToEarth) {
        super(maxHealth, maxMana, isImmuneToFire, isImmuneToIce, isImmuneToEarth);
        this.name = name;
        this.experience = 0;
        this.level = 1;

        // Attributes
        this.strength = strength;
        this.dexterity = dexterity;
        this.charisma = charisma;
    }

    public Character() {
        super(100, 50, false, false, false);
        name = "";
        level = 0;
        experience = 0;
        strength = 1;
        charisma = 1;
        dexterity = 1;
    }

    // Getters + Setters
    public String getName() {
        return name;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    // Method to increase the level
    @Override
    public void levelUp() {
        if (experience >= level * 100) {
            experience -= level * 100;
            level++;
            increaseAttributes();
            System.out.println(name + " is now level " + level + "! Great job, keep going! :D");
        }
    }

    // Method to increase the attributes
    @Override
    public void increaseAttributes() {
        int increment = level * 2;
        setCurrentHealth(getCurrentHealth() + increment);
        setCurrentMana(getCurrentMana() + increment);
        setCurrentMana(getCurrentMana() + increment);
        setStrength(getStrength() + increment);
        setDexterity(getDexterity() + increment);
        setCharisma(getCharisma() + increment);
    }

    // Method to display character stats
    @Override
    public String toString() {
        return name + "\nlevel: " + level + "\nexperience: " + experience + "\nhealth: " +
                getCurrentHealth() + "\nmana: " + getCurrentMana() + "\nstrength: " + getStrength() + "\ndexterity: " +
                getDexterity() + "\ncharisma: " + getCharisma() + "\n";
    }
}
