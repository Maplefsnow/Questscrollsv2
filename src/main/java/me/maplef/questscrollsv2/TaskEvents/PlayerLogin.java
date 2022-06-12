package me.maplef.questscrollsv2.TaskEvents;

import me.maplef.questscrollsv2.plugincore.giveTask;
import me.maplef.questscrollsv2.resource.ReadConfig;
import me.maplef.questscrollsv2.resource.ReadTime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PlayerLogin implements Listener {

    @EventHandler
    public void login(PlayerJoinEvent event){

        if(ReadConfig.task.equalsIgnoreCase("-")){
            return;
        }

        if(!ReadTime.time.equalsIgnoreCase(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy")))){ //假如时间不对了 就重载
            ReadTime.reload();
        }

        if(ReadTime.cha(event.getPlayer().getName())){
            giveTask.give(event.getPlayer().getName(),ReadConfig.task,event.getPlayer());
        }

    }

}
