package lt.pokeclear.common.api.enums;

import lt.pokeclear.common.laotou.PokeClear;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum MessageType {
    PLAYER, //用聊天框提示玩家
    CONSOLE, //只接提示后天
    ALL, //用聊天框提示玩家和后台
    PLAYER_ACTIONBAR;

    public void sendMessage(String message) {
        switch (this) {
            case PLAYER: {
                for (Player player : Bukkit.getOnlinePlayers())
                    player.sendMessage(message);
                break;
            }
            case CONSOLE: {
                PokeClear.INSTANCE.getLogger().info(message);
                break;
            }
            case ALL: {
                Bukkit.getServer().broadcastMessage(message);
                break;
            }
            case PLAYER_ACTIONBAR: {
                for (Player player : Bukkit.getOnlinePlayers())
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                break;
            }
        }
    }
}
