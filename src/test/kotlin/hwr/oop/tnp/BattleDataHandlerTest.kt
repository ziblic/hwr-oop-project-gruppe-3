package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createTempDirectory

class BattleDataHandlerTest : AnnotationSpec() {

    private lateinit var tempDir: Path
    private lateinit var battleDir: File
    private lateinit var handler: BattleDataHandler
    private lateinit var counterFile: File

    private lateinit var trainerOne: Trainer
    private lateinit var trainerTwo: Trainer

    @BeforeEach
    fun setup() {
        tempDir = createTempDirectory(Paths.get(System.getProperty("user.dir")), "tmp")
        battleDir = File(tempDir.toFile(), "battles")
        counterFile = File(tempDir.toFile(), "battle_counter.txt")
        handler = BattleDataHandler(battleDir, counterFile)


        trainerOne =
            Trainer(
                "Ash",
                listOf(
                    Monster(
                        name = "Pikachu",
                        stats = BattleStats(hp = 100, speed = 80),
                        type = Type.Normal,
                        attacks = listOf(Attack.NORMAL_SLAM, Attack.GROUND_HAMMER)
                    )
                )
            )

        trainerTwo =
            Trainer(
                "Misty",
                listOf(
                    Monster(
                        name = "Staryu",
                        stats = BattleStats(hp = 90, speed = 70),
                        type = Type.Water,
                        attacks = listOf(Attack.TSUNAMI, Attack.SPLASH)
                    )
                )
            )
    }

    @AfterEach
    fun cleanup() {
        tempDir.toFile().deleteRecursively()
    }

    @Test
    fun `saveBattle should write a valid JSON file to disk`() {
        val battle = handler.createBattle(trainerOne, trainerTwo)
        battle!!
        val file = File(battleDir, "${battle.getBattleId()}.json")
        assertThat(file.exists()).isTrue
        assertThat(file.readText()).contains("Ash", "Misty", "battleId", "monsters")
        assertThat(battle).isNotNull
    }

    @Test
    fun `loadBattle should correctly read battle from file`() {

        val battle = handler.createBattle(trainerOne, trainerTwo)

        assertThat(battle).isNotNull()

        battle!!


        val loaded = handler.loadBattle(battle.getBattleId())

        assertThat(loaded.getBattleId()).isEqualTo(battle.getBattleId())

        assertThat(loaded.trainerOne.name).isEqualTo(trainerOne.name)
        assertThat(loaded.trainerOne.getMonsters()).hasSize(1)
        assertThat(loaded.trainerOne.getMonsters()[0].attacks)
            .containsExactly(Attack.NORMAL_SLAM, Attack.GROUND_HAMMER)

        assertThat(loaded.trainerTwo.name).isEqualTo(trainerTwo.name)
        assertThat(loaded.trainerTwo.getMonsters()).hasSize(1)
        assertThat(loaded.trainerTwo.getMonsters()[0].attacks)
            .containsExactly(Attack.TSUNAMI, Attack.SPLASH)
    }

    @Test
    fun `loadBattle should throw FileNotFoundException for nonexistent battle`() {
        val invalidId = Int.MAX_VALUE
        val exception = catchThrowable { handler.loadBattle(invalidId) }

        assertThat(exception)
            .isInstanceOf(FileNotFoundException::class.java)
            .hasMessageContaining("Battle file with ID $invalidId not found")
    }


    // --------------------------
    // Additional Tests for Counter
    // --------------------------

    @Test
    fun `getNextBattleId should return 1 on first call`() {
        counterFile.delete()
        val nextId = handler.getNextBattleId()
        assertThat(counterFile.exists()).isTrue()
        assertThat(nextId).isEqualTo(1)
    }

    @Test
    fun `getNextBattleId should increment and persist correctly`() {
        val id1 = handler.getNextBattleId()
        val id2 = handler.getNextBattleId()
        val id3 = handler.getNextBattleId()

        assertThat(id1).isEqualTo(1)
        assertThat(id2).isEqualTo(2)
        assertThat(id3).isEqualTo(3)

        val saved = counterFile.readText().trim()
        assertThat(saved).isEqualTo("3")
    }

    @Test
    fun `counter file should be created automatically if missing`() {
        counterFile.delete() // simulate missing file

        val newHandler = BattleDataHandler(battleDir, counterFile)
        val id = newHandler.getNextBattleId()

        assertThat(id).isEqualTo(1)
        assertThat(counterFile.exists()).isTrue
        assertThat(counterFile.readText().trim()).isEqualTo("1")
    }

    @Test
    fun `getNextBattleId should recover from invalid counter content`() {
        counterFile.writeText("not-a-number")

        val id = handler.getNextBattleId()

        assertThat(id).isEqualTo(1)
        assertThat(counterFile.readText().trim()).isEqualTo("1")
    }

}
