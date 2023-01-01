package com.torben.ai;

import org.bukkit.Bukkit;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        TalkCommand cmd = new TalkCommand(this);
        getCommand("talk").setExecutor(cmd);
        Bukkit.getPluginManager().registerEvents(cmd, this  );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
