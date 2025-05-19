package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.captureStandardOut
import io.kotest.extensions.system.withEnvironment
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import java.io.File

class BattleTest : AnnotationSpec() {
    @Test
    fun `battle has BattleID`() {
        val m1 =
            Monster(
                "M1",
                BattleStats(hp = 100, speed = 20),
                Type.NORMAL,
                attacks = listOf(Attack.PUNCH)
            )
        val t1 = Trainer("T1", listOf(m1))
        val t2 = Trainer("T2", listOf(m1))
        val battle = Battle(t1, t2, 100)
        assertThat(battle.battleId).isEqualTo(100)
    }

    @Test
    fun `creating a battle, which is not finished and the current round is 0`() {
        val m1 =
            Monster(
                "M1",
                BattleStats(hp = 100, speed = 20),
                Type.NORMAL,
                attacks = listOf(Attack.PUNCH)
            )
        val m2 =
            Monster(
                "M2",
                BattleStats(hp = 100, speed = 10),
                Type.NORMAL,
                attacks = listOf(Attack.PUNCH)
            )
        val t1 = Trainer("T1", listOf(m1))
        val t2 = Trainer("T2", listOf(m2))
        val battle = Battle(t1, t2, 0)

        assertThat(battle.finished).isFalse()
        assertThat(battle.currentRound).isEqualTo(0)
    }

    @Test
    fun `taking a turn, the trainer with the faster monster attacks, taking another turn, the other trainer is now attacking`() {
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
                BattleStats(100, speed = 10),
                Type.NORMAL,
                attacks = listOf(Attack.PUNCH)
            )
        val t1 = Trainer("T1", listOf(m1))
        val t2 = Trainer("T2", listOf(m2))
        val battle = Battle(t1, t2, 1)

        battle.takeTurn(Attack.PUNCH)
        assertThat(m2.stats.hp).isLessThan(100)

        val prevHp1 = m1.stats.hp
        battle.takeTurn(Attack.PUNCH)
        assertThat(m1.stats.hp).isLessThan(prevHp1)

        assertThat(battle.currentRound).isEqualTo(2)
    }

    @Test
    fun `trainer 2 has the faster monster, he will do the first move`() {
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
                BattleStats(100, speed = 30),
                Type.NORMAL,
                attacks = listOf(Attack.PUNCH)
            )
        val t1 = Trainer("T1", listOf(m1))
        val t2 = Trainer("T2", listOf(m2))
        val battle = Battle(t1, t2, 2)

        battle.takeTurn(Attack.PUNCH)
        assertThat(m1.stats.hp).isLessThan(100)
    }

    @Test
    fun `trainers with equal speed monsters, will start with trainerOne`() {
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
        val battle = Battle(t1, t2, 2)

        battle.takeTurn(Attack.PUNCH)
        assertThat(m2.stats.hp).isLessThan(100)
    }

    @Test
    fun `creating a battle with any trainer having no monsters causes an IllegalStateException`() {
        val m1 =
            Monster(
                "M1",
                BattleStats(100, speed = 20),
                Type.NORMAL,
                attacks = listOf(Attack.PUNCH)
            )
        assertThrows<IllegalStateException> {
            Battle(Trainer("T1", listOf(m1)), Trainer("T2", emptyList()), 9)
        }
        assertThrows<IllegalStateException> {
            Battle(Trainer("T1", emptyList()), Trainer("T2", listOf(m1)), 17)
        }
    }

    @Test
    fun `using an attack the first monster doesnt have throws IllegalArgumentException`() {
        val m1 =
            Monster(
                "M1",
                BattleStats(50, speed = 10),
                Type.NORMAL,
                attacks = listOf(Attack.PUNCH)
            )
        val m2 =
            Monster(
                "M2",
                BattleStats(50, speed = 10),
                Type.NORMAL,
                attacks = listOf(Attack.PUNCH)
            )
        val t1 = Trainer("T1", listOf(m1))
        val t2 = Trainer("T2", listOf(m2))
        val battle = Battle(t1, t2, 3)

        assertThrows<IllegalArgumentException> { battle.takeTurn(Attack.DRUM) }
    }

    @Test
    fun `all monsters of one trainer are KO, determineWinner shows the other and battle finishes`() {
        val m1 =
            Monster(
                "M1",
                BattleStats(100, speed = 20),
                Type.NORMAL,
                attacks = listOf(Attack.GROUND_HAMMER)
            )
        val m2 =
            Monster(
                "M2",
                BattleStats(30, speed = 10),
                Type.NORMAL,
                attacks = listOf(Attack.GROUND_HAMMER)
            )
        val t1 = Trainer("T1", listOf(m1))
        val t2 = Trainer("T2", listOf(m2))
        val battle = Battle(t1, t2, 4)
        battle.takeTurn(Attack.GROUND_HAMMER)

        assertThat(m2.isKO()).isTrue()
        assertThat(battle.finished).isFalse()
        assertThat(battle.determineWinner()).isEqualTo(t1)
    }

    @Test
    fun `trainer 2 wins, determineWinnner shows trainer 2`() {
        val m1 =
            Monster(
                "M1",
                BattleStats(40, speed = 20),
                Type.NORMAL,
                attacks = listOf(Attack.GROUND_HAMMER)
            )
        val m2 =
            Monster(
                "M2",
                BattleStats(30, speed = 25),
                Type.NORMAL,
                attacks = listOf(Attack.GROUND_HAMMER)
            )
        val t1 = Trainer("T1", listOf(m1))
        val t2 = Trainer("T2", listOf(m2))
        val battle = Battle(t1, t2, 4)
        battle.takeTurn(Attack.GROUND_HAMMER)
        assertThat(battle.determineWinner()).isEqualTo(t2)
    }

    @Test
    fun `taking a turn when battle is finished throws IllegalStateException`() {
        val m1 =
            Monster(
                "M1",
                BattleStats(100, speed = 20),
                Type.NORMAL,
                attacks = listOf(Attack.GROUND_HAMMER)
            )
        val m2 =
            Monster(
                "M2",
                BattleStats(30, speed = 10),
                Type.NORMAL,
                attacks = listOf(Attack.GROUND_HAMMER)
            )
        val t1 = Trainer("T1", listOf(m1))
        val t2 = Trainer("T2", listOf(m2))
        val battle = Battle(t1, t2, 5)
        battle.takeTurn(Attack.GROUND_HAMMER)

        assertThrows<IllegalStateException> { battle.takeTurn(Attack.GROUND_HAMMER) }
        assertThat(battle.finished).isTrue()
    }

    @Test
    fun `Show all battles`() {
        File(System.getProperty("user.dir"), "temp-dir").deleteRecursively()
        withEnvironment("DATADIR" to "temp-dir") {
            val output = captureStandardOut { Battle.showAll() }.trim()
            assertThat(output).isEqualTo("No battles found")

            DataHandler()
            val battlesFolder = File(System.getProperty("user.dir"), "temp-dir/battles")
            File(battlesFolder, "1.json").createNewFile()
            File(battlesFolder, "a.json").createNewFile()

            val output2 = captureStandardOut { Battle.showAll() }.trim()
            assertThat(output2).isEqualTo("List of all Battles\nBattleID: 1")
        }
        File(System.getProperty("user.dir"), "temp-dir").deleteRecursively()
    }

    @Test
    fun `Show a specific battle`() {
        val m1 =
            Monster(
                "M1",
                BattleStats(hp = 100, speed = 20),
                Type.NORMAL,
                attacks = listOf(Attack.PUNCH)
            )
        val t1 = Trainer("T1", listOf(m1))
        val t2 = Trainer("T2", listOf(m1))
        val battle = Battle(t1, t2, 1)

        val output = captureStandardOut { battle.viewStatus() }.trim()

        assertThat(output).isEqualTo("Battle (1):\nT1 vs T2\nNext trainer to turn is T1")
    }
}
