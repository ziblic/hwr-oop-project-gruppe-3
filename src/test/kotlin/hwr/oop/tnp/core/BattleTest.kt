package hwr.oop.tnp.core

import io.kotest.core.spec.style.AnnotationSpec
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import java.io.File

class BattleTest : AnnotationSpec() {
  private val firstMonsterOfRed =
    Monster(
      "firstMonsterOfRed",
      BattleStats(100, 20, 20, 20, 20, 20),
      PrimitiveType.NORMAL,
      attacks = listOf(Attack.PUNCH),
    )
  private val firstMonsterOfBlue =
    Monster(
      "firstMonsterOfBlue",
      BattleStats(100, 10, 20, 20, 20, 20),
      PrimitiveType.NORMAL,
      attacks = listOf(Attack.PUNCH),
    )
  private val red = Trainer("red", mutableListOf(firstMonsterOfRed))
  private val blue = Trainer("blue", mutableListOf(firstMonsterOfBlue))

  fun createFinishedBattle(firstTrainerWins: Boolean = true): Battle {
    val m1 =
      Monster(
        "M1",
        BattleStats(100, 20, 20, 20, 20, 20),
        PrimitiveType.NORMAL,
        attacks = listOf(Attack.PUNCH),
      )
    val m2 =
      Monster(
        "M2",
        BattleStats(10, 10, 20, 20, 20, 20),
        PrimitiveType.NORMAL,
        attacks = listOf(Attack.PUNCH),
      )
    val winningTrainer = Trainer("T1", mutableListOf(m1))
    val otherTrainer = Trainer("T2", mutableListOf(m2))
    val battle = Battle("1", DamageStrategy.DETERMINISTIC)
    if (firstTrainerWins) {
      battle.addTrainerToBattle(winningTrainer)
      battle.addTrainerToBattle(otherTrainer)
    } else {
      battle.addTrainerToBattle(otherTrainer)
      battle.addTrainerToBattle(winningTrainer)
    }
    battle.startBattle()
    battle.takeTurn(Attack.PUNCH)
    return battle
  }

  @AfterEach
  fun cleanUp() {
    File(System.getProperty("user.dir"), "data/1.json").delete()
  }

  @Test
  fun `test serializable`() {
    val battle = Battle("1", DamageStrategy.DETERMINISTIC)
    val jsonBattle = Json.encodeToString(battle)
    assertThat(jsonBattle).isEqualTo("{\"battleId\":\"1\",\"damageStrategy\":\"DETERMINISTIC\"}")
    val decodedBattle = Json.decodeFromString<Battle>(jsonBattle)
    assertThat(battle.battleId).isEqualTo(decodedBattle.battleId)
    assertThat(battle.trainerOne).isEqualTo(decodedBattle.trainerOne)
    assertThat(battle.trainerTwo).isEqualTo(decodedBattle.trainerTwo)
    assertThat(battle.currentTrainer).isEqualTo(decodedBattle.currentTrainer)
    assertThat(battle.status).isEqualTo(decodedBattle.status)
    assertThat(battle.currentRound).isEqualTo(decodedBattle.currentRound)
  }

  @Test
  fun `battle shows info`() {
    val battle: Battle = Battle("1", DamageStrategy.DETERMINISTIC)
    assertThatThrownBy { battle.startBattle() }
      .hasMessage("Trainer need to have been set for this operation")
    assertThatThrownBy { battle.determineWinner() }
      .hasMessage("Trainer need to have been set for this operation")
    assertThat(battle.toString()).isEqualTo("Battle with ID: 1 is in a pregame state")
  }

  @Test
  fun `Battle gets saved and progresses in round when playing`() {
    val m1 =
      Monster(
        "M1",
        BattleStats(100, 20, 20, 20, 20, 20),
        PrimitiveType.NORMAL,
        attacks = listOf(Attack.PUNCH),
      )
    val m2 =
      Monster(
        "M2",
        BattleStats(100, 10, 20, 20, 20, 20),
        PrimitiveType.NORMAL,
        attacks = listOf(Attack.PUNCH),
      )
    val winningTrainer = Trainer("T1", mutableListOf(m1))
    val otherTrainer = Trainer("T2", mutableListOf(m2))
    val battle = Battle("1", DamageStrategy.DETERMINISTIC)
    battle.addTrainerToBattle(otherTrainer)
    battle.addTrainerToBattle(winningTrainer)
    battle.startBattle()
    assertThat(File(System.getProperty("user.dir"), "data/1.json").readLines())
      .isEqualTo(
        listOf(
          "{\"battleId\":\"1\",\"damageStrategy\":\"DETERMINISTIC\",\"trainerOne\":{\"name\":\"T2\",\"monsters\":[{\"name\":\"M2\",\"stats\":{\"maxHp\":100,\"hp\":100,\"speed\":10,\"attack\":20,\"specialAttack\":20,\"defense\":20,\"specialDefense\":20},\"primitiveType\":\"NORMAL\",\"attacks\":[\"PUNCH\"]}]},\"trainerTwo\":{\"name\":\"T1\",\"monsters\":[{\"name\":\"M1\",\"stats\":{\"maxHp\":100,\"hp\":100,\"speed\":20,\"attack\":20,\"specialAttack\":20,\"defense\":20,\"specialDefense\":20},\"primitiveType\":\"NORMAL\",\"attacks\":[\"PUNCH\"]}]},\"currentTrainer\":{\"name\":\"T1\",\"monsters\":[{\"name\":\"M1\",\"stats\":{\"maxHp\":100,\"hp\":100,\"speed\":20,\"attack\":20,\"specialAttack\":20,\"defense\":20,\"specialDefense\":20},\"primitiveType\":\"NORMAL\",\"attacks\":[\"PUNCH\"]}]},\"status\":\"STARTED\"}",
        ),
      )
    battle.takeTurn(Attack.PUNCH)
    assertThat(File(System.getProperty("user.dir"), "data/1.json").readLines())
      .isEqualTo(
        listOf(
          "{\"battleId\":\"1\",\"damageStrategy\":\"DETERMINISTIC\",\"trainerOne\":{\"name\":\"T2\",\"monsters\":[{\"name\":\"M2\",\"stats\":{\"maxHp\":100,\"hp\":86,\"speed\":10,\"attack\":20,\"specialAttack\":20,\"defense\":20,\"specialDefense\":20},\"primitiveType\":\"NORMAL\",\"attacks\":[\"PUNCH\"]}]},\"trainerTwo\":{\"name\":\"T1\",\"monsters\":[{\"name\":\"M1\",\"stats\":{\"maxHp\":100,\"hp\":100,\"speed\":20,\"attack\":20,\"specialAttack\":20,\"defense\":20,\"specialDefense\":20},\"primitiveType\":\"NORMAL\",\"attacks\":[\"PUNCH\"]}]},\"currentTrainer\":{\"name\":\"T2\",\"monsters\":[{\"name\":\"M2\",\"stats\":{\"maxHp\":100,\"hp\":86,\"speed\":10,\"attack\":20,\"specialAttack\":20,\"defense\":20,\"specialDefense\":20},\"primitiveType\":\"NORMAL\",\"attacks\":[\"PUNCH\"]}]},\"status\":\"STARTED\",\"currentRound\":2}",
        ),
      )
  }

  @Test
  fun `battle is not finished and round is 1 after creation`() {
    val battle: Battle = Battle("1", DamageStrategy.DETERMINISTIC)
    assertThat(battle.status).isNotEqualTo(BattleStatus.FINISHED)
    assertThat(battle.currentRound).isEqualTo(1)
  }

  @Test
  fun `add 3 trainer throws exception`() {
    val battle: Battle = Battle("1", DamageStrategy.DETERMINISTIC)
    battle.addTrainerToBattle(red)
    battle.addTrainerToBattle(blue)
    assertThatThrownBy { battle.addTrainerToBattle(red) }
      .hasMessage("Both trainers are already set.")
  }

  @Test
  fun `currentTrainer is null`() {
    val battle: Battle = Battle("1", DamageStrategy.DETERMINISTIC)
    battle.addTrainerToBattle(red)
    battle.addTrainerToBattle(blue)
    assertThatThrownBy { battle.takeTurn(Attack.PUNCH) }
      .hasMessage("Trainer needs to have been set for this operation")
  }

  @Test
  fun `toString returns right representation of Battle`() {
    val battle = Battle("1", DamageStrategy.DETERMINISTIC)
    assertThat(battle.toString()).isEqualTo("Battle with ID: 1 is in a pregame state")
    battle.addTrainerToBattle(red)
    battle.addTrainerToBattle(blue)
    battle.startBattle()
    assertThat(battle.toString())
      .isEqualTo("Battle (1):\nred vs. blue\nRound: 1\nNext Attacker: red")
  }

  @Test
  fun `finished battle has status FINISHED`() {
    val finishedBattle = createFinishedBattle()
    assertThat(finishedBattle.status).isEqualTo(BattleStatus.FINISHED)
    assertThatThrownBy { finishedBattle.takeTurn(Attack.PUNCH) }
      .hasMessage("Battle is already finished")
    assertThat(File(System.getProperty("user.dir"), "data/1.json").readLines())
      .isEqualTo(
        listOf(
          "{\"battleId\":\"1\",\"damageStrategy\":\"DETERMINISTIC\",\"trainerOne\":{\"name\":\"T1\",\"monsters\":[{\"name\":\"M1\",\"stats\":{\"maxHp\":100,\"hp\":100,\"speed\":20,\"attack\":20,\"specialAttack\":20,\"defense\":20,\"specialDefense\":20},\"primitiveType\":\"NORMAL\",\"attacks\":[\"PUNCH\"]}]},\"trainerTwo\":{\"name\":\"T2\",\"monsters\":[{\"name\":\"M2\",\"stats\":{\"maxHp\":10,\"hp\":0,\"speed\":10,\"attack\":20,\"specialAttack\":20,\"defense\":20,\"specialDefense\":20},\"primitiveType\":\"NORMAL\",\"attacks\":[\"PUNCH\"]}]},\"currentTrainer\":{\"name\":\"T2\",\"monsters\":[{\"name\":\"M2\",\"stats\":{\"maxHp\":10,\"hp\":0,\"speed\":10,\"attack\":20,\"specialAttack\":20,\"defense\":20,\"specialDefense\":20},\"primitiveType\":\"NORMAL\",\"attacks\":[\"PUNCH\"]}]},\"status\":\"FINISHED\"}",
        ),
      )
  }
}
