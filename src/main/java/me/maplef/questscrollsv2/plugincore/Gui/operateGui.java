package me.maplef.questscrollsv2.plugincore.Gui;

import me.maplef.questscrollsv2.plugincore.MainBusiness;
import me.maplef.questscrollsv2.plugincore.ToolClass;
import me.maplef.questscrollsv2.resource.ReadConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class operateGui implements Listener {

    @EventHandler
    public void operate(InventoryClickEvent event){

        if(event.getView().getTitle().equalsIgnoreCase(ReadConfig.Gui_Title)){ //此物品栏是 提交物品物品栏

            if (!(event.getWhoClicked() instanceof Player)) { return;} //如果不是玩家就 直接结束

            event.setCancelled(false);

            if(event.getRawSlot() == 0){ //1号位置 是按钮的所在位置 不可以被操作！！
                event.setCancelled(true);

                ItemStack[] wp = event.getInventory().getContents(); //获得所有物品
                Player player =  (Player)event.getWhoClicked(); //获得玩家

                for (ItemStack itemStack : wp){

                    if(itemStack == null){
                        continue;
                    }

                    if(itemStack.getItemMeta().hasDisplayName()){ //是否有展示名
                        if(new MainBusiness().Task("submit",itemStack.getItemMeta().getDisplayName(),player,itemStack.getAmount())){ //如果展示名无法提交
                            itemStack.setAmount(0);
                        }else if(new MainBusiness().Task("submit", ToolClass.nmsitem(itemStack),player,itemStack.getAmount())){ //尝试使用 id 提交
                            itemStack.setAmount(0);
                        }
                    }else{
                        if(new MainBusiness().Task("submit", ToolClass.nmsitem(itemStack),player,itemStack.getAmount())){
                            itemStack.setAmount(0);
                        }
                    }

                }

                return;
            }
        }
    }

}
