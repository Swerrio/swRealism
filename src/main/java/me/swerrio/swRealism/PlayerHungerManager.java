package me.swerrio.swRealism;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class PlayerHungerManager {
    private final Map<Player, Integer> playerHunger = new HashMap<>();
    private final BossBar bossBar;

    public PlayerHungerManager(Main plugin) {
        bossBar = Bukkit.createBossBar("Жажда",BarColor.BLUE,BarStyle.SEGMENTED_10);
        Bukkit.getScheduler().runTaskTimer(plugin, this::updateHunger, 0, 600);
    }

    public void addPlayer(Player player) {
        playerHunger.put(player, 50);
        bossBar.addPlayer(player);
        updateBossBar(player);
    }

    public void removePlayer(Player player) {
        playerHunger.remove(player);
        bossBar.removePlayer(player);
    }

    private void updateHunger() {
        for (Player player : playerHunger.keySet()) {
            // Проверяем режим игрока
            if (player.getGameMode().equals(org.bukkit.GameMode.CREATIVE) || player.getGameMode().equals(org.bukkit.GameMode.SPECTATOR)) {
                continue; // Пропускаем игроков в креативе и наблюдателе
            }

            int hunger = playerHunger.get(player);
            if (hunger > 0) {
                hunger--;
                playerHunger.put(player, hunger);
                updateBossBar(player);

                if (hunger == 50) {
                    player.sendMessage(ChatColor.BLUE + "Вы устаёте, вам нужно выпить воды");
                }
                if (hunger < 50) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 1));
                }
                if (hunger == 25) {
                    player.sendMessage(ChatColor.BLUE + "Вы в полуобморочном состоянии, вам нужно срочно попить!");
                }
                if (hunger < 25) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 100, 0));
                }
                if (hunger == 1) {
                    player.sendMessage(ChatColor.RED + "Критический запас воды в организме!!!");
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 0));
                }
                if (hunger <= 0) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 2000, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 2000, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 2000, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE, 2000, 1));
//                    player.setHealth(0);
                }
            }
        }
    }


    private void updateBossBar(Player player) {
        int hunger = playerHunger.get(player);
        bossBar.setProgress(playerHunger.get(player) / 100.0);
        // Изменение цвета боссбара в зависимости от уровня жажды
        if (hunger > 36) {
            bossBar.setColor(BarColor.BLUE);
        }
         if (hunger < 35) {
            bossBar.setColor(BarColor.PINK);
        } if (hunger < 15) {
            bossBar.setColor(BarColor.RED);
        }
    }

    public void drinkWater(Player player) {
        int hunger = playerHunger.getOrDefault(player, 100);

        if (hunger >= 99) {
            player.sendMessage(ChatColor.BLUE + "Вы уже напились");
            return; // Если жажда полная, выходим из метода
        }

        hunger = Math.min(hunger + 5, 100);
        playerHunger.put(player, hunger);
        updateBossBar(player);
    }
}
