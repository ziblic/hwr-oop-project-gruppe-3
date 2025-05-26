package hwr.oop.tnp.core

import io.kotest.core.spec.style.AnnotationSpec
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatNoException

class BattleTest : AnnotationSpec() {
        private val m1 =
                Monster("M1", BattleStats(100, 20), Type.NORMAL, attacks = listOf(Attack.PUNCH))
        private val m2 =
                Monster("M2", BattleStats(100, 10), Type.NORMAL, attacks = listOf(Attack.PUNCH))
        private val t1 = Trainer("T1", listOf(m1))
        private val t2 = Trainer("T2", listOf(m2))

        lateinit var battle: Battle

        @BeforeEach
        fun init() {
                battle = Battle()
        }

        fun createFinishedBattle(firstTrainerWins: Boolean = true): Battle {
                val m1 =
                        Monster(
                                "M1",
                                BattleStats(100, 20),
                                Type.NORMAL,
                                attacks = listOf(Attack.PUNCH)
                        )
                val m2 =
                        Monster(
                                "M2",
                                BattleStats(10, 10),
                                Type.NORMAL,
                                attacks = listOf(Attack.PUNCH)
                        )
                val winningTrainer = Trainer("T1", listOf(m1))
                val otherTrainer = Trainer("T2", listOf(m2))
                val battle = Battle()
                if (firstTrainerWins) {
                        battle.addTrainerToBattle(winningTrainer)
                        battle.addTrainerToBattle(otherTrainer)
                } else {
                        battle.addTrainerToBattle(otherTrainer)
                        battle.addTrainerToBattle(winningTrainer)
                }
                battle.takeTurn(Attack.PUNCH)
                return battle
        }

        // @Test
        // fun `battle shows info`() {
        //         assertThat(battle.toString())
        //                 .contains(t1.name, t2.name, battle.battleId,
        // battle.currentRound.toString())
        // }

        @Test
        fun `battle has ID`() {
                assertThat(battle.battleId).isNotEmpty
                assertThatNoException().isThrownBy { UUID.fromString(battle.battleId) }
        }

        @Test
        fun `battle is not finished and round is 1 after creation`() {
                assertThat(battle.status).isNotEqualTo(BattleStatus.FINISHED)
                assertThat(battle.currentRound).isEqualTo(1)
        }

        // @Test
        // fun `the trainer with the faster monster will attack first, next attack is the other
        // trainer`() {
        //         val prevHpM2 = m2.stats.hp
        //         battle.takeTurn(Attack.PUNCH)
        //         assertThat(m2.stats.hp).isLessThan(prevHpM2)
        //
        //         assertThat(battle.currentTrainer).isEqualTo(t2)
        //
        //         val prevHpM1 = m1.stats.hp
        //         battle.takeTurn(Attack.PUNCH)
        //         assertThat(m1.stats.hp).isLessThan(prevHpM1)
        //
        //         assertThat(battle.currentRound).isEqualTo(3)
        //
        //         assertThat(battle.currentTrainer).isEqualTo(t1)
        // }

        // @Test
        // fun `trainer two has the faster monster, so he will begin`() {
        //         val battle = Battle() // switch the trainer order
        //         assertThat(battle.currentTrainer).isEqualTo(t1)
        // }

        // @Test
        // fun `using an attack the attacking monster doesnt have throws IllegalArgumentException`()
        // {
        //         assertThrows<IllegalArgumentException> { battle.takeTurn(Attack.DRUM) }
        // }

        // @Test
        // fun `all monsters of one trainer are KO, determineWinner shows the other and battle
        // finishes`() {
        //         val battle = createFinishedBattle()
        //
        //         assertThat(battle.status).isEqualTo(BattleStatus.FINISHED)
        //         assertThat(battle.determineWinner()).isEqualTo(battle.trainerOne)
        // }

        // @Test
        // fun `trainer two wins, determineWinnner shows trainer two`() {
        //         val battle = createFinishedBattle(firstTrainerWins = false)
        //
        //         assertThat(battle.status).isEqualTo(BattleStatus.FINISHED)
        //         assertThat(battle.determineWinner()).isEqualTo(battle.trainerTwo)
        // }

        // @Test
        // fun `taking a turn when battle is finished throws IllegalStateException`() {
        //         val battle = createFinishedBattle()
        //
        //         assertThat(battle.status).isEqualTo(BattleStatus.FINISHED)
        //         assertThrows<IllegalStateException> { battle.takeTurn(Attack.PUNCH) }
        // }
}
