package me.maplef.questscrollsv2.plugincore;

import me.maplef.questscrollsv2.Main;
import me.maplef.questscrollsv2.resource.ReadConfig;
import me.maplef.questscrollsv2.resource.ReadManual;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class ToolClass {


    //检测背包并返回任务卷轴道具集合
    public static HashMap<String, ItemStack> knapsack(Inventory inventory){
        //创建一个集合用于存放 获取到的任务卷轴的集合
        HashMap<String,ItemStack> ls = new HashMap<>();
        //遍历背包里的全部道具
        for (ItemStack itemStack : inventory.getContents()){

            //如果道具的名字 在 任务卷轴集合中存在
            if(itemStack == null){continue;}
            if(ReadManual.RepeatTaskName(Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName())){

                //判定这是一个有效的任务卷轴 并添加到集合中
                ls.put(itemStack.getItemMeta().getDisplayName(),itemStack);

            }

        }

        //返回集合
        return ls;
    }

    // Nms 工具方法

    public static String nmsitem(ItemStack itemStack){

        if(isb(16)){ //要做多版本兼容性 真的很难啊 ~

            try {

                //v1_16_R3 +
                Class<?> obi = Class.forName("org.bukkit.craftbukkit."+ Main.Banben+".inventory.CraftItemStack"); //获取 obi 目录
                Class<?> nms = Class.forName("net.minecraft.world.item.ItemStack"); //获取 nms 目录
                //高于 1.16 的版本 包名有变
                Method asnmscopy = obi.getMethod("asNMSCopy",ItemStack.class); //获取 asNMSCopy 方法
                Object item = asnmscopy.invoke(null,itemStack); //使用 asNMSCopy 方法
                Method string = nms.getMethod("toString"); //获取 getName 方法
                Object zifu = string.invoke(item); //使用方法 且 对象是 asNMSCopy获取的对象
                String a = zifu+"";
                a = a.replaceAll("\\d","");
                a = a.trim();
                return a;

            }catch (Exception e){
                return "null";
            }

        }
        try {

            //v1_16_R3

            Class<?> obi = Class.forName("org.bukkit.craftbukkit."+ Main.Banben+".inventory.CraftItemStack"); //获取 obi 目录
            Class<?> nms = Class.forName("net.minecraft.server." + Main.Banben + ".ItemStack"); //获取 nms 目录
            //高于 1.16 的版本 包名有变
            Method asnmscopy = obi.getMethod("asNMSCopy",ItemStack.class); //获取 asNMSCopy 方法
            Object item = asnmscopy.invoke(null,itemStack); //使用 asNMSCopy 方法
            Method string = nms.getMethod("getName"); //获取 getName 方法
            Object zifu = string.invoke(item); //使用 getName 方法 且 对象是 asNMSCopy获取的对象
            if(isb(12)){
                return isz(zifu);
            }else{
                return zifu+""; //版本大于 1.12 则
            }

        }catch (Exception e){
            return "null";
        }

    }

    public static String nmsblock(Block block){

        if(isb(12)){

            String a = block.getState().getBlockData().toString();
            int A = a.indexOf(":");
            A++;
            int B = a.indexOf("}");
            return a.substring(A,B);

        }else{

            String a = block.toString();
            int A = a.indexOf("type=");
            A += 5;
            int B = a.indexOf(",",A);
            String z = a.substring(A,B);

            A = a.indexOf("data=");
            A += 5;
            B = a.indexOf("}",A);
            String Z = a.substring(A,B);

            return z+":"+Z;
        }


    }

    public static boolean isb(int bb){
        int a = Main.Banben.indexOf("_");
        a++;
        int b = Main.Banben.indexOf("_",a);
        String c = Main.Banben.substring(a,b);
        return Integer.parseInt(c) > bb;
    }

    public static String isz(Object o){
        String A = o+"";

        int a = A.indexOf("'");
        a++;
        int b = A.indexOf("'",a);
        String c = A.substring(a,b);
        String[] B = c.split("\\.");
        int C = B.length;
        C--;
        return B[C];
    }

    //检测卷轴的 归属有效性
    public static boolean Guishu(ItemStack itemStack,String name){

        return Objects.requireNonNull(Objects.requireNonNull(itemStack.getItemMeta()).getLore()).get(0).equalsIgnoreCase("§7 "+name);

    }


    //检测卷轴的 日期有效性
    public static boolean Riqi(ItemStack itemStack){

        return Objects.requireNonNull(Objects.requireNonNull(itemStack.getItemMeta()).getLore()).get(1).equalsIgnoreCase("§7 "+LocalDate.now());

    }

    //背包空间是否充足
    public static boolean YouKong(int a, Player player){

        int b = 0;
        while (player.getInventory().firstEmpty() != -1){
            b++;
            if(b>a){
                break;
            }
        }

        return b>a;
    }

    //打开提交物品GUI
    public static boolean openGui(Player player){
        try {

            Inventory ls = Bukkit.createInventory(null, 27, ReadConfig.Gui_Title);

            ItemStack ls_item = new ItemStack(Material.PAPER,1);
            ItemMeta ls_meta = ls_item.getItemMeta();
            ls_meta.setDisplayName("§a§l点击提交");
            ls_meta.setLore(Arrays.asList(
                    ""
                    ,"§7 点击逐个将Gui的物品"
                    ,"§7 内容提交到任务卷轴中"
                    ,"§7 达成可能较多时,需要点击多次"
                    ,""
            ));

            if(ToolClass.isb(13)) { //假如版本大于 13 则
                ls_meta.setCustomModelData(10003);
            }

            ls_item.setItemMeta(ls_meta);

            ls.addItem(ls_item);

            player.closeInventory();
            player.openInventory(ls); //打开此物品

            return true;
        }catch (Exception e){
            return false;
        }
    }

    //冷却系统
    public static boolean cooling(Player player){

        if(!ReadConfig.open_gui_cooling){
            return false;
        }

        //判断玩家是否在冷却集合中?
        if(Main.coolings.keySet().contains(player.getName())){

            //在的话获取 集合里的毫秒值 是否 小于当前毫秒值?

            long Time =  Main.coolings.get(player.getName()) - System.currentTimeMillis() ;

            if(Time < 0){

                //意味着时间到了
                Main.coolings.put(player.getName(),2000L);

            }else{

                //冷却还没结束
                player.sendMessage("时间未到"+Time);
                return true;
            }

        }else{            //不在的话添加进冷却集合 值就是获取当前毫秒值 + 3000
            Main.coolings.put(player.getName(),2000L);
        }

        return false;
    }

}
