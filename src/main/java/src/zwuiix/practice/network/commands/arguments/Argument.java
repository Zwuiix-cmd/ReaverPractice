package src.zwuiix.practice.network.commands.arguments;

import cn.nukkit.command.data.CommandParamType;

abstract public class Argument {
    protected String name;
    protected boolean optional;

    public Argument(String name, boolean optional)
    {
        this.name = name;
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public boolean isOptional() {
        return optional;
    }

    public abstract CommandParamType getCommandParamType();
}
