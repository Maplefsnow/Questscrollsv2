package me.maplef.questscrollsv2.TaskEvents;

import me.maplef.questscrollsv2.Main;
import me.maplef.questscrollsv2.command.QuestscrollsCommandExecutor;
import me.maplef.questscrollsv2.plugincore.MainBusiness;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Manualcommand implements Listener {

    public void command(PlayerCommandPreprocessEvent event){

        if(QuestscrollsCommandExecutor.isQuery){
            Main.SendManualObject(event.getPlayer(),"command(指令)", event.getMessage().replace("§","&"));
        }
        new MainBusiness().Task("command", event.getMessage().replace("§","&"), event.getPlayer(),1);


    }

}
