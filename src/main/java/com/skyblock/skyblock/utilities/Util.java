package com.skyblock.skyblock.utilities;

import com.skyblock.skyblock.utilities.item.ItemBuilder;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

@UtilityClass
public class Util {

    public List<String> listOf(String... strings) {
        return Arrays.asList(strings);
    }

    private final static TreeMap<Integer, String> romanMap = new TreeMap<>();

    static {

        romanMap.put(1000, "M");
        romanMap.put(900, "CM");
        romanMap.put(500, "D");
        romanMap.put(400, "CD");
        romanMap.put(100, "C");
        romanMap.put(90, "XC");
        romanMap.put(50, "L");
        romanMap.put(40, "XL");
        romanMap.put(10, "X");
        romanMap.put(9, "IX");
        romanMap.put(5, "V");
        romanMap.put(4, "IV");
        romanMap.put(1, "I");

    }

    public String toRoman(int number) {
        int l = romanMap.floorKey(number);
        if (number == l) {
            return romanMap.get(number);
        }
        return romanMap.get(l) + toRoman(number - l);
    }

    public String[] buildLore(String lore) {
        return ChatColor.translateAlternateColorCodes('&', lore).split("\n");
    }

    public ItemStack buildCloseButton() {
        return new ItemBuilder(ChatColor.RED + "Close", Material.BARRIER).toItemStack();
    }

    public ItemStack generateBackButton() {
        return new ItemBuilder(ChatColor.GREEN + "Go Back", Material.ARROW).addLore(ChatColor.GRAY + "To SkyBlock Menu").toItemStack();
    }

}
