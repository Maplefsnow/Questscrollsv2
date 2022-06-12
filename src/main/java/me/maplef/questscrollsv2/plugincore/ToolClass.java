package me.maplef.questscrollsv2.plugincore;

import me.maplef.questscrollsv2.Main;
import me.maplef.questscrollsv2.resource.ReadManual;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.time.LocalDate;
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

        try {

            //v1_16_R3

            Class<?> obi = Class.forName("org.bukkit.craftbukkit."+ Main.Banben+".inventory.CraftItemStack"); //获取 obi 目录

            Class<?> nms = Class.forName("net.minecraft.server."+Main.Banben+".ItemStack"); //获取 nms 目录

            Method asnmscopy = obi.getMethod("asNMSCopy",ItemStack.class); //获取 asNMSCopy 方法

            Object item = asnmscopy.invoke(null,itemStack); //使用 asNMSCopy 方法

            Method string = nms.getMethod("getName"); //获取 getName 方法

            Object zifu = string.invoke(item); //使用 getName 方法 且 对象是 asNMSCopy获取的对象

            if(isb()){
                return isz(zifu);
            }else{
                return zifu+""; //版本大于 1.12 则
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
            return "null";
        }

    }

    public static String nmsblock(Block block){

        if(isb()){

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

    public static boolean isb(){
        int a = Main.Banben.indexOf("_");
        a++;
        int b = Main.Banben.indexOf("_",a);
        String c = Main.Banben.substring(a,b);
        return Integer.parseInt(c) > 12;
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
        ItemStack[] inventory = player.getInventory().getStorageContents();

        int emptyCnt = 0;
        for (ItemStack itemStack : inventory) {
            if (itemStack == null) emptyCnt++;
        }

        return emptyCnt >= a;
    }
}
