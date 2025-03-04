package lt.pokeclear

import com.cobblemon.mod.common.api.events.entity.SpawnEvent
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.Species
import lt.pokeclear.api.VCobblemonAPI
import lt.pokeclear.api.bukkitEntity
import lt.pokeclear.common.laotou.PokeClear
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.figsq.cobblemonbukkitevent.cobblemonbukkitevent.CobblemonEvent

class Main : PokeClear<Species, Pokemon, PokemonEntity>(),Listener{
    override fun getApi(): VCobblemonAPI {
        return VCobblemonAPI
    }

    override fun listenerImpl(waitClearOnSpawn: String?) {
    }

    override fun onEnable() {
        super.onEnable()

        this.server.pluginManager.registerEvents(this,this)
    }

    @EventHandler
    fun voCobblemonEvent(event: CobblemonEvent){
        if (event.cobblemonEvent is SpawnEvent<*>) {
            val e = event.cobblemonEvent as SpawnEvent<*>
            this.waitClear?.onSpawn(e.entity.bukkitEntity())
        }
    }
}