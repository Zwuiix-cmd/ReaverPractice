package src.zwuiix.practice.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.event.server.DataPacketSendEvent;
import cn.nukkit.event.server.QueryRegenerateEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemPotionSplash;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.utils.TextFormat;
import src.zwuiix.practice.manager.list.CooldownManager.Cooldown;
import src.zwuiix.practice.manager.list.CooldownManager.CooldownManager;
import src.zwuiix.practice.manager.list.KitManager.KitManager;
import src.zwuiix.practice.session.ReaverSession;
import src.zwuiix.practice.utils.Cooldowns;
import src.zwuiix.practice.utils.Permissions;

import java.util.Map;

public class EventListener implements Listener {
    @EventHandler()
    public void onCreation(PlayerCreationEvent event)
    {
        event.setPlayerClass(ReaverSession.class);
    }

    @EventHandler()
    public void onLogin(PlayerLoginEvent event)
    {
        Player player = event.getPlayer();
        player.getInventory().clearAll();
        player.getOffhandInventory().clearAll();
        player.getCursorInventory().clearAll();
        player.getEnderChestInventory().clearAll();
        player.getUIInventory().clearAll();
    }

    @EventHandler()
    public void onJoin(PlayerJoinEvent event)
    {
        ReaverSession session = (ReaverSession) event.getPlayer();
        event.setJoinMessage("");

        session.setImmobile(false);
        session.spawn();
    }

    @EventHandler()
    public void onQuit(PlayerQuitEvent event)
    {
        ReaverSession session = (ReaverSession) event.getPlayer();
        event.setQuitMessage("");

        Cooldown combatTag =  CooldownManager.getInstance().getCooldown(session.getName(), Cooldowns.COMBAT_LOGGER.name(), session);
        if(combatTag.isInCooldown()) {
            EntityDamageEvent lastDamageCause = session.getLastDamageCause();
            if(lastDamageCause instanceof EntityDamageByEntityEvent) {
                session.setHealth(0.0000001f);
                this.onDamage(lastDamageCause);
            }
        }
    }

    @EventHandler()
    public void onChat(PlayerChatEvent event)
    {
        ReaverSession session = (ReaverSession) event.getPlayer();

        String message = event.getMessage();
        if(!session.hasPermission(Permissions.CHAT_COLOR.name())) {
            message = TextFormat.clean(message);
        }

        event.setFormat(session.getRank().format(session.getName(), message));
    }

    @EventHandler()
    public void onExhaust(PlayerFoodLevelChangeEvent event)
    {
        event.setFoodLevel(18);
        event.setFoodSaturationLevel(18);
    }

    @EventHandler()
    public void onDrop(PlayerDropItemEvent event)
    {
        Player player = event.getPlayer();
        event.setCancelled(!player.isCreative());
    }

    @EventHandler()
    public void onBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        event.setCancelled(!player.isCreative());
    }

    @EventHandler()
    public void onPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        event.setCancelled(!player.isCreative());
    }

    @EventHandler()
    public void onDeath(PlayerDeathEvent event)
    {
        event.setDeathMessage("");
        event.setDrops(new Item[]{});

        ReaverSession session = (ReaverSession) event.getEntity();
        EntityDamageEvent entityDamageEvent = session.getLastDamageCause();
        if (entityDamageEvent instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) entityDamageEvent).getDamager();
            if (damager instanceof ReaverSession) {
                ReaverSession damagerSession = (ReaverSession) damager;
                int sessionPots = 0;
                int damagerPots = 0;

                for (Map.Entry<Integer, Item> entry : session.getInventory().getContents().entrySet()) {
                    Item item = entry.getValue();
                    if (item instanceof ItemPotionSplash) {
                        sessionPots++;
                    }
                }

                for (Map.Entry<Integer, Item> entry : damagerSession.getInventory().getContents().entrySet()) {
                    Item item = entry.getValue();
                    if (item instanceof ItemPotionSplash) {
                        damagerPots++;
                    }
                }

                session.effectKill();
                Server.getInstance().broadcastMessage("§a" + damagerSession.getName() + "§2[" + damagerPots + " pots]§f killed §c" + session.getName() + "§4[" + sessionPots + "pots]§r");
                Cooldown combatTag = CooldownManager.getInstance().getCooldown(damager.getName(), Cooldowns.COMBAT_LOGGER.name(), damagerSession);
                combatTag.setCooldown(false, true, "§6CombatTag Reduced", 0);

                Cooldown combatTag2 = CooldownManager.getInstance().getCooldown(session.getName(), Cooldowns.COMBAT_LOGGER.name(), session);
                combatTag2.setCooldown(false, false, 0);

                damagerSession.setExperience(0, 0);
                KitManager.getInstance().getKitByName("Nodebuff").give(damagerSession);
            } else event.setCancelled();
        }
    }

    @EventHandler()
    public void onRespawn(PlayerRespawnEvent event)
    {
        ReaverSession session = (ReaverSession) event.getPlayer();
        event.setRespawnPosition(Server.getInstance().getDefaultLevel().getSpawnLocation());
        KitManager.getInstance().getKitByName("Nodebuff").give(session);
    }

    @EventHandler()
    public void onDamage(EntityDamageEvent event)
    {
        Entity entity = event.getEntity();
        if(entity instanceof ReaverSession) {
            ReaverSession session = (ReaverSession) entity;
            event.setAttackCooldown(9);

            if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                event.setCancelled();
                return;
            }

            if(session.getLastDamageCause() != null && session.getAttackTime() > 0) { // Anti Switch
                event.setCancelled();
                return;
            }

            if (event instanceof EntityDamageByEntityEvent) {
                Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
                if (damager instanceof ReaverSession && !event.isCancelled()) {
                    ReaverSession damagerSession = (ReaverSession) damager;

                    Cooldown combatTag = CooldownManager.getInstance().getCooldown(damager.getName(), Cooldowns.COMBAT_LOGGER.name(), damagerSession);
                    combatTag.setCooldown(true, false,300);

                    Cooldown combatTag2 = CooldownManager.getInstance().getCooldown(session.getName(), Cooldowns.COMBAT_LOGGER.name(), session);
                    combatTag2.setCooldown(true, false,300);

                    if (event.getFinalDamage() >= session.getHealth()) {
                        event.setCancelled();

                        int sessionPots = 0;
                        int damagerPots = 0;

                        for (Map.Entry<Integer, Item> entry : session.getInventory().getContents().entrySet()) {
                            Item item = entry.getValue();
                            if (item instanceof ItemPotionSplash) {
                                sessionPots++;
                            }
                        }

                        for (Map.Entry<Integer, Item> entry : damagerSession.getInventory().getContents().entrySet()) {
                            Item item = entry.getValue();
                            if (item instanceof ItemPotionSplash) {
                                damagerPots++;
                            }
                        }

                        Server.getInstance().broadcastMessage("§a" + damagerSession.getName() + "§2[" + damagerPots + " pots]§f killed §c" + session.getName() + "§4[" + sessionPots + "pots]§r");
                        combatTag.setCooldown(false, true, "§6CombatTag Reduced", 0);
                        damagerSession.setExperience(0, 0);
                        session.effectKill();
                        session.spawn();
                        KitManager.getInstance().getKitByName("Nodebuff").give(damagerSession);
                    }
                } else event.setCancelled();
            } else event.setCancelled();
        } else event.setCancelled();
    }

    @EventHandler()
    public void onDataSend(DataPacketSendEvent event)
    {
        ReaverSession session = (ReaverSession) event.getPlayer();
        DataPacket packet = event.getPacket();
        if(packet instanceof LevelSoundEventPacket) {
            if(((LevelSoundEventPacket) packet).sound == LevelSoundEventPacket.SOUND_ATTACK_STRONG || ((LevelSoundEventPacket) packet).sound == LevelSoundEventPacket.SOUND_ATTACK_NODAMAGE) {
                event.setCancelled();
            }
        }

       /* if(packet instanceof MovePlayerPacket) {
            if(((MovePlayerPacket) packet).mode != MovePlayerPacket.MODE_TELEPORT) {
                ((MovePlayerPacket) packet).mode = MovePlayerPacket.MODE_PITCH;
                MoveEntityAbsolutePacket pk = new MoveEntityAbsolutePacket();
                pk.eid = ((MovePlayerPacket) packet).eid;
                pk.x = ((MovePlayerPacket) packet).x;
                pk.y = ((MovePlayerPacket) packet).y;
                pk.z = ((MovePlayerPacket) packet).z;
                pk.headYaw = ((MovePlayerPacket) packet).yaw;
                pk.pitch = ((MovePlayerPacket) packet).pitch;
                pk.yaw = ((MovePlayerPacket) packet).yaw;
                pk.teleport = false;
                pk.onGround = ((MovePlayerPacket) packet).onGround;
                pk.protocol = packet.protocol;
                session.getNetworkSession().sendPacket(pk);
            }
        }*/
    }

    @EventHandler()
    public void onQuery(QueryRegenerateEvent event)
    {
        event.setPlayerCount(Server.getInstance().getOnlinePlayersCount());
        event.setPlayerCount(Server.getInstance().getOnlinePlayersCount() + 1);
    }
}
