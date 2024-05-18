package src.zwuiix.practice.manager.list.RankManager;

import java.util.ArrayList;

public class Rank {
    protected String name;
    protected String nameTagFormat;
    protected String chatFormat;
    protected ArrayList<String> permissions;

    public Rank(String name, String nameTagFormat, String chatFormat, ArrayList<String> permissions)
    {
        this.name = name;
        this.nameTagFormat = nameTagFormat;
        this.chatFormat = chatFormat;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public String getNameTagFormat() {
        return nameTagFormat;
    }

    public String getChatFormat() {
        return chatFormat;
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public String format(String targetName, String message)
    {
        return this.getChatFormat().replace("{NAME}", targetName).replace("{RANK}", this.getName()).replace("{MESSAGE}", message);
    }

    public String format(String targetName)
    {
        return this.getNameTagFormat().replace("{NAME}", targetName).replace("{RANK}", this.getName());
    }
}
