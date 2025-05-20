package hwr.oop.tnp.core

import io.kotest.core.spec.style.AnnotationSpec
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat

class BattleStatsTest : AnnotationSpec() {
    private val battleStats = BattleStats(200, 100)

    @Test
    fun `check if serializable`() {
        val battleStats = BattleStats(100, 25)
        val jsonStats = Json.encodeToString(battleStats)
        assertThat(jsonStats).isEqualTo("{\"maxHp\":100,\"hp\":100,\"speed\":25}")

        val decodedStats = Json.decodeFromString<BattleStats>(jsonStats)
        assertThat(battleStats.hp).isEqualTo(decodedStats.hp)
        assertThat(battleStats.speed).isEqualTo(decodedStats.speed)
    }

    @Test
    fun `test battleStats has HP`() {
        assertThat(battleStats.hp).isEqualTo(200)
    }

    @Test
    fun `test battleStats has speed`() {
        assertThat(battleStats.speed).isEqualTo(100)
    }
}
