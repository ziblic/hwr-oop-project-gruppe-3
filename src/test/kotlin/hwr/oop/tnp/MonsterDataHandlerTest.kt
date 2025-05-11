package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.*
import java.io.File

class MonsterDataHandlerTest : AnnotationSpec() {

    private lateinit var tempDir: File
    private lateinit var monstersFile: File
    private lateinit var handler: MonsterDataHandler

    private val testMonster = Monster(
        name = "Testmon",
        stats = BattleStats(hp = 100, speed = 30),
        type = Type.Normal,
        attacks = listOf(
            Attack.DRUM
        )
    )

    @BeforeEach
    fun setup() {
        tempDir = createTempDir()
        monstersFile = File(tempDir, "monsters.json")
        handler = MonsterDataHandler(monstersFile)
    }

    @AfterEach
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `saveMonster should persist monster to file`() {
        handler.saveMonster(testMonster)

        val loaded = handler.loadMonster("Testmon")
        assertThat(loaded).isNotNull
        assertThat(loaded!!.name).isEqualTo(testMonster.name)
        assertThat(loaded.stats.hp).isEqualTo(testMonster.stats.hp)
        assertThat(loaded.attacks).hasSize(1)
        assertThat(loaded.attacks[0].name).isEqualTo("Flame Burst")
    }

    @Test
    fun `saveMonster should not overwrite existing monster`() {
        handler.saveMonster(testMonster)
        val lastModified = monstersFile.lastModified()

        Thread.sleep(10) // Ensure file system clock moves forward
        handler.saveMonster(testMonster)

        assertThat(monstersFile.lastModified()).isEqualTo(lastModified)
    }

    @Test
    fun `loadMonster should return null if file does not exist`() {
        val freshHandler = MonsterDataHandler(File(tempDir, "nonexistent.json"))
        val loaded = freshHandler.loadMonster("Ghostmon")
        assertThat(loaded).isNull()
    }

    @Test
    fun `loadMonster should return null if monster not found`() {
        handler.saveMonster(testMonster)
        val loaded = handler.loadMonster("Unknownmon")
        assertThat(loaded).isNull()
    }

    @Test
    fun `deleteMonster by name should remove monster`() {
        handler.saveMonster(testMonster)
        handler.deleteMonster("Testmon")
        val loaded = handler.loadMonster("Testmon")
        assertThat(loaded).isNull()
    }

    @Test
    fun `deleteMonster by instance should remove monster`() {
        handler.saveMonster(testMonster)
        val loaded = handler.loadMonster("Testmon")
        handler.deleteMonster(loaded)
        val afterDelete = handler.loadMonster("Testmon")
        assertThat(afterDelete).isNull()
    }

    @Test
    fun `deleteMonster should do nothing if monster is null`() {
        handler.deleteMonster(null) // should not throw
        // Test passes if no exception is thrown
    }
}
