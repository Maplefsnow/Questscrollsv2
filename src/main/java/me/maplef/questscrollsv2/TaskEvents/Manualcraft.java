package me.maplef.questscrollsv2.TaskEvents;

import me.maplef.questscrollsv2.Main;
import me.maplef.questscrollsv2.command.QuestscrollsCommandExecutor;
import me.maplef.questscrollsv2.plugincore.MainBusiness;
import me.maplef.questscrollsv2.plugincore.ToolClass;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;


public class Manualcraft implements Listener {
    @EventHandler
    public void craft(CraftItemEvent event){
        for (HumanEntity humanEntity : event.getInventory().getViewers()) { //获取能看到这个物品栏的玩家
            if(QuestscrollsCommandExecutor.isQuery){
                Main.SendManualObject((Player) humanEntity,"craft(合成)", ToolClass.nmsitem(event.getCurrentItem()));
            }
            new MainBusiness().Task("craft", ToolClass.nmsitem(event.getCurrentItem()), (Player) humanEntity,event.getRecipe().getResult().getAmount());
        }
    }
}


