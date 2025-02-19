package Characters;

import java.util.*;
import Characters.Spells.*;

public class Enemy extends Entity {
    private final int damage;
    // Constructor
    public Enemy() {
        super(randomValue(20, 300), randomValue(30, 100),  randomBoolean(), randomBoolean(), randomBoolean());
        damage = randomValue(10,30);
        List<Spell> abilities = generateRandomAbilities();
        setAbilities(abilities);
    }

    // Method to generate a random boolean value
    private static boolean randomBoolean() {
        return new Random().nextBoolean();
    }

    // Method to generate a random value between min and max
    private static int randomValue(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    // Method to calculate and apply damage received by the enemy
    @Override
    public void receiveDamage(int damage) {
        Random random = new Random();

        // 50% chance for the enemy to dodge the attack
        if (random.nextDouble() < 0.5) {
            System.out.println("The enemy dodged the attack, better luck next time!");
        } else {
            setCurrentHealth(getCurrentHealth() - damage);
            System.out.println("Enemy received " + damage + " damage. Current health: " + getCurrentHealth());
        }
    }

    // Method to calculate the damage dealt by the enemy
    @Override
    public int getDamage() {
        Random random = new Random();
        int damage = this.damage ;

        // 50% chance for a critical hit that doubles the damage
        if (random.nextDouble() < 0.5) {
            damage *= 2;
            System.out.println("Oh no! The enemy landed a powerful hit! Watch out!");
        }
        return damage;
    }

    // Method to attack a target
    public void attack(Entity target) {
        Random random = new Random();

        // Decide whether to use an ability or perform a normal attack
        boolean useAbility = !getAbilities().isEmpty() && random.nextBoolean();

        if (useAbility) {
            Spell selected = getAbilities().get(random.nextInt(getAbilities().size()));

            // Check if the enemy has enough mana to use the ability
            if (getCurrentMana() >= selected.getManaCost()) {
                if ((selected instanceof Fire && target.isImmuneToFire()) ||
                        (selected instanceof Ice && target.isImmuneToIce()) ||
                        (selected instanceof Earth && target.isImmuneToEarth())) {
                    System.out.println("Enemy tried to use " + selected.getName() + ", but the hero is immune to " + selected.getType() + "!");
                } else {
                    System.out.println("Enemy used the ability " + selected.getName());
                    System.out.print("Ability details: \n" +selected.getName() + "; damage: " + selected.getDamage());
                    System.out.println("; mana cost: " + selected.getManaCost());
                    setCurrentMana(getCurrentMana() - selected.getManaCost());
                    target.receiveDamage(selected.getDamage());
                    getAbilities().remove(selected);
                    System.out.println("Oh. no! Your current health is " + target.getCurrentHealth());
                }
            } else {
                System.out.println("Enemy tried to use " + selected.getName() + ", but didn't have enough mana!");
                normalAttack(target);
            }
        } else {
            normalAttack(target);
        }
    }

    // Method to perform a normal attack
    private void normalAttack(Entity target) {
        int damage = getDamage();
        System.out.println("Enemy attacks normally for " + damage + " damage.");
        target.receiveDamage(damage);
        System.out.println("Your current health is " + target.getCurrentHealth());
    }

    @Override
    public void levelUp() {}

    @Override
    public void increaseAttributes() {}

    // Method to display the enemy's stats
    @Override
    public String toString() {
        return "\nhealth: " + getCurrentHealth() + "\nmana: " + getCurrentMana() + "\ndamage: " + damage +
                "\nimmunity to fire: " + isImmuneToFire() + "\nimmunity to ice: " + isImmuneToIce() +
                "\nimmunity to earth: " + isImmuneToEarth();
    }
}
