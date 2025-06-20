package hwr.oop.tnp.core

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat

class DamageStrategyTest : AnnotationSpec() {
  private val attacker =
    Monster("Pika", BattleStats(2000, 30, 51, 30, 10, 200), PrimitiveType.FIRE, listOf())
  private val defender =
    Monster("Glurak", BattleStats(2000, 20, 10, 30, 10, 23), PrimitiveType.WATER, listOf())

  @Test
  fun `Test the deterministic strategy with special attack`() {
    val damageStrat = DamageStrategy.DETERMINISTIC
    val damage = damageStrat.calculateDamage(Attack.FIRE_VOW, attacker, defender)
    assertThat(damage).isEqualTo(18)
  }

  @Test
  fun `Test the random strategy with special attack`() {
    val damageStrat = DamageStrategy.RANDOM
    val damage = damageStrat.calculateDamage(Attack.FIRE_VOW, attacker, defender, 0.1)
    assertThat(damage).isEqualTo(18)
    val damage2 = damageStrat.calculateDamage(Attack.FIRE_VOW, attacker, defender, 0.09)
    assertThat(damage2).isEqualTo(27)
  }
}
