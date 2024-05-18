package src.zwuiix.practice.manager.list.KitManager;

import cn.nukkit.item.Item;
import cn.nukkit.potion.Effect;
import src.zwuiix.practice.session.ReaverSession;

import java.util.HashMap;
import java.util.Map;

public class Kit {
    protected String name;
    protected Map<Integer, Item> contents = new HashMap<>();
    protected Item[] armorContents = new Item[]{};
    protected Map<Integer, Effect> effects = new HashMap<>();

    public Kit(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, Item> getContents() {
        return contents;
    }

    public void setContents(Map<Integer, Item> contents) {
        this.contents = contents;
    }

    public Item[] getArmorContents() {
        return armorContents;
    }

    public void setArmorContents(Item[] armorContents) {
        this.armorContents = armorContents;
    }

    public Map<Integer, Effect> getEffects() {
        return effects;
    }

    public void setEffects(Map<Integer, Effect> effects) {
        this.effects = effects;
    }

    public void give(ReaverSession session)
    {
        session.setMaxHealth(20);
        session.setHealth(session.getMaxHealth());

        session.getFoodData().setFoodLevel(20);
        session.getFoodData().setFoodSaturationLevel(20);

        session.getInventory().clearAll();
        session.getInventory().setContents(this.getContents());
        session.getInventory().setArmorContents(this.getArmorContents());

        this.getEffects().forEach((integer, effect) -> {
            session.addEffect(effect);
        });
    }
}
