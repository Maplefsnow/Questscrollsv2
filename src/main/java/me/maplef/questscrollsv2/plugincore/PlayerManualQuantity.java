package me.maplef.questscrollsv2.plugincore;

import me.maplef.questscrollsv2.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class PlayerManualQuantity {

    public static YamlConfiguration fileConfiguration;

    public static File file;

    public static HashMap<String,Integer> playerdata = new HashMap<>();

    //我是专门排序用的
    public static HashMap<String,Integer> Paixu = new HashMap<>();

    public static void reloadPlayerManualQuantity(){

        //创建 file
        file = new File(Main.getInstance().getDataFolder(),"/playerdata.yml");

        if(!file.exists()){  //不存在的话才会创建文件

            try {
                file.createNewFile();
            }catch (Exception e){
                System.out.println(e.getMessage());
                Main.senmessage(null,4);
            }

        }

        //创建 file的操作类
        fileConfiguration = YamlConfiguration.loadConfiguration(file);

        //循环读取
        for (String key : fileConfiguration.getKeys(false)) {

            playerdata.put(key,fileConfiguration.getInt(key));

        }
        //非0 排序
        if(playerdata.size() != 0) {
            px();
        }

    }

    public static void inputplayerdata(){
        px();

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

    //排序
    public static void px(){
        //对 Map 的值排序 然后 重新放入另一个 Map中
        //TreeMap 只能对 键排序 不能对值 排序
        //同样的 Stream流 是 1.8 的新特性
        Paixu = playerdata
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .collect(
                        toMap(Map.Entry :: getKey, Map.Entry::getValue,(e1, e2) -> e2,
                                LinkedHashMap::new));
    }

    //次数排名
    public static String Pm(Integer mc,String name){

        //假如给的排名 甚至超过了总共有记载的玩家数量 则这个名次是无效的
        //或者传入的是 0
        if( (playerdata.keySet().size() < mc)){
            return "暂无";
        }

        //我们得到了一个 对 玩家数据进行排序过的 新集合 我把他命名为 Ls(临时)
        //在正式使用前 我们需要将 Set 转为 List 方便操作
        List<String> Ls = new ArrayList<>(Paixu.keySet());

        //如果是0 那么意味着 是查询玩家在 集合中的位置
        if(mc == 0){

            //有吗?
            if(Ls.contains(name)) {
                //同样的索引值是从 0 开始 所以实际的查询结果是会 小1的
                return (Ls.indexOf(name) + 1) + "";
            }
            return "暂无";
        }

        //为什么要减去? 因为 索引从 0 开始
        mc = mc-1;

        //如果不是则
        return Ls.get(mc);
    }

}
