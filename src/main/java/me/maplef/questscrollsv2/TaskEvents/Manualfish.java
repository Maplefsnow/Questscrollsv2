package me.maplef.questscrollsv2.TaskEvents;

import me.maplef.questscrollsv2.Main;
import me.maplef.questscrollsv2.command.QuestscrollsCommandExecutor;
import me.maplef.questscrollsv2.plugincore.MainBusiness;
import me.maplef.questscrollsv2.plugincore.ToolClass;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class Manualfish implements Listener {

    @EventHandler
    public void fish(PlayerFishEvent event){
        if(event.getState().equals(PlayerFishEvent.State.valueOf("CAUGHT_FISH"))){
            if(event.getCaught() != null) {
                ItemStack e = ((Item)event.getCaught()).getItemStack();
                if(QuestscrollsCommandExecutor.isQuery){
                    Main.SendManualObject(event.getPlayer(),"fish(钓鱼)", ToolClass.nmsitem(e));
                }
                new MainBusiness().Task("fish", ToolClass.nmsitem(e), event.getPlayer(), 1);
            }
        }

    }

}
