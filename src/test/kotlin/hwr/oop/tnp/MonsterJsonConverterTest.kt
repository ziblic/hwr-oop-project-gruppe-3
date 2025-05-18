package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class MonsterJsonConverterTest : AnnotationSpec() {

    private lateinit var monster: Monster

    @BeforeEach
    fun setup() {
        monster = Monster(
            name = "Onix",
            stats = BattleStats(hp = 120, speed = 30),
            type = Type.Normal,
            attacks = listOf(Attack.PUNCH, Attack.DRUM)
        )
    }

    @Test
    fun `toJson converts Monster to JSON correctly`() {
        val json = MonsterJsonConverter.toJson(monster)

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
        val json = MonsterJsonConverter.toJson(monster)
        val converted = MonsterJsonConverter.fromJson(json)

        assertThat(converted.name).isEqualTo(monster.name)
        assertThat(converted.type).isEqualTo(monster.type)
        assertThat(converted.stats.hp).isEqualTo(monster.stats.hp)
        assertThat(converted.stats.speed).isEqualTo(monster.stats.speed)
        assertThat(converted.attacks).containsExactlyElementsOf(monster.attacks)
    }

    @Test
    fun `fromJson handles missing attacks field gracefully`() {
        val json = MonsterJsonConverter.toJson(monster)
        json.remove("attacks")

        val result = MonsterJsonConverter.fromJson(json)
        assertThat(result.attacks).isEmpty()
    }

    @Test
    fun `fromJson handles empty attacks array`() {
        val json = MonsterJsonConverter.toJson(monster)
        json.put("attacks", JSONArray())

        val result = MonsterJsonConverter.fromJson(json)
        assertThat(result.attacks).isEmpty()
    }

    @Test
    fun `fromJson logs warning when attack name is invalid`() {
        val json = MonsterJsonConverter.toJson(monster)
        val attacksArray = json.getJSONArray("attacks")
        attacksArray.put("INVALID_ATTACK")

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val result = MonsterJsonConverter.fromJson(json)

        assertThat(result.attacks).containsOnly(Attack.PUNCH, Attack.DRUM)
        assertThat(outputStream.toString()).contains("Invalid attack name in JSON")
    }

    @Test
    fun `fromJson skips null or unknown attack names`() {
        val json = MonsterJsonConverter.toJson(monster)
        val attacksArray = json.getJSONArray("attacks")
        attacksArray.put(JSONObject.NULL)

        val result = MonsterJsonConverter.fromJson(json)
        assertThat(result.attacks).containsExactly(Attack.PUNCH, Attack.DRUM)
    }

    @Test
    fun `fromJson reads correct number of attacks`() {
        // Arrange
        val json = JSONObject()
            .put("name", "Charizard")
            .put("type", "Fire")
            .put("stats", JSONObject().put("hp", 200).put("speed", 60))
            .put("attacks", JSONArray(listOf("PUNCH", "DRUM", "PREDATOR_LEAF")))

        // Act
        val monster = MonsterJsonConverter.fromJson(json)

        // Assert
        assertThat(monster.attacks).hasSize(3)
        assertThat(monster.attacks).containsExactly(Attack.PUNCH, Attack.DRUM, Attack.PREDATOR_LEAF)
    }

}
