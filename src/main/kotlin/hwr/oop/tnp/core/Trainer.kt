package hwr.oop.tnp.core

import kotlinx.serialization.Serializable

@Serializable
data class Trainer(
  val name: String,
  val monsters: MutableList<Monster> = mutableListOf(),
) {
  private val MAX_ALLOWED_MONSTERS_PER_TRAINER = 6

  companion object {
    val EMPTY = Trainer("Unknown")
  }

  init {
    require(monsters.size <= MAX_ALLOWED_MONSTERS_PER_TRAINER) {
      "Too many monsters: $monsters"
    }
  }

  fun addMonster(monster: Monster) {
    require(monsters.size < MAX_ALLOWED_MONSTERS_PER_TRAINER) {
      "Too many monsters: maximum allowed is $MAX_ALLOWED_MONSTERS_PER_TRAINER"
    }
    monsters.add(monster)
  }

  fun nextMonster(): Monster =
    monsters.firstOrNull()
      ?: throw IllegalStateException("No monsters available")

  fun nextBattleReadyMonster(): Monster =
    monsters.firstOrNull { !it.isKO() }
      ?: throw IllegalStateException("No monster is alive anymore")

  fun isDefeated(): Boolean = monsters.all { it.isKO() }
}
