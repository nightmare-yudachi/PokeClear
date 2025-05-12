package lt.pokeclear

import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.entity.SpawnEvent
import com.cobblemon.mod.common.api.reactive.ObservableSubscription
import lt.pokeclear.api.bukkitEntity
import lt.pokeclear.common.laotou.PokeClear

object CobListener {
    val subs = mutableListOf<ObservableSubscription<*>>()

    fun register() {
        subs.add(CobblemonEvents.ENTITY_SPAWN.subscribe { spawnEvent(it) })
    }

    fun unregister() {
        subs.forEach(ObservableSubscription<*>::unsubscribe)
    }

    //handler
    fun spawnEvent(event: SpawnEvent<*>) {
        PokeClear.INSTANCE.waitClear?.onSpawn(event.entity.bukkitEntity())
    }
}
