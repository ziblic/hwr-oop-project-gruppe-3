package hwr.oop.tnp.cli

import io.kotest.core.spec.style.AnnotationSpec

class BattleCliAdapterTest : AnnotationSpec() {
  //
  // private lateinit var cliAdapter: BattleCliAdapter
  // private lateinit var battle: Battle
  //
  // @BeforeEach
  // fun setUp() {
  //   cliAdapter = BattleCliAdapter("1")
  //   battle = Battle()
  // }
  //
  // @AfterEach
  // fun cleanUp() {
  //   val file = File(System.getProperty("user.dir", "data/1"))
  //   file.delete()
  // }
  //
  // @Test
  // fun `createTrainer should add trainer to battle`() {
  //   cliAdapter.createTrainer("Ash")
  //
  //   val trainer = battle.getTrainerByName("Ash")
  //   assertThat(trainer.name).isEqualTo("Ash")
  // }
  //
  // @Test
  // fun `createTrainer should print exception when battle already full`() {
  //   battle.addTrainerToBattle(Trainer("Ash"))
  //   battle.addTrainerToBattle(Trainer("Misty"))
  //
  //   val output = captureStandardOut { cliAdapter.createTrainer("Brock") }
  //
  //   assertThat(output).contains("Both trainers are already set.")
  // }
  //
  // @Test
  // fun `addMonster should add monster to trainer`() {
  //   val trainerName = "Ash"
  //   cliAdapter.createTrainer(trainerName)
  //
  //   cliAdapter.addMonster(
  //           monsterName = "Pikachu",
  //           hp = 100,
  //           speed = 80,
  //           primitiveType = PrimitiveType.FIRE,
  //           attacks = listOf(Attack.PUNCH),
  //           trainerName = trainerName,
  //   )
  //
  //   val trainer = battle.getTrainerByName(trainerName)
  //   assertThat(trainer.monsters).hasSize(1)
  //   assertThat(trainer.monsters[0].name).isEqualTo("Pikachu")
  // }
  //
  // @Test
  // fun `addMonster should print exception when trainer not found`() {
  //   val output = captureStandardOut {
  //     cliAdapter.addMonster(
  //             monsterName = "Charmander",
  //             hp = 80,
  //             speed = 60,
  //             primitiveType = PrimitiveType.FIRE,
  //             attacks = listOf(Attack.PUNCH),
  //             trainerName = "UnknownTrainer",
  //     )
  //   }
  //
  //   assertThat(output).contains("Trainer 'UnknownTrainer' not found.")
  // }
  //
  // @Test
  // fun `viewStatus should print current battle status`() {
  //   val output = captureStandardOut { cliAdapter.viewStatus() }
  //   assertThat(output).contains("Battle")
  // }
  //
  // @Test
  // fun `showAllBattles should print each battle`() {
  //   val battle1 = Battle()
  //   val battle2 = Battle()
  //
  //   val output = captureStandardOut { BattleCliAdapter.showAllBattles() }
  //
  //   assertThat(output).contains("Battle").contains(battle1.battleId).contains(battle2.battleId)
  // }
  //
  // @Test
  // fun `showAllBattles should notify when list is empty`() {
  //   val output = captureStandardOut { BattleCliAdapter.showAllBattles() }
  //   assertThat(output).contains("No battles created yet")
  // }
  //
  // @Test
  // fun `performAttack should start battle if status is PREGAME`() {
  //   val ash =
  //           Trainer("Ash").apply {
  //             addMonster(
  //                     Monster(
  //                             name = "Bulbasaur",
  //                             stats = BattleStats(100, 50),
  //                             primitiveType = PrimitiveType.PLANT,
  //                             attacks = listOf(Attack.PUNCH)
  //                     )
  //             )
  //           }
  //
  //   val gary =
  //           Trainer("Gary").apply {
  //             addMonster(
  //                     Monster(
  //                             name = "Charmander",
  //                             stats = BattleStats(90, 60),
  //                             primitiveType = PrimitiveType.FIRE,
  //                             attacks = listOf(Attack.PUNCH)
  //                     )
  //             )
  //           }
  //
  //   battle.addTrainerToBattle(ash)
  //   battle.addTrainerToBattle(gary)
  //
  //   cliAdapter.performAttack(Attack.PUNCH)
  //
  //   assertThat(battle.status).isEqualTo(BattleStatus.STARTED)
  // }
  //
  // @Test
  // fun `check performAttack exception messages`() {
  //   val output = captureStandardOut { cliAdapter.performAttack(Attack.PUNCH) }.trim()
  //   assertThat(output).isEqualTo("Trainer needs to have been set for this operation")
  //   val ash =
  //           Trainer("Ash").apply {
  //             addMonster(
  //                     Monster(
  //                             name = "Bulbasaur",
  //                             stats = BattleStats(10, 50),
  //                             primitiveType = PrimitiveType.PLANT,
  //                             attacks = listOf(Attack.PUNCH)
  //                     )
  //             )
  //           }
  //
  //   val gary =
  //           Trainer("Gary").apply {
  //             addMonster(
  //                     Monster(
  //                             name = "Charmander",
  //                             stats = BattleStats(20, 60),
  //                             primitiveType = PrimitiveType.FIRE,
  //                             attacks = listOf(Attack.PUNCH)
  //                     )
  //             )
  //           }
  //
  //   battle.addTrainerToBattle(ash)
  //   battle.addTrainerToBattle(gary)
  //   battle.startBattle()
  //   cliAdapter.performAttack(Attack.PUNCH)
  //   val output2 = captureStandardOut { cliAdapter.performAttack(Attack.PUNCH) }.trim()
  //   assertThat(output2).isEqualTo("Battle is already finished")
  // }
}
