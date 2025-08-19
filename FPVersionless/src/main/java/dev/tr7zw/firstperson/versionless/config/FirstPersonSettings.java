package dev.tr7zw.firstperson.versionless.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FirstPersonSettings {

    public int configVersion = 2;
    public boolean enabledByDefault = true;
    public int xOffset = 0;
    public int sneakXOffset = 0;

    public int sitXOffset = 0;

    // NEW
    public int swimXOffset = 0;                         // 游泳时 X 轴偏移
    public int crawlXOffset = 0;                        // 爬行时 X 轴偏移
    public boolean swimOrCrawlY = true;                 // 是否启用游泳或爬行时 Y 轴偏移
    // 渲染开关
    public boolean renderSleepingModel = false;         // 睡觉
    public boolean renderSpinAttackModel = false;       // 三叉戟旋风斩
    public boolean renderFlyingModel = false;           // 是否渲染飞行模型
    public boolean renderSwimTransitionModel= false;    // 游泳过渡
    public boolean renderScopingModel = false;          // 望远镜
    public boolean modifyPlayerModel = true;            // 是否修改玩家模型
    public boolean keepModelVisible = false;            // 保持模型显示（除动态模式）

    public boolean renderStuckFeatures = true;
    public VanillaHands vanillaHandsMode = VanillaHands.OFF;
    public boolean dynamicMode = true;
    public boolean vanillaHandsSkipSwimming = true;

    public Set<String> autoVanillaHands = new HashSet<>(Arrays.asList("antiqueatlas:antique_atlas",
            "twilightforest:filled_magic_map", "twilightforest:filled_maze_map", "twilightforest:filled_ore_map",
            "create:potato_cannon", "create:extendo_grip", "create:handheld_worldshaper", "map_atlases:atlas"));

    public Set<String> autoToggleModItems = new HashSet<>(Arrays.asList("exposure:camera"));

}
