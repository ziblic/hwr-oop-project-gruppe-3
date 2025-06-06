package hwr.oop.tnp.core

import kotlinx.serialization.Serializable
import kotlin.math.max

@Serializable
data class BattleStats(
  val maxHp: Int,
  private var hp: Int,
  val speed: Int,
) {
  constructor(maxHp: Int, speed: Int) : this(maxHp, maxHp, speed)

  fun takeDamage(amountOfDamage: Int) {
    hp = max(hp - amountOfDamage, 0)
  }

  fun isKO(): Boolean = hp == 0
}
