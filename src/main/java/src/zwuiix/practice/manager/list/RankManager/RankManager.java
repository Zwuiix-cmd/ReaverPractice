package src.zwuiix.practice.manager.list.RankManager;

import src.zwuiix.practice.manager.Manager;

import java.util.ArrayList;
import java.util.HashMap;

public class RankManager extends Manager {
    protected static RankManager instance;

    public static RankManager getInstance() {
        return instance;
    }

    protected HashMap<String, Rank> ranks = new HashMap<>();
    protected Rank defaultRank;

    public RankManager()
    {
        super("RankManager");
        instance = this;
        this.registerRank(new Rank("Player", "§7{NAME}", "§7{NAME}: {MESSAGE}", new ArrayList<>()), true);
        this.registerRank(new Rank("Admin", "§cAdmin {NAME}", "§cAdmin {NAME}: {MESSAGE}", new ArrayList<>()), false);
        this.registerRank(new Rank("Dev", "§6Dev {NAME}", "§6Dev {NAME}: {MESSAGE}", new ArrayList<>()), false);
    }

    public void registerRank(Rank rank, boolean isDefault)
    {
        ranks.put(rank.getName().toLowerCase(), rank);
        if(isDefault) {
            defaultRank = rank;
        }
    }

    public Rank getRankByName(String name)
    {
        return ranks.getOrDefault(name.toLowerCase(), defaultRank);
    }

    public HashMap<String, Rank> getRanks() {
        return ranks;
    }

    public Rank getPlayerRank(String name)
    {
        return this.getRankByName(this.config.get(name.toLowerCase(), defaultRank.getName()));
    }

    public void setPlayersRank(String name, Rank rank)
    {
        this.config.set(name.toLowerCase(), rank.getName());
    }
}
