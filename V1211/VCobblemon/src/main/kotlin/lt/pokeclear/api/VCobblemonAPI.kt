package lt.pokeclear.api

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.pokemon.labels.CobblemonPokemonLabels
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.Species
import lt.pokeclear.common.api.PokeClearAPI
import me.fullidle.ficore.ficore.common.SomeMethod
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import java.lang.reflect.Method
import java.util.*

object VCobblemonAPI : PokeClearAPI<Species, Pokemon, PokemonEntity>() {
    override fun allSpecies(): Array<Species> {
        return PokemonSpecies.species.toTypedArray()
    }

    override fun isPokeEntity(entity: Entity): Boolean {
        return entity.mcEntity() is PokemonEntity
    }

    override fun getPokeEntity(entity: Entity): PokemonEntity {
        return entity.mcEntity() as PokemonEntity
    }

    override fun inRanch(pokemon: Pokemon?): Boolean {
        return false
    }

    override fun inBattle(entity: PokemonEntity): Boolean {
        return entity.isBattling
    }

    override fun getOwnerUUID(pokemon: Pokemon): UUID? {
        return pokemon.getOwnerUUID()
    }

    override fun canDespawn(entity: PokemonEntity): Boolean {
        return true
    }

    override fun getSpeciesDexID(species: Species): Int {
        return species.nationalPokedexNumber
    }

    override fun getSpecies(pokemon: Pokemon): Species {
        return pokemon.species
    }

    override fun isBossPokeEntity(pokeEntity: PokemonEntity?): Boolean {
        return false
    }

    override fun getPokeData(pokeEntity: PokemonEntity): Pokemon {
        return pokeEntity.pokemon
    }

    override fun isShiny(pokemon: Pokemon): Boolean {
        return pokemon.shiny
    }

    override fun unloadEntity(pokeEntity: PokemonEntity) {
        pokeEntity.bukkitEntity().remove()
    }

    override fun speciesName(species: Species): String {
        return species.name
    }

    override fun isLegendary(species: Species): Boolean {
        return species.labels.contains(CobblemonPokemonLabels.LEGENDARY)
    }

    override fun isMythical(species: Species): Boolean {
        return species.labels.contains(CobblemonPokemonLabels.MYTHICAL)
    }

    override fun isUltraBeast(species: Species): Boolean {
        return species.labels.contains(CobblemonPokemonLabels.ULTRA_BEAST)
    }
}

val craftEntityClass: Class<*> by lazy {
    Class.forName("org.bukkit.craftbukkit.${SomeMethod.getNmsVersion()}.entity.CraftEntity")
}

val getHandle: Method by lazy {
    craftEntityClass.declaredMethods.find { it.name == "getHandle" }!!.apply {
        this.isAccessible = true
    }
}

fun Entity.mcEntity(): net.minecraft.world.entity.Entity {
    return getHandle.invoke(this) as net.minecraft.world.entity.Entity
}

val getEntity:Method by lazy {
    craftEntityClass.declaredMethods.find {
        it.name == "getEntity"
    }!!.apply {
        this.isAccessible = true
    }
}

fun net.minecraft.world.entity.Entity.bukkitEntity(): Entity {
    return getEntity.invoke(craftEntityClass, Bukkit.getServer(), this) as Entity
}