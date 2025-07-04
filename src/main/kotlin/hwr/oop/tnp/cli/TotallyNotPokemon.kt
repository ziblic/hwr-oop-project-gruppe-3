package hwr.oop.tnp.cli

import hwr.oop.tnp.core.Attack
import hwr.oop.tnp.core.DamageStrategy
import hwr.oop.tnp.core.PrimitiveType
import hwr.oop.tnp.persistency.LoadBattleException

class TotallyNotPokemon(
  private val args: List<String>,
) {
  class ParseToIntException(
    message: String,
  ) : Exception(message)

  class ParseToPrimitiveTypeException(
    message: String,
  ) : Exception(message)

  class ParseToAttackException(
    message: String,
  ) : Exception(message)

  class ParseToDamageStrategyException(
    message: String,
  ) : Exception(message)

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

Usage: ./tnp add_monster <MONSTERNAME> <HP_VALUE> <SPEED_VALUE> <ATTACK_VALUE> <SPECIAL_ATTACK_VALUE> <DEFENSE_VALUE> <SPECIAL_DEFENSE_VALUE> <TYPE> <ATTACK 1> [<ATTACK 2> <ATTACK 3> <ATTACK 4>] <TRAINER> <BATTLE_ID>"""

  private val newBattleHelp =
    """._   _                 ____        _   _   _        _   _      _
| \ | | _____      __ | __ )  __ _| |_| |_| | ___  | | | | ___| |_ __
|  \| |/ _ \ \ /\ / / |  _ \ / _` | __| __| |/ _ \ | |_| |/ _ \ | '_ \
| |\  |  __/\ V  V /  | |_) | (_| | |_| |_| |  __/ |  _  |  __/ | |_) |
|_| \_|\___| \_/\_/   |____/ \__,_|\__|\__|_|\___| |_| |_|\___|_| .__/
                                                                |_|

Usage: ./tnp new_battle <DAMAGE_STRATEGY: [random | deterministic]>"""

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

  private val commands: List<String> =
    listOf("new_trainer", "add_monster", "new_battle", "view_battle", "on", "help")

  private val commandsHelpMap: Map<String, String> =
    mapOf(
      commands[0] to newTrainerHelp,
      commands[1] to addMonsterHelp,
      commands[2] to newBattleHelp,
      commands[3] to viewBattleHelp,
      commands[4] to attackHelp,
      commands[5] to defaultHelp,
    )

  private lateinit var cliAdapter: BattleCliAdapter

  init {
    parseArguments()
  }

  private fun parseArguments() {
    if (args.isEmpty()) {
      printHelp()
      return
    }

    val command = args[0]
    val arguments = args.slice(1..args.size - 1)
    when (command) {
      commands[0] -> prepareForCreateTrainer(arguments)
      commands[1] -> prepareForAddMonster(arguments)
      commands[2] -> prepareForNewBattle(arguments)
      commands[3] -> prepareForViewBattle(arguments)
      commands[4] -> prepareForPerformAttack(arguments)
      commands[5] -> {
        if (args.size > 1) {
          printHelp(args[1])
        } else {
          printHelp()
        }
      }
      else -> println("'$command' is not a valid command. Use 'help' for usage.")
    }
  }

  private fun parseToInt(argument: String): Int =
    try {
      argument.toInt()
    } catch (e: NumberFormatException) {
      throw ParseToIntException("Error: Failed to convert '$argument' to int. Reason: ${e.message}")
    }

  private fun parseToAttack(input: String): Attack =
    try {
      Attack.valueOf(input.uppercase())
    } catch (e: IllegalArgumentException) {
      throw ParseToAttackException(
        "Error: Failed to convert '$input' to Attack. Reason: ${e.message}",
      )
    }

  private fun parseToPrimitiveType(input: String): PrimitiveType =
    try {
      PrimitiveType.valueOf(input.uppercase())
    } catch (e: IllegalArgumentException) {
      throw ParseToPrimitiveTypeException(
        "Error: Failed to convert '$input' to Type. Reason: ${e.message}",
      )
    }

  private fun parseToDamageStrategy(input: String): DamageStrategy =
    try {
      DamageStrategy.valueOf(input.uppercase())
    } catch (e: IllegalArgumentException) {
      throw ParseToDamageStrategyException(
        "Error: Failed to convert '$input' to Damage Strategy. Reason: ${e.message}",
      )
    }

  private fun prepareForCreateTrainer(args: List<String>) {
    if (args.size != 2) {
      println(newTrainerHelp)
      return
    }

    try {
      cliAdapter = BattleCliAdapter(args[1])
      cliAdapter.createTrainer(args[0])
    } catch (e: LoadBattleException) {
      println(e.message)
    }
  }

  private fun prepareForAddMonster(args: List<String>) {
    if (args.isEmpty() || !(args.size >= 11 && args.size <= 14)) {
      println(addMonsterHelp)
      return
    }

    val monsterName = args[0]
    val trainerName = args[args.size - 2]

    try {
      val hp = parseToInt(args[1])
      val speed = parseToInt(args[2])
      val attackVal = parseToInt(args[3])
      val specialAttack = parseToInt(args[4])
      val defense = parseToInt(args[5])
      val specialDefense = parseToInt(args[6])
      val type = parseToPrimitiveType(args[7])
      val attackList: MutableList<Attack> = mutableListOf()
      for (attack in args.slice(8..args.size - 3).toList()) {
        attackList.add(parseToAttack(attack))
      }
      cliAdapter = BattleCliAdapter(args[args.size - 1])

      cliAdapter.addMonster(
        monsterName,
        hp,
        speed,
        attackVal,
        specialAttack,
        defense,
        specialDefense,
        type,
        attackList,
        trainerName,
      )
    } catch (e: ParseToIntException) {
      println(e.message)
    } catch (e: LoadBattleException) {
      println(e.message)
    } catch (e: ParseToPrimitiveTypeException) {
      println(e.message)
    } catch (e: ParseToAttackException) {
      println(e.message)
    }
  }

  private fun prepareForNewBattle(args: List<String>) {
    if (args.size != 1) {
      println(newBattleHelp)
      return
    }

    try {
      BattleCliAdapter.initiateBattle(parseToDamageStrategy(args[0]))
    } catch (e: ParseToDamageStrategyException) {
      println(e.message)
    }
  }

  private fun prepareForViewBattle(args: List<String>) {
    if (args.size != 1) {
      println(viewBattleHelp)
      return
    }

    if (args[0].trim().lowercase() == "all") {
      BattleCliAdapter.showAllBattles()
      return
    }

    try {
      cliAdapter = BattleCliAdapter(args[0])
      cliAdapter.viewStatus()
    } catch (e: LoadBattleException) {
      println(e.message)
    }
  }

  private fun prepareForPerformAttack(args: List<String>) {
    if (args.isEmpty() || args.size != 2) {
      println(attackHelp)
      return
    }

    try {
      cliAdapter = BattleCliAdapter(args[0])
      cliAdapter.performAttack(parseToAttack(args[1]))
    } catch (e: LoadBattleException) {
      println(e.message)
    }
  }

  private fun printHelp(command: String = "") {
    val helpMsg = commandsHelpMap.getOrDefault(command, defaultHelp)
    println(helpMsg)
  }
}

fun main(args: Array<String>) {
  TotallyNotPokemon(args.toList())
}
