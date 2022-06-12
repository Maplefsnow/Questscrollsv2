package me.maplef.questscrollsv2.TaskEvents;

import me.maplef.questscrollsv2.Main;
import me.maplef.questscrollsv2.command.QuestscrollsCommandExecutor;
import me.maplef.questscrollsv2.plugincore.MainBusiness;
import me.maplef.questscrollsv2.plugincore.ToolClass;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.Objects;

public class Manualcraft implements Listener {

    @EventHandler
    public void craft(CraftItemEvent event){
        for (HumanEntity humanEntity : event.getInventory().getViewers()) { //获取能看到这个物品栏的玩家
            if(QuestscrollsCommandExecutor.isQuery){
                Main.SendManualObject(Objects.requireNonNull(humanEntity.getKiller()),"craft(合成)", ToolClass.nmsitem(event.getCurrentItem()));
            }
            new MainBusiness().Task("put", ToolClass.nmsitem(event.getCurrentItem()), Objects.requireNonNull(humanEntity.getKiller()),event.getRecipe().getResult().getAmount());
        }

    }

    }

