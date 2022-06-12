package me.maplef.questscrollsv2;

import me.maplef.questscrollsv2.TaskEvents.*;
import me.maplef.questscrollsv2.command.QuestscrollsCommandExecutor;
import me.maplef.questscrollsv2.plugincore.Gui.closeGui;
import me.maplef.questscrollsv2.plugincore.Gui.operateGui;
import me.maplef.questscrollsv2.plugincore.PlayerManualQuantity;
import me.maplef.questscrollsv2.plugincore.QuestscrollsPapi;
import me.maplef.questscrollsv2.plugincore.useReward;
import me.maplef.questscrollsv2.resource.ReadConfig;
import me.maplef.questscrollsv2.resource.ReadLanguage;
import me.maplef.questscrollsv2.resource.ReadManual;
import me.maplef.questscrollsv2.resource.ReadTime;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;

public class Main extends JavaPlugin {

    public static String Banben = "";

    public static HashMap<String,String> ExecuteTask = new HashMap<>();

    public static HashMap<String,Long> coolings = new HashMap<>();

    private static Main instance;

    @Override
    public void onEnable() {

        //获取当前版本的 nms 字段
        String packet = Bukkit.getServer().getClass().getPackage().getName();
        Banben = packet.substring(packet.lastIndexOf('.') + 1);

        getLogger().info("插件加载中..." + Banben);

        getLogger().info("版本: 5.0 作者: BIDE");
        getLogger().info("最终版 此插件已放弃维护");

        instance = this;

        getLogger().info("正在创建各种文件...");

        saveDefaultConfig();                                      //创建配置文件

        File mulu = new File(getDataFolder(), "/Tasks");   //创建 Tasks 目录

        if (!mulu.exists()) { //如果此文件夹不存在

            if(mulu.mkdirs()){ //补全此文件夹

                InputStream input = this.getResource("Tasks/task1.yml");

                getLogger().info("系统自动生成一份可供参考的任务文件");

                try {

                    OutputStream oput = new FileOutputStream(mulu.getPath()+"/task1.yml");
                    byte[] ls = new byte[1024];
                    int a;

                    while(true){
                        assert input != null;
                        if (!((a = input.read(ls)) > 0)) break;
                        oput.write(ls,0,a);
                    }

                    input.close();
                    oput.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{

                getLogger().info("创建文件夹失败！");

            }

        }


        getLogger().info("加载玩家数据文件中...");
        PlayerManualQuantity.reloadPlayerManualQuantity(); //读取玩家数据文件

        getLogger().info("读取语言文件中");

        if(!new File(getDataFolder(),"Language.yml").exists()) { //文件不存在才会创建
            saveResource("language.yml", false);   //创建语言文件 不覆盖
        }
        ReadLanguage.reloadLanguage();

        if(!new File(getDataFolder(),"playerlogin.yml").exists()) { //文件不存在才会创建
            saveResource("playerlogin.yml", false);   //
        }
        ReadTime.loads();   //

        getLogger().info("注册指令中");

        Objects.requireNonNull(this.getCommand("questscrolls")).setExecutor(new QuestscrollsCommandExecutor()); //注册指令


        getLogger().info("读取配置文件中");

        new ReadConfig(getConfig());

        ReadConfig.reloadopen_gui_cooling();


        getLogger().info("读取任务中...");

        new ReadManual(mulu.getPath());

        getLogger().info("注册事件中...");

        Bukkit.getPluginManager().registerEvents(new Manualdestroy(), this); //玩家破坏物品事件
        Bukkit.getPluginManager().registerEvents(new Manualkill(), this); //玩家击杀事件
        Bukkit.getPluginManager().registerEvents(new Manualput(),this); //玩家放置事件
        Bukkit.getPluginManager().registerEvents(new Manualcraft(),this); //玩家合成事件 有问题
        Bukkit.getPluginManager().registerEvents(new Manualfish(),this); //玩家钓鱼事件
        Bukkit.getPluginManager().registerEvents(new Manualenchant(),this); //玩家附魔事件
        Bukkit.getPluginManager().registerEvents(new Manualeat(),this); //玩家食用
        Bukkit.getPluginManager().registerEvents(new Manualdamage(),this); // 损坏物品
        Bukkit.getPluginManager().registerEvents(new Manualshear(),this); // 剪刀
        Bukkit.getPluginManager().registerEvents(new Manualupgrade(),this);// 升级
        Bukkit.getPluginManager().registerEvents(new Manualspeak(),this); // 发言
        Bukkit.getPluginManager().registerEvents(new Manualcommands(),this); //指令

        Bukkit.getPluginManager().registerEvents(new PlayerLogin(),this); //玩家登录

        Bukkit.getPluginManager().registerEvents(new closeGui(),this); //关闭Gui
        Bukkit.getPluginManager().registerEvents(new operateGui(),this); //玩家操作 GUI
        Bukkit.getPluginManager().registerEvents(new useReward(),this); //玩家使用卷轴


        if( Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){ //注册中

            getLogger().info("检测到前置插件 PlaceholderAPI ;");
            if(new QuestscrollsPapi().register()){
                getLogger().info("变量注册成功");
            }else{
                getLogger().info("变量注册失败！ 请联系作者！");
            }

        }else{
            getLogger().info("[非必要] 未安装前置 PlaceholderAPI");
            getLogger().info("无法使用变量功能");
        }

    }

    @Override
    public void onDisable() {
        getLogger().info("欢迎你的下次使用~");
    }

    public static void sendMessage(CommandSender c, String s){

        if(c instanceof Player){
            c.sendMessage(s);
        }else{
            Bukkit.getServer().getLogger().info(s);
        }

    }

    public static void mistake(int a,String b){

        switch (a){
            case 1:{
                getPlugin(Main.class).getLogger().info("ERROR！  =》 " +b+ " 《=");
                getPlugin(Main.class).getLogger().info("lore中必须包含 <d> 和 <m> !!");
                getPlugin(Main.class).getLogger().info("错误的任务lore不会加载！");
                getPlugin(Main.class).getLogger().info("");
                break;
            }
            case 2:{
                getPlugin(Main.class).getLogger().info("ERROR！  =》 " +b+ " 《=");
                getPlugin(Main.class).getLogger().info("<d> 必须位于 <m> 的后面！");
                getPlugin(Main.class).getLogger().info("错误的任务lore不会加载！");
                getPlugin(Main.class).getLogger().info("");
                break;
            }
            case 3:{
                getPlugin(Main.class).getLogger().info("ERROR! : "+b+" 剩余的任务数量");
                getPlugin(Main.class).getLogger().info("远远小于 此任务卷轴随机需要的数量");
                getPlugin(Main.class).getLogger().info("因此 此任务卷轴将不会被加载");
                getPlugin(Main.class).getLogger().info("");
                break;
            }
            case 4:{
                getPlugin(Main.class).getLogger().info("ERROR! : "+b+"");
                getPlugin(Main.class).getLogger().info("请不要混入奇怪的文件！");
                getPlugin(Main.class).getLogger().info("");
                break;
            }
            case 5:{
                getPlugin(Main.class).getLogger().info("ERROR! : "+b+"");
                getPlugin(Main.class).getLogger().info("已在其他的 任务卷轴中 存在!");
                getPlugin(Main.class).getLogger().info("请不要在不同的卷轴中 用同样的Name值");
                getPlugin(Main.class).getLogger().info("");
                break;
            }
            case 6:{
                getPlugin(Main.class).getLogger().info("对应的玩家不在线！");
                getPlugin(Main.class).getLogger().info("");
                break;
            }
            case 7:{
                getPlugin(Main.class).getLogger().info("你输入的任务卷轴不存在,请查询可用卷轴!");
                getPlugin(Main.class).getLogger().info("/questscrolls list");
                getPlugin(Main.class).getLogger().info("");
            }
            default:{
                break;
            }
        }


    }

    public static Player giveplayer(String name){

        return getPlugin(Main.class).getServer().getPlayer(name);

    }

    public static boolean IsOnline(Player player){

        return getPlugin(Main.class).getServer().getOnlinePlayers().contains(player);

    }

    public static void senmessage(Player player,int m){

        switch (m){
            case 1:{
                player.sendMessage("§c 玩家不在线或不存在!");
                break;
            }
            case 2:{
                player.sendMessage("§c不存在此任务！！");
                player.sendMessage("§c你可以使用 /questscrolls list 显示可用卷轴");
                break;
            }
            case 3:{
                player.sendMessage("§a 奖励已经发放给你了！");
                break;
            }
            case 4:{
                getPlugin(Main.class).getLogger().info("玩家数据文件初始化失败！");
                break;
            }
            default:{
                break;
            }
        }

    }

    public static void SendManualObject(Player player,String a,String b){

        player.sendMessage("§7你发生的行为" + a);
        player.sendMessage("§7所获取的对象是" + b);

    }

    public static FileConfiguration getconfig(){
        Main.getPlugin(Main.class).reloadConfig();
        return Main.getPlugin(Main.class).getConfig();
    }

    public static Main getInstance() {
        return instance;
    }

}