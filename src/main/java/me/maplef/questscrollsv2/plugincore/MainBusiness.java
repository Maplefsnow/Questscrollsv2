package me.maplef.questscrollsv2.plugincore;

/*
* 主要业务代码区
* 代码逻辑:
*
*  先对玩家执行的具体操作触发的事件传入的 行为 对象 和 数量 进行记录
*  对玩家背包中的任务卷轴进行 获取
*  获取获取到的任务卷轴名对应的任务卷轴对象 ( 从 manualList 中 )
*  将符合 行为和对象 的任务lore 筛选出来
*  将 任务lore 的小节符号 和 数字删除
*  将 获取的任务卷轴道具的 lore取出
*   判断是否以 &m 开头 (开头则意味着 这个已经完成了 continue)
*  将 道具任务lore 的小节符号 和 数字 删除
*  比对lore是否一致
*
*  一致则
*  获取 <d> 的位置的数字 自增 数量是触发事件传入的数量
*  之后 和 对应任务lore的 <m> 对比
*  相同则 在 首部 加上 &m 删除线
*
* */

import me.maplef.questscrollsv2.Main;
import me.maplef.questscrollsv2.resource.ReadConfig;
import me.maplef.questscrollsv2.resource.ReadLanguage;
import me.maplef.questscrollsv2.resource.ReadManual;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainBusiness {

        // 检测背包里 有没有任务卷轴 并且返回背包里 全部的任务卷轴的集合！
    public void Task(String behavior, String object, Player player,int quantity){
        //如果你需要修改 最大的 目标值 请修改 [0-99999] 的 99999 应该很简单吧
        Pattern yuanzi = Pattern.compile("[0-99999]\\d*");    //创建正则表达式的原子

        Pattern YuanZi = Pattern.compile("§.");    //创建正则表达式的原子

        //获取 获得到的 String物品名(以物品名去找对应的任务卷轴对象) 以及 ItemStack（具体的任务卷轴物品）
        HashMap<String, ItemStack> items = ToolClass.knapsack(player.getInventory());

        if(items.size() == 0){ //一个都找不到就直接结束方法吧
            return;
        }
        //遍历背包中符合的 任务卷轴 的名字
        for (String s : items.keySet()){

            if(!ToolClass.Guishu(items.get(s),player.getName())){ //任务卷轴的归属
                player.sendMessage(ReadLanguage.Not_your_own);
                continue;
            }

            if(ReadConfig.Delete_every_other_day){ //配置文件中 是否定义了任务卷轴具有时效性

                if(!ToolClass.Riqi(items.get(s))){ //日期是否准确无误?

                    player.sendMessage(ReadLanguage.Delete_Reel);
                    player.getInventory().remove(items.get(s)); //删除此卷轴

                }

            }

            //返回道具中的 有效任务的lore
            List<String> LS = Objects.requireNonNull(items.get(s).getItemMeta()).getLore();


            assert LS != null;
            LS = LS.subList( //截取一整个道具lore中 有效的任务lore部分
                    1 + ReadManual.manualList.get(s).loreTolenth(),
                    1 + ReadManual.manualList.get(s).getrandom()
                    );

            //获取匹配的索引的内容,如果什么都没获取到 就跳过
            if(ReadManual.manualList.get(s).isbehaviors(behavior,object).size() < 1) {
                continue;
            }

            //返回道具名对应的任务卷轴对象的 行为和对象都符合的 任务lore
            for (String isbehavior : ReadManual.manualList.get(s).isbehaviors(behavior, object)) {

                    //背包中的任务卷轴的全部任务lore
                    for (String S:LS){

                        //以 §7§m 开头的任务将被视为已完成
                        if(S.startsWith("§7§m")){
                            continue;
                        }

                        //对字符串 A 和 B 进行修改 方便判定
                        String A = isbehavior.replaceAll("<d>","");
                        A = A.replaceAll("<m>","");
                        A = A.replaceAll("\\d","");
                        A = A.replaceAll("§","");

                        String B = S.replaceAll("\\d","");
                        B = B.replaceAll("§","");

                        //假如字符串匹配则 , 不匹配的话是不会进入的
                        if(A.equalsIgnoreCase(B)){
                            //修改lore里的成绩
                            int ls = 0;

                            Matcher zf1 = yuanzi.matcher(S.substring(ReadManual.manualList.get(s).SuoyingD(isbehavior)));

                            if(zf1.find()){

                                //获取的正是这个值
                                ls = Integer.parseInt(zf1.group());

                            }

                            //对有效的任务lore修改 计数
                            String Tasklore = isbehavior.replaceAll("<d>",ls+quantity+"");

                            Tasklore = Tasklore.replaceAll("<m>",ReadManual.manualList.get(s).getquantity(isbehavior)+"");

                            //修改正在执行的任务 ( 用于papi变量 )
                            Main.ExecuteTask.put(player.getName(),Tasklore);

                            //假如任务的目标已经达成
                            if(ReadManual.manualList.get(s).Reached((ls+quantity),isbehavior)){

                                //如果此任务完成了 则又没任务可做了 (用于papi变量)
                                Main.ExecuteTask.put(player.getName(),ReadConfig.variable);

                                Matcher zf2 = YuanZi.matcher(Tasklore);

                                while (zf2.find()){ //删除所有的颜色字符
                                    Tasklore = Tasklore.replaceAll(zf2.group(),"");
                                }
                                player.sendMessage(ReadLanguage.Finish_One_Task); //发送消息
                                Tasklore = "§7§m" + Tasklore;

                                if(ReadConfig.sound_effect){ //是否播放音效

                                    player.playSound(player.getLocation(), Sound.valueOf(ReadConfig.sound_on_complete), 5, 1);

                                }

                            }


                            //获取此物品的lore方便修改
                            List<String> Lores = Objects.requireNonNull(items.get(s).getItemMeta()).getLore();

                            //找到对应的lore的位置
                            assert Lores != null;
                            int suoying = Lores.indexOf(S);


                            //替换lore集合指定位置的lore
                            Lores.set(suoying,Tasklore);

                            //假如此时的字符串是刚替换成 §7§m 的 说明玩家刚完成了一条任务
                            //那么此时 则会开始判定 是否全部的任务lore 都是 §7§m开头?
                            if(Tasklore.startsWith("§7§m")) {
                                //获取任务lore的 长度
                                int len = ReadManual.manualList.get(s).getrandom() - ReadManual.manualList.get(s).loreTolenth();
                                for (String tasklores : Lores.subList(
                                        ReadManual.manualList.get(s).loreTolenth()+1,
                                        ReadManual.manualList.get(s).getrandom()+1
                                )) {
                                    if(tasklores.startsWith("§7§m")){
                                        //如果这条lore是 完成状态
                                        //则将剩余 未完成lore --
                                        len --;
                                    }
                                }

                                //没有剩余未完成的任务lore了
                                if(len == 0){
                                    //视为任务全部完成
                                    player.getInventory().remove(items.get(s)); //删除任务卷轴
                                    player.getInventory().addItem(ReadManual.manualList.get(s).giveKit()); //给予奖励卷轴

                                    //给予对应的完成奖励卷轴

                                    player.sendMessage(ReadLanguage.Finish_All_Task);//发送消息
                                    PlayerManualQuantity.addplayerquantity(player.getName()); //将玩家的值 (完成的任务卷轴总数) 自增

                                    if(ReadConfig.sound_effect){ //是否播放音效

                                        player.playSound(player.getLocation(), Sound.valueOf(ReadConfig.sound_on_complete_all), 5, 1);

                                    }

                                    //插件是否开启了 累计奖励
                                    if(ReadConfig.commands.size() != 0){ //如果插件开启了 那么 就不会是 0 ( 如果是0就意味着没配置 既然没配置那么也没有执行的必要了)

                                        //假如 有对应 玩家现在累计达成次数的 key 则执行指令 ( 不必担心重复,因为下一次进入这里的时候他的累计值已经自增了 )
                                        if(ReadConfig.commands.containsKey(PlayerManualQuantity.getplayerdata(player.getName()))){

                                            //执行指令
                                            for(String command : ReadConfig.commands.get(PlayerManualQuantity.getplayerdata(player.getName()))){

                                                //command 就是指令
                                                String a = command.replaceAll("<player>", player.getName());   //将<player>替换为玩家名
                                                if (player.isOp()) { //是op则正常执行
                                                    player.performCommand(a);
                                                } else {    //不是op则先给予op
                                                    player.setOp(true);
                                                    player.performCommand(a);
                                                    player.setOp(false);
                                                }

                                            }

                                        }

                                    }

                                    return; //既然都完成了 就直接结束了

                                }

                            }

                            //替换道具lore
                            // 获取玩家背包中此物品的位置
                            int wz = player.getInventory().first(items.get(s));

                            //获取这件物品的副本
                            ItemStack lsitem = items.get(s);

                            //获取这件物品副本的 meta 并修改 lore
                            ItemMeta itemMeta = lsitem.getItemMeta();
                            assert itemMeta != null;
                            itemMeta.setLore(Lores);
                            lsitem.setItemMeta(itemMeta);

                            //替换原物品
                            player.getInventory().setItem(wz,lsitem);

                            }

                        }
                    }
            }

        }


    }

