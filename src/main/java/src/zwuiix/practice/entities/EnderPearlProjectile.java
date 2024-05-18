package src.zwuiix.practice.entities;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import src.zwuiix.practice.session.ReaverSession;

import java.util.List;

public class EnderPearlProjectile extends EntityProjectile {
    public static final int NETWORK_ID = 87;

    public EnderPearlProjectile(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
        this.setScale(0.6f);
    }

    public int getNetworkId() {
        return 87;
    }

    public float getWidth() {
        return 0.25F;
    }

    public float getLength() {
        return 0.25F;
    }

    public float getHeight() {
        return 0.25F;
    }

    @Override
    protected float getGravity() {
        return 0.065f;
    }

    @Override
    protected float getDrag() {
        return 0.0085f;
    }

    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        } else if (this.isCollided && this.shootingEntity instanceof Player) {
            List<Block> b = this.getCollisionBlocks();
            boolean portal = false;

            for (Block collided : b) {
                if (collided.getId() == 90) {
                    portal = true;
                }
            }

            this.close();
            if (!portal) {
                this.teleport();
                if (Server.getInstance().mobsFromBlocks && Utils.rand(1, 20) == 5) {
                    CreatureSpawnEvent ev = new CreatureSpawnEvent(87, CreatureSpawnEvent.SpawnReason.ENDER_PEARL);
                    this.level.getServer().getPluginManager().callEvent(ev);
                    if (ev.isCancelled()) {
                        return false;
                    }
                }
            }

            return false;
        } else {
            if (this.age > 1200 || this.isCollided) {
                this.close();
            }

            return super.onUpdate(currentTick);
        }
    }

    public void onCollideWithEntity(Entity entity) {
        if (this.shootingEntity instanceof Player) {
            this.teleport();
        }

        super.onCollideWithEntity(entity);
    }

    private void teleport() {
        if(shootingEntity instanceof ReaverSession) {
            ReaverSession session = (ReaverSession) shootingEntity;
            if (this.level.equals(session.getLevel())) {
                Vector3 position = new Vector3(this.x, this.y, this.z);

                this.level.addLevelEvent(session.getPosition(), 2013);
                this.level.addLevelEvent(session.getPosition(), 1018);

                session.teleportFix(position);

                this.level.addLevelEvent(position, 2013);
                this.level.addLevelEvent(position, 1018);
            }
        }
    }
}
