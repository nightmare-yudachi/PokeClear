package lt.pokeclear.common.laotou;

import lt.pokeclear.common.api.PokeClearAPI;
import lt.pokeclear.common.api.enums.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WaitClear<SPECIES, POKEMON, POKE_ENTITY> {
    int time;

    int time_;

    int wait;

    boolean le;

    boolean ul;

    boolean shiny;

    boolean boss;

    boolean canDeSpawn;

    MessageType messageType;

    Map<Integer, String> msg = new HashMap<>();

    Map<UUID, Long> waitEntity = new HashMap<>();

    PokeClear<SPECIES, POKEMON, POKE_ENTITY> INSTANCE;

    //TODO: >>需要被事件处理调用<<!!!!
    public void onSpawn(Entity entity) {
        PokeClearAPI<SPECIES, POKEMON, POKE_ENTITY> api = INSTANCE.getApi();
        if (api.isPokeEntity(entity)) {
            POKE_ENTITY pokeEntity = api.getPokeEntity(entity);
            POKEMON pokeData = api.getPokeData(pokeEntity);
            SPECIES species = api.getSpecies(pokeData);
            String speciesName = api.speciesName(species);
            if (!this.boss && api.isBossPokeEntity(pokeEntity))
                return;
            if (!this.shiny && api.isShiny(pokeData))
                return;
            if (!this.le && this.INSTANCE.legendaries.contains(speciesName))
                return;
            if (!this.ul && this.INSTANCE.ultrabeasts.contains(speciesName))
                return;
            if (this.INSTANCE.unClearDexID.contains(api.getSpeciesDexID(species)))
                return;
            this.waitEntity.put(entity.getUniqueId(), System.currentTimeMillis() + (this.wait * 1000L));
        }
    }

    public WaitClear(PokeClear<SPECIES, POKEMON, POKE_ENTITY> INSTANCE) {
        this.INSTANCE = INSTANCE;
        load();
        INSTANCE.getServer().getScheduler().scheduleSyncRepeatingTask(INSTANCE, () -> {
            if (this.time <= 0) {
                clearPokemon();
                this.time = this.time_;
            } else {
                this.time--;
                if (this.msg.containsKey(this.time) && this.time != 0)
                    messageType.sendMessage(this.msg.get(this.time));
            }
        },20L, 20L);
        INSTANCE.getLogger().info("WaitClear");
        PokeClearAPI<SPECIES, POKEMON, POKE_ENTITY> api = INSTANCE.getApi();

        for (String worldName : INSTANCE.world) {
            World world = Bukkit.getWorld(worldName);
            if (world == null) continue;
            for (Entity entity : world.getEntities()) {
                if (api.isPokeEntity(entity)) {
                    POKE_ENTITY pokeEntity = api.getPokeEntity(entity);
                    POKEMON pokeData = api.getPokeData(pokeEntity);
                    SPECIES species = api.getSpecies(pokeData);
                    String speciesName = api.speciesName(species);
                    if (!this.ul && INSTANCE.ultrabeasts.contains(speciesName))
                        continue;
                    if (!this.le && INSTANCE.legendaries.contains(speciesName))
                        continue;
                    if (!this.shiny && api.isShiny(pokeData))
                        continue;
                    if (!this.boss && api.isBossPokeEntity(pokeEntity))
                        continue;
                    if (INSTANCE.unClearDexID.contains(api.getSpeciesDexID(species)))
                        continue;
                    if (!this.canDeSpawn && api.canDespawn(pokeEntity))
                        continue;
                    if (!api.hasOwner(pokeData) && !api.inBattle(pokeEntity) && !api.inRanch(pokeData))
                        this.waitEntity.put(entity.getUniqueId(), System.currentTimeMillis() + (this.wait * 1000L));
                }
            }
        }
    }

    public void load() {
        this.time = this.INSTANCE.getConfig().getInt("wait.time.cycle");
        this.time_ = this.INSTANCE.getConfig().getInt("wait.time.cycle");
        this.wait = this.INSTANCE.getConfig().getInt("wait.time.wait");
        this.le = this.INSTANCE.getConfig().getBoolean("wait.le");
        this.ul = this.INSTANCE.getConfig().getBoolean("wait.ul");
        this.shiny = this.INSTANCE.getConfig().getBoolean("wait.shiny");
        this.boss = this.INSTANCE.getConfig().getBoolean("wait.boss");
        this.canDeSpawn = this.INSTANCE.getConfig().getBoolean("wait.canDeSpawn");
        this.messageType = MessageType.valueOf(this.INSTANCE.getConfig().getString("wait.message-type"));
        this.INSTANCE.getConfig().getConfigurationSection("wait.message").getKeys(false).forEach(it -> {
            try {
                this.msg.put(Integer.parseInt(it), this.INSTANCE.getConfig().getString("wait.message." + it).replace('&','§'));
            } catch (Exception ignored) {
            }
        });
    }

    public void clearPokemon() {
        int sum = 0;
        int wt = 0;

        PokeClearAPI<SPECIES, POKEMON, POKE_ENTITY> api = this.INSTANCE.getApi();

        for (String worldName : this.INSTANCE.world) {
            World world = Bukkit.getWorld(worldName);
            if (world == null) continue;
            for (Entity entity : world.getEntities())
                if (api.isPokeEntity(entity)) {
                    POKE_ENTITY pokeEntity = api.getPokeEntity(entity);
                    POKEMON pokeData = api.getPokeData(pokeEntity);
                    if (!this.waitEntity.containsKey(entity.getUniqueId()))
                        continue;
                    if (System.currentTimeMillis() < this.waitEntity.get(entity.getUniqueId())) {
                        if (System.currentTimeMillis() + (this.time_ * 1000L) > this.waitEntity.get(entity.getUniqueId()))
                            wt++;
                        continue;
                    }
                    if (!api.hasOwner(pokeData) && !api.inBattle(pokeEntity) && !api.inRanch(pokeData)) {
                        api.unloadEntity(pokeEntity);
                        sum++;
                    }
                }
        }
        Bukkit.getServer().broadcastMessage(this.msg.get(0).replace("%num%", String.valueOf(sum)).replace("%wait%", String.valueOf(wt)));
    }
}
