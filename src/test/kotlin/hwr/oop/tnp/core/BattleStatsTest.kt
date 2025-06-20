package hwr.oop.tnp.core

import io.kotest.core.spec.style.AnnotationSpec
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat

class BattleStatsTest : AnnotationSpec() {
  private val battleStats = BattleStats(200, 100, 20, 20, 20, 20)

  @Test
  fun `check if serializable`() {
    val battleStats = BattleStats(100, 25, 20, 20, 20, 20)
    val jsonStats = Json.encodeToString(battleStats)
    assertThat(jsonStats)
      .isEqualTo(
        "{\"maxHp\":100,\"hp\":100,\"speed\":25,\"attack\":20,\"specialAttack\":20,\"defense\":20,\"specialDefense\":20}",
      )

    val decodedStats = Json.decodeFromString<BattleStats>(jsonStats)
    assertThat(battleStats.maxHp).isEqualTo(decodedStats.maxHp)
    assertThat(battleStats.speed).isEqualTo(decodedStats.speed)
  }

  @Test
  fun `test battleStats has maxHP`() {
    assertThat(battleStats.maxHp).isEqualTo(200)
  }

  @Test
  fun `test battleStats has attack`() {
    assertThat(battleStats.attack).isEqualTo(20)
  }

  @Test
  fun `test battleStats has defense`() {
    assertThat(battleStats.defense).isEqualTo(20)
  }

  @Test
  fun `test battleStats has specialAttack`() {
    assertThat(battleStats.specialAttack).isEqualTo(20)
  }

  @Test
  fun `test battleStats has specialDefense`() {
    assertThat(battleStats.specialDefense).isEqualTo(20)
  }

  @Test
  fun `test battleStats has speed`() {
    assertThat(battleStats.speed).isEqualTo(100)
  }
}
