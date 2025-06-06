package hwr.oop.tnp.core

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat

class TypeTest : AnnotationSpec() {
  @Test
  fun `Test type WATER is effective against FIRE and less effective against PLANT`() {
    val monsterType = PrimitiveType.WATER
    assertThat(monsterType.effectiveAgainst).isEqualTo(PrimitiveType.FIRE)
    assertThat(
      monsterType.lessEffectiveAgainst,
    ).isEqualTo(PrimitiveType.PLANT)
  }

  @Test
  fun `Test type FIRE is effective against PLANT and less effective against WATER`() {
    val monsterType = PrimitiveType.FIRE
    assertThat(monsterType.effectiveAgainst).isEqualTo(PrimitiveType.PLANT)
    assertThat(
      monsterType.lessEffectiveAgainst,
    ).isEqualTo(PrimitiveType.WATER)
  }

  @Test
  fun `Test type PLANT is effective against WATER and less effective against FIRE`() {
    val monsterType = PrimitiveType.PLANT
    assertThat(monsterType.effectiveAgainst).isEqualTo(PrimitiveType.WATER)
    assertThat(
      monsterType.lessEffectiveAgainst,
    ).isEqualTo(PrimitiveType.FIRE)
  }

  @Test
  fun `Test type SPIRIT has no effect against NORMAL`() {
    assertThat(
      PrimitiveType.SPIRIT.noEffectAgainst,
    ).isEqualTo(PrimitiveType.NORMAL)
  }

  @Test
  fun `Test type NORMAL has no effect against SPIRIT`() {
    assertThat(
      PrimitiveType.NORMAL.noEffectAgainst,
    ).isEqualTo(PrimitiveType.SPIRIT)
  }
}
