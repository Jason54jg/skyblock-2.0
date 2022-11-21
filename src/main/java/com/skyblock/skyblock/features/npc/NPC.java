package com.skyblock.skyblock.features.npc;

import com.skyblock.skyblock.Skyblock;
import com.skyblock.skyblock.utilities.Util;
import de.tr7zw.nbtapi.NBTEntity;
import lombok.Data;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.function.Consumer;

@Data
public class NPC implements Listener {

    private final String name;

    private final boolean doesLookClose;
    private final boolean villager;
    private final boolean hasSkin;

    private final Location location;

    private final Consumer<Player> action;

    private final String skinValue;
    private final String skinSignature;

    private net.citizensnpcs.api.npc.NPC npc;
    private ArmorStand stand;
    private ArmorStand click;

    public void spawn() {
        List<Object> npcData = Util.spawnSkyblockNpc(this.location, this.name, this.skinValue, this.skinSignature, this.hasSkin, this.doesLookClose, this.villager);

        this.npc = (net.citizensnpcs.api.npc.NPC) npcData.get(0);
        this.stand = (ArmorStand) npcData.get(1);
        this.click = (ArmorStand) npcData.get(2);
    }

    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        if (!event.getNPC().equals(this.npc)) return;

        Player player = event.getClicker();

        this.action.accept(player);
    }

}
