package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat

class TypeTest : AnnotationSpec() {

    @Test
    fun `test type Water is effective against fire and less effective against Plant`() {
        assertThat(Type.Water.effectiveAgainst).isEqualTo(Type.Fire)
        assertThat(Type.Water.lessEffectiveAgainst).isEqualTo(Type.Plant)
    }

    @Test
    fun `test type fire is effective against plant and less effective against Water`() {
        assertThat(Type.Fire.effectiveAgainst).isEqualTo(Type.Plant)
        assertThat(Type.Fire.lessEffectiveAgainst).isEqualTo(Type.Water)
    }

    @Test
    fun `test type plant is effective against water and less effective against Fire`() {
        assertThat(Type.Plant.effectiveAgainst).isEqualTo(Type.Water)
        assertThat(Type.Plant.lessEffectiveAgainst).isEqualTo(Type.Fire)
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
