package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.captureStandardOut
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createTempDirectory

class BattleDataHandlerTest : AnnotationSpec() {

    @TempDir
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
        // Ensure the file does not exist
        if (counterFile.exists()) {
            counterFile.delete()
        }
        assertThat(counterFile.exists()).isFalse
        // Act
        val localHandler = BattleDataHandler(battleDir, counterFile)
        val id = localHandler.getNextBattleId()

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


    @Test
    fun `saveBattle should create the file if it does not exist`() {
        val localHandler = BattleDataHandler(battleDir, counterFile)
        val battle = Battle(trainerOne, trainerTwo, 99)
        val battleFile = File(battleDir, "99.json")

        // Delete the file forcibly

        // the program won't delete the file so you can't check the condition when the file doesn't exist!
        var deleted = battleFile.delete()

//        assertThat(deleted).isTrue()
        assertThat(battleFile.exists()).isFalse()  // must be false here!

        // Extra wait or retry to avoid race conditions (if needed)
        repeat(3) {
            if (!battleFile.exists()) return@repeat
            Thread.sleep(10)
        }
        assertThat(battleFile.exists()).isFalse() // confirm again

        localHandler.saveBattle(battle)

        assertThat(battleFile.exists()).isTrue()
        assertThat(battleFile.readText()).contains("Ash", "Misty")
    }



    @Test
    fun `saveBattle should print success message`() {
        val battle = Battle(trainerOne, trainerTwo, 42)

        val output = captureStandardOut {
            handler.saveBattle(battle)
        }.trim()

        assertThat(output).contains("✅ Battle '${battle.getBattleId()}' successfully saved between '${trainerOne.name}' and '${trainerTwo.name}'")
    }





}
