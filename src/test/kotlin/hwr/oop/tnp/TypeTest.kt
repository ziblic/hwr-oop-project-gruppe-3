package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat

class TypeTest : AnnotationSpec() {
    private val type = Type.Water

    @Test
    fun `test type Water is effective against fire`() {
        assertThat(type.effectiveAgainst).isEqualTo(Type.Fire)
    }

    @Test
    fun `test type Water is effectless against Plant`() {
        assertThat(type.effectlessAgainst).isEqualTo(Type.Plant)
    }


    }

