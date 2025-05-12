package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MonsterJsonConverterTest : AnnotationSpec() {

    private lateinit var monster: Monster

    @BeforeEach
    fun setup() {
        // Prepare a sample Monster object to test with
        monster = Monster(
            name = "Onix",
            stats = BattleStats(hp = 120, speed = 30),
            type = Type.Normal,
            attacks = listOf(Attack.PUNCH, Attack.DRUM)
        )
    }

    @Test
    fun `toJson converts Monster to JSON correctly`() {
        // Convert monster to JSON
        val json = MonsterJsonConverter.toJson(monster)

        // Check the JSON structure and values
        assertThat(json.getString("name")).isEqualTo(monster.name)
        assertThat(json.getString("type")).isEqualTo(monster.type.name)

        val statsJson = json.getJSONObject("stats")
        assertThat(statsJson.getInt("hp")).isEqualTo(monster.stats.hp)
        assertThat(statsJson.getInt("speed")).isEqualTo(monster.stats.speed)

        val attacksJsonArray = json.getJSONArray("attacks")
        assertThat(attacksJsonArray.length()).isEqualTo(monster.attacks.size)
        for (i in 0 until attacksJsonArray.length()) {
            assertThat(attacksJsonArray.getString(i)).isEqualTo(monster.attacks[i].name)
        }
    }

    @Test
    fun `fromJson converts JSON to Monster correctly`() {
        // Convert monster to JSON first
        val json = MonsterJsonConverter.toJson(monster)

        // Convert the JSON back to a Monster object
        val deserializedMonster = MonsterJsonConverter.fromJson(json)

        // Check if the values are correctly restored
        assertThat(deserializedMonster.name).isEqualTo(monster.name)
        assertThat(deserializedMonster.type).isEqualTo(monster.type)
        assertThat(deserializedMonster.stats.hp).isEqualTo(monster.stats.hp)
        assertThat(deserializedMonster.stats.speed).isEqualTo(monster.stats.speed)

        val attackNames = deserializedMonster.attacks.map { it.name }
        val originalAttackNames = monster.attacks.map { it.name }
        assertThat(attackNames).containsExactlyElementsOf(originalAttackNames)
    }
}
