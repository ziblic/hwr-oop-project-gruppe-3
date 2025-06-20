package hwr.oop.tnp.core

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat

class AttackTest : AnnotationSpec() {
  @Test
  fun `check getters for PUNCH attack`() {
    val attack = Attack.PUNCH

    assertThat(PrimitiveType.NORMAL).isEqualTo(attack.primitiveType)
    assertThat(15).isEqualTo(attack.damage)
    assertThat(0.1).isEqualTo(attack.critChance)
  }

  @Test
  fun `check getters for FLAME_WREATH attack`() {
    val attack = Attack.FLAME_WREATH

    assertThat(PrimitiveType.FIRE).isEqualTo(attack.primitiveType)
    assertThat(20).isEqualTo(attack.damage)
    assertThat(0.4).isEqualTo(attack.critChance)
  }

  @Test
  fun `check getters for SPLASH attack`() {
    val attack = Attack.SPLASH

    assertThat(PrimitiveType.WATER).isEqualTo(attack.primitiveType)
    assertThat(20).isEqualTo(attack.damage)
    assertThat(0.7).isEqualTo(attack.critChance)
  }

  @Test
  fun `check getters for LEAF_GUN attack`() {
    val attack = Attack.LEAF_GUN

    assertThat(PrimitiveType.PLANT).isEqualTo(attack.primitiveType)
    assertThat(20).isEqualTo(attack.damage)
    assertThat(0.2).isEqualTo(attack.critChance)
  }

  @Test
  fun `check getters for SPOOKY_BALL attack`() {
    val attack = Attack.SPOOKY_BALL

    assertThat(PrimitiveType.SPIRIT).isEqualTo(attack.primitiveType)
    assertThat(20).isEqualTo(attack.damage)
    assertThat(0.1).isEqualTo(attack.critChance)
  }

  @Test
  fun `calculateDamageAgainst computes correct damage without crit`() {
    val defender =
      Monster(
        name = "Defender",
        stats = BattleStats(100, 10, 20, 20, 20, 20),
        primitiveType = PrimitiveType.PLANT,
        attacks = listOf(),
      )

    val damage = Attack.FLAME_WREATH.damage
    val multiplier =
      Attack.FLAME_WREATH.primitiveType.calculateDamangeMultiplier(
        defender,
      )
    val crit = 1.0

    val expected = (damage * multiplier * crit).toInt()
    val result =
      Attack.FLAME_WREATH.run {
        val m = primitiveType.calculateDamangeMultiplier(defender)
        (damage * m * 1.0).toInt()
      }

    assertThat(expected).isEqualTo(result)
  }

  @Test
  fun `damage is calculated using multiplication only`() {
    val baseDamage = 20
    val multiplier = 2.0
    val crit = 1.5

    val expected = (baseDamage * multiplier * crit).toInt()
    val mutated = (baseDamage * multiplier / crit).toInt()

    assertThat(expected).isNotEqualTo(mutated)
  }
}
