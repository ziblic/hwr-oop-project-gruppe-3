

/*
creating a battle, its not finished
current round is 0

taking a turn, the faster monster attacks
taking another turn, the other trainer is now attacking

using an attack the first monster doesnt have?

all monsters of one trainer are KO, determineWinner shows winner? isFinished()?

take turn when battle is finished
*/


package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows

class battleTest : AnnotationSpec() {

    @Test
    fun `creating a battle, which is not finished and the current round is 0`() {
        val m1 = Monster(
            "M1",
            BattleStats(hp = 100, speed = 20),
            Type.Normal,
            attacks = listOf(Attack.PUNCH)
        )
        val m2 = Monster(
            "M2",
            BattleStats(hp = 100, speed = 10),
            Type.Normal,
            attacks = listOf(Attack.PUNCH)
        )
        val t1 = Trainer("T1", listOf(m1))
        val t2 = Trainer("T2", listOf(m2))
        val battle = Battle(t1, t2)


        assertThat(battle.isFinished()).isFalse()

        val roundField = Battle::class.java.getDeclaredField("currentRound").apply { isAccessible = true }
        assertThat(roundField.getInt(battle)).isEqualTo(0)
    }

    @Test
    fun `taking a turn, the faster monster attacks taking another turn, the other trainer is now attacking`() {
        val m1 = Monster(
            "M1",
            BattleStats(100, speed = 20),
            Type.Normal,
            attacks = listOf(Attack.PUNCH)
        )
        val m2 = Monster(
            "M2",
            BattleStats(100, speed = 10),
            Type.Normal,
            attacks = listOf(Attack.PUNCH)
        )
        val t1 = Trainer("T1", listOf(m1))
        val t2 = Trainer("T2", listOf(m2))
        val battle = Battle(t1, t2)

        battle.takeTurn(Attack.PUNCH)
        assertThat(m2.stats.hp).isLessThan(100)

        val prevHp1 = m1.stats.hp
        battle.takeTurn(Attack.PUNCH)
        assertThat(m1.stats.hp).isLessThan(prevHp1)
    }

    @Test
    fun `using an attack the first monster doesnt have, IllegalArgumentException`() {
        val m1 = Monster(
            "M1",
            BattleStats(50, speed = 10),
            Type.Normal,
            attacks = listOf(Attack.PUNCH)
        )
        val m2 = Monster(
            "M2",
            BattleStats(50, speed = 10),
            Type.Normal,
            attacks = listOf(Attack.PUNCH)
        )
        val battle = Battle(Trainer("T1", listOf(m1)), Trainer("T2", listOf(m2)))

        val ex = assertThrows<IllegalArgumentException> {
            battle.takeTurn(Attack.DRUM)
        }
        assertThat(ex).hasMessageContaining("not part of the attacks")
    }

    @Test
    fun `all monsters of one trainer are KO, determineWinner shows winner? isFinished()?`() {

        val m1 = Monster(
            "M1",
            BattleStats(100, speed = 20),
            Type.Normal,
            attacks = listOf(Attack.GROUND_HAMMER)
        )
        val m2 = Monster(
            "M2",
            BattleStats(30, speed = 10),
            Type.Normal,
            attacks = listOf(Attack.GROUND_HAMMER)
        )
        val t1 = Trainer("T1", listOf(m1))
        val t2 = Trainer("T2", listOf(m2))
        val battle = Battle(t1, t2)


        battle.takeTurn(Attack.GROUND_HAMMER)
        assertThat(m2.isKO()).isTrue()


        assertThat(battle.isFinished()).isFalse()
        assertThat(battle.determineWinner()).isEqualTo(t1)
    }

    @Test
    fun `take turn when battle is finished`() {

        val m1 = Monster(
            "M1",
            BattleStats(100, speed = 20),
            Type.Normal,
            attacks = listOf(Attack.GROUND_HAMMER)
        )
        val m2 = Monster(
            "M2",
            BattleStats(30, speed = 10),
            Type.Normal,
            attacks = listOf(Attack.GROUND_HAMMER)
        )
        val battle = Battle(Trainer("T1", listOf(m1)), Trainer("T2", listOf(m2)))


        battle.takeTurn(Attack.GROUND_HAMMER)


        val ex = assertThrows<IllegalStateException> {
            battle.takeTurn(Attack.GROUND_HAMMER)
        }
        assertThat(ex).hasMessage("Battle is already finished")
        assertThat(battle.isFinished()).isTrue()
    }
}




