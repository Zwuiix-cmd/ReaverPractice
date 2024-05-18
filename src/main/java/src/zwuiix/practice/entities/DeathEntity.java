package src.zwuiix.practice.entities;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.EntityFlameParticle;
import cn.nukkit.nbt.tag.CompoundTag;

public class DeathEntity extends EntityHuman {
    protected int life = 0;

    public DeathEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean attack(float damage) {
        return false;
    }

    @Override
    public boolean entityBaseTick() {
        life++;
        if(life >= 25) {
            close();
            getLevel().addParticle(new EntityFlameParticle(this.getPosition()));
            return true;
        }
        return super.entityBaseTick();
    }

    @Override
    public void collidingWith(Entity ent) {}

    @Override
    public boolean canBeMovedByCurrents() {
        return false;
    }

    @Override
    public boolean canClimbWalls() {
        return false;
    }
}
