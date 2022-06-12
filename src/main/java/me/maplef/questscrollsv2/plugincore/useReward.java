package me.maplef.questscrollsv2.plugincore;

import me.maplef.questscrollsv2.resource.ReadManual;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;


public class useReward implements Listener {
    @EventHandler
    public void use(PlayerInteractEvent event){
        if((event.getAction().name().equalsIgnoreCase("RIGHT_CLICK_AIR"))||(event.getAction().name().equalsIgnoreCase("RIGHT_CLICK_BLOCK"))) {
            //遍历已经成功配置的任务卷轴
            for (String name : ReadManual.manualList.keySet()){
                if(ReadManual.manualList.get(name).checkTaskName(event.getItem().getItemMeta().getDisplayName(), event.getPlayer())){
                    event.getPlayer().getInventory().setItemInMainHand(null);
                }else {
                    Bukkit.getServer().getLogger().info("clicked!");
                }
            }
        }
    }

}
