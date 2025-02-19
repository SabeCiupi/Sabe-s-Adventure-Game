package Characters;

public abstract class Spell {
    private final String name;
    private final int damage;
    private final int manaCost;

    // Constructor
    public Spell(String name, int damage, int manaCost) {
        this.name = name;
        this.damage = damage;
        this.manaCost = manaCost;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getManaCost() {
        return manaCost;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return name + "\nType: " + getType() + "\nDamage: " + damage + "\nMana Cost: " + manaCost + "\n";
    }
}
