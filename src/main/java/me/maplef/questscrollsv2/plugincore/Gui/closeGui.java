package me.maplef.questscrollsv2.plugincore.Gui;

import me.maplef.questscrollsv2.resource.ReadConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class closeGui implements Listener {

    @EventHandler
    public void close(InventoryCloseEvent event){

        if(event.getView().getTitle().equalsIgnoreCase(ReadConfig.Gui_Title)){

            ItemStack[] wp = event.getInventory().getContents(); //获得所有物品

            wp[0].setAmount(0); //删除第一个物品

            for (ItemStack itemStack:wp){
                if(itemStack != null){

                    if(itemStack.getAmount() != 0) {
                        event.getPlayer().getInventory().addItem(itemStack);
                    }

                }
            }

        }

    }

}
