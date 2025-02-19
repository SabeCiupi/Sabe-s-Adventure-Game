package Characters.Spells;

import Characters.Spell;

public class Earth extends Spell {
    public Earth(String name, int damage, int manaCost) {
        super(name, damage, manaCost);
    }

    @Override
    public String getType() {
        return "Earth";
    }
}
