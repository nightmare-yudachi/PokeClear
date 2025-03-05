package lt.pokeclear.common.api.enums;

import lt.pokeclear.common.laotou.PokeClear;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum MessageType {
    PLAYER,
    CONSOLE,
    ALL;

    public void sendMessage(String message){
        switch (this){
            case PLAYER:{
                for (Player player : Bukkit.getOnlinePlayers())
                    player.sendMessage(message);
                break;
            }
            case CONSOLE:{
                PokeClear.INSTANCE.getLogger().info(message);
                break;
            }
            case ALL:{
                Bukkit.getServer().broadcastMessage(message);
                break;
            }
        }
    }
}
