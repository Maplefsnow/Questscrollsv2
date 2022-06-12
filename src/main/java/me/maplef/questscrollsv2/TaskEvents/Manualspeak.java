package me.maplef.questscrollsv2.TaskEvents;

import me.maplef.questscrollsv2.Main;
import me.maplef.questscrollsv2.command.QuestscrollsCommandExecutor;
import me.maplef.questscrollsv2.plugincore.MainBusiness;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Manualspeak implements Listener {

    @EventHandler
    public void Speak(AsyncPlayerChatEvent event){

        if(QuestscrollsCommandExecutor.isQuery){
            Main.SendManualObject(event.getPlayer(), "speak(说话)", event.getMessage());
        }
        new MainBusiness().Task("speak", event.getMessage(),event.getPlayer(),1);

    }

}
