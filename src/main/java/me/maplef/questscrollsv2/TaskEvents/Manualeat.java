package me.maplef.questscrollsv2.TaskEvents;

import me.maplef.questscrollsv2.Main;
import me.maplef.questscrollsv2.command.QuestscrollsCommandExecutor;
import me.maplef.questscrollsv2.plugincore.MainBusiness;
import me.maplef.questscrollsv2.plugincore.ToolClass;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class Manualeat implements Listener {

    @EventHandler
    public void eat(PlayerItemConsumeEvent event){

        if(QuestscrollsCommandExecutor.isQuery){
            Main.SendManualObject(event.getPlayer(),"eat(食用)", ToolClass.nmsitem(event.getItem()));
        }

        new MainBusiness().Task("eat", ToolClass.nmsitem(event.getItem()),event.getPlayer(),1);

    }

}
