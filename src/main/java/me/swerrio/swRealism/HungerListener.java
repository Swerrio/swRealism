package me.swerrio.swRealism;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HungerListener implements Listener {
    private final Main plugin;
    private final PlayerHungerManager hungerManager;

    public HungerListener(Main plugin) {
        this.plugin = plugin;
        this.hungerManager = new PlayerHungerManager(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        hungerManager.addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        hungerManager.removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract1(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.POTION) {
            Player player = event.getPlayer();
            hungerManager.drinkWater(player);

            // Получаем предмет и проверяем его количество
            ItemStack POTION = event.getItem();
            if (POTION.getAmount() > 1) {
                POTION.setAmount(POTION.getAmount() - 1);
                player.getInventory().setItem(event.getHand(), POTION); // Обновляем инвентарь
            } else {
                player.getInventory().removeItem(POTION); // Удаляем бутылку полностью
            }

            // Выдаем пустую бутылку
            player.getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE));
            event.setCancelled(true); // Отменяем событие, чтобы избежать лишних изменений
        }
    }

    @EventHandler
    public void onPlayerInteract2(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.MILK_BUCKET) {
            Player player = event.getPlayer();
            hungerManager.drinkWater(player);

            // Получаем предмет и проверяем его количество
            ItemStack MILK_BUCKET = event.getItem();
            if (MILK_BUCKET.getAmount() > 1) {
                MILK_BUCKET.setAmount(MILK_BUCKET.getAmount() - 1);
                player.getInventory().setItem(event.getHand(), MILK_BUCKET); // Обновляем инвентарь
            } else {
                player.getInventory().removeItem(MILK_BUCKET); // Удаляем бутылку полностью
            }

            // Выдаем пустую бутылку
            player.getInventory().addItem(new ItemStack(Material.BUCKET));
            event.setCancelled(true); // Отменяем событие, чтобы избежать лишних изменений
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        hungerManager.addPlayer(player); // Восстанавливаем жажду при респауне
    }
}
