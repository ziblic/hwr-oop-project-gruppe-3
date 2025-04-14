package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat

class BattleStatsTest : AnnotationSpec() {
    private val battleStats = BattleStats(200, 100, 30, 30, 40, 40)

    @Test
    fun `test battleStats has HP`() {
        assertThat(battleStats.hp).isEqualTo(200)
    }

    @Test
    fun `test battleStats has speed`() {
        assertThat(battleStats.speed).isEqualTo(100)
    }

    @Test
    fun `test battleStats has attack`() {
        assertThat(battleStats.attack).isEqualTo(30)
    }

    @Test
    fun `test battleStats has defense`() {
        assertThat(battleStats.defense).isEqualTo(30)
    }

    @Test
    fun `test battleStats has special attack`() {
        assertThat(battleStats.specialAttack).isEqualTo(40)
    }

    @Test
    fun `test battleStats has special defense`() {
        assertThat(battleStats.specialDefense).isEqualTo(40)
    }
}
