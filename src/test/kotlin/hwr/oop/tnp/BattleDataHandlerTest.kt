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

    private lateinit var trainer1: Trainer
    private lateinit var trainer2: Trainer

    @BeforeEach
    fun setup() {
        tempDir = createTempDirectory(Paths.get(System.getProperty("user.dir")), "tmp")
        battleDir = File(tempDir.toFile(), "battles")
        handler = BattleDataHandler(battleDir)

        trainer1 =
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

        trainer2 =
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
        val battle = Battle(trainer1 = trainer1, trainer2 = trainer2)
        handler.saveBattle(battle)

        val file = File(battleDir, "${battle.battleId}.json")
        assertThat(file.exists()).isTrue
        assertThat(file.readText()).contains("Ash", "Misty", "battleId", "monsters", "status")
    }

    @Test
    fun `loadBattle should correctly read battle from file`() {
        val battle = Battle(trainer1 = trainer1, trainer2 = trainer2)
        handler.saveBattle(battle)

        val loaded = handler.loadBattle(battle.battleId)

        assertThat(loaded.battleId).isEqualTo(battle.battleId)

        assertThat(loaded.trainer1.name).isEqualTo("Ash")
        assertThat(loaded.trainer1.getMonsters()).hasSize(1)
        assertThat(loaded.trainer1.getMonsters()[0].attacks)
            .containsExactly(Attack.NORMAL_SLAM, Attack.GROUND_HAMMER)

        assertThat(loaded.trainer2.name).isEqualTo("Misty")
        assertThat(loaded.trainer2.getMonsters()).hasSize(1)
        assertThat(loaded.trainer2.getMonsters()[0].attacks)
            .containsExactly(Attack.TSUNAMI, Attack.SPLASH)
    }

    @Test
    fun `loadBattle should throw FileNotFoundException for nonexistent battle`() {
        val invalidId = 999999
        val exception = catchThrowable { handler.loadBattle(invalidId) }

        assertThat(exception)
            .isInstanceOf(FileNotFoundException::class.java)
            .hasMessageContaining("Battle file with ID $invalidId not found")
    }
}
