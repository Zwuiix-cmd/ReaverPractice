package src.zwuiix.practice.network.commands.arguments;

import cn.nukkit.command.data.CommandParamType;

public class TargetArgument extends Argument {
    public TargetArgument(String name, boolean optional) {
        super(name, optional);
    }

    @Override
    public CommandParamType getCommandParamType() {
        return CommandParamType.TARGET;
    }
}
