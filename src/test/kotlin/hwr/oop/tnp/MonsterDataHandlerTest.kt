package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createTempDirectory
import io.kotest.extensions.system.captureStandardOut

class MonsterDataHandlerTest : AnnotationSpec() {

    private lateinit var tempDir: Path
    private lateinit var monstersFile: File
    private lateinit var handler: MonsterDataHandler

    private val testMonster =
        Monster(
            name = "Testmon",
            stats = BattleStats(hp = 100, speed = 30),
            type = Type.Normal,
            attacks = listOf(Attack.LAVA_FLOOD)
        )

    @BeforeEach
    fun setup() {
        tempDir = createTempDirectory(Paths.get(System.getProperty("user.dir")), "tmp")
        monstersFile = File(tempDir.toFile(), "monsters.json")
        handler = MonsterDataHandler(monstersFile)
    }

    @AfterEach
    fun cleanup() {
        tempDir.toFile().deleteRecursively()
    }

    @Test
    fun `saveMonster should persist monster to file`() {
        handler.saveMonster(testMonster)

        val loaded = handler.loadMonster("Testmon")
        assertThat(loaded).isNotNull
        assertThat(loaded!!.name).isEqualTo(testMonster.name)
        assertThat(loaded.stats.hp).isEqualTo(testMonster.stats.hp)
        assertThat(loaded.attacks).hasSize(1)
        assertThat(loaded.attacks[0].name).isEqualTo("LAVA_FLOOD")
    }

    @Test
    fun `saveMonster should not overwrite existing monster`() {
        handler.saveMonster(testMonster)
        val lastModified = monstersFile.lastModified()

        Thread.sleep(10) // Ensure file system clock moves forward
        val output = captureStandardOut {
            handler.saveMonster(testMonster)
        }.trim()

        assertThat(monstersFile.lastModified()).isEqualTo(lastModified)
        assertThat(output).contains("Monster 'Testmon' already exists.")
    }

    @Test
    fun `loadMonster should return null and print message if file does not exist`() {
        val freshHandler = MonsterDataHandler(File(tempDir.toFile(), "nonexistent.json"))
        val loaded = freshHandler.loadMonster("Ghostmon")
        val output = captureStandardOut {
            freshHandler.loadMonster("Ghostmon")
        }.trim()
        assertThat(loaded).isNull()
        assertThat(output).contains("Monsters file does not exist.")
    }

    @Test
    fun `loadMonster should return null and print message if monster not found`() {
        handler.saveMonster(testMonster)
        val loaded = handler.loadMonster("Unknownmon")
        val output = captureStandardOut {
            handler.loadMonster("Unknownmon")
        }.trim()
        assertThat(loaded).isNull()
        assertThat(output).contains("No monster with name 'Unknownmon' found.")
    }

    @Test
    fun `deleteMonster by name should remove monster and print message`() {
        handler.saveMonster(testMonster)

        val output = captureStandardOut {
            handler.deleteMonster(testMonster)
        }.trim()
        val loaded = handler.loadMonster("Testmon")
        assertThat(loaded).isNull()
        assertThat(output).contains("Monster 'Testmon' deleted from monsters.json.")
    }

    @Test
    fun `deleteMonster by instance should remove monster and print message`() {
        handler.saveMonster(testMonster)
        val loaded = handler.loadMonster("Testmon")


        val output = captureStandardOut {
            handler.deleteMonster(loaded)
        }.trim()

        val afterDelete = handler.loadMonster("Testmon")
        assertThat(afterDelete).isNull()
        assertThat(output).contains("Monster 'Testmon' deleted from monsters.json.")

    }

    @Test
    fun `deleteMonster should do nothing if monster is null`() {
        handler.deleteMonster(null) // should not throw
        // Test passes if no exception is thrown
    }

    @Test
    fun `deleteMonster should print message if monster not found in file`() {
        handler.saveMonster(testMonster)

        val monster = Monster("Ghostmon", BattleStats(1, 1), Type.Fire, listOf(Attack.TSUNAMI))
        val output = captureStandardOut {
            handler.deleteMonster(monster)
        }.trim()

        assertThat(output).contains("Monster 'Ghostmon' not found.")
    }

    @Test
    fun `deleteMonster should print message if monsters file does not exist`() {
        val freshHandler = MonsterDataHandler(File(tempDir.toFile(), "missing.json"))

        val output = captureStandardOut {
            freshHandler.deleteMonster(testMonster)
        }.trim()

        assertThat(output).contains("No monsters file found.")
    }

    @Test
    fun `saveMonster should not throw if parentFile is null`() {
        val fileWithoutParent = File("monsters_no_parent.json")
        val handler = MonsterDataHandler(fileWithoutParent)
        handler.saveMonster(testMonster)
        assertThat(fileWithoutParent.exists()).isTrue()
        fileWithoutParent.delete()
    }

    @Test
    fun `deleteMonster should do nothing if monster is not in JSON`() {
        handler.saveMonster(testMonster)
        val unknown = Monster("NotInJson", testMonster.stats, testMonster.type, testMonster.attacks)

        val output = captureStandardOut {
            handler.deleteMonster(unknown)
        }
        assertThat(output).contains("Monster 'NotInJson' not found.")
    }
    @Test
    fun `deleteMonster should handle null monster`() {
        handler.saveMonster(testMonster)
        val output = captureStandardOut {
            handler.deleteMonster(null)
        }
        assertThat(output).contains("Monster 'null' not found.")
    }





}
