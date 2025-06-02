package hwr.oop.tnp.core

import hwr.oop.tnp.cli.Game
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.captureStandardOut
import org.assertj.core.api.Assertions.assertThat

class GameTest : AnnotationSpec() {

  private lateinit var game: Game
  private lateinit var battle: Battle

  @BeforeEach
  fun setUp() {
    game = Game()
    battle = Battle()
  }

  @Test
  fun `createTrainer should add trainer to battle`() {
    game.createTrainer("Ash", battle)

    val trainer = battle.getTrainerByName("Ash")
    assertThat(trainer.name).isEqualTo("Ash")
  }

  @Test
  fun `createTrainer should print exception when battle already full`() {
    battle.addTrainerToBattle(Trainer("Ash"))
    battle.addTrainerToBattle(Trainer("Misty"))

    val output = captureStandardOut { game.createTrainer("Brock", battle) }

    assertThat(output).contains("Both trainers are already set.")
  }

  @Test
  fun `addMonster should add monster to trainer`() {
    val trainerName = "Ash"
    game.createTrainer(trainerName, battle)

    game.addMonster(
      monsterName = "Pikachu",
      hp = 100,
      speed = 80,
      primitiveType = PrimitiveType.FIRE,
      attacks = listOf(Attack.PUNCH),
      trainerName = trainerName,
      battle = battle
    )

    val trainer = battle.getTrainerByName(trainerName)
    assertThat(trainer.monsters).hasSize(1)
    assertThat(trainer.monsters[0].name).isEqualTo("Pikachu")
  }

  @Test
  fun `addMonster should print exception when trainer not found`() {
    val output = captureStandardOut {
      game.addMonster(
        monsterName = "Charmander",
        hp = 80,
        speed = 60,
        primitiveType = PrimitiveType.FIRE,
        attacks = listOf(Attack.PUNCH),
        trainerName = "UnknownTrainer",
        battle = battle
      )
    }

    assertThat(output).contains("Trainer 'UnknownTrainer' not found.")
  }

  @Test
  fun `initiateBattle should return a battle and print status`() {
    val output = captureStandardOut {
      val newBattle = game.initiateBattle()
      assertThat(newBattle).isNotNull
    }

    assertThat(output).contains("Battle")
  }

  @Test
  fun `viewStatus should print current battle status`() {
    val output = captureStandardOut { game.viewStatus(battle) }

    assertThat(output).contains("Battle")
  }

  @Test
  fun `showAllBattles should print each battle`() {
    val battle1 = Battle()
    val battle2 = Battle()

    val output =
      captureStandardOut { game.showAllBattles(listOf(battle1, battle2)) }

    assertThat(output)
      .contains("Battle")
      .contains(battle1.battleId)
      .contains(battle2.battleId)
  }

  @Test
  fun `showAllBattles should notify when list is empty`() {
    val output = captureStandardOut { game.showAllBattles(emptyList()) }

    assertThat(output).contains("No battles created yet")
  }

  @Test
  fun `performAttack should start battle if status is PREGAME`() {
    val ash =
      Trainer("Ash").apply {
        addMonster(
          Monster(
            name = "Bulbasaur",
            stats = BattleStats(100, 50),
            primitiveType = PrimitiveType.PLANT,
            attacks = listOf(Attack.PUNCH)
          )
        )
      }

    val gary =
      Trainer("Gary").apply {
        addMonster(
          Monster(
            name = "Charmander",
            stats = BattleStats(90, 60),
            primitiveType = PrimitiveType.FIRE,
            attacks = listOf(Attack.PUNCH)
          )
        )
      }

    battle.addTrainerToBattle(ash)
    battle.addTrainerToBattle(gary)

    game.performAttack(battle, Attack.PUNCH)

    assertThat(battle.status).isEqualTo(BattleStatus.STARTED)
  }

  @Test
  fun `check performAttack exception messages`() {
    val output =
      captureStandardOut { game.performAttack(battle, Attack.PUNCH) }.trim()
    assertThat(output).isEqualTo("Trainer needs to have been set for this operation")
    val ash =
      Trainer("Ash").apply {
        addMonster(
          Monster(
            name = "Bulbasaur",
            stats = BattleStats(10, 50),
            primitiveType = PrimitiveType.PLANT,
            attacks = listOf(Attack.PUNCH)
          )
        )
      }

    val gary =
      Trainer("Gary").apply {
        addMonster(
          Monster(
            name = "Charmander",
            stats = BattleStats(20, 60),
            primitiveType = PrimitiveType.FIRE,
            attacks = listOf(Attack.PUNCH)
          )
        )
      }

    battle.addTrainerToBattle(ash)
    battle.addTrainerToBattle(gary)
    battle.startBattle()
    game.performAttack(battle, Attack.PUNCH)
    val output2 =
      captureStandardOut { game.performAttack(battle, Attack.PUNCH) }.trim()
    assertThat(output2).isEqualTo("Battle is already finished")
  }
}
