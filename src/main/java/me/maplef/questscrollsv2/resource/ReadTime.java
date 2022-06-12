package me.maplef.questscrollsv2.resource;

import me.maplef.questscrollsv2.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReadTime {

    public static FileConfiguration fileConfiguration;

    public static String time;

    public static List<String> playernames = new ArrayList<>();

    public static void loads(){ //读取
        fileConfiguration = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(),"playerlogin.yml"));

        time = fileConfiguration.getString("Time");
        playernames = fileConfiguration.getStringList("players");

        if (time.equalsIgnoreCase("time")) {
            reload();
        }

    }

    public static void reload(){
        //time = System.currentTimeMillis()+"";
        time = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy"));
        playernames.clear();
        saves();

    }

    public static void saves(){ //保存
        fileConfiguration.set("Time",time);
        fileConfiguration.set("players",playernames);

        try {
            fileConfiguration.save(new File(Main.getInstance().getDataFolder(),"/playerlogin.yml"));
        }catch (Exception e){
            System.out.println("");
        }

    }

    /*
    * 传入一个 name 玩家名
    * 查询是否存在 如果存在则 返回 false
    * 不存在 则 添加 并且 保存 随后返回 true
    * */
    public static boolean cha(String name){

        if(playernames.contains(name)){
         return false;
        }

        playernames.add(name);
        saves();
        return true;

    }

}
