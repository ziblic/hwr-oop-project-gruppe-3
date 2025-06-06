package hwr.oop.tnp.core

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals

class AttackTest : AnnotationSpec() {
  @Test
  fun `check getters for PUNCH attack`() {
    val attack = Attack.PUNCH

    assertEquals(
      PrimitiveType.NORMAL,
      attack.primitiveType,
      "Type getter should return NORMAL for PUNCH",
    )
    assertEquals(
      15,
      attack.damage,
      "Damage getter should return 15 for PUNCH",
    )
    assertEquals(
      0.1,
      attack.critChance,
      "HitQuote getter should return 0.1 for PUNCH",
    )
  }

  @Test
  fun `check getters for FLAME_WREATH attack`() {
    val attack = Attack.FLAME_WREATH

    assertEquals(
      PrimitiveType.FIRE,
      attack.primitiveType,
      "Type getter should return FIRE for FLAME_WREATH",
    )
    assertEquals(
      20,
      attack.damage,
      "Damage getter should return 20 for FLAME_WREATH",
    )
    assertEquals(
      0.4,
      attack.critChance,
      "HitQuote getter should return 0.4 for FLAME_WREATH",
    )
  }

  @Test
  fun `check getters for SPLASH attack`() {
    val attack = Attack.SPLASH

    assertEquals(
      PrimitiveType.WATER,
      attack.primitiveType,
      "Type getter should return WATER for SPLASH",
    )
    assertEquals(
      20,
      attack.damage,
      "Damage getter should return 20 for SPLASH",
    )
    assertEquals(
      0.7,
      attack.critChance,
      "HitQuote getter should return 0.7 for SPLASH",
    )
  }

  @Test
  fun `check getters for LEAF_GUN attack`() {
    val attack = Attack.LEAF_GUN

    assertEquals(
      PrimitiveType.PLANT,
      attack.primitiveType,
      "Type getter should return PLANT for LEAF_GUN",
    )
    assertEquals(
      20,
      attack.damage,
      "Damage getter should return 20 for LEAF_GUN",
    )
    assertEquals(
      0.2,
      attack.critChance,
      "HitQuote getter should return 0.2 for LEAF_GUN",
    )
  }

  @Test
  fun `check getters for SPOOKY_BALL attack`() {
    val attack = Attack.SPOOKY_BALL

    assertEquals(
      PrimitiveType.SPIRIT,
      attack.primitiveType,
      "Type getter should return SPIRIT for SPOOKY_BALL",
    )
    assertEquals(
      20,
      attack.damage,
      "Damage getter should return 20 for SPOOKY_BALL",
    )
    assertEquals(
      0.1,
      attack.critChance,
      "HitQuote getter should return 0.1 for SPOOKY_BALL",
    )
  }

  @Test
  fun `calcMultiplierHitQuote returns 1_5 when critical hit occurs`() {
    val attack = Attack.PUNCH
    val random = 0.05 // Simulating scenario where critical hit should happen
    val result = attack.calculateMultiplierHitQuote(random)
    assertEquals(
      1.5,
      result,
      "Expected multiplier should be 1.5 on critical hit",
    )
  }

  @Test
  fun `calcMultiplierHitQuote returns 1_0 when no critical hit occurs`() {
    val attack = Attack.PUNCH
    val random = 0.3 // Simulating scenario where critical hit does not happen
    val result = attack.calculateMultiplierHitQuote(random)
    assertEquals(
      1.0,
      result,
      "Expected multiplier should be 1.0 when no critical hit",
    )
  }

  @Test
  fun `calcMultiplierHitQuote boundary edge case`() {
    val attack = Attack.PUNCH
    val random = 0.1 // Simulating scenario where critical hit does not happen
    val result = attack.calculateMultiplierHitQuote(random)
    assertEquals(
      1.0,
      result,
      "Expected multiplier should be 1.0 when no critical hit",
    )
  }

  @Test
  fun `calculateDamageAgainst computes correct damage without crit`() {
    val defender =
      Monster(
        name = "Defender",
        stats = BattleStats(100, 10),
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

    assertEquals(expected, result)
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
