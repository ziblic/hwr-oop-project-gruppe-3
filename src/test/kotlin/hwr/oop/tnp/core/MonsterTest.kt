package hwr.oop.tnp.core

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy

class MonsterTest : AnnotationSpec() {
  private val stats = BattleStats(200, 20, 20, 20, 20, 20)
  private val monster = Monster("Glurak", stats, PrimitiveType.FIRE, listOf(Attack.SHADOW_CLAW))
  private val monster2 =
    Monster(
      "Pika",
      BattleStats(200, 10, 20, 20, 20, 20),
      PrimitiveType.FIRE,
      listOf(Attack.SHADOW_CLAW),
    )

  @Test
  fun `Monster has its attribute`() {
    assertThat(monster.name).isEqualTo("Glurak")
    assertThat(monster.stats).isEqualTo(stats)
    assertThat(monster.primitiveType).isEqualTo(PrimitiveType.FIRE)
    assertThat(monster.attacks).isEqualTo(listOf(Attack.SHADOW_CLAW))
  }

  @Test
  fun `Attack with not existing attack`() {
    assertThatThrownBy {
      monster.attack(Attack.SPIRIT_WAVE, monster2, DamageStrategy.DETERMINISTIC)
    }.hasMessage("The used attack is not part of the attacks of the monster")
  }

  @Test
  fun `Monster 1 is faster than Monster 2`() {
    assertThat(monster.isFasterThan(monster2)).isEqualTo(true)
    assertThat(!monster2.isFasterThan(monster)).isEqualTo(true)
  }

  @Test
  fun `Monster is slower if both have same speed`() {
    val monster2 =
      Monster(
        "Pika",
        BattleStats(200, 20, 20, 20, 20, 20),
        PrimitiveType.FIRE,
        listOf(Attack.SHADOW_CLAW),
      )
    assertThat(!monster.isFasterThan(monster2)).isEqualTo(true)
  }
}
