package src.zwuiix.practice.manager.list.KitManager.kits;

import cn.nukkit.item.*;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.potion.Effect;
import src.zwuiix.practice.items.EnderPearlItem;
import src.zwuiix.practice.manager.list.KitManager.Kit;

import java.util.HashMap;
import java.util.Map;

public class Nodebuff extends Kit {
    public Nodebuff()
    {
        super("Nodebuff");

        Enchantment enchantment = Enchantment.getEnchantment(Enchantment.ID_DURABILITY);
        enchantment.setLevel(10);
        Map<Integer, Item> contents = new HashMap<>();
        ItemSwordDiamond swordDiamond = (ItemSwordDiamond) ((ItemSwordDiamond) Item.get(ItemID.DIAMOND_SWORD)).setUnbreakable(true).setCustomName("§r§cReaver");
        swordDiamond.addEnchantment(enchantment);

        Item pearl = (new EnderPearlItem()).setCustomName("§r§cReaver");
        pearl.setCount(16);

        contents.put(0, swordDiamond);
        contents.put(1, pearl);
        for (int i = 2; i < 36; i++) {
            contents.put(i, Item.get(ItemID.SPLASH_POTION, 22).setCustomName("§r§cReaver"));
        }
        this.setContents(contents);

        ItemHelmetDiamond helmetDiamond = (ItemHelmetDiamond) Item.get(ItemID.DIAMOND_HELMET).setCustomName("§r§cReaver");
        helmetDiamond.getNamedTag().putByte("Unbreakable", 1);
        helmetDiamond.addEnchantment(enchantment);

        ItemChestplateDiamond chestplateDiamond = (ItemChestplateDiamond) Item.get(ItemID.DIAMOND_CHESTPLATE).setCustomName("§r§cReaver");
        chestplateDiamond.getNamedTag().putByte("Unbreakable", 1);
        chestplateDiamond.addEnchantment(enchantment);

        ItemLeggingsDiamond leggingsDiamond = (ItemLeggingsDiamond) Item.get(ItemID.DIAMOND_LEGGINGS).setCustomName("§r§cReaver");
        leggingsDiamond.getNamedTag().putByte("Unbreakable", 1);
        leggingsDiamond.addEnchantment(enchantment);

        ItemBootsDiamond bootsDiamond = (ItemBootsDiamond) Item.get(ItemID.DIAMOND_BOOTS).setCustomName("§r§cReaver");
        bootsDiamond.getNamedTag().putByte("Unbreakable", 1);
        bootsDiamond.addEnchantment(enchantment);
        this.setArmorContents(new Item[]{helmetDiamond, chestplateDiamond, leggingsDiamond, bootsDiamond});

        Map<Integer, Effect> effects = new HashMap<>();
        effects.put(Effect.SPEED, Effect.getEffect(Effect.SPEED).setDuration(999999999).setVisible(false));
        effects.put(Effect.NIGHT_VISION, Effect.getEffect(Effect.NIGHT_VISION).setDuration(999999999).setVisible(false));
        this.setEffects(effects);
    }
}
