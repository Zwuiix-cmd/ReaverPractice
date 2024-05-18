package src.zwuiix.practice.utils;

public enum Cooldowns {
    COMBAT_LOGGER("reaper.combat_logger"),
    ENDER_PEARL("reaper.ender_pearl"),
    ;

    final String name;
    Cooldowns(String name) {
        this.name = name;
    }
}
