package hwr.oop.tnp.cli

import hwr.oop.tnp.core.Attack
import hwr.oop.tnp.core.Battle
import hwr.oop.tnp.core.BattleStats
import hwr.oop.tnp.core.Monster
import hwr.oop.tnp.core.PrimitiveType
import hwr.oop.tnp.core.Trainer
import hwr.oop.tnp.persistency.FileSystemBasedJsonPersistence
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.captureStandardOut
import java.io.File
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach

class TotallyNotPokemonTest : AnnotationSpec() {
  private val defaultHelp =
          """.___________  _____  ___     _______
("     _   ")(\"   \|"  \   |   __ "\
 )__/  \\__/ |.\\   \    |  (. |__) :)
    \\_ /    |: \.   \\  |  |:  ____/
    |.  |    |.  \    \. |  (|  /
    \:  |    |    \    \ | /|__/ \
     \__|     \___|\____\)(_______)

Totally Not Pokémon Usage: ./tnp command [arguments]
   Commands:
      new_trainer     - Creates a new trainer
      add_monster     - Adds a new monster to your roster
      new_battle      - Starts a battle sequence
      view_battle     - Views current game status
      on              - Select a attack to perform
      help            - Shows this help message"""

  private val newTrainerHelp =
          """._____          _                   _   _      _
|_   _| __ __ _(_)_ __   ___ _ __  | | | | ___| |_ __
  | || '__/ _` | | '_ \ / _ \ '__| | |_| |/ _ \ | '_ \
  | || | | (_| | | | | |  __/ |    |  _  |  __/ | |_) |
  |_||_|  \__,_|_|_| |_|\___|_|    |_| |_|\___|_| .__/
                                                |_|

Usage: ./tnp new_trainer <TRAINERNAME> <BATTLE_ID>"""

  private val addMonsterHelp =
          """.   _       _     _   __  __                 _              _   _      _
   / \   __| | __| | |  \/  | ___  _ __  ___| |_ ___ _ __  | | | | ___| |_ __
  / _ \ / _` |/ _` | | |\/| |/ _ \| '_ \/ __| __/ _ \ '__| | |_| |/ _ \ | '_ \
 / ___ \ (_| | (_| | | |  | | (_) | | | \__ \ ||  __/ |    |  _  |  __/ | |_) |
/_/   \_\__,_|\__,_| |_|  |_|\___/|_| |_|___/\__\___|_|    |_| |_|\___|_| .__/
                                                                        |_|

Usage: ./tnp add_monster <MONSTERNAME> <HP_VALUE> <SPEED_VALUE> <TYPE> <ATTACK 1> [<ATTACK 2> <ATTACK 3> <ATTACK 4>] <TRAINER> <BATTLE_ID>"""

  private val newBattleHelp =
          """._   _                 ____        _   _   _        _   _      _
| \ | | _____      __ | __ )  __ _| |_| |_| | ___  | | | | ___| |_ __
|  \| |/ _ \ \ /\ / / |  _ \ / _` | __| __| |/ _ \ | |_| |/ _ \ | '_ \
| |\  |  __/\ V  V /  | |_) | (_| | |_| |_| |  __/ |  _  |  __/ | |_) |
|_| \_|\___| \_/\_/   |____/ \__,_|\__|\__|_|\___| |_| |_|\___|_| .__/
                                                                |_|

Usage: ./tnp new_battle"""

  private val viewBattleHelp =
          """__     ___                 ____        _   _   _        _   _      _
\ \   / (_) _____      __ | __ )  __ _| |_| |_| | ___  | | | | ___| |_ __
 \ \ / /| |/ _ \ \ /\ / / |  _ \ / _` | __| __| |/ _ \ | |_| |/ _ \ | '_ \
  \ V / | |  __/\ V  V /  | |_) | (_| | |_| |_| |  __/ |  _  |  __/ | |_) |
   \_/  |_|\___| \_/\_/   |____/ \__,_|\__|\__|_|\___| |_| |_|\___|_| .__/
                                                                    |_|

Usage: ./tnp view_battle <BATTLE_ID> | ALL

Examples:
1. View a specific battle with ID 123:
   - `./tnp view_battle 123`

2. View all battles:
   - `./tnp view_battle ALL`"""

  private val attackHelp =
          """.   _   _   _             _      _   _      _
   / \ | |_| |_ __ _  ___| | __ | | | | ___| |_ __
  / _ \| __| __/ _` |/ __| |/ / | |_| |/ _ \ | '_ \
 / ___ \ |_| || (_| | (__|   <  |  _  |  __/ | |_) |
/_/   \_\__|\__\__,_|\___|_|\_\ |_| |_|\___|_| .__/
                                             |_|

Usage: ./tnp on <BATTLE_ID> <ATTACKNAME>"""

  @AfterEach
  fun cleanUp() {
    File(System.getProperty("user.dir"), "data/1.json").delete()
  }

  @Test
  fun `test parseToXXXXX with invalid input`() {
    val output =
            captureStandardOut {
                      TotallyNotPokemon(
                              listOf(
                                      "add_monster",
                                      "Bob",
                                      "abc",
                                      "abc",
                                      "Fire",
                                      "PUNCH",
                                      "Trainer_Kevin",
                                      "1"
                              ),
                      )
                    }
                    .trim()
    assertThat(output).contains("Error: Failed to convert", "to int. Reason:")
    val output2 =
            captureStandardOut {
                      TotallyNotPokemon(
                              listOf(
                                      "add_monster",
                                      "Bob",
                                      "100",
                                      "20",
                                      "Hello",
                                      "PUNCH",
                                      "Trainer_Kevin",
                                      "1"
                              ),
                      )
                    }
                    .trim()
    assertThat(output2).contains("Error: Failed to convert", "to Type. Reason:")
    val output3 =
            captureStandardOut {
                      TotallyNotPokemon(
                              listOf(
                                      "add_monster",
                                      "Bob",
                                      "100",
                                      "20",
                                      "Fire",
                                      "Tackle_3",
                                      "Trainer_Kevin",
                                      "1"
                              ),
                      )
                    }
                    .trim()
    assertThat(output3).contains("Error: Failed to convert", "to Attack. Reason:")
  }

  @Test
  fun `Run invalid command`() {
    val output = captureStandardOut { TotallyNotPokemon(listOf("some_unknown_command")) }.trim()
    assertThat(output)
            .isEqualTo("'some_unknown_command' is not a valid command. Use 'help' for usage.")
  }

  @Test
  fun `Run without any command`() {
    val output = captureStandardOut { TotallyNotPokemon(listOf()) }.trim()
    assertThat(output).isEqualTo(defaultHelp)
  }

  @Test
  fun `parseForPerformAttack throw Exception`() {
    val output = captureStandardOut { TotallyNotPokemon(listOf("on", "a", "PUNCH")) }.trim()
    assertThat(output).isEqualTo("Could not find battle with id: a.")
  }

  @Test
  fun `Get help message from cli`() {
    val output = captureStandardOut { main(arrayOf("help")) }.trim()
    assertThat(output).isEqualTo(defaultHelp)
  }

  @Test
  fun `Get help message for new_trainer command`() {
    val output = captureStandardOut { TotallyNotPokemon(listOf("help", "new_trainer")) }.trim()
    assertThat(output).isEqualTo(newTrainerHelp)
  }

  @Test
  fun `Get help message for add_monster command`() {
    val output = captureStandardOut { TotallyNotPokemon(listOf("help", "add_monster")) }.trim()
    assertThat(output).isEqualTo(addMonsterHelp)
  }

  @Test
  fun `Get help message for new_battle command`() {
    val output = captureStandardOut { TotallyNotPokemon(listOf("help", "new_battle")) }.trim()
    assertThat(output).isEqualTo(newBattleHelp)
  }

  @Test
  fun `Get help message for view_battle command`() {
    val output = captureStandardOut { TotallyNotPokemon(listOf("help", "view_battle")) }.trim()
    assertThat(output).isEqualTo(viewBattleHelp)
  }

  @Test
  fun `Get help message for on command`() {
    val output = captureStandardOut { TotallyNotPokemon(listOf("help", "on")) }.trim()
    assertThat(output).isEqualTo(attackHelp)
  }

  @Test
  fun `Create new trainer`() {
    val battle = Battle("1")
    val saveAdapter = FileSystemBasedJsonPersistence()
    saveAdapter.saveBattle(battle)
    val output =
            captureStandardOut { TotallyNotPokemon(listOf("new_trainer", "Kevin", "1")) }.trim()
    assertThat(output)
            .isEqualTo(captureStandardOut { BattleCliAdapter("1").createTrainer("Kevin") }.trim())
  }

  @Test
  fun `Create new trainer with no arguments`() {
    val output = captureStandardOut { TotallyNotPokemon(listOf("new_trainer")) }.trim()
    assertThat(output).isEqualTo(newTrainerHelp)
  }

  @Test
  fun `Try adding trainer to non existing battle print exception message`() {
    val output =
            captureStandardOut { TotallyNotPokemon(listOf("new_trainer", "Kevin", "1")) }.trim()
    assertThat(output).isEqualTo("Could not find battle with id: 1.")
  }

  @Test
  fun `Add new monster`() {
    val battleId = "1"
    val battle = Battle(battleId)
    battle.addTrainerToBattle(Trainer("Trainer_Kevin"))

    val output =
            captureStandardOut {
                      TotallyNotPokemon(
                              listOf(
                                      "add_monster",
                                      "Bob",
                                      "100",
                                      "20",
                                      "FIRE",
                                      "PUNCH",
                                      "Trainer_Kevin",
                                      battleId
                              ),
                      )
                    }
                    .trim()
    val output_2 =
            captureStandardOut {
                      TotallyNotPokemon(
                              listOf(
                                      "add_monster",
                                      "Bob",
                                      "100",
                                      "20",
                                      "Fire",
                                      "PUNCH",
                                      "FIRE_Vow",
                                      "Splash",
                                      "Foliage_storm",
                                      "Trainer_Kevin",
                                      battleId
                              ),
                      )
                    }
                    .trim()
    assertThat(output)
            .isEqualTo(
                    captureStandardOut {
                              BattleCliAdapter(battleId)
                                      .addMonster(
                                              "Bob",
                                              100,
                                              20,
                                              PrimitiveType.FIRE,
                                              listOf(Attack.PUNCH),
                                              "Trainer_Kevin",
                                      )
                            }
                            .trim()
            )
    assertThat(output_2)
            .isEqualTo(
                    captureStandardOut {
                              BattleCliAdapter(battleId)
                                      .addMonster(
                                              "Bob",
                                              100,
                                              20,
                                              PrimitiveType.FIRE,
                                              listOf(
                                                      Attack.PUNCH,
                                                      Attack.FIRE_VOW,
                                                      Attack.SPLASH,
                                                      Attack.FOLIAGE_STORM
                                              ),
                                              "Trainer_Kevin",
                                      )
                            }
                            .trim()
            )
  }

  @Test
  fun `Add new monster with no arguments`() {
    val output = captureStandardOut { TotallyNotPokemon(listOf("add_monster")) }.trim()
    assertThat(output).isEqualTo(addMonsterHelp)
  }

  @Test
  fun `Add new monster with not enough or to many arguments`() {
    val output1 =
            captureStandardOut {
                      TotallyNotPokemon(
                              listOf(
                                      "add_monster",
                                      "Bob",
                                      "100",
                                      "20",
                                      "FIRE",
                                      "Flame_Wreath",
                                      "PUNCH",
                                      "Splash",
                                      "Leaf_gun",
                                      "Trainer_Kevin",
                                      "1",
                                      "too many args"
                              ),
                      )
                    }
                    .trim()
    val output2 =
            captureStandardOut {
                      TotallyNotPokemon(
                              listOf("add_monster", "Bob", "100", "20", "FIRE", "PUNCH"),
                      )
                    }
                    .trim()
    assertThat(output1).isEqualTo(addMonsterHelp)
    assertThat(output2).isEqualTo(addMonsterHelp)
  }

  @Test
  fun `Start new battle`() {
    // FIX: the test
    TotallyNotPokemon(listOf("new_battle"))
  }

  @Test
  fun `Start new battle with not enough or to many arguments`() {
    val output1 =
            captureStandardOut { TotallyNotPokemon(listOf("new_battle", "to many args")) }.trim()
    assertThat(output1).isEqualTo(newBattleHelp)
  }

  @Test
  fun `View battle status`() {
    val battle = Battle("1")
    val saveAdapter = FileSystemBasedJsonPersistence()
    saveAdapter.saveBattle(battle)
    val output = captureStandardOut { TotallyNotPokemon(listOf("view_battle", "1")) }.trim()
    val expectedOutput = captureStandardOut { BattleCliAdapter("1").viewStatus() }.trim()
    assertThat(output).isEqualTo(expectedOutput)
  }

  @Test
  fun `Throw exception on invalid battleId`() {
    val output = captureStandardOut { TotallyNotPokemon(listOf("view_battle", "nr1")) }.trim()
    assertThat(output).isEqualTo("Could not find battle with id: nr1.")
  }

  @Test
  fun `View all battles`() {
    val output = captureStandardOut { TotallyNotPokemon(listOf("view_battle", "  ALL  ")) }.trim()
    assertThat(output).isEqualTo(captureStandardOut { BattleCliAdapter.showAllBattles() }.trim())
  }

  @Test
  fun `View battle with no arguments`() {
    val output = captureStandardOut { TotallyNotPokemon(listOf("view_battle")) }.trim()
    assertThat(output).isEqualTo(viewBattleHelp)
  }

  @Test
  fun `Attack enemy`() {
    val battle = Battle("1")
    val t1 = Trainer("Trainer_Kevin")
    val t2 = Trainer("Trainer_Bob")
    battle.addTrainerToBattle(t1)
    battle.addTrainerToBattle(t2)
    battle.addMonsterToTrainer(
            "Trainer_Kevin",
            Monster("Pika", BattleStats(100, 20), PrimitiveType.FIRE, listOf(Attack.PUNCH))
    )
    battle.addMonsterToTrainer(
            "Trainer_Bob",
            Monster("Glurak", BattleStats(100, 20), PrimitiveType.FIRE, listOf(Attack.PUNCH))
    )

    val output = captureStandardOut { TotallyNotPokemon(listOf("on", "1", "PUNCH")) }.trim()
    assertThat(output)
            .isEqualTo(
                    captureStandardOut { BattleCliAdapter("1").performAttack(Attack.PUNCH) }.trim()
            )
  }

  @Test
  fun `Attack enemy with no arguments`() {
    val output = captureStandardOut { TotallyNotPokemon(listOf("on")) }.trim()
    assertThat(output).isEqualTo(attackHelp)
  }

  @Test
  fun `Attack enemy with not enough or to many arguments`() {
    val output1 =
            captureStandardOut { TotallyNotPokemon(listOf("on", "0", "PUNCH", "too many args")) }
                    .trim()
    val output2 = captureStandardOut { TotallyNotPokemon(listOf("on", "0")) }.trim()
    assertThat(output1).isEqualTo(attackHelp)
    assertThat(output2).isEqualTo(attackHelp)
  }
}
