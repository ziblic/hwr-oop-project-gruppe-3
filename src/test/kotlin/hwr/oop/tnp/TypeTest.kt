package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat

class TypeTest : AnnotationSpec() {
    private val type = Type("Wasser")

    @Test
    fun `test type has Type Wasser`() {
        assertThat(type.getName()).isEqualTo("Wasser")
    }
}
