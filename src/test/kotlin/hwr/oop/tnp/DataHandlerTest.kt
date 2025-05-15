package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.captureStandardOut
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.io.path.createTempDirectory

class DataHandlerTest : AnnotationSpec() {

    private lateinit var tempDir: File
    private lateinit var dataHandler: DataHandler

    @BeforeEach
    fun setup() {
        tempDir = createTempDirectory().toFile()
        dataHandler = DataHandler(tempDir.absolutePath)
    }

    @AfterEach
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `save and load trainer`() {
        val trainerName = "Ash"
        dataHandler.saveTrainer(trainerName)

        val trainer = dataHandler.loadTrainer(trainerName)

        assertThat(trainer).isNotNull
        assertThat(trainer?.name).isEqualTo(trainerName)
    }

    @Test
    fun `save monster and load it correctly`() {
        val trainerName = "Brock"
        dataHandler.saveTrainer(trainerName)

        val monsterName = "Onix"
        val attacks = listOf("PUNCH", "DRUM")
        dataHandler.saveMonster(monsterName, 120, 30, attacks, trainerName)

        val monster = dataHandler.loadMonster(monsterName)

        assertThat(monster).isNotNull
        assertThat(monster!!.name).isEqualTo(monsterName)
        assertThat(monster.stats.hp).isEqualTo(120)
        assertThat(monster.stats.speed).isEqualTo(30)
        assertThat(monster.attacks.map { it.name }).containsExactlyElementsOf(attacks)
        assertThat(monster.type).isEqualTo(Attack.valueOf(attacks.first()).type)
    }

    @Test
    fun `save monster adds it to trainer`() {
        val trainerName = "Misty"
        dataHandler.saveTrainer(trainerName)

        val monsterName = "Staryu"
        dataHandler.saveMonster(monsterName, 60, 45, listOf("SPLASH"), trainerName)

        val trainer = dataHandler.loadTrainer(trainerName)

        assertThat(trainer).isNotNull
        assertThat(trainer!!.getMonsters().map { it.name }).contains(monsterName)
    }

    @Test
    fun `create and load battle with trainers having no monsters`() {
        val t1 = Trainer("Red")
        val t2 = Trainer("Blue")

        dataHandler.saveTrainer(t1)
        dataHandler.saveTrainer(t2)


        val battle = dataHandler.createBattle(t1, t2)
        val output = captureStandardOut {dataHandler.createBattle(t1, t2) }.trim()

        assertThat(battle).isNull()
        assertThat(output).contains("Battle that contains trainers with no monsters could not be created")



    }

    @Test
    fun `delete trainer deletes associated monsters`() {
        val trainerName = "Erika"
        dataHandler.saveTrainer(trainerName)

        val monsterName = "Tangela"
        dataHandler.saveMonster(monsterName, 90, 20, listOf("LEAF_GUN"), trainerName)

        dataHandler.deleteTrainer(trainerName)

        val trainer = dataHandler.loadTrainer(trainerName)
        val monster = dataHandler.loadMonster(monsterName)

        assertThat(trainer).isNull()
        assertThat(monster).isNull()
    }

    @Test
    fun `delete monster removes it from trainer`() {
        val trainerName = "LtSurge"
        dataHandler.saveTrainer(trainerName)

        val monsterName = "Voltorb"
        dataHandler.saveMonster(monsterName, 50, 90, listOf("SPARK_TAIL"), trainerName)

        dataHandler.deleteMonster(monsterName)

        val monster = dataHandler.loadMonster(monsterName)
        val trainer = dataHandler.loadTrainer(trainerName)

        assertThat(monster).isNull()
        assertThat(trainer!!.getMonsters().map { it.name }).doesNotContain(monsterName)
    }

    @Test
    fun `createBattle throws when trainer not found`() {
        val ex = assertThrows<IllegalArgumentException> {
            dataHandler.createBattle("Ghost", "Phantom")
        }
        assertThat(ex.message).contains("Trainer 'Ghost' not found")
    }
}
