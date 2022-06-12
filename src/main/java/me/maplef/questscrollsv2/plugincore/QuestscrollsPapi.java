package me.maplef.questscrollsv2.plugincore;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.maplef.questscrollsv2.Main;
import me.maplef.questscrollsv2.resource.ReadConfig;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class QuestscrollsPapi extends PlaceholderExpansion {
    //下面这三个是必须重写的！

    public String getIdentifier() { //注册的变量的变量名
        return "Questscrolls";
    }
    //比如 %TaskManualPapi.xxx%
    public String getAuthor() { //此变量的作者
        return "WeiXiaoDaRen";
    }

    public String getVersion() {    //变量版本
        return "1.0.0";
    }

    public String onPlaceholderRequest(Player player, @NotNull String identifier) { //核心部分

        if(player == null){return "";} //player等于空时

        if(identifier.equalsIgnoreCase("Task")){ //变量名 .xxx 中的 xxx

            //有吗?
            if(Main.ExecuteTask.containsKey(player.getName())){
                //缓存区有记录 就返回
                return Main.ExecuteTask.get(player.getName());
            }else{
                //无则返回默认值
                return ReadConfig.variable;
            }

        }

        if(identifier.equalsIgnoreCase("Number")){    //可以一直这么写 想写几个写几个
            return PlayerManualQuantity.getplayerdata(player.getName())+"";
        }

        return null;
    }
}
//准备完毕后 去插件的主类 new 类名.register()
//类名 就是 现在这个类
// /papi bcparse 玩家名 %变量名_子名% 解析变量{
