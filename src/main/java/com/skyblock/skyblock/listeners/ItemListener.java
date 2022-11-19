package com.skyblock.skyblock.listeners;

import com.skyblock.skyblock.Skyblock;
import com.skyblock.skyblock.features.items.SkyblockItemHandler;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener {

    private final Skyblock plugin;
    private final SkyblockItemHandler handler;

    public ItemListener(Skyblock skyblock) {
        this.plugin = skyblock;
        this.handler = plugin.getSkyblockItemHandler();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();

            ItemStack item = player.getItemInHand();

            if (handler.isRegistered(item)) {
                handler.getRegistered(item).onEntityDamage(e);
            }
        } else if (e.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();

                ItemStack item = player.getItemInHand();

                if (handler.isRegistered(item)) {
                    handler.getRegistered(item).onEntityDamage(e);
                }
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        ItemStack item = e.getItem();

        if (item != null) {
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (handler.isRegistered(item)) {
                    handler.getRegistered(item).onRightClick(e);
                }
            } else if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                if (handler.isRegistered(item)) {
                    handler.getRegistered(item).onLeftClick(e);
                }
            }
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            ItemStack item = player.getItemInHand();
            if (handler.isRegistered(item)) {
                handler.getRegistered(item).onBowShoot(e);
            }
        }
    }
}