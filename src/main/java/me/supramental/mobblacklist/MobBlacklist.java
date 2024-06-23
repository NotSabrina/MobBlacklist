package me.supramental.mobblacklist;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MobBlacklist extends JavaPlugin implements Listener {

    private Map<String, Map<String, Boolean>> mobBlacklist = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    private void loadConfig() {
        FileConfiguration config = getConfig();
        Set<String> mobs = config.getKeys(false);

        for (String mob : mobs) {
            Map<String, Boolean> worldBlacklist = new HashMap<>();
            for (String world : config.getConfigurationSection(mob).getKeys(false)) {
                worldBlacklist.put(world, config.getBoolean(mob + "." + world));
            }
            mobBlacklist.put(mob, worldBlacklist);
        }
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        String mobName = event.getEntityType().name();
        String worldName = event.getLocation().getWorld().getName();

        if (mobBlacklist.containsKey(mobName) && mobBlacklist.get(mobName).getOrDefault(worldName, false)) {
            event.setCancelled(true);
        }
    }
}
