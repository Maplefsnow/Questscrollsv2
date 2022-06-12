package me.maplef.questscrollsv2.resource;

/*
* 是 库
* 存放所有读取到的任务
* 任务以对象的形式存在 包括
*
*   0. 此卷轴对应的 Name
*   1. 原lore 集合
*   2. 任务行为 集合
*   3. 任务对象 集合
*   4. 任务目标 集合
*   5. <d> 的位置 集合
*   6. <m> 的位置 集合
*
*   7. 此任务卷轴的材质
*   8. 此任务卷轴的loreTo
*   9. 此任务卷轴的loreWei
*   10. 此任务卷轴的随机任务数量
*
*   10. 奖励卷轴的材质
*   11. 奖励卷轴的名字
*   12. 奖励卷轴的lore
*   13. 奖励卷轴的具体奖励内容
*
* 包含的方法
*
* 1. 判断传入的lore 在本对象中 对应的 原lore 任务行为 等等.... 并判断 行为和对象是否符合
*
*
*
* */

import me.maplef.questscrollsv2.Main;
import me.maplef.questscrollsv2.plugincore.ToolClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Manuals {

    String Name;    //任务卷轴名
    String Type;    //任务卷轴材质
    Integer random; //随机的任务数量
    ArrayList<String> taskLores; //全部任务lore的集合
    ArrayList<String> behaviors ; //全部任务行为的集合
    ArrayList<String> objects ;   //全部任务对象的集合
    ArrayList<Integer> quantity ; //全部的任务数量集合
    ArrayList<Integer> D ;        //全部任务lore的 <d> 位置集合
    ArrayList<Integer> M ;        //全部任务lore的 <m> 位置集合

    Integer CustomModelData;
    Integer KitCustomModelData;

    ArrayList<String> loreHead ;  //任务卷轴的lore头部
    ArrayList<String> loreTail ;  //任务卷轴的lore尾部

    Integer KitNumber;
    String JName;                 //奖励卷轴的名字
    String JType;                 //奖励卷轴的材质
    ArrayList<String> Jlore ;     //奖励卷轴的lore
    ArrayList<String> Commands ;  //奖励卷轴的指令集合

    Boolean Commands_random;

    public Manuals(String Name,     //简单的构建对象赋值 不补充空参构造
                   String Type,
                   Integer random,
                   ArrayList<String> taskLores,
                   ArrayList<String> behaviors,
                   ArrayList<String> objects,
                   ArrayList<Integer> quantity,
                   ArrayList<Integer> D,
                   ArrayList<Integer> M,
                   ArrayList<String> loreHead,
                   ArrayList<String> loreTail,
                   String JName,
                   String JType,
                   ArrayList<String> Jlore,
                   ArrayList<String> Commands,
                   Integer KitNumber,
                   Integer CustomModelData,
                   Integer KitCustomModelData,
                   Boolean Commands_random
    ){
        this.Name = Name;
        this.Type = Type;
        this.random = random;
        this.taskLores = taskLores;
        this.behaviors = behaviors;
        this.objects = objects;
        this.quantity = quantity;
        this.D = D;
        this.M = M;
        this.loreHead = loreHead; //
        this.loreTail = loreTail; //

        this.JName = JName;
        this.JType = JType;
        this.Jlore = Jlore; //
        this.Commands = Commands; //

        this.KitNumber = KitNumber;

        this.CustomModelData = CustomModelData;
        this.KitCustomModelData = KitCustomModelData;

        this.Commands_random = Commands_random;

    }

    //给一个行为 和 对象 返回匹配的 任务lore
    public ArrayList<String> isbehaviors(String behavior,String object){

        ArrayList<String> ls = new ArrayList<>();
        //如果包含此任务行为
        int a = 0;

        for (String s : behaviors) { //遍历全部的行为


            if(s.equalsIgnoreCase(behavior)){   //是否匹配


                if(objects.get(a).equalsIgnoreCase(object)){    //是否对象也对应

                    ls.add(taskLores.get(a));   //添加

                }

            }

            a++; //索引自增
        }

        return ls;
    }

    public int loreTolenth(){
        return loreHead.size()+1;
    }

    public int getrandom(){
        return loreTolenth()+random;
    }

    public int SuoyingD(String a){

        int b = taskLores.indexOf(a); //获取索引

        return D.get(b);

    }

    public int getquantity(String a){
        int A = taskLores.indexOf(a);
        return quantity.get(A);
    }

    public String getName(){
        return this.Name;
    }

    public boolean giveItem(Player player){

        if(!Main.IsOnline(player)){
            return false;
        }

        ItemStack itemStack = new ItemStack(Material.valueOf(Type)); //创建物品

        ItemMeta itemMeta = itemStack.getItemMeta();    //获取物品的ItemMeta

        assert itemMeta != null;
        itemMeta.setDisplayName(Name);  //设置物品的名字

        List<String> lore = new ArrayList<>();  //创建一个临时的lore集合

        lore.add("§7 "+player.getName());   //添加lore 第一行

        lore.add("§7 "+LocalDate.now());    //添加lore 第二行

        lore.addAll(loreHead);              //将lore头添加进来

        Random randoms = new Random();

        int a;
        while (lore.size() < (loreHead.size()+(random+2))){ //生成不重复的随机lore

            a = randoms.nextInt(taskLores.size());  //获取随机数
            String tasklore = taskLores.get(a); //获取对应字符串

            tasklore = tasklore.replaceAll("<d>",0+"");    //修改字符串

            tasklore = tasklore.replaceAll("<m>",quantity.get(a)+""); //修改字符串

            if(lore.contains(tasklore)){    //如果已经存在了 就不添加了
                continue;
            }
            //不存在才添加
            lore.add(tasklore);
        }

        lore.addAll(loreTail); //添加lore的尾部

        itemMeta.setLore(lore);

        if(ToolClass.isb(13)) { //假如版本大于 13 则
            itemMeta.setCustomModelData(CustomModelData);
        }

        itemStack.setItemMeta(itemMeta);

        player.getInventory().addItem(itemStack); //将物品给玩家
        player.sendMessage(ReadLanguage.Give_new_Reel); //发送消息
        return true;
    }

    //是否达成了 目标值
    public boolean Reached(int a,String b){

        return a >= quantity.get(taskLores.indexOf(b));

    }

    public ItemStack giveKit(){
        ItemStack itemStack = new ItemStack(Material.valueOf(JType));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(JName);
        itemMeta.setLore(Jlore);

        if(ToolClass.isb(13)) { //假如版本大于 13 则
            itemMeta.setCustomModelData(KitCustomModelData);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    //核对传入的道具的名字 是否能匹配 此任务卷轴的 奖励卷轴
    //如果匹配则会返回 相应的指令 集合 不匹配则返回 null
    public Boolean checkTaskName(String s,Player player,ItemStack itemStack){

        //是否匹配奖励卷轴名?
        if(JName.equalsIgnoreCase(s)){

            //背包空间是否充足?
            if(ToolClass.YouKong(KitNumber,player)){

                //扣除物品
                itemStack.setAmount(itemStack.getAmount() - 1);   //堆叠数自减 假如为0 则直接没了

                if(Commands_random){ //是否是随机指令？
                    String a = Commands.get(
                            (int)(Math.random()*(Commands.size())) //随机范围 0 - Commands.size() 前开后闭 (0-Commands.size]
                    );

                    a = a.replaceAll("<player>", player.getName());   //将<player>替换为玩家名
                    a = a.trim(); //去除空格

                    if (player.isOp()) { //是op则正常执行
                        player.performCommand(a);
                    } else {    //不是op则先给予op
                        player.setOp(true);
                        player.performCommand(a);
                        player.setOp(false);
                    }
                    Main.senmessage(player,3);

                    return true;
                }

                //执行指令
                for (String command:Commands){

                    //command 就是指令
                    String a = command.replaceAll("<player>", player.getName());   //将<player>替换为玩家名
                    a = a.trim(); //去除空格

                    if (player.isOp()) { //是op则正常执行
                        player.performCommand(a);
                    } else {    //不是op则先给予op
                        player.setOp(true);
                        player.performCommand(a);
                        player.setOp(false);
                    }
                }
                Main.senmessage(player,3);
                return true;
            }else{
                player.sendMessage(ReadLanguage.Not_enough_space);
            }
        }

        return false;
    }
}
