package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.captureStandardOut
import io.kotest.extensions.system.withEnvironment
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatNoException
import java.io.File

class GameTest : AnnotationSpec() {
    @Test
    fun `test createTrainer`() {
        File(System.getProperty("user.dir"), "temp-dir").deleteRecursively()
        withEnvironment(mapOf("DATADIR" to "temp-dir")) {
            val output = captureStandardOut { Game().createTrainer("Bob") }.trim()
            assertThat(output).isEqualTo("Created Trainer with name Bob")
            assertThatNoException().isThrownBy { DataHandler().loadTrainer("Bob") }
        }
        File(System.getProperty("user.dir"), "temp-dir").deleteRecursively()
    }

    @Test
    fun `test addMonster`() {
        File(System.getProperty("user.dir"), "temp-dir").deleteRecursively()
        withEnvironment(mapOf("DATADIR" to "temp-dir")) {
            val dataHandler = DataHandler()
            val output =
                captureStandardOut {
                    Game().addMonster(
                        "Pika",
                        100,
                        20,
                        Type.NORMAL,
                        listOf(Attack.ROOT_SHOT),
                        "Bob"
                    )
                }
                    .trim()
            assertThat(output).isEqualTo("Trainer with the name Bob does not exist")

            dataHandler.saveTrainer(Trainer("Bob"))
            val output2 =
                captureStandardOut {
                    Game().addMonster(
                        "Pika",
                        100,
                        20,
                        Type.NORMAL,
                        listOf(Attack.ROOT_SHOT),
                        "Bob"
                    )
                }
                    .trim()
            assertThat(output2)
                .isEqualTo(
                    """Created new Monster:
Name:               Pika
HP:                 100
Speed:              20
Type:               NORMAL
Attacks:            [ROOT_SHOT]
Trainer:            Bob"""
                )
            val trainer = dataHandler.loadTrainer("Bob")
            assertThat(trainer.monsters).isNotEmpty
            assertThatNoException().isThrownBy { DataHandler().loadMonster("Pika") }
        }
        File(System.getProperty("user.dir"), "temp-dir").deleteRecursively()
    }


    @Test
    fun `test initiateBattle`() {
        File(System.getProperty("user.dir"), "temp-dir").deleteRecursively()
        withEnvironment(mapOf("DATADIR" to "temp-dir")) {
            val dataHandler = DataHandler()
            val m1 =
                Monster(
                    "M1",
                    BattleStats(100, speed = 20),
                    Type.NORMAL,
                    attacks = listOf(Attack.PUNCH)
                )
            val m2 =
                Monster(
                    "M2",
                    BattleStats(100, speed = 20),
                    Type.NORMAL,
                    attacks = listOf(Attack.PUNCH)
                )
            dataHandler.saveTrainer(Trainer("Bob", listOf(m1)))
            dataHandler.saveTrainer(Trainer("Kevin", listOf(m2)))

            val output =
                captureStandardOut { Game().initiateBattle("Bob", "Kevin") }.trim()
            val output2 =
                captureStandardOut { Game().initiateBattle("Bob", "Ash") }.trim()
//            assertThat(output).isEqualTo("Battle with ID 1 was created")
            assertThat(output2).isEqualTo("Trainer with the name Ash does not exist")
            assertThatNoException().isThrownBy { DataHandler().loadTrainer("Bob") }
        }
        File(System.getProperty("user.dir"), "temp-dir").deleteRecursively()
    }

    @Test
    fun `View battle status`() {
        File(System.getProperty("user.dir"), "temp-dir").deleteRecursively()
        withEnvironment("DATADIR" to "temp-dir") {
            val output = captureStandardOut { Game().viewStatus("2") }.trim()
            assertThat(output).isEqualTo("The battle with ID 2 does not exist")
            val m1 =
                Monster(
                    "M1",
                    BattleStats(100, speed = 20),
                    Type.NORMAL,
                    attacks = listOf(Attack.PUNCH)
                )
            val m2 =
                Monster(
                    "M2",
                    BattleStats(100, speed = 20),
                    Type.NORMAL,
                    attacks = listOf(Attack.PUNCH)
                )
            val t1 = Trainer("T1", listOf(m1))
            val t2 = Trainer("T2", listOf(m2))
            val battle = Battle(t1, t2, "12345")
            DataHandler().saveBattle(battle)

            val output2 = captureStandardOut { Game().viewStatus("12345") }.trim()
            assertThat(output2)
                .isEqualTo(
                    """Battle (12345):
T1 vs. T2
Round: 1
Next Attacker: T2"""
                )
        }
        File(System.getProperty("user.dir"), "temp-dir").deleteRecursively()
    }

    @Test
    fun `perform Attack test`() {
        File(System.getProperty("user.dir"), "temp-dir").deleteRecursively()
        withEnvironment("DATADIR" to "temp-dir") {
            val m1 =
                Monster(
                    "M1",
                    BattleStats(100, speed = 20),
                    Type.NORMAL,
                    attacks = listOf(Attack.PUNCH)
                )
            val m2 =
                Monster(
                    "M2",
                    BattleStats(100, speed = 20),
                    Type.NORMAL,
                    attacks = listOf(Attack.PUNCH)
                )
            val t1 = Trainer("T1", listOf(m1))
            val t2 = Trainer("T2", listOf(m2))
            val battle = Battle(t1, t2)

            val dataHandler = DataHandler()
            dataHandler.saveTrainer(t1)
            dataHandler.saveTrainer(t2)
            dataHandler.saveMonster(m1)
            dataHandler.saveMonster(m2)
            dataHandler.saveBattle(battle)

            Game().performAttack("2", Attack.PUNCH)

//            assertThat(dataHandler.loadMonster("M2").stats.hp).isLessThan(100)
        }
        File(System.getProperty("user.dir"), "temp-dir").deleteRecursively()
    }
}
