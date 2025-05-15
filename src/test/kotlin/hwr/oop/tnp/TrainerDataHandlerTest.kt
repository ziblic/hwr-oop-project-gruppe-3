package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.captureStandardOut
import org.assertj.core.api.Assertions.assertThat
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createTempDirectory

class TrainerDataHandlerTest : AnnotationSpec() {
    private lateinit var tempDir: Path
    private lateinit var trainersFile: File
    private lateinit var handler: TrainerDataHandler

    @BeforeEach
    fun setup() {
        tempDir = createTempDirectory(Paths.get(System.getProperty("user.dir")), "tmp")
        trainersFile = File(tempDir.toFile(), "trainers.json")
        handler = TrainerDataHandler(trainersFile)
    }

    @AfterEach
    fun cleanup() {
        tempDir.toFile().deleteRecursively()
    }

    @Test
    fun `saveTrainer when the file was deleted`() {
        // Simulate the case where the trainers file does not exist
        val trainersFile = File(tempDir.toFile(), "trainers.json")
        trainersFile.delete() // Ensure it doesn't exist

        val trainer = Trainer("Ash")

        // Capture the output
        val output = captureStandardOut { handler.saveTrainer(trainer) }.trim()

         // Verify that the appropriate message is printed
        assertThat(trainersFile.exists()).isTrue()
    }



    @Test
    fun `saving and loading a trainer should preserve trainer name and empty monster list`() {
        val trainer = Trainer("Ash")
        handler.saveTrainer(trainer)

        val loaded = handler.loadTrainer("Ash")

        assertThat(loaded)
            .withFailMessage("Trainer 'Ash' was not loaded correctly")
            .isNotNull

        assertThat(loaded!!.name).isEqualTo("Ash")
        assertThat(loaded.getMonsters())
            .withFailMessage("Trainer 'Ash' should have an empty monster list")
            .isEmpty()
    }

    @Test
    fun `saving a trainer twice should not overwrite the existing trainer entry`() {
        val trainer = Trainer("Brock")
        handler.saveTrainer(trainer)

        // second save
        val output = captureStandardOut {  handler.saveTrainer(trainer) }.trim()


        val content = trainersFile.readText()
        val trainerKeyCount = "\"Brock\"".toRegex().findAll(content).count()

        assertThat(output).isEqualTo("Trainer '${trainer.name}' already exists.")
        assertThat(trainerKeyCount)
            .withFailMessage("Trainer 'Brock' appears $trainerKeyCount times instead of 1 key and 1 value")
            .isEqualTo(2) // one key, one value
    }

    @Test
    fun `adding a monster to a trainer should persist correctly`() {
        val monsterName = "Squirtle"
        val battleStats = BattleStats(100, 100)
        val monster = Monster(monsterName, battleStats, Type.Water, emptyList())
        val monsterHandler = MonsterDataHandler(File(tempDir.toFile(), "monsters.json"))
        monsterHandler.saveMonster(monster)

        val trainer = Trainer("Misty")
        handler.saveTrainer(trainer)
        handler.addMonsterToTrainer("Misty", monsterName)

        val updatedTrainer = handler.loadTrainer("Misty")

        assertThat(updatedTrainer)
            .withFailMessage("Trainer 'Misty' was not found after adding a monster")
            .isNotNull

        assertThat(updatedTrainer!!.getMonsters().map { it.name })
            .withFailMessage("Trainer 'Misty' should have monster '$monsterName'")
            .contains(monsterName)
    }

    @Test
    fun `deleting a trainer should remove it from storage`() {
        val trainer = Trainer("Gary")
        handler.saveTrainer(trainer)

        handler.deleteTrainer(trainer)

        val loaded = handler.loadTrainer("Gary")
        assertThat(loaded)
            .withFailMessage("Trainer 'Gary' should be deleted but was found")
            .isNull()
    }

    @Test
    fun `deleting a monster from all trainers should remove its reference`() {
        val monster = Monster("Charmander", BattleStats(100, 100), Type.Fire, emptyList())
        val monsterHandler = MonsterDataHandler(File(tempDir.toFile(), "monsters.json"))
        monsterHandler.saveMonster(monster)

        val trainer = Trainer("Red", mutableListOf(monster))
        handler.saveTrainer(trainer)

        handler.deleteMonsterInTrainers("Charmander")
        val updatedTrainer = handler.loadTrainer("Red")

        assertThat(updatedTrainer)
            .withFailMessage("Trainer 'Red' not found after deleting monster")
            .isNotNull

        assertThat(updatedTrainer!!.getMonsters())
            .withFailMessage("Trainer 'Red' should have no monsters after deletion")
            .isEmpty()
    }
}
