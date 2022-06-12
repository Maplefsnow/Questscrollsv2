package me.maplef.questscrollsv2.TaskEvents;

import me.maplef.questscrollsv2.Main;
import me.maplef.questscrollsv2.command.QuestscrollsCommandExecutor;
import me.maplef.questscrollsv2.plugincore.MainBusiness;
import me.maplef.questscrollsv2.plugincore.ToolClass;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class Manualput implements Listener {

    @EventHandler
    public void put(BlockPlaceEvent event){

        if(QuestscrollsCommandExecutor.isQuery){
            Main.SendManualObject(event.getPlayer(),"put(放置)", ToolClass.nmsitem(event.getItemInHand()));
        }
        new MainBusiness().Task("put", ToolClass.nmsitem(event.getItemInHand()),event.getPlayer(),1);

    }

}
