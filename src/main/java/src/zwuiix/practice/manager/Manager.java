package src.zwuiix.practice.manager;

import cn.nukkit.utils.Config;
import src.zwuiix.practice.Loader;

public class Manager {
    protected Config config;

    public Manager(String name)
    {
        this.config = new Config(Loader.getInstance().getDataFolder() + "/" + name, Config.JSON);
    }

    public Config getConfig() {
        return config;
    }

    public void save()
    {
        this.config.save();
    }
}
