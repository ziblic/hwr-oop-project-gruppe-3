package hwr.oop.tnp.persistency

import hwr.oop.tnp.core.Battle
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.captureStandardOut
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import java.io.File

class PersistenceAdapterTest : AnnotationSpec() {
    private val tmpDir = File(System.getProperty("user.dir"), "tmp")

    @Test
    fun `Create folder if not exists yet`() {
        tmpDir.deleteRecursively()
        PersistenceAdapter(tmpDir)
        assert(tmpDir.exists())
        tmpDir.deleteRecursively()
    }

    @Test
    fun `Save battle`() {
        val adapter = PersistenceAdapter(tmpDir)
        val dataFile = File(tmpDir, "1.json")
        assert(!dataFile.exists())
        adapter.saveBattle(Battle("1"))
        assert(dataFile.exists())
        assertThat(dataFile.readLines())
            .isEqualTo(
                listOf(
                    "{",
                    "    \"battleId\": \"1\",",
                    "    \"trainerOne\": null,",
                    "    \"trainerTwo\": null,",
                    "    \"currentTrainer\": null,",
                    "    \"status\": \"PREGAME\",",
                    "    \"currentRound\": 1",
                    "}"
                )
            )
        tmpDir.deleteRecursively()
    }

    @Test
    fun `Load Battle correctly`() {
        val adapter = PersistenceAdapter(tmpDir)
        adapter.saveBattle(Battle("1"))
        val battleJson = Json.encodeToString(adapter.loadBattle("1"))
        assertThat(battleJson).isEqualTo("{\"battleId\":\"1\"}")
        tmpDir.deleteRecursively()
    }

    @Test
    fun `Load all battles correctly`() {
        val adapter = PersistenceAdapter(tmpDir)
        adapter.saveBattle(Battle("1"))
        adapter.saveBattle(Battle("2"))
        val battles = adapter.loadAllBattles()
        assert(battles.size == 2)
        battles.forEachIndexed { index, battle ->
            assertThat(Json.encodeToString(battle))
                .isEqualTo("{\"battleId\":\"${index + 1}\"}")
        }
        tmpDir.deleteRecursively()
    }

    @Test
    fun `Load invalid file`() {
        val adapter = PersistenceAdapter(tmpDir)
        File(tmpDir, "3.json").writeText("Invalid Json")
        val output = captureStandardOut { adapter.loadAllBattles() }.trim()
        assertThat(output)
            .isEqualTo(
                "Failed to load battle from file 3.json: Unexpected JSON token at offset 0: Expected start of the object '{', but had 'I' instead at path: $\nJSON input: Invalid Json"
            )
        tmpDir.deleteRecursively()
    }
}
