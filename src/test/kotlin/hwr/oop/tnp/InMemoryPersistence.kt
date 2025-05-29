package hwr.oop.tnp

import hwr.oop.tnp.core.Battle
import hwr.oop.tnp.persistency.GamePersistencePort
import kotlinx.serialization.json.Json

class InMemoryPersistence : GamePersistencePort {
    val savedBattles = mutableMapOf<String, String>()

    override fun saveBattle(battle: Battle) {
        savedBattles[battle.battleId] = Json.encodeToString(battle)
    }

    override fun loadBattle(battleId: String): Battle {
        val json =
            savedBattles[battleId]
                ?: throw IllegalArgumentException("Battle not found: $battleId")
        return Json.decodeFromString(json)
    }

    override fun loadAllBattles(): List<Battle> {
        return savedBattles.values.map { Json.decodeFromString(it) }
    }

    fun countBattles(): Int {
        return savedBattles.size
    }
}
