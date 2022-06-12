package me.maplef.questscrollsv2.resource;

import me.maplef.questscrollsv2.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/*
* 防呆设计 防不住故意为之的人
* 只能用于 提示 疏忽 遗漏的 小细节
* 如果大面积 漏写 故意 大面积漏写
* 那只能报错！
* */

public class ReadManual {

    public static ArrayList<String> lores = new ArrayList<>();    //暂存全部任务lore
    public static ArrayList<String> behaviors = new ArrayList<>();//暂存全部任务行为
    public static ArrayList<String> objects = new ArrayList<>();  //暂存全部任务对象
    public static ArrayList<Integer> quantity = new ArrayList<>();//暂存全部任务数量
    public static ArrayList<Integer> D = new ArrayList<>();       //暂存全部<d> 位置
    public static ArrayList<Integer> M = new ArrayList<>();       //暂存全部<m> 位置
    public static ArrayList<String> LoreHead = new ArrayList<>();
    public static ArrayList<String> LoreTail = new ArrayList<>();
    public static ArrayList<String> JLores = new ArrayList<>();



    public static File file;

    public static HashMap<String,Manuals> manualList = new HashMap<>();

    public ReadManual(String p){

        file = new File(p); //构建方法只负责赋予文件对象

        reloadManual(); //开始读取

    }

    public static void reloadManual(){

        for (String s : Objects.requireNonNull(file.list())) {                   //遍历Tasks文件夹下的文件

            if (!s.endsWith(".yml")) {    //结尾如果不是 .yml

                Main.mistake(4, s);
                continue;

            }

            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(new File(file, s));    //创建一个读取Tasks文件夹下 任务文件的工具类

            if (manualList.size() != 0) { //判断是否有重复的卷轴名

                if (RepeatTaskName(Objects.requireNonNull(fileConfiguration.getString("Name")).replace("&","§"))) {

                    Main.mistake(5, s);
                    continue;

                }

            }

            for (int a = 0; a < fileConfiguration.getInt("Tasks.total"); a++) {   //先对任务进行循环读取

                lores.add(Objects.requireNonNull(fileConfiguration.getString("Tasks." + (a + 1) + ".lore")).replace("&","§")); //读取任务lore 并添加到 lore暂存

                if (judgementlore(lores.get(a))) {  //如果 <d> 位于 <m> 的前面则

                    lores.set(a, "S"); //将错误的任务lore替换为 S 方便删除
                    continue;

                }

                quantity.add(fileConfiguration.getInt("Tasks." + (a+1) + ".quantity")); //读取任务数量 并添加到数量暂存
                behaviors.add(fileConfiguration.getString("Tasks." + (a+1) + ".behavior")); //读取任务行为 并添加到 行为暂存
                objects.add(Objects.requireNonNull(fileConfiguration.getString("Tasks." + (a + 1) + ".object")).replace("&","§")); //读取任务对象 并添加到 对象暂存
                D.add(lores.get(a).indexOf("<d>") - 3 + (String.valueOf(quantity.get(a)).length())); // 获取 <d> 的位置 并添加到暂存
                M.add(lores.get(a).indexOf("<m>")); // 获取 <m> 的位置 并添加到暂存

            }

            lores.removeIf(S -> S.equals("S"));  //删除无效的任务

            if (lores.size() < fileConfiguration.getInt("Random_quantity")) {    //如果剩余有效的任务小于需要的任务数量
                Main.mistake(3, s);
            } else {

                for (String lore : fileConfiguration.getStringList("LoreHead")) {
                    lore = lore.replace("&","§");
                    LoreHead.add(lore);
                }
                for (String lore : fileConfiguration.getStringList("LoreTail")) {
                    lore = lore.replace("&","§");
                    LoreTail.add(lore);
                }
                for (String lore : fileConfiguration.getStringList("reward_reel.Lores")) {
                    lore = lore.replace("&","§");
                    JLores.add(lore);
                }

                manualList.put(Objects.requireNonNull(fileConfiguration.getString("Name")).replace("&","§"), new Manuals( //创建任务对象 并添加到 任务集合
                        Objects.requireNonNull(fileConfiguration.getString("Name")).replace("&","§"),    //此任务对象的卷轴名
                        fileConfiguration.getString("Type"),    //此任务对象的材质名
                        fileConfiguration.getInt("Random_quantity"),    //此任务对象最大 随机几个任务?
                        new ArrayList<>(lores),  //此任务对象的全部任务lore
                        new ArrayList<>(behaviors),  //此任务对象的全部任务行为
                        new ArrayList<>(objects),    //此任务对象的全部任务对象
                        new ArrayList<>(quantity),   //此任务对象的全部任务数量
                        new ArrayList<>(D),          //此任务对象的全部 <d> 位置
                        new ArrayList<>(M),          //此任务对象的全部 <m> 位置
                        new ArrayList<>(LoreHead),    //此任务对象的任务卷轴的lore头部
                        new ArrayList<>(LoreTail),    //此任务对象的任务卷轴的lore尾部
                        Objects.requireNonNull(fileConfiguration.getString("reward_reel.Name")).replace("&","§"),                    //此任务对象的奖励卷轴的名字
                        Objects.requireNonNull(fileConfiguration.getString("reward_reel.Type")).replace("&","§"),                    //此任务对象的奖励卷轴的材质名
                        new ArrayList<>(JLores),
                        (ArrayList<String>) fileConfiguration.getStringList("reward_reel.Commands"), //此任务对象的奖励卷轴的具体执行指令
                        fileConfiguration.getInt("reward_reel.KitNumber") //奖励占据的格子
                ));

                Bukkit.getServer().getLogger().info(s + " - Tasks: " + lores.size());
//                Main.sendMessage(null, s + " - Tasks: " + lores.size());   //输出 任务lore的总数 代表着 读取到了多少任务

            }

            //清空暂存区 准备迎接下一个任务卷轴.yml
            lores.clear();
            behaviors.clear();
            objects.clear();
            quantity.clear();
            D.clear();
            M.clear();
            LoreHead.clear();
            LoreTail.clear();
            JLores.clear();
        }
        }


    public static boolean judgementlore(String a){

        //lore中是否带了 <d> 和 <m> ?
        if(a.contains("<d>") && a.contains("<m>")){

            //lore中的 <d> 和 <m> 的顺序是否正确?
            if(a.indexOf("<m>") < a.indexOf("<d>")){

                return false;

            }else{
                Main.mistake(2,a);
                return true;
            }

        }else{
            Main.mistake(1,a);
            return true;
        }

    }

    public static boolean RepeatTaskName(String a){

        for(String s:manualList.keySet()){
            if(manualList.get(s).Name.equalsIgnoreCase(a)){
                return true;
            }

        }

        return false;
    }

}
