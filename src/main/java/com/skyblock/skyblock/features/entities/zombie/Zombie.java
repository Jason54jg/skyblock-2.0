package com.skyblock.skyblock.features.entities.zombie;

import com.skyblock.skyblock.Skyblock;
import com.skyblock.skyblock.features.entities.SkyblockEntity;
import com.skyblock.skyblock.utilities.Util;
import com.skyblock.skyblock.utilities.item.ItemBuilder;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Zombie extends SkyblockEntity {

    private org.bukkit.entity.Zombie zombie;
    private ZombieType type;
    public Zombie(Skyblock sb, String type) {
        super(sb, EntityType.ZOMBIE);

        this.type = ZombieType.valueOf(type);

        Equipment equipment = new Equipment();

        switch (this.type) {
            case GRAVEYARD:
                loadStats(100, 20, true, false, true, equipment, "Zombie", 1);
                break;
            case SEA_WALKER:
                equipment.hand = Util.IDtoSkull(new ItemStack(Material.SKULL, SkullType.PLAYER.ordinal()), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJlN2ZhMmY5YjhkNmQxZTczNGVkYTVlM2NlMDI2Njg4MTM0MjkyZmNhZmMzMjViMWVhZDQzZDg5Y2MxZTEifX19");
                equipment.chest = new ItemBuilder("", Material.LEATHER_CHESTPLATE).dyeColor(Color.BLUE).toItemStack();
                equipment.legs = new ItemBuilder("", Material.LEATHER_LEGGINGS).dyeColor(Color.BLUE).toItemStack();
                equipment.boots = new ItemBuilder("", Material.LEATHER_BOOTS).dyeColor(Color.BLUE).toItemStack();

                loadStats(1500, 60, true, false, true, equipment, "Sea Walker", 4);
                break;
            case CRYPT_GHOUL:
                equipment.chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                equipment.legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
                equipment.boots = new ItemStack(Material.CHAINMAIL_BOOTS);

                loadStats(2000, 350, true, false, true, equipment, "Crypt Ghoul", 30);
                break;
            case GOLDEN_GHOUL:
                equipment.hand = new ItemStack(Material.GOLD_SWORD);
                equipment.chest = new ItemStack(Material.GOLD_CHESTPLATE);
                equipment.legs = new ItemStack(Material.GOLD_LEGGINGS);
                equipment.boots = new ItemStack(Material.GOLD_BOOTS);

                loadStats(45000, 800, true, false, true, equipment, "Golden Ghoul", 50);
                break;
            case LAPIS_ZOMBIE:
                equipment.helmet = new ItemStack(Material.SEA_LANTERN);
                equipment.chest = new ItemBuilder("", Material.LEATHER_CHESTPLATE).dyeColor(Color.BLUE).toItemStack();
                equipment.legs = new ItemBuilder("", Material.LEATHER_LEGGINGS).dyeColor(Color.BLUE).toItemStack();
                equipment.boots = new ItemBuilder("", Material.LEATHER_BOOTS).dyeColor(Color.BLUE).toItemStack();

                loadStats(200, 50, true, false, true, equipment, "Lapis Zombie", 7);
                break;
            case ZOMBIE_VILLAGER:
                equipment.helmet = new ItemStack(Material.LEATHER_HELMET);
                equipment.chest = new ItemStack(Material.LEATHER_CHESTPLATE);
                equipment.legs = new ItemStack(Material.LEATHER_LEGGINGS);
                equipment.boots = new ItemStack(Material.LEATHER_BOOTS);

                loadStats(120, 24, true, false, true, equipment, "Zombie Villager", 1);
                break;
            case DIAMOND_RESERVE:
                equipment.helmet = new ItemStack(Material.DIAMOND_HELMET);
                equipment.chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
                equipment.legs = new ItemStack(Material.DIAMOND_LEGGINGS);
                equipment.boots = new ItemStack(Material.DIAMOND_BOOTS);
                equipment.hand = new ItemStack(Material.DIAMOND_SWORD);

                loadStats(250, 200, true, false, true, equipment, "Zombie", 15);
                break;
            case OBSIDIAN_SANCTUARY:
                equipment.helmet = new ItemBuilder(Material.DIAMOND_HELMET).addEnchantmentGlint().toItemStack();
                equipment.chest = new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchantmentGlint().toItemStack();
                equipment.legs = new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchantmentGlint().toItemStack();
                equipment.boots = new ItemBuilder(Material.DIAMOND_BOOTS).addEnchantmentGlint().toItemStack();
                equipment.hand = new ItemBuilder(Material.DIAMOND_SWORD).addEnchantmentGlint().toItemStack();
                
                loadStats(300, 275, true, false, true, equipment, "Zombie", 20);
                break;
        }
    }

    @Override
    protected void tick() {
        if (tick == 0) {
            zombie = (org.bukkit.entity.Zombie) getVanilla();

            zombie.setVillager(false);
            zombie.setBaby(false);

            if (type.equals(ZombieType.ZOMBIE_VILLAGER)) zombie.setVillager(true);

            List<ZombieType> speedMod = Arrays.asList(ZombieType.ZOMBIE_VILLAGER, ZombieType.CRYPT_GHOUL, ZombieType.GOLDEN_GHOUL);

            if (speedMod.contains(type)) {
                AttributeInstance attributes = ((EntityInsentient)((CraftEntity) getVanilla()).getHandle()).getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);

                attributes.setValue(0.3);
            }
        }
    }
}