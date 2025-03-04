package lt.pokeclear.common.api;

import org.bukkit.entity.Entity;

import java.util.UUID;

public abstract class PokeClearAPI<SPECIES,POKEMON,POKE_ENTITY> {
    public abstract SPECIES[] allSpecies();
    public abstract boolean isUltraBeast(SPECIES species);
    public abstract boolean isLegendary(SPECIES species);
    public abstract String speciesName(SPECIES species);
    public abstract boolean isPokeEntity(Entity entity);
    public abstract POKE_ENTITY getPokeEntity(Entity entity);
    public abstract void unloadEntity(POKE_ENTITY pokeEntity);
    public abstract boolean isShiny(POKEMON pokemon);
    public abstract POKEMON getPokeData(POKE_ENTITY pokeEntity);
    public abstract boolean isBossPokeEntity(POKE_ENTITY pokeEntity);
    public abstract SPECIES getSpecies(POKEMON pokemon);
    public abstract int getSpeciesDexID(SPECIES species);
    public abstract boolean canDespawn(POKE_ENTITY entity);
    public abstract UUID getOwnerUUID(POKEMON pokemon);
    public boolean hasOwner(POKEMON pokemon){
        return getOwnerUUID(pokemon) != null;
    }
    public abstract boolean inBattle(POKE_ENTITY entity);
    public abstract boolean inRanch(POKEMON pokemon);
}
