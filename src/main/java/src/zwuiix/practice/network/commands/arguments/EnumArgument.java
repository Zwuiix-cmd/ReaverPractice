package src.zwuiix.practice.network.commands.arguments;

import cn.nukkit.command.data.CommandParamType;

public class EnumArgument extends Argument {
    protected String[] enumValues;

    public EnumArgument(String name, boolean optional, String[] enumValues)
    {
        super(name, optional);
        this.enumValues = enumValues;
    }

    public String[] getEnumValues() {
        return enumValues;
    }

    @Override
    public CommandParamType getCommandParamType() {
        return CommandParamType.RAWTEXT;
    }
}
