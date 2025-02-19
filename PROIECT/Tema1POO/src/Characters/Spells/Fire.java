package Characters.Spells;

import Characters.Spell;

public class Fire extends Spell {
    public Fire(String name, int damage, int manaCost) {
        super(name, damage, manaCost);
    }

    @Override
    public String getType() {
        return "Fire";
    }
}
