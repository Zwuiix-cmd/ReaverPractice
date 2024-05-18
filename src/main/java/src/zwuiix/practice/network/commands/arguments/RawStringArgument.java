package src.zwuiix.practice.network.commands.arguments;

import cn.nukkit.command.data.CommandParamType;

public class RawStringArgument extends Argument {
    public RawStringArgument(String name, boolean optional) {
        super(name, optional);
    }

    @Override
    public CommandParamType getCommandParamType() {
        return CommandParamType.RAWTEXT;
    }
}
