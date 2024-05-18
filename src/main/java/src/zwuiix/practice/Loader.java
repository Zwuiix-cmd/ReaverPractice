package src.zwuiix.practice;

import cn.nukkit.entity.Entity;
import cn.nukkit.plugin.PluginBase;
import src.zwuiix.practice.commands.global.LatencyCommand;
import src.zwuiix.practice.commands.global.SpawnCommand;
import src.zwuiix.practice.commands.staff.RankCommand.RankCommand;
import src.zwuiix.practice.entities.DeathEntity;
import src.zwuiix.practice.entities.EnderPearlProjectile;
import src.zwuiix.practice.entities.SplashPotionProjectile;
import src.zwuiix.practice.listener.EventListener;
import src.zwuiix.practice.manager.Managers;
import src.zwuiix.practice.tasks.CooldownTask;
import src.zwuiix.practice.tasks.ScoreboardTask;

public class Loader extends PluginBase {
    protected static Loader instance;

    public static Loader getInstance() {
        return instance;
    }

    public static void setInstance(Loader instance) {
        Loader.instance = instance;
    }

    @Override
    public void onLoad() {
        setInstance(this);
    }

    @Override
    public void onEnable() {
        getLogger().info("Loaded ReaverPractice Core.");
        initialize();
    }

    public void initialize()
    {
        new CooldownTask();
        new ScoreboardTask();
        new Managers();

        getServer().getNetwork().setName("§r§cReaver MultiVersion");

        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getServer().getCommandMap().register("practice", new RankCommand());
        getServer().getCommandMap().register("practice", new LatencyCommand());
        getServer().getCommandMap().register("practice", new SpawnCommand());
        Entity.registerEntity("EnderPearl", EnderPearlProjectile.class);
        Entity.registerEntity("ThrownPotion", SplashPotionProjectile.class);
        Entity.registerEntity("DeathEntity", DeathEntity.class);
    }

    @Override
    public void onDisable() {
        Managers.getInstance().saves();
    }
}
