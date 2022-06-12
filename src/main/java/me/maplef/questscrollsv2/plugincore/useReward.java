package me.maplef.questscrollsv2.plugincore;

import me.maplef.questscrollsv2.Main;
import me.maplef.questscrollsv2.command.QuestscrollsCommandExecutor;
import me.maplef.questscrollsv2.resource.ReadLanguage;
import me.maplef.questscrollsv2.resource.ReadManual;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class useReward implements Listener {

    @EventHandler
    public void use(PlayerInteractEvent event){

        if(event.getItem() == null){
            return;
        }

        //判断玩家动作 为 持道具右击空气
        if((event.getAction().name().equalsIgnoreCase("RIGHT_CLICK_AIR"))||(event.getAction().name().equalsIgnoreCase("RIGHT_CLICK_BLOCK"))) {
            //遍历已经成功配置的任务卷轴
            for (String name : ReadManual.manualList.keySet()){

                //部分用户出现了一些比较严重的问题 疑似 并发安全

                try {

                    if (ReadManual.manualList.get(name).checkTaskName(event.getItem().getItemMeta().getDisplayName(), event.getPlayer(),event.getItem())) {

                        return; //结束方法

                    } else {

                    }

                }catch (Exception e){}

            }

            if(QuestscrollsCommandExecutor.isQuery){

                ItemStack ls = event.getItem();
                if(ls.getItemMeta().hasDisplayName()){
                    Main.SendManualObject(event.getPlayer(),"submit(提交)", ls.getItemMeta().getDisplayName());
                }else {
                    Main.SendManualObject(event.getPlayer(),"submit(提交)", ToolClass.nmsitem(ls));
                }

            }

            if(!Objects.requireNonNull(event.getItem().getItemMeta()).hasDisplayName()){ //如果手中物品没有展示名 则没有必要继续执行了！
                return;
            }

            //手持的是否是任务卷轴 且 此任务卷轴中是否有提交道具的任务
            for(String name : ReadManual.manualList.keySet()){

                if(ReadManual.manualList.get(name).getName().equalsIgnoreCase(event.getItem().getItemMeta().getDisplayName())){

                    if (ToolClass.cooling(event.getPlayer())) {
                        return;
                    }

                    //向玩家发送 打开 GUI 的消息
                    event.getPlayer().sendMessage(ReadLanguage.Open_GUI);

                    //为玩家打开GUI
                    ToolClass.openGui(event.getPlayer());

                    return; //结束方法

                } //手中物品的 展示名 是否匹配 某一任务卷轴的展示名

            }

        }
    }

}


/*
public class useReward implements Listener {
    @EventHandler
    public void use(PlayerInteractEvent event){
        if((event.getAction().name().equalsIgnoreCase("RIGHT_CLICK_AIR"))||(event.getAction().name().equalsIgnoreCase("RIGHT_CLICK_BLOCK"))) {
            //遍历已经成功配置的任务卷轴
            if(event.getItem() == null) return;
            String itemName = event.getItem().getItemMeta().getDisplayName();

            for (String name : ReadManual.manualList.keySet()){
                if(ReadManual.manualList.get(name).checkTaskName(itemName, event.getPlayer())){
                    event.getPlayer().getInventory().setItemInMainHand(null);
                }
            }
        }
    }

}
*/
