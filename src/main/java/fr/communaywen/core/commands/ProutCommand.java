package fr.communaywen.core.commands;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Cooldown;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * THE Prout command.
 *
 * Usage: /prout
 * Permission: PREFIX.command.prout
 */
public final class ProutCommand {
    @Command("prout")
    @Description("Prout !")
    @CommandPermission("ayw.command.prout")
    @Cooldown(value = 5, unit = TimeUnit.MINUTES)
    public void onCommand(Player player) {
        player.sendMessage("§2Beuuurk, ça pue !");

        // Make the player jump
        final Vector currentVelocity = player.getVelocity();
        currentVelocity.setY(0.55d);

        player.setVelocity(currentVelocity);

        // Spawn some cloud particles
        final Location location = player.getLocation();
        final @Nullable World world = location.getWorld();

        if (world != null) {
            world.spawnParticle(Particle.CLOUD, location, 3, 0.02d, -0.04d, 0.02d, 0.09d);

            // Funny sound!
            world.playSound(location, Sound.ENTITY_VILLAGER_NO, 0.8f, 2.3f);
            world.playSound(location, Sound.ENTITY_GOAT_EAT, 0.7f, 0.2f);
        }

        // Add glowing effect for 30 seconds
        addGlowingEffect(player);

        // Broadcast the message
        String broadcastMessage = "[§c§l§ka§r] §f§lPROUT !!! §r" + player.getName() + " a §f§lpété§r. §2§lBeurk !";
        Bukkit.broadcastMessage(broadcastMessage);
    }

    private void addGlowingEffect(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("glowGreen");

        if (team == null) {
            team = scoreboard.registerNewTeam("glowGreen");
            team.setColor(org.bukkit.ChatColor.GREEN);
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }

        final Team finalTeam = team;
        team.addEntry(player.getName());
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 600, 0, false, false, true));

        Bukkit.getScheduler().runTaskLater(AywenCraftPlugin.getInstance(), () -> finalTeam.removeEntry(player.getName()), 600L);
    }
}
