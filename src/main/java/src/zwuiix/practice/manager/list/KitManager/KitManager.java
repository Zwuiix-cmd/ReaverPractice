package src.zwuiix.practice.manager.list.KitManager;

import src.zwuiix.practice.manager.Manager;
import src.zwuiix.practice.manager.list.KitManager.kits.Nodebuff;

import java.util.HashMap;

public class KitManager extends Manager {
    protected static KitManager instance;
    protected HashMap<String, Kit> kits = new HashMap<>();

    public static KitManager getInstance() {
        return instance;
    }

    public KitManager() {
        super("KitManager");
        instance = this;

        this.registerKit(new Nodebuff());
    }

    public void registerKit(Kit kit)
    {
        kits.put(kit.getName().toLowerCase(), kit);
    }

    public Kit getKitByName(String name)
    {
        return kits.get(name.toLowerCase());
    }
}
