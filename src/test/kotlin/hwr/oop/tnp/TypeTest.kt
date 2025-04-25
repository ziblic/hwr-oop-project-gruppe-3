package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat

class TypeTest : AnnotationSpec() {

    private val water = Type.Water
    @Test
    fun `test type Water is effective against fire`() {
        assertThat(water.effectiveAgainst).isEqualTo(Type.Fire)
    }
    @Test
    fun `test type Water is less effective against Plant`() {
        assertThat(water.lessEffectiveAgainst).isEqualTo(Type.Plant)
    }

    private val fire = Type.Fire
    @Test
    fun `test type fire is effective against plant`() {
        assertThat(fire.effectiveAgainst).isEqualTo(Type.Plant)
    }
    @Test
    fun `test type fire is less effective against water`() {
        assertThat(fire.lessEffectiveAgainst).isEqualTo(Type.Water)
    }
    private val plant = Type.Plant
    @Test
    fun `test type plant is effective against water`() {
        assertThat(plant.effectiveAgainst).isEqualTo(Type.Water)
    }
    @Test
    fun `test type plant is less effective against fire`() {
        assertThat(plant.lessEffectiveAgainst).isEqualTo(Type.Fire)
    }
    private val spirit = Type.Spirit
    @Test
    fun `test type spirit has no Effect against normal`() {
        assertThat(spirit.noEffectAgainst).isEqualTo(Type.Normal)
    }
    private val normal = Type.Normal
    @Test
    fun `test type normal has no Effect against spirit`() {
        assertThat(normal.noEffectAgainst).isEqualTo(Type.Spirit)
    }

}
