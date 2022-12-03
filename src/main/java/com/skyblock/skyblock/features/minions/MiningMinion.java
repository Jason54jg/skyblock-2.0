package com.skyblock.skyblock.features.minions;

import com.skyblock.skyblock.Skyblock;
import com.skyblock.skyblock.SkyblockPlayer;
import com.skyblock.skyblock.enums.MiningMinionType;
import com.skyblock.skyblock.features.island.IslandManager;
import com.skyblock.skyblock.utilities.Util;
import com.skyblock.skyblock.utilities.item.ItemBuilder;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MiningMinion extends MinionBase {

    private boolean lastActionBroke;

    private BukkitRunnable task;
    private int i;

    private int radius;

    private MiningMinionType type;

    public MiningMinion(MiningMinionType minion) {
        super(
                minion.getName(),
                minion.getRecipe(),
                minion.getHand(),
                minion.getHead(),
                minion.getLeatherArmorColor(),
                minion.getTimeBetweenActions(),
                minion.getGetMaximumStorage()
        );

        this.lastActionBroke = false;
        this.task = null;
        this.i = 0;

        this.type = minion;
    }

    @Override
    public void load(SkyblockPlayer player, int index) {

    }

    @Override
    public void spawn(SkyblockPlayer player, Location location, int level) {
        if (!location.getWorld().getName().startsWith(IslandManager.ISLAND_PREFIX)) return;

        if (this.minion != null) this.minion.remove();

        this.level = level;

        this.minion = location.getWorld().spawn(location, ArmorStand.class);
        this.minion.setCustomName("");
        this.minion.setCustomNameVisible(false);
        this.minion.setGravity(false);
        this.minion.setVisible(false);
        this.minion.setSmall(true);
        this.minion.setArms(true);
        this.minion.setBasePlate(false);

        ItemStack head = Util.IDtoSkull(new ItemBuilder("", Material.SKULL_ITEM, 1, (short) 3).toItemStack(), this.head.apply(this.level));
        this.minion.setHelmet(head);

        ItemStack hand = this.hand.apply(this.level);
        this.minion.setItemInHand(hand);

        this.minion.setChestplate(Util.colorLeatherArmor(new ItemBuilder("", Material.LEATHER_CHESTPLATE, 1).toItemStack(), this.leatherArmorColor));
        this.minion.setLeggings(Util.colorLeatherArmor(new ItemBuilder("", Material.LEATHER_LEGGINGS, 1).toItemStack(), this.leatherArmorColor));
        this.minion.setBoots(Util.colorLeatherArmor(new ItemBuilder("", Material.LEATHER_BOOTS, 1).toItemStack(), this.leatherArmorColor));

        this.i = 0;

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                if (minion == null || minion.isDead()) {
                    cancel();
                    return;
                }

                int ticksBetweenActions = getTimeBetweenActions.apply(level) * 20;

                if (i >= ticksBetweenActions) {
                    i = 0;

                    tick(player, location);
                } else {
                    i++;
                }
            }
        };

        this.task.runTaskTimerAsynchronously(Skyblock.getPlugin(Skyblock.class), 0, 1);
    }

    @Override
    public void pickup(SkyblockPlayer player, Location location) {

    }

    @Override
    public void showInventory(SkyblockPlayer player) {

    }

    @Override
    protected void tick(SkyblockPlayer player, Location location) {
        List<Block> blocksInRadius = new ArrayList<>();

        for (double x = location.getX() - radius; x <= location.getX() + radius; x++) {
            for (double z = location.getZ() - radius; z <= location.getZ() + radius; z++) {
                Block block = location.clone().add(x, -1, z).getBlock();

                blocksInRadius.add(block);
            }
        }

        if (lastActionBroke) {
            // this time, place a block

            List<Block> airBlocks = blocksInRadius.stream().filter(block -> block.getType() == Material.AIR).collect(Collectors.toList());

            if (airBlocks.size() > 0) {
                Block block = airBlocks.get(Skyblock.getPlugin(Skyblock.class).getRandom().nextInt(airBlocks.size()));

                block.setType(this.type.getMaterial());

                this.lastActionBroke = false;

                return;
            }
        }

        // break a block
        player.getBukkitPlayer().sendMessage("Blocks: " + blocksInRadius);
        List<Block> blocks = blocksInRadius.stream().filter(block -> block.getType() == this.type.getMaterial()).collect(Collectors.toList());

        if (blocks.size() > 0) {
            Block block = blocks.get(Skyblock.getPlugin(Skyblock.class).getRandom().nextInt(blocks.size()));

            new BukkitRunnable() {
                int animation = 1;

                @Override
                public void run() {
                    ((CraftPlayer) player.getBukkitPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutBlockBreakAnimation(minion.getEntityId(), new BlockPosition(block.getX(), block.getY(), block.getZ()), animation));

                    animation++;

                    if (animation >= 9) {
                        cancel();

                        ItemStack[] drops = type.getCalculateDrops().apply(level);

                        // for now
                        player.getBukkitPlayer().getInventory().addItem(drops);
                    }
                }
            }.runTaskTimer(Skyblock.getPlugin(Skyblock.class), 0, 2);

            this.lastActionBroke = true;
        }
    }
}