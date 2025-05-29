package hwr.oop.tnp.core

import kotlinx.serialization.Serializable

@Serializable
class Trainer(val name: String, val monsters: List<Monster> = emptyList()) {
    init {
        require(monsters.size <= MAX_ALLOWED_MONSTERS_PER_TRAINER) {
            "Too many monsters: $monsters"
        }
    }

    fun addMonster(monster: Monster): Trainer {
        require(monsters.size < MAX_ALLOWED_MONSTERS_PER_TRAINER) {
            "Too many monsters: maximum allowed is $MAX_ALLOWED_MONSTERS_PER_TRAINER"
        }
        val updatedMonsters = monsters + monster
        return Trainer(name, updatedMonsters)
    }

    fun nextMonster(): Monster = monsters.firstOrNull() ?: throw IllegalStateException("No monsters available")

    fun nextBattleReadyMonster(): Monster? = monsters.firstOrNull { !it.isKO() }

    fun isDefeated(): Boolean = monsters.all { it.isKO() }
}
