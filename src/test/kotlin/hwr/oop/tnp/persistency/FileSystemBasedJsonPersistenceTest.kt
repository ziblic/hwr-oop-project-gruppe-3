package hwr.oop.tnp.persistency

import hwr.oop.tnp.core.Battle
import hwr.oop.tnp.core.DamageStrategy
import io.kotest.core.spec.style.AnnotationSpec
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import java.io.File

class FileSystemBasedJsonPersistenceTest : AnnotationSpec() {
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
    FileSystemBasedJsonPersistence(tmpDir)
    assert(tmpDir.exists())
    tmpDir.deleteRecursively()
  }

  @Test
  fun `Save battle`() {
    val adapter = FileSystemBasedJsonPersistence(tmpDir)
    val dataFile = File(tmpDir, "1.json")
    adapter.saveBattle(Battle("1", DamageStrategy.DETERMINISTIC))
    assert(dataFile.exists())
    assertThat(
      dataFile.readLines(),
    ).isEqualTo(listOf("{\"battleId\":\"1\",\"damageStrategy\":\"DETERMINISTIC\"}"))
    tmpDir.deleteRecursively()
  }

  @Test
  fun `Load Battle correctly`() {
    val adapter = FileSystemBasedJsonPersistence(tmpDir)
    adapter.saveBattle(Battle("1", DamageStrategy.DETERMINISTIC))
    val battleJson = Json.encodeToString(adapter.loadBattle("1"))
    assertThat(battleJson).isEqualTo("{\"battleId\":\"1\",\"damageStrategy\":\"DETERMINISTIC\"}")
    tmpDir.deleteRecursively()
  }

  @Test
  fun `Throw IllegalArgumentException on loading non existing battle`() {
    val adapter = FileSystemBasedJsonPersistence(tmpDir)
    assertThatThrownBy { adapter.loadBattle("1") }.hasMessage("Could not find battle with id: 1.")
    tmpDir.deleteRecursively()
  }

  @Test
  fun `loadAllBattles returns empty list when data folder is not a directory`() {
    // Create a file (not a directory)
    val fakeDataFolder = File(tmpDir, "notADir")
    fakeDataFolder.writeText("I'm a file, not a directory")

    val adapter = FileSystemBasedJsonPersistence(fakeDataFolder)

    assertThatThrownBy {
      val battles = adapter.loadAllBattles()
      assertThat(battles).isEmpty()
    }
      .hasMessageContaining("Failed to list files in directory")
  }

  @Test
  fun `Load all battles correctly`() {
    val adapter = FileSystemBasedJsonPersistence(tmpDir)

    // Save two battles
    adapter.saveBattle(Battle("1", DamageStrategy.DETERMINISTIC))
    adapter.saveBattle(Battle("2", DamageStrategy.DETERMINISTIC))
    File(tmpDir, "3").createNewFile()
    File(tmpDir, "folder/").createNewFile()

    // Load all battles
    val battles = adapter.loadAllBattles()

    // Make sure both were loaded
    assertThat(battles).hasSize(2)

    // Compare the encoded JSON outputs, order-independent
    val encodedBattles = battles.map { Json.encodeToString(it) }
    assertThat(encodedBattles)
      .containsExactlyInAnyOrder(
        "{\"battleId\":\"1\",\"damageStrategy\":\"DETERMINISTIC\"}",
        "{\"battleId\":\"2\",\"damageStrategy\":\"DETERMINISTIC\"}",
      )

    tmpDir.deleteRecursively()
  }

  @Test
  fun `Load invalid file`() {
    val adapter = FileSystemBasedJsonPersistence(tmpDir)
    File(tmpDir, "3.json").writeText("Invalid Json")
    File(tmpDir, "4.json").writeText("{\"battleId\":1}")
    assertThat(adapter.loadAllBattles()).isEmpty()
    tmpDir.deleteRecursively()
  }
}
