package com.skyblock.skyblock.features.items;

import com.skyblock.skyblock.Skyblock;
import com.skyblock.skyblock.features.items.misc.GrapplingHook;
import com.skyblock.skyblock.features.items.weapons.AspectOfTheDragons;
import com.skyblock.skyblock.features.items.weapons.AspectOfTheEnd;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SkyblockItemHandler {

    private final HashMap<String, SkyblockItem> items;
    private int amountRegistered;

    public SkyblockItemHandler(Skyblock plugin){
        items = new HashMap<>();

        // Misc
        registerItem(new GrapplingHook(plugin));

        // Weapons
        registerItem(new AspectOfTheDragons(plugin));
        registerItem(new AspectOfTheEnd(plugin));
    }

    public ArrayList<ItemStack> getItems() {
        ArrayList<ItemStack> list = new ArrayList<>();
        for (Map.Entry<String, SkyblockItem> entry : items.entrySet()) {
            list.add(entry.getValue().getItem());
        }
        return list;
    }

    public int getAmountRegistered() { return amountRegistered; }

    private void registerItem(SkyblockItem item){
        items.put(item.getInternalName(), item);
        amountRegistered++;
    }

    public boolean isRegistered(ItemStack item){
        if (getRegistered(item) != null) {
            return true;
        }

        return false;
    }

    public SkyblockItem getRegistered(ItemStack item){
        if (item == null)  return null;
        if (item.getItemMeta() == null) return null;

        if (item.getItemMeta().hasDisplayName()) {
            NBTItem nbtItem = new NBTItem(item);
            return items.get(nbtItem.getString("skyblockId"));
        }

        return null;
    }

    public SkyblockItem getRegistered(String s){
        return items.get(s);
    }

}