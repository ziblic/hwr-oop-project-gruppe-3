package hwr.oop.tnp

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BattleTest {

    private lateinit var trainerOne: Trainer
    private lateinit var trainerTwo: Trainer
    private lateinit var monsterOne: Monster
    private lateinit var monsterTwo: Monster
    private lateinit var battle: Battle

    @BeforeEach
    fun setup() {
        monsterOne = Monster("A", BattleStats(100, 10, 20, 0, 0, 0), Type.Fire, emptyList())
        monsterTwo = Monster("B", BattleStats(50, 5, 10, 0, 0, 0), Type.Water, emptyList())
        trainerOne = Trainer("T1", mutableListOf(monsterOne))
        trainerTwo = Trainer("T2", mutableListOf(monsterTwo))
        battle = Battle(trainerOne, trainerTwo)
    }

    @Test
    fun testStartBattle() {
        battle.startBattle()
        assertEquals(1, battle.getCurrentTurn())
        assertFalse(battle.isFinished())
    }

    @Test
    fun testExecuteTurnReducesHpCorrectly() {
        battle.startBattle()
        battle.executeTurn()
        // 先手 A(20 攻击)打 B：50-20=30
        assertEquals(30, monsterTwo.getHp())
        // 后手 B(10 攻击)打 A：100-10=90
        assertEquals(90, monsterOne.getHp())
        // 回合数依然是 1
        assertEquals(1, battle.getCurrentTurn())
    }

    @Test
    fun testExecuteTurnIncrementsTurn() {
        battle.startBattle()
        battle.executeTurn()
        battle.executeTurn()
        // 双方都没 KO，所以回合数自增一次：1 -> 2
        assertEquals(2, battle.getCurrentTurn())
    }

    @Test
    fun testBattleEndsWhenMonsterKo() {
        // 构造一个一击必杀的强怪
        val strong = Monster("Strong", BattleStats(100, 10, 100, 0, 0, 0), Type.Normal, emptyList())
        val weak   = Monster("Weak",   BattleStats(50, 5, 10, 0, 0, 0), Type.Normal, emptyList())
        trainerOne = Trainer("T1", mutableListOf(strong))
        trainerTwo = Trainer("T2", mutableListOf(weak))
        battle = Battle(trainerOne, trainerTwo)

        battle.startBattle()
        battle.executeTurn()
        assertTrue(battle.isFinished())
        assertEquals(0, weak.getHp())
        assertEquals(100, strong.getHp())
    }

    @Test
    fun testDetermineWinner() {
        val strong = Monster("Strong", BattleStats(100, 10, 100, 0, 0, 0), Type.Normal, emptyList())
        val weak   = Monster("Weak",   BattleStats(50, 5, 10, 0, 0, 0), Type.Normal, emptyList())
        trainerOne = Trainer("T1", mutableListOf(strong))
        trainerTwo = Trainer("T2", mutableListOf(weak))
        battle = Battle(trainerOne, trainerTwo)

        battle.startBattle()
        battle.executeTurn()
        val winner = battle.determineWinner()
        assertEquals(trainerOne, winner)
    }

    @Test
    fun testDetermineWinnerReturnsNullIfNotFinished() {
        battle.startBattle()
        assertNull(battle.determineWinner())
    }
}
