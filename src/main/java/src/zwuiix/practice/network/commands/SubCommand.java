package src.zwuiix.practice.network.commands;

import cn.nukkit.command.CommandSender;
import src.zwuiix.practice.network.commands.arguments.Argument;
import src.zwuiix.practice.session.ReaverSession;

import java.util.LinkedHashMap;

public abstract class SubCommand {
    protected String name;
    protected String permission = "";
    protected LinkedHashMap<Integer, Argument> arguments = new LinkedHashMap<>();

    protected boolean onlyPlayer = false;

    public SubCommand(String name)
    {
        this.name = name;
        this.prepare();
    }

    public String getName() {
        return name;
    }

    abstract public void prepare();

    public boolean isOnlyPlayer() {
        return onlyPlayer;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void registerArgument(int number, Argument argument)
    {
        this.arguments.put(number, argument);
    }

    public LinkedHashMap<Integer, Argument> getArguments() {
        return arguments;
    }

    public void setOnlyPlayer(boolean onlyPlayer) {
        this.onlyPlayer = onlyPlayer;
    }

    public void onRun(CommandSender commandSender, String[] args)
    {
    }

    public void onSessionRun(ReaverSession session, String[] args)
    {
    }
}
