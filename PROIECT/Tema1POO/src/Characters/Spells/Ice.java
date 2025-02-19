package Characters.Spells;

import Characters.Spell;

public class Ice extends Spell{
    public Ice(String name, int damage, int manaCost) {
        super(name, damage, manaCost);
    }

    @Override
    public String getType() {
        return "Ice";
    }
}
