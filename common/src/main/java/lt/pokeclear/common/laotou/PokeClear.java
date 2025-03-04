package lt.pokeclear.common.laotou;

import lt.pokeclear.common.api.PokeClearAPI;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class PokeClear<SPECIES, POKEMON, POKE_ENTITY> extends JavaPlugin {
    public NormalClear<SPECIES, POKEMON, POKE_ENTITY> normalClear;

    public WaitClear<SPECIES, POKEMON, POKE_ENTITY> waitClear;

    public List<String> world = new ArrayList<>();

    public List<Integer> unClearDexID = new ArrayList<>();

    public List<String> legendaries = new ArrayList<>();

    public List<String> ultrabeasts = new ArrayList<>();

    public abstract PokeClearAPI<SPECIES, POKEMON, POKE_ENTITY> getApi();

    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        this.world = getConfig().getStringList("world");
        this.unClearDexID = getConfig().getIntegerList("unClearDexID");
        if (getConfig().getString("clearType").equalsIgnoreCase("normal")) {
            this.normalClear = new NormalClear<>(this);
        } else {
            this.waitClear = new WaitClear<>(this);
        }
        Bukkit.getScheduler().runTaskAsynchronously(this, AD::new);

        for (SPECIES value : this.getApi().allSpecies()) {
            if (this.getApi().isUltraBeast(value)) {
                this.ultrabeasts.add(this.getApi().speciesName(value));
            }
            if (this.getApi().isLegendary(value)) {
                this.legendaries.add(this.getApi().speciesName(value));
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp() && args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            this.world = getConfig().getStringList("world");
            this.unClearDexID = getConfig().getIntegerList("unClearDexID");
            if (getConfig().getString("clearType").equalsIgnoreCase("normal")) {
                this.normalClear.load();
            } else {
                this.waitClear.load();
            }
            sender.sendMessage("重载完成");
            Bukkit.getScheduler().runTaskAsynchronously(this, AD::new);
        }
        if (sender.isOp() && args.length == 1 && args[0].equalsIgnoreCase("clear")) {
            PokeClearAPI<SPECIES, POKEMON, POKE_ENTITY> api = this.getApi();
            for (World world : Bukkit.getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (api.isPokeEntity(entity)) {
                        api.unloadEntity(api.getPokeEntity(entity));
                    }
                }
            }
        }
        return false;
    }

    /*仅用作提示去实现事件监听以及处理
     *
     *
     * TODO: 需要被调用的有↓
     *  WaitClear.onSpawn()
     * */
    public abstract void listenerImpl(
            String waitClearOnSpawn
    );
}
