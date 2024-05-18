package src.zwuiix.practice.items;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.ItemEnderPearl;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import src.zwuiix.practice.entities.EnderPearlProjectile;
import src.zwuiix.practice.manager.list.CooldownManager.Cooldown;
import src.zwuiix.practice.manager.list.CooldownManager.CooldownManager;
import src.zwuiix.practice.session.ReaverSession;
import src.zwuiix.practice.utils.Cooldowns;

public class EnderPearlItem extends ItemEnderPearl {

    @Override
    public boolean onClickAir(Player player, Vector3 directionVector) {
        if(!player.isCreative()) {
            Cooldown cooldown = CooldownManager.getInstance().getCooldown(player.getName(), Cooldowns.ENDER_PEARL.name(), (ReaverSession) player);
            if(cooldown.isInCooldown()) {
                return false;
            } else cooldown.setCooldown(true, true, 300);
        }

        CompoundTag nbt = (new CompoundTag()).putList((new ListTag("Pos")).add(new DoubleTag("", player.x)).add(new DoubleTag("", player.y + (double)player.getEyeHeight())).add(new DoubleTag("", player.z))).putList((new ListTag("Motion")).add(new DoubleTag("", directionVector.x)).add(new DoubleTag("", directionVector.y)).add(new DoubleTag("", directionVector.z))).putList((new ListTag("Rotation")).add(new FloatTag("", (float)player.yaw)).add(new FloatTag("", (float)player.pitch)));
        this.correctNBT(nbt);
        Entity projectile = Entity.createEntity(this.getProjectileEntityType(), player.getLevel().getChunk(player.getFloorX() >> 4, player.getFloorZ() >> 4), nbt, new Object[]{player});
        if (projectile != null) {
            if (projectile instanceof EnderPearlProjectile && player.getServer().getTick() - player.getLastEnderPearlThrowingTick() < 20) {
                projectile.close();
                return false;
            }

            projectile.setMotion(projectile.getMotion().multiply((double)this.getThrowForce()));
            if (projectile instanceof EntityProjectile) {
                ProjectileLaunchEvent ev = new ProjectileLaunchEvent((EntityProjectile)projectile);
                player.getServer().getPluginManager().callEvent(ev);
                if (ev.isCancelled()) {
                    projectile.close();
                } else {
                    if (!player.isCreative()) {
                        --this.count;
                    }

                    if (projectile instanceof EnderPearlProjectile) {
                        player.onThrowEnderPearl();
                    }

                    projectile.spawnToAll();
                }
            }
        }

        return true;
    }
}
