package src.zwuiix.practice.session;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.SourceInterface;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.PlaySoundPacket;
import src.zwuiix.practice.manager.list.CooldownManager.Cooldown;
import src.zwuiix.practice.manager.list.CooldownManager.CooldownManager;
import src.zwuiix.practice.manager.list.KitManager.KitManager;
import src.zwuiix.practice.manager.list.RankManager.Rank;
import src.zwuiix.practice.manager.list.RankManager.RankManager;
import src.zwuiix.practice.utils.Cooldowns;
import src.zwuiix.practice.utils.Scoreboard;

import java.net.InetSocketAddress;

public class ReaverSession extends Player {
    protected Scoreboard scoreboard = new Scoreboard(this);

    public ReaverSession(SourceInterface interfaz, Long clientID, InetSocketAddress socketAddress) {
        super(interfaz, clientID, socketAddress);
    }

    @Override
    protected void doFirstSpawn() {
        super.doFirstSpawn();
        CooldownManager.getInstance().setCooldown(getName(), new Cooldown(Cooldowns.COMBAT_LOGGER.name(), this));
        CooldownManager.getInstance().setCooldown(getName(), new Cooldown(Cooldowns.ENDER_PEARL.name(), this));
    }

    @Override
    public void knockBack(Entity attacker, double damage, double x, double z, double base) {
        float xz = 0.375f;
        float y = 0.38f;

        float maxDistance = 1.5f;
        float maxHeightReduce = 0.025f;
        float defaultAll = 0.005f;

        Position position = attacker.getPosition();
        float dist = (float) (this.getPosition().getY() - position.getY());
        if(!this.isOnGround()) {
            boolean value = dist > maxDistance;
            float diff = value ? maxHeightReduce : defaultAll;
            y -= (dist * diff);
        }

        double f = Math.sqrt(x * x + z * z);
        if (!(f <= 0.0)) {
            f = 1.0 / f;
            Vector3 motion = new Vector3(this.motionX, this.motionY, this.motionZ);
            motion.x /= 2.0;
            motion.y /= 2.0;
            motion.z /= 2.0;
            motion.x += x * f * xz;
            motion.y += y;
            motion.z += z * f * xz;

           /* if (motion.y > base) {
                motion.y = base;
            }*/

            this.resetFallDistance();
            this.setMotion(motion);
        }
    }

    public void sendCustomExperience(float exp) {
        if (this.spawned) {
            this.setAttribute(Attribute.getAttribute(10).setValue(Math.max(0.0F, Math.min(1.0F, exp / (float)calculateRequireExperience(this.getExperienceLevel())))));
        }
    }

    public int getAttackTime()
    {
        return this.attackTime;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        return super.attack(source);
    }

    public Rank getRank()
    {
        return RankManager.getInstance().getPlayerRank(this.getName());
    }

    public void setRank(Rank rank)
    {
        RankManager.getInstance().setPlayersRank(this.getName(), rank);
        this.sync();
    }

    public void sync()
    {
        setNameTag(getRank().format(this.getName()));
    }

    public void teleportFix(Vector3 vector3)
    {
        this.setPosition(vector3);
        this.sendPosition(vector3, this.yaw, this.pitch, MovePlayerPacket.MODE_TELEPORT);
    }

    public void spawn()
    {
        this.sync();
        KitManager.getInstance().getKitByName("Nodebuff").give(this);
        this.setGamemode(3);
        this.setGamemode(2);
        this.teleportFix(new Vector3(35, 71, 160));
    }

    public void effectKill()
    {
        long id = Entity.entityCount++;
        AddEntityPacket addEntity = new AddEntityPacket();
        addEntity.type = EntityLightning.NETWORK_ID;
        addEntity.entityUniqueId = id;
        addEntity.entityRuntimeId = id;
        addEntity.yaw = 0;
        addEntity.headYaw = 0;
        addEntity.pitch = 0;
        addEntity.x = (float) this.x;
        addEntity.y = (float) this.y + this.getBaseOffset();
        addEntity.z = (float) this.z;

        PlaySoundPacket playSoundPacket = new PlaySoundPacket();
        playSoundPacket.name = "ambient.weather.thunder";
        playSoundPacket.volume = 1;
        playSoundPacket.pitch = 1;
        playSoundPacket.x = this.getFloorX();
        playSoundPacket.y = this.getFloorY();
        playSoundPacket.z = this.getFloorZ();
        Server.broadcastPacket(this.getViewers().values(), addEntity);
        Server.broadcastPacket(this.getViewers().values(), playSoundPacket);
    }

    @Override
    protected void broadcastMovement() {
        super.broadcastMovement();
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
