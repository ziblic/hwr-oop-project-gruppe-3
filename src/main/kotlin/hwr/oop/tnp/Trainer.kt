package hwr.oop.tnp

import kotlinx.serialization.Serializable

@Serializable
class Trainer(val name: String, val monsters: List<Monster> = emptyList()) {
    init {
        require(monsters.size <= MAX_ALLOWED_MONSTERS_PER_TRAINER) {
            "Too many monsters: $monsters"
        }
    }

    fun addMonster(monster: Monster): Trainer {
        require(monsters.size < MAX_ALLOWED_MONSTERS_PER_TRAINER) { "Too many monsters" }
        val monsters =
            listOf(monsters.iterator()).flatMap { it.asSequence().toList() } + monster
        return Trainer(name, monsters)
    }

    fun nextMonster(): Monster? = monsters.firstOrNull { !it.isKO() }

    fun isDefeated(): Boolean = monsters.all { it.isKO() }
}
