package net.re_renderreality.bigbertha.achievements;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

import java.util.ArrayList;
import java.util.List;

public class Achievements {
    public static AchievementPage page;

//    public static Achievement clearVision;
//    public static Achievement specialOres;

    public static void init() {
        List<Achievement> achievements = new ArrayList<Achievement>();

        page = new AchievementPage("Big Bertha", achievements.toArray(new Achievement[achievements.size()]));
        AchievementPage.registerAchievementPage(page);
    }

    public static void trigger(EntityPlayer player, Achievement achievement) {
        if (achievement.parentAchievement != null) {
            trigger(player, achievement.parentAchievement);
        }
        //@todo
//        player.triggerAchievement(achievement);
    }
}
