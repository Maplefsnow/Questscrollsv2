package me.maplef.questscrollsv2.resource;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ReadConfig {

    public static HashMap<Integer,List<String>> commands = new HashMap<>();

    public static boolean Delete_every_other_day;

    public static boolean sound_effect;

    public static String variable;

    public static String sound_on_complete;

    public static String sound_on_complete_all;

    public static String Questscrolls_Rank;

    public static FileConfiguration fileConfiguration; // 配置文件的主要对象

    public static String Gui_Title;

    public static boolean open_gui_cooling;

    public static String task;

    public ReadConfig(FileConfiguration fileConfiguration){ //第一次构建配置参数

        ReadConfig.fileConfiguration = fileConfiguration;

        variable = Objects.requireNonNull(fileConfiguration.getString("variable")).replace("&","§");

        Questscrolls_Rank = Objects.requireNonNull(fileConfiguration.getString("Questscrolls_Rank")).replace("&","§");

        sound_on_complete = fileConfiguration.getString("sound_on_complete");

        sound_on_complete_all = fileConfiguration.getString("sound_on_complete_all");

        Delete_every_other_day = fileConfiguration.getBoolean("Delete_every_other_day");

        sound_effect = fileConfiguration.getBoolean("sound_effect");

        task = fileConfiguration.getString("player_login_givetask");

        Gui_Title = fileConfiguration.getString("Gui_Title", "Title").replace("&","§");

        commands.clear(); //先清空集合 避免万一是reload congfig

        fileConfiguration.getList("rewards");

        if(!fileConfiguration.getBoolean("on_Cumulative")){return;} //如果关闭了累计奖励的功能 就索性不读取了
        for (Object object : fileConfiguration.getList("Cumulative")) {

            String a = object.toString();
            Integer key = Integer.parseInt(
                    a.substring(
                            a.indexOf("{")+1,
                            a.indexOf("=")
                    )
            );
            List<String> value = Arrays.asList(a.substring(
                    a.indexOf("[") +1,
                    a.indexOf("]")
            ).split(","));

            commands.put(key,value);
        }

    }

    public static void reloadopen_gui_cooling(){
        open_gui_cooling = fileConfiguration.getBoolean("open_gui_cooling");
    }

}