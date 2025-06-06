package hwr.oop.tnp.core

import kotlinx.serialization.Serializable

@Serializable
data class Trainer(
  val name: String,
  val monsters: MutableList<Monster> = mutableListOf(),
) {
  private val maxAllowedMonsterPerTrainer = 6

  companion object {
    val EMPTY = Trainer("Unknown")
  }

  init {
    require(monsters.size <= maxAllowedMonsterPerTrainer) { "Too many monsters: $monsters" }
  }

  fun addMonster(monster: Monster) {
    require(monsters.size < maxAllowedMonsterPerTrainer) {
      "Too many monsters: maximum allowed is $maxAllowedMonsterPerTrainer"
    }
    monsters.add(monster)
  }

  fun nextMonster(): Monster = monsters.firstOrNull() ?: throw IllegalStateException("No monsters available")

  fun nextBattleReadyMonster(): Monster =
    monsters.firstOrNull { !it.isKO() }
      ?: throw IllegalStateException("No monster is alive anymore")

  fun isDefeated(): Boolean = monsters.all { it.isKO() }
}
