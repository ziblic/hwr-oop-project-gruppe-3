package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat

class TypeTest : AnnotationSpec() {
    @Test
    fun `Test type Water is effective against Fire and less effective against Plant`() {
        val monsterType = Type.Water
        assertThat(monsterType.effectiveAgainst).isEqualTo(Type.Fire)
        assertThat(monsterType.lessEffectiveAgainst).isEqualTo(Type.Plant)
    }

    @Test
    fun `Test type Fire is effective against Plant and less effective against Water`() {
        val monsterType = Type.Fire
        assertThat(monsterType.effectiveAgainst).isEqualTo(Type.Plant)
        assertThat(monsterType.lessEffectiveAgainst).isEqualTo(Type.Water)
    }

    @Test
    fun `Test type Plant is effective against Water and less effective against Fire`() {
        val monsterType = Type.Plant
        assertThat(monsterType.effectiveAgainst).isEqualTo(Type.Water)
        assertThat(monsterType.lessEffectiveAgainst).isEqualTo(Type.Fire)
    }

    @Test
    fun `Test type Spirit has no effect against Normal`() {
        assertThat(Type.Spirit.noEffectAgainst).isEqualTo(Type.Normal)
    }

    @Test
    fun `Test type Normal has no effect against Spirit`() {
        assertThat(Type.Normal.noEffectAgainst).isEqualTo(Type.Spirit)
    }
}
