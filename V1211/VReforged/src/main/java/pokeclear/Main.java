package pokeclear;

import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import lt.pokeclear.common.laotou.PokeClear;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pokeclear.api.V1211PokeClearAPI;

public class Main extends PokeClear<Species, Pokemon, PixelmonEntity> implements Listener {

    @Override
    public V1211PokeClearAPI getApi() {
        return V1211PokeClearAPI.INSTANCE;
    }

    @Override
    public void onEnable() {
        super.onEnable();

        //listenerImpl
        this.getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public void listenerImpl(String waitClearOnSpawn) {
    }

    @EventHandler
    public void onForge(ForgeEvent event){
        if (event.getForgeEvent() instanceof SpawnEvent) {
            SpawnEvent e = (SpawnEvent) event.getForgeEvent();
            if (waitClear == null) return;
            waitClear.onSpawn(V1211PokeClearAPI.bukkitEntity(e.action.getOrCreateEntity()));
        }
    }
}
