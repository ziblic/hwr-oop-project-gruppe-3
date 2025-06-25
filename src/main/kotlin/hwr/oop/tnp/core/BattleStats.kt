package hwr.oop.tnp.core

import kotlinx.serialization.Serializable
import kotlin.math.max

@Serializable
data class BattleStats(
  val maxHp: Int,
  private var hp: Int,
  val speed: Int,
  val attack: Int,
  val specialAttack: Int,
  val defense: Int,
  val specialDefense: Int,
) {
  constructor(
    maxHp: Int,
    speed: Int,
    attack: Int,
    specialAttack: Int,
    defense: Int,
    specialDefense: Int,
  ) : this(maxHp, maxHp, speed, attack, specialAttack, defense, specialDefense)

  fun takeDamage(amountOfDamage: Int) {
    hp = max(hp - amountOfDamage, 0)
  }

  fun isKO(): Boolean = hp == 0
}
