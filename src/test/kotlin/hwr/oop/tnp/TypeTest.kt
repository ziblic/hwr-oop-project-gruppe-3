package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat

class TypeTest : AnnotationSpec() {

    @Test
    fun `test type Water is effective against fire and less effective against Plant`() {
        val monsterType = Type.Water
        assertThat(monsterType.effectiveAgainst).isEqualTo(Type.Fire)
        assertThat(monsterType.lessEffectiveAgainst).isEqualTo(Type.Plant)
    }

    @Test
    fun `test type fire is effective against plant and less effective against Water`() {
        val monsterType = Type.Fire
        assertThat(monsterType.effectiveAgainst).isEqualTo(Type.Plant)
        assertThat(monsterType.lessEffectiveAgainst).isEqualTo(Type.Water)
    }

    @Test
    fun `test type plant is effective against water and less effective against Fire`() {
        val monsterType = Type.Plant
        assertThat(monsterType.effectiveAgainst).isEqualTo(Type.Water)
        assertThat(monsterType.lessEffectiveAgainst).isEqualTo(Type.Fire)
    }

    @Test
    fun `test type spirit has no Effect against normal`() {
        assertThat(Type.Spirit.noEffectAgainst).isEqualTo(Type.Normal)
    }

    @Test
    fun `test type normal has no Effect against spirit`() {
        assertThat(Type.Normal.noEffectAgainst).isEqualTo(Type.Spirit)
    }
}
