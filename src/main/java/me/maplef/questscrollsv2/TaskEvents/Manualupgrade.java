package me.maplef.questscrollsv2.TaskEvents;

import me.maplef.questscrollsv2.plugincore.MainBusiness;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class Manualupgrade implements Listener {
    @EventHandler
    public void upgrade(PlayerLevelChangeEvent event){
        int level = event.getNewLevel() - event.getOldLevel();
        if(level < 1){ //会出现是负数的情况 原因未知
            return;
        }
        new MainBusiness().Task("upgrade", "No",event.getPlayer(),level);
    }
}
