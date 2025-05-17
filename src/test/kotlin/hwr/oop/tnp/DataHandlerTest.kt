package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.withEnvironment
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatNoException
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.nio.file.Files

class DataHandlerTest : AnnotationSpec() {
    @Test
    fun `test isDryRun`() {
        val tempDir = "tmp"
        File(System.getProperty("user.dir"), tempDir).deleteRecursively()
        withEnvironment(mapOf("TESTING" to "true", "DATADIR" to tempDir)) {
            val isTesting = System.getenv("TESTING")
            val dataDir = System.getenv("DATADIR")

            assert(isTesting == "true" && dataDir == tempDir) {
                "Env not set correctly: TESTING is '$isTesting', and DATADIR is '$dataDir'"
            }

            val dataHandler = DataHandler()
            assert(!File(System.getProperty("user.dir"), tempDir).exists()) {
                "Folder should not exist"
            }
            assertThatNoException().isThrownBy {
                dataHandler.saveMonster(
                    Monster(
                        "Pika",
                        BattleStats(100, 20),
                        Type.FIRE,
                        listOf(Attack.FLAME_WREATH)
                    )
                )
                dataHandler.saveTrainer(Trainer("Kevin"))
                dataHandler.saveBattle(
                    Battle(
                        Trainer(
                            "Kevin",
                            listOf(
                                Monster(
                                    "Pika",
                                    BattleStats(100, 20),
                                    Type.FIRE,
                                    listOf(Attack.FLAME_WREATH)
                                )
                            )
                        ),
                        Trainer(
                            "Bob",
                            listOf(
                                Monster(
                                    "Bulbasaur",
                                    BattleStats(200, 10),
                                    Type.FIRE,
                                    listOf(Attack.FLAME_WREATH)
                                )
                            )
                        )
                    )
                )
            }
            assertThrows<Exception> { dataHandler.loadTrainer("Bob") }
            assertThrows<Exception> { dataHandler.loadMonster("Pika") }
            assertThrows<Exception> { dataHandler.loadBattle(1) }
        }
    }

    @Test
    fun `test non dry run`() {
        File(System.getProperty("user.dir"), "temp-dir").deleteRecursively()
        withEnvironment("DATADIR" to "temp-dir") {
            assert(!File(System.getProperty("user.dir"), "temp-dir").exists())

            val dataHandler = DataHandler()

            assert(File(System.getProperty("user.dir"), "temp-dir").exists())
            assert(
                File(System.getProperty("user.dir"), "temp-dir/monster.json")
                    .exists()
            )
            assert(
                File(System.getProperty("user.dir"), "temp-dir/trainer.json")
                    .exists()
            )

            assertThrows<Exception> { dataHandler.loadTrainer("Bob") }
            assertThrows<Exception> { dataHandler.loadMonster("Pika") }
            assertThrows<Exception> { dataHandler.loadBattle(1) }

            val monster1 =
                Monster(
                    "Pika",
                    BattleStats(100, 20),
                    Type.FIRE,
                    listOf(Attack.FLAME_WREATH)
                )
            val monster2 =
                Monster(
                    "Glurak",
                    BattleStats(100, 20),
                    Type.FIRE,
                    listOf(Attack.FLAME_WREATH)
                )
            val trainer1 = Trainer("Kevin", listOf(monster1))
            val trainer2 = Trainer("Bob", listOf(monster2))
            val battle = Battle(trainer1, trainer2)

            dataHandler.saveTrainer(trainer1)
            dataHandler.saveMonster(monster1)
            dataHandler.saveBattle(battle)

            assertThat(Json.encodeToString(dataHandler.loadTrainer("Kevin")))
                .isEqualTo(Json.encodeToString(trainer1))
            assertThat(Json.encodeToString(dataHandler.loadMonster("Pika")))
                .isEqualTo(Json.encodeToString(monster1))
            assertThat(Json.encodeToString(dataHandler.loadBattle(1)))
                .isEqualTo(Json.encodeToString(battle))
        }
        File(System.getProperty("user.dir"), "temp-dir").deleteRecursively()
    }

    @Test
    fun `getNextBattleId returns correct next ID`() {
        val tempBattleFolder = Files.createTempDirectory("temp-dir").toFile()

        try {
            File(tempBattleFolder, "1.json").createNewFile()
            File(tempBattleFolder, "2.json").createNewFile()
            File(tempBattleFolder, "abc.json").createNewFile()

            val nextBattleId = DataHandler.getNextBattleId(tempBattleFolder)

            assertThat(3).isEqualTo(nextBattleId)
        } finally {
            tempBattleFolder.deleteRecursively()
        }
    }
}
