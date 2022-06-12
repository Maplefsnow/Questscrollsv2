package me.maplef.questscrollsv2.TaskEvents;

import me.maplef.questscrollsv2.Main;
import me.maplef.questscrollsv2.command.QuestscrollsCommandExecutor;
import me.maplef.questscrollsv2.plugincore.MainBusiness;
import me.maplef.questscrollsv2.plugincore.ToolClass;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;

public class Manualdamage implements Listener {

    @EventHandler
    public void damage(PlayerItemBreakEvent event){
        if(QuestscrollsCommandExecutor.isQuery){
            if(event.getBrokenItem().getItemMeta().displayName() == null){
               Main.SendManualObject(event.getPlayer(),"damage(损坏物品,工具)", ToolClass.nmsitem(event.getBrokenItem()));
            }else if(event.getBrokenItem().getItemMeta().getDisplayName().equalsIgnoreCase("")){
                Main.SendManualObject(event.getPlayer(),"damage(损坏物品,工具)", ToolClass.nmsitem(event.getBrokenItem()));
            }else{
                Main.SendManualObject(event.getPlayer(),"damage(损坏物品,工具)", event.getBrokenItem().getItemMeta().getDisplayName().replace("§","&"));
            }
        }

        if(event.getBrokenItem().getItemMeta().displayName() == null){
            new MainBusiness().Task("damage", ToolClass.nmsitem(event.getBrokenItem()),event.getPlayer(),1);
        } else if(event.getBrokenItem().getItemMeta().getDisplayName().equalsIgnoreCase("")){
            new MainBusiness().Task("damage", ToolClass.nmsitem(event.getBrokenItem()),event.getPlayer(),1);
        } else {
            new MainBusiness().Task("damage", event.getBrokenItem().getItemMeta().getDisplayName().replace("§","&"),event.getPlayer(),1);
        }
    }

}
