package src.zwuiix.practice.utils;

public enum Permissions {
    CHAT_COLOR("reaver.command"),
    RANK("reaver.rank"),
    ;

    final String name;
    Permissions(String name) {
        this.name = name;
    }
}
