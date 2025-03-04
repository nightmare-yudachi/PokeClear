package lt.pokeclear.api;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import lt.pokeclear.common.api.PokeClearAPI;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

import java.util.UUID;

public final class V1122PokeClearAPI extends PokeClearAPI<EnumSpecies, Pokemon, EntityPixelmon> {
    public static final V1122PokeClearAPI INSTANCE = new V1122PokeClearAPI();

    private V1122PokeClearAPI(){
    }

    @Override
    public EnumSpecies[] allSpecies() {
        return EnumSpecies.values();
    }

    @Override
    public boolean isUltraBeast(EnumSpecies species) {
        return species.isUltraBeast();
    }

    @Override
    public boolean isLegendary(EnumSpecies species) {
        return species.isLegendary();
    }

    @Override
    public String speciesName(EnumSpecies species) {
        return species.name;
    }

    @Override
    public boolean isPokeEntity(Entity entity) {
        return mcEntity(entity) instanceof EntityPixelmon;
    }

    @Override
    public EntityPixelmon getPokeEntity(Entity entity) {
        return (EntityPixelmon) mcEntity(entity);
    }

    @Override
    public void unloadEntity(EntityPixelmon entityPixelmon) {
        entityPixelmon.unloadEntity();
    }

    @Override
    public boolean isShiny(Pokemon pokemon) {
        return pokemon.isShiny();
    }

    @Override
    public Pokemon getPokeData(EntityPixelmon entityPixelmon) {
        return entityPixelmon.getPokemonData();
    }

    @Override
    public boolean isBossPokeEntity(EntityPixelmon entityPixelmon) {
        return entityPixelmon.isBossPokemon();
    }

    @Override
    public EnumSpecies getSpecies(Pokemon pokemon) {
        return pokemon.getSpecies();
    }

    @Override
    public int getSpeciesDexID(EnumSpecies species) {
        return species.getNationalPokedexInteger();
    }

    @Override
    public boolean canDespawn(EntityPixelmon entityPixelmon) {
        return entityPixelmon.canDespawn;
    }

    @Override
    public UUID getOwnerUUID(Pokemon pokemon) {
        UUID uuid = pokemon.getOwnerPlayerUUID();
        return uuid == null ? pokemon.getOwnerTrainerUUID() : uuid;
    }

    @Override
    public boolean inBattle(EntityPixelmon entityPixelmon) {
        return entityPixelmon.battleController != null;
    }

    @Override
    public boolean inRanch(Pokemon pokemon) {
        return pokemon.isInRanch();
    }

    public static net.minecraft.entity.Entity mcEntity(Entity entity){
        return (((net.minecraft.entity.Entity) (Object) ((CraftEntity) entity).getHandle()));
    }

    public static Entity bukkitEntity(net.minecraft.entity.Entity entity){
        return CraftEntity.getEntity(((CraftServer) Bukkit.getServer()),(((net.minecraft.server.v1_12_R1.Entity) (Object) entity)));
    }
}
