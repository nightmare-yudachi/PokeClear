package lt.pokeclear

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.Species
import lt.pokeclear.api.VCobblemonAPI
import lt.pokeclear.common.laotou.PokeClear

class Main : PokeClear<Species, Pokemon, PokemonEntity>(){
    override fun getApi(): VCobblemonAPI {
        return VCobblemonAPI
    }

    override fun listenerImpl(waitClearOnSpawn: String?) {
        //在CobListener中实现了
    }

    override fun onEnable() {
        super.onEnable()
        CobListener.register()
    }

    override fun onDisable() {
        CobListener.unregister()
    }
}