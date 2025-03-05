package lt.pokeclear.common.laotou;

import lt.pokeclear.common.api.PokeClearAPI;
import lt.pokeclear.common.api.enums.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class NormalClear<SPECIES, POKEMON, POKE_ENTITY> {
    PokeClear<SPECIES, POKEMON, POKE_ENTITY> INSTANCE;

    int time;

    boolean le;

    boolean ul;

    boolean shiny;

    boolean boss;

    boolean canDeSpawn;

    MessageType messageType;

    Map<Integer, String> msg = new HashMap<>();

    public NormalClear(PokeClear<SPECIES, POKEMON, POKE_ENTITY> INSTANCE) {
        this.INSTANCE = INSTANCE;
        load();
        INSTANCE.getServer().getScheduler().scheduleSyncRepeatingTask(INSTANCE, () -> {
            if (this.time <= 0) {
                clearPokemon();
                this.time = INSTANCE.getConfig().getInt("normal.time");
            } else {
                this.time--;
                if (this.msg.containsKey(this.time) && this.time != 0)
                    messageType.sendMessage(this.msg.get(this.time));
            }
        }, 20L, 20L);
        INSTANCE.getLogger().info("NormalClear");
    }

    public void load() {
        this.time = this.INSTANCE.getConfig().getInt("normal.time");
        this.le = this.INSTANCE.getConfig().getBoolean("normal.le");
        this.ul = this.INSTANCE.getConfig().getBoolean("normal.ul");
        this.shiny = this.INSTANCE.getConfig().getBoolean("normal.shiny");
        this.boss = this.INSTANCE.getConfig().getBoolean("normal.boss");
        this.canDeSpawn = this.INSTANCE.getConfig().getBoolean("normal.canDeSpawn");
        this.messageType = MessageType.valueOf(this.INSTANCE.getConfig().getString("normal.message-type"));
        this.INSTANCE.getConfig().getConfigurationSection("normal.message").getKeys(false).forEach(it -> {
            try {
                this.msg.put(Integer.parseInt(it), this.INSTANCE.getConfig().getString("normal.message." + it).replace('&', '§'));
            } catch (Exception ignored) {
            }
        });
    }

    public void clearPokemon() {
        int sum = 0;

        PokeClearAPI<SPECIES, POKEMON, POKE_ENTITY> api = this.INSTANCE.getApi();

        for (String worldName : this.INSTANCE.world) {
            World world = Bukkit.getWorld(worldName);
            if (world == null) continue;
            for (Entity entity : world.getEntities())
                if (api.isPokeEntity(entity)) {
                    POKE_ENTITY pokeEntity = api.getPokeEntity(entity);
                    POKEMON pokeData = api.getPokeData(pokeEntity);
                    SPECIES species = api.getSpecies(pokeData);
                    String speciesName = api.speciesName(species);
                    if (!this.ul && this.INSTANCE.ultrabeasts.contains(speciesName))
                        continue;
                    if (!this.le && this.INSTANCE.legendaries.contains(speciesName))
                        continue;
                    if (!this.shiny && api.isShiny(pokeData))
                        continue;
                    if (!this.boss && api.isBossPokeEntity(pokeEntity))
                        continue;
                    if (this.INSTANCE.unClearDexID.contains(api.getSpeciesDexID(species)))
                        continue;
                    if (!this.canDeSpawn && api.canDespawn(pokeEntity))
                        continue;
                    if (!api.hasOwner(pokeData) && !api.inBattle(pokeEntity) && !api.inRanch(pokeData)) {
                        api.unloadEntity(pokeEntity);
                        sum++;
                    }
                }
        }
        messageType.sendMessage(this.msg.get(0).replace("%num%", String.valueOf(sum)));
    }
}
