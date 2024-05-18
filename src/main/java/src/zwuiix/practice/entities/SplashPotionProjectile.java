package src.zwuiix.practice.entities;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityPotion;
import cn.nukkit.event.potion.PotionCollideEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.level.particle.SpellParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.potion.Effect;
import cn.nukkit.potion.Potion;

public class SplashPotionProjectile extends EntityPotion {
    public SplashPotionProjectile(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
        setScale(0.6f);
    }

    @Override
    protected float getGravity() {
        return 0.06f;
    }

    @Override
    protected float getDrag() {
        return 0.0025f;
    }

    @Override
    protected void splash(Entity collidedWith) {
        Potion potion = Potion.getPotion(this.potionId);
        PotionCollideEvent event = new PotionCollideEvent(potion, this);
        this.close();

        potion = event.getPotion();
        if (potion == null) {
            return;
        }

        potion.setSplash(true);

        Particle particle;
        int r;
        int g;
        int b;

        Effect effect = Potion.getEffect(potion.getId(), true);

        if (effect == null) {
            r = 40;
            g = 40;
            b = 255;
        } else {
            int[] colors = effect.getColor();
            r = colors[0];
            g = colors[1];
            b = colors[2];
        }

        particle = new SpellParticle(this, r, g, b);

        this.getLevel().addParticle(particle);
        this.getLevel().addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_GLASS);

        Entity[] entities = this.getLevel().getNearbyEntities(this.getBoundingBox().expand(1.85, 2.65, 1.85));
        for (Entity anEntity : entities) {
            potion.applyPotion(anEntity, 1.0515);
        }
    }
}
