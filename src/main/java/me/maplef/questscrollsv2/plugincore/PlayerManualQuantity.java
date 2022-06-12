package me.maplef.questscrollsv2.plugincore;

import me.maplef.questscrollsv2.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class PlayerManualQuantity {

    public static YamlConfiguration fileConfiguration;

    public static File file;

    public static HashMap<String,Integer> playerdata = new HashMap<>();

    public static void reloadPlayerManualQuantity(){
        //创建 file
        file = new File(Main.getPlugin(Main.class).getDataFolder(),"\\playerdata.yml");

        if(!file.exists()){  //不存在的话才会创建文件

            try {
                file.createNewFile();
            }catch (Exception ignored){}

        }

        //创建 file的操作类
        fileConfiguration = YamlConfiguration.loadConfiguration(file);

        //循环读取
        for (String key : fileConfiguration.getKeys(false)) {

            playerdata.put(key,fileConfiguration.getInt(key));

        }

    }

    public static void inputplayerdata(){

        //重新将内容写入
        for (String s : playerdata.keySet()) {

            if(fileConfiguration.getKeys(false).contains(s)){
                //有就更改
                fileConfiguration.set(s,playerdata.get(s));
            }else {
                //无则添加
                fileConfiguration.set(s,playerdata.get(s));
            }

        }
            try { //保存文件
                fileConfiguration.save(file);
            }catch (Exception ignored){}

    }

    public static Integer getplayerdata(String s){

        //如果是空的
        if(playerdata.keySet().size()==0){
            //添加 玩家名 值 就是 0
            playerdata.put(s,0);
            //写入配置文件 ( 玩家数据需要随时保存,不然会丢失！ )
            inputplayerdata();

            //如果不是空的 那么判断里面有没有我?
        }else if(playerdata.containsKey(s)){

            //有就返回具体值
            return playerdata.get(s);

            //没有就添加 并且 保存数据
        }else{
            playerdata.put(s,0);
            inputplayerdata();
        }
        return 0;

    }

    //增加玩家数据
    public static void addplayerquantity(String s){

        //先确定有没有
        if(playerdata.containsKey(s)){

            //将值自增一次
            playerdata.put(s,playerdata.get(s)+1);
            //保存数据

        }else{

            //没有就添加
            playerdata.put(s,1);  //为什么 1 呢？ 因为如果进入了这一条 只能是 ( 玩家达成了任务卷轴然后数据文件里没有 )
            //即刻保存

        }
        inputplayerdata();

    }

}
