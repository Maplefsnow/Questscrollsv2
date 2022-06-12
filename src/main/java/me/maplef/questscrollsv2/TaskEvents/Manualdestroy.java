package me.maplef.questscrollsv2.TaskEvents;

import me.maplef.questscrollsv2.Main;
import me.maplef.questscrollsv2.command.QuestscrollsCommandExecutor;
import me.maplef.questscrollsv2.plugincore.MainBusiness;
import me.maplef.questscrollsv2.plugincore.ToolClass;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Manualdestroy implements Listener {

    // 破坏方块事件
    @EventHandler
    public void destroy(BlockBreakEvent event){
        if(QuestscrollsCommandExecutor.isQuery){
            Main.SendManualObject(event.getPlayer(),"destroy(破坏)", ToolClass.nmsblock(event.getBlock()));
        }
        new MainBusiness().Task("destroy", ToolClass.nmsblock(event.getBlock()),event.getPlayer(),1);
    }
}
