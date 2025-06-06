package hwr.oop.tnp.core

// FIX: Comment the test back in to refactor them

import io.kotest.core.spec.style.AnnotationSpec

// import kotlinx.serialization.json.Json
// import org.assertj.core.api.Assertions.assertThat
// import org.assertj.core.api.Assertions.assertThatThrownBy
// import org.junit.jupiter.api.assertThrows
//
class BattleTest : AnnotationSpec() {
  //   private val firstMonsterOfRed =
  //     Monster(
  //       "firstMonsterOfRed",
  //       BattleStats(100, 20),
  //       PrimitiveType.NORMAL,
  //       attacks = listOf(Attack.PUNCH)
  //     )
  //   private val firstMonsterOfBlue =
  //     Monster(
  //       "firstMonsterOfBlue",
  //       BattleStats(100, 10),
  //       PrimitiveType.NORMAL,
  //       attacks = listOf(Attack.PUNCH)
  //     )
  //   private val red = Trainer("red", mutableListOf(firstMonsterOfRed))
  //   private val blue = Trainer("blue", mutableListOf(firstMonsterOfBlue))
  //
  //   fun createFinishedBattle(firstTrainerWins: Boolean = true): Battle {
  //     val m1 =
  //       Monster(
  //         "M1",
  //         BattleStats(100, 20),
  //         PrimitiveType.NORMAL,
  //         attacks = listOf(Attack.PUNCH)
  //       )
  //     val m2 =
  //       Monster(
  //         "M2",
  //         BattleStats(10, 10),
  //         PrimitiveType.NORMAL,
  //         attacks = listOf(Attack.PUNCH)
  //       )
  //     val winningTrainer = Trainer("T1", mutableListOf(m1))
  //     val otherTrainer = Trainer("T2", mutableListOf(m2))
  //     val battle = Battle("1")
  //     if (firstTrainerWins) {
  //       battle.addTrainerToBattle(winningTrainer)
  //       battle.addTrainerToBattle(otherTrainer)
  //     } else {
  //       battle.addTrainerToBattle(otherTrainer)
  //       battle.addTrainerToBattle(winningTrainer)
  //     }
  //     battle.startBattle()
  //     battle.takeTurn(Attack.PUNCH)
  //     return battle
  //   }
  //
  //   @Test
  //   fun `test serializable`() {
  //     val battle = Battle("1")
  //     val jsonBattle = Json.encodeToString(battle)
  //     assertThat(jsonBattle).isEqualTo("{\"battleId\":\"1\"}")
  //     val decodedBattle = Json.decodeFromString<Battle>(jsonBattle)
  //     assertThat(battle.battleId).isEqualTo(decodedBattle.battleId)
  //     assertThat(battle.trainerOne).isEqualTo(decodedBattle.trainerOne)
  //     assertThat(battle.trainerTwo).isEqualTo(decodedBattle.trainerTwo)
  //     assertThat(battle.currentTrainer).isEqualTo(decodedBattle.currentTrainer)
  //     assertThat(battle.status).isEqualTo(decodedBattle.status)
  //     assertThat(battle.currentRound).isEqualTo(decodedBattle.currentRound)
  //   }
  //
  //   @Test
  //   fun `battle shows info`() {
  //     val battle: Battle = Battle("1")
  //     assertThatThrownBy { battle.startBattle() }.hasMessage("Trainer needs to have been set for
  // this operation")
  //     assertThrows<Exception> { battle.determineWinner() }
  //     assertThat(battle.toString()).isEqualTo("Battle with ID: 1 is in a pregame state")
  //   }
  //
  //   @Test
  //   fun `battle is not finished and round is 1 after creation`() {
  //     val battle: Battle = Battle("1")
  //     assertThat(battle.status).isNotEqualTo(BattleStatus.FINISHED)
  //     assertThat(battle.currentRound).isEqualTo(1)
  //   }
  //
  //   @Test
  //   fun `add 3 trainer throws exception`() {
  //     val battle: Battle = Battle("1")
  //     battle.addTrainerToBattle(red)
  //     battle.addTrainerToBattle(blue)
  //     assertThrows<IllegalStateException> { battle.addTrainerToBattle(red) }
  //   }
  //
  //   @Test
  //   fun `the trainer with the faster battle will attack first, next attack is the other
  // trainer`()
  // {
  //     val battle: Battle = Battle("3")
  //     val prevHpM2 = firstMonsterOfBlue.stats.hp
  //     battle.addTrainerToBattle(red)
  //     battle.addTrainerToBattle(blue)
  //     battle.startBattle()
  //     battle.takeTurn(Attack.PUNCH)
  //     assertThat(firstMonsterOfBlue.stats.hp).isLessThan(prevHpM2)
  //
  //     assertThat(battle.currentTrainer).isEqualTo(blue)
  //
  //     val prevHpM1 = firstMonsterOfRed.stats.hp
  //     battle.takeTurn(Attack.PUNCH)
  //     assertThat(firstMonsterOfRed.stats.hp).isLessThan(prevHpM1)
  //
  //     assertThat(battle.currentRound).isEqualTo(3)
  //
  //     assertThat(battle.currentTrainer).isEqualTo(red)
  //   }
  //
  //   @Test
  //   fun `currentTrainer is null`() {
  //     val battle: Battle = Battle("3")
  //     battle.addTrainerToBattle(red)
  //     battle.addTrainerToBattle(blue)
  //     assertThrows<IllegalStateException> { battle.takeTurn(Attack.PUNCH) }
  //   }
  //
  //   @Test
  //   fun `get trainer two by name and exception if not contained`() {
  //     val battle = Battle("1")
  //     battle.addTrainerToBattle(red)
  //     battle.addTrainerToBattle(blue)
  //     battle.getTrainerByName("T2")
  //     assertThrows<IllegalArgumentException> { battle.getTrainerByName("T3") }
  //   }
  //
  //   @Test
  //   fun `toString returns right representation of Battle`() {
  //     val battle = Battle("1")
  //     assertThat(battle.toString()).isEqualTo("Battle with ID: 1 is in a pregame state")
  //     battle.addTrainerToBattle(red)
  //     battle.addTrainerToBattle(blue)
  //     battle.startBattle()
  //     assertThat(battle.toString())
  //       .isEqualTo("Battle (1):\nT1 vs. T2\nRound: 1\nNext Attacker: T1")
  //   }
  //
  //   @Test
  //   fun `finished battle has status FINISHED`() {
  //     val finishedBattle = createFinishedBattle()
  //     assertThat(finishedBattle.status).isEqualTo(BattleStatus.FINISHED)
  //     assertThrows<IllegalStateException> { finishedBattle.takeTurn(Attack.PUNCH) }
  //   }
}
