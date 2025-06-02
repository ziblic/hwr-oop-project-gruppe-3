package hwr.oop.tnp.persistency

import hwr.oop.tnp.core.Battle
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.captureStandardOut
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import java.io.File

class PersistenceAdapterTest : AnnotationSpec() {
  private val tmpDir = File(System.getProperty("user.dir"), "tmp")

  @BeforeEach
  fun prepare() {
    tmpDir.deleteRecursively()
    tmpDir.mkdirs()
  }

  @AfterEach
  fun cleanup() {
    tmpDir.deleteRecursively()
  }

  @Test
  fun `Create folder if not exists yet`() {
    tmpDir.deleteRecursively()
    FileSystemBasedJSONPersistence(tmpDir)
    assert(tmpDir.exists())
    tmpDir.deleteRecursively()
  }

  @Test
  fun `Save battle`() {
    val adapter = FileSystemBasedJSONPersistence(tmpDir)
    val dataFile = File(tmpDir, "1.json")
    adapter.saveBattle(Battle("1"))
    assert(dataFile.exists())
    assertThat(dataFile.readLines()).isEqualTo(listOf("{\"battleId\":\"1\"}"))
    tmpDir.deleteRecursively()
  }

  @Test
  fun `Load Battle correctly`() {
    val adapter = FileSystemBasedJSONPersistence(tmpDir)
    adapter.saveBattle(Battle("1"))
    val battleJson = Json.encodeToString(adapter.loadBattle("1"))
    assertThat(battleJson).isEqualTo("{\"battleId\":\"1\"}")
    tmpDir.deleteRecursively()
  }

  @Test
  fun `Throw IllegalArgumentException on loading non existing battle`() {
    val adapter = FileSystemBasedJSONPersistence(tmpDir)
    assertThrows<IllegalArgumentException> { adapter.loadBattle("1") }
    tmpDir.deleteRecursively()
  }

  @Test
  fun `Load all battles correctly`() {
    val adapter = FileSystemBasedJSONPersistence(tmpDir)

    // Save two battles
    adapter.saveBattle(Battle("1"))
    adapter.saveBattle(Battle("2"))

    // Load all battles
    val battles = adapter.loadAllBattles()

    // Make sure both were loaded
    assertThat(battles).hasSize(2)

    // Compare the encoded JSON outputs, order-independent
    val encodedBattles = battles.map { Json.encodeToString(it) }
    assertThat(encodedBattles)
      .containsExactlyInAnyOrder("""{"battleId":"1"}""", """{"battleId":"2"}""")

    tmpDir.deleteRecursively()
  }

  @Test
  fun `Load invalid file`() {
    val adapter = FileSystemBasedJSONPersistence(tmpDir)
    File(tmpDir, "3.json").writeText("Invalid Json")
    val output = captureStandardOut { adapter.loadAllBattles() }.trim()
    assertThat(output)
      .isEqualTo(
        "Failed to load battle from file 3.json: Unexpected JSON token at offset 0: Expected start of the object '{', but had 'I' instead at path: $\nJSON input: Invalid Json"
      )
    tmpDir.deleteRecursively()
  }
}
