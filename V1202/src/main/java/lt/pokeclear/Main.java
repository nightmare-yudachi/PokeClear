package lt.pokeclear;

import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import lt.pokeclear.api.V1202PokeClearAPI;
import lt.pokeclear.common.laotou.PokeClear;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Main extends PokeClear<Species, Pokemon, PixelmonEntity> implements Listener {

    @Override
    public V1202PokeClearAPI getApi() {
        return V1202PokeClearAPI.INSTANCE;
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
            waitClear.onSpawn(V1202PokeClearAPI.bukkitEntity(e.action.getOrCreateEntity()));
        }
    }
}
