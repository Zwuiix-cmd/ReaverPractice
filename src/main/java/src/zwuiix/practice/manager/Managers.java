package src.zwuiix.practice.manager;

import src.zwuiix.practice.manager.list.CooldownManager.CooldownManager;
import src.zwuiix.practice.manager.list.KitManager.KitManager;
import src.zwuiix.practice.manager.list.RankManager.RankManager;

import java.util.ArrayList;

public class Managers {
    protected static Managers instance;
    protected ArrayList<Manager> managers = new ArrayList<>();

    public static Managers getInstance() {
        return instance;
    }

    public static void setInstance(Managers instance) {
        Managers.instance = instance;
    }

    public Managers()
    {
        setInstance(this);
        register(new RankManager());
        register(new KitManager());
        register(new CooldownManager());
    }

    public void register(Manager manager)
    {
        managers.add(manager);
    }

    public void saves()
    {
        managers.forEach(Manager::save);
    }
}
