package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.*
import java.io.File

class TrainerDataHandlerTest : AnnotationSpec() {
    private lateinit var tempDir: File
    private lateinit var trainersFile: File
    private lateinit var handler: TrainerDataHandler

    @BeforeEach
    fun setup() {
        tempDir = createTempDir()
        trainersFile = File(tempDir, "trainers.json")
        handler = TrainerDataHandler(trainersFile)
    }

    @AfterEach
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `save and load trainer`() {
        val trainer = Trainer("Ash")
        handler.saveTrainer(trainer)

        val loaded = handler.loadTrainer("Ash")
        assertThat(loaded).isNotNull
        assertThat(loaded!!.name).isEqualTo("Ash")
        assertThat(loaded.getMonsters()).isEmpty()
    }

    @Test
    fun `saveTrainer does not overwrite existing trainer`() {
        val trainer = Trainer("Brock")
        handler.saveTrainer(trainer)
        handler.saveTrainer(trainer) // should not overwrite

        val content = trainersFile.readText()
        val trainerCount = content.split("Brock").size - 1
        assertThat(trainerCount).isEqualTo(2) // One key, one value
    }

    @Test
    fun `add monster to trainer`() {
        val monsterName = "Squirtle"
        val bs = BattleStats(100, 100)
        val monster = Monster(monsterName, bs, Type.Water, emptyList())
        MonsterDataHandler(File(tempDir, "monsters.json")).saveMonster(monster)

        val trainer = Trainer("Misty")
        handler.saveTrainer(trainer)
        handler.addMonsterToTrainer("Misty", monsterName)

        val updated = handler.loadTrainer("Misty")
        assertThat(updated!!.getMonsters().map { it.name }).contains(monsterName)
    }

    @Test
    fun `delete trainer removes entry`() {
        val trainer = Trainer("Gary")
        handler.saveTrainer(trainer)

        handler.deleteTrainer(trainer)
        assertThat(handler.loadTrainer("Gary")).isNull()
    }

    @Test
    fun `deleteMonsterInTrainers removes reference`() {
        val bs = BattleStats(100, 100)
        val monster = Monster("Charmander", bs, Type.Fire, emptyList())
        val monsterHandler = MonsterDataHandler(File(tempDir, "monsters.json"))
        monsterHandler.saveMonster(monster)

        val trainer = Trainer("Red", mutableListOf(monster))
        handler.saveTrainer(trainer)

        handler.deleteMonsterInTrainers("Charmander")
        val updated = handler.loadTrainer("Red")

        assertThat(updated!!.getMonsters()).isEmpty()
    }
}
