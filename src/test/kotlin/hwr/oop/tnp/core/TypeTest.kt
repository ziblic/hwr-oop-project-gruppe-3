package hwr.oop.tnp.core

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat

class TypeTest : AnnotationSpec() {
    @Test
    fun `Test type WATER is effective against FIRE and less effective against PLANT`() {
        val monsterType = Type.WATER
        assertThat(monsterType.effectiveAgainst).isEqualTo(Type.FIRE)
        assertThat(monsterType.lessEffectiveAgainst).isEqualTo(Type.PLANT)
    }

    @Test
    fun `Test type FIRE is effective against PLANT and less effective against WATER`() {
        val monsterType = Type.FIRE
        assertThat(monsterType.effectiveAgainst).isEqualTo(Type.PLANT)
        assertThat(monsterType.lessEffectiveAgainst).isEqualTo(Type.WATER)
    }

    @Test
    fun `Test type PLANT is effective against WATER and less effective against FIRE`() {
        val monsterType = Type.PLANT
        assertThat(monsterType.effectiveAgainst).isEqualTo(Type.WATER)
        assertThat(monsterType.lessEffectiveAgainst).isEqualTo(Type.FIRE)
    }

    @Test
    fun `Test type SPIRIT has no effect against NORMAL`() {
        assertThat(Type.SPIRIT.noEffectAgainst).isEqualTo(Type.NORMAL)
    }

    @Test
    fun `Test type NORMAL has no effect against SPIRIT`() {
        assertThat(Type.NORMAL.noEffectAgainst).isEqualTo(Type.SPIRIT)
    }
}
