package hwr.oop.tnp

import hwr.oop.tnp.core.Battle
import hwr.oop.tnp.persistency.GamePersistencePort

class InMemoryPersistence : GamePersistencePort {
    val savedBattles = mutableMapOf<String, Battle>()

    override fun saveBattle(battle: Battle) {
        savedBattles[battle.battleId] = battle
    }

    override fun loadBattle(battleId: String): Battle {
        return savedBattles[battleId]
            ?: throw IllegalArgumentException("Battle not found: $battleId")
    }

    override fun loadAllBattles(): List<Battle> {
        return savedBattles.values.toList()
    }

    fun countBattles(): Int {
        return savedBattles.count()
    }
}
