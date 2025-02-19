package Characters.Types;

import Characters.Character;

import java.util.Random;

public class Mage extends Character {
    // Constructor
    public Mage(String name, int experience, int lvl) {
        super(name, 100, 50, 8, 10, 25, false, true, false);
        this.setExperience(experience);
        this.setLevel(lvl);
    }

    // Method to calculate and apply damage received
    @Override
    public void receiveDamage(int damage) {
        Random random = new Random();

        // Calculate chance of halving damage based on secondary attributes
        double totalAttri = getStrength() + getCharisma() + getDexterity();
        double second = Math.min((getDexterity() + getStrength()) / totalAttri, 0.5);
        if (random.nextDouble() < second) {
            damage /= 2;
            System.out.println("Damage reduced! " + getName() + " received " + damage + ".");
        }

        // Apply the damage
        setCurrentHealth(getCurrentHealth() - damage);
    }

    // Method to calculate the damage dealt
    @Override
    public int getDamage() {
        Random random = new Random();

        // Calculate base damage based on attributes
        int damage = (int) (getCharisma() * 1.5 + getStrength() * 0.3 + getDexterity() * 0.2);

        // Calculate chance of a critical hit based on primary attribute
        double totalAttri = getStrength() + getCharisma() + getDexterity();
        double primary = Math.min(getCharisma() / totalAttri, 0.7);

        if (random.nextDouble() < primary) {
            damage *= 2;
            System.out.println(getName() + " unleashed a critical hit!");
        }

        return damage;
    }

    // Method to increase the mage's attributes
    @Override
    public void increaseAttributes() {
        super.increaseAttributes();
        System.out.println(getName() + " has grown wiser and more powerful! Charisma now: " + getCharisma());
        System.out.print("Attributes improved!\n Strength: " + getStrength() + ", Dexterity: " + getDexterity());
        System.out.println(", Charisma: " + getCharisma());
    }
}
