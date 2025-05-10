package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat

class BattleStatsTest : AnnotationSpec() {
    private val battleStats = BattleStats(200, 100)

    @Test
    fun `test battleStats has HP`() {
        assertThat(battleStats.hp).isEqualTo(200)
    }

    @Test
    fun `test battleStats has speed`() {
        assertThat(battleStats.speed).isEqualTo(100)
    }
}
