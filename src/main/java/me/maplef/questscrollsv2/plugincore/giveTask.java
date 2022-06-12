package me.maplef.questscrollsv2.plugincore;


import me.maplef.questscrollsv2.Main;
import me.maplef.questscrollsv2.resource.Manuals;
import me.maplef.questscrollsv2.resource.ReadManual;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class giveTask {

    public static void give(String name, String s, CommandSender commandSender){

        //获取 Manual对象

        s = s.replace("&","§");

        if (ReadManual.manualList.containsKey(s)) {   //确定你输入的东西没问题

            Manuals manuals = ReadManual.manualList.get(s);

            if (!manuals.giveItem(Main.giveplayer(name))) {
                if(commandSender instanceof Player){
                    Main.senmessage((Player) commandSender,1);
                }else{
                    Main.mistake(6,"");
                }
            }

        }else{ //输入的任务不存在则

            if(commandSender instanceof Player){
                Main.senmessage((Player) commandSender,2);
            }else{
                Main.mistake(7,"");
            }

        }

    }

    public static void giveKit(String name, String s, CommandSender commandSender){

        s = s.replace("&","§");

        if (ReadManual.manualList.containsKey(s)) {   //确定你输入的东西没问题

            Manuals manuals = ReadManual.manualList.get(s);

            if(Main.IsOnline(Main.giveplayer(name))){

                Main.giveplayer(name).getInventory().addItem(manuals.giveKit()); //给予奖励卷轴

            }else{ //玩家不在线则

                if(commandSender instanceof Player){
                    Main.senmessage((Player) commandSender,1);
                }else{
                    Main.mistake(6,"");
                }

            }

        }else{ //输入的任务不存在则

            if(commandSender instanceof Player){
                Main.senmessage((Player) commandSender,2);
            }else{
                Main.mistake(7,"");
            }

        }

    }

}
