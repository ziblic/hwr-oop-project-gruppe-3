package hwr.oop.tnp.cli

const val COULD_NOT_PARSE_ERROR = "Some of the provided arguments could not be parsed correctly"
const val COULD_NOT_PARSE_TO_INT_ERROR =
    "Some of the provided arguments could not be parsed to an Int"

const val defaultHelp =
    """___________  _____  ___     _______
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

const val newTrainerHelp =
    """._____          _                   _   _      _
|_   _| __ __ _(_)_ __   ___ _ __  | | | | ___| |_ __
  | || '__/ _` | | '_ \ / _ \ '__| | |_| |/ _ \ | '_ \
  | || | | (_| | | | | |  __/ |    |  _  |  __/ | |_) |
  |_||_|  \__,_|_|_| |_|\___|_|    |_| |_|\___|_| .__/
                                                |_|

Usage: ./tnp new_trainer <TRAINERNAME>"""

const val addMonsterHelp =
    """.   _       _     _   __  __                 _              _   _      _
   / \   __| | __| | |  \/  | ___  _ __  ___| |_ ___ _ __  | | | | ___| |_ __
  / _ \ / _` |/ _` | | |\/| |/ _ \| '_ \/ __| __/ _ \ '__| | |_| |/ _ \ | '_ \
 / ___ \ (_| | (_| | | |  | | (_) | | | \__ \ ||  __/ |    |  _  |  __/ | |_) |
/_/   \_\__,_|\__,_| |_|  |_|\___/|_| |_|___/\__\___|_|    |_| |_|\___|_| .__/
                                                                        |_|

Usage: ./tnp add_monster <MONSTERNAME> <HP_VALUE> <SPEED_VALUE> <TYPE> <ATTACK 1> [<ATTACK 2> <ATTACK 3> <ATTACK 4>] <TRAINER>"""

const val newBattleHelp =
    """._   _                 ____        _   _   _        _   _      _
| \ | | _____      __ | __ )  __ _| |_| |_| | ___  | | | | ___| |_ __
|  \| |/ _ \ \ /\ / / |  _ \ / _` | __| __| |/ _ \ | |_| |/ _ \ | '_ \
| |\  |  __/\ V  V /  | |_) | (_| | |_| |_| |  __/ |  _  |  __/ | |_) |
|_| \_|\___| \_/\_/   |____/ \__,_|\__|\__|_|\___| |_| |_|\___|_| .__/
                                                                |_|

Usage: ./tnp new_battle <TRAINERNAME 1> <TRAINERNAME 2>"""

const val viewBattleHelp =
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

const val attackHelp =
    """.   _   _   _             _      _   _      _
   / \ | |_| |_ __ _  ___| | __ | | | | ___| |_ __
  / _ \| __| __/ _` |/ __| |/ / | |_| |/ _ \ | '_ \
 / ___ \ |_| || (_| | (__|   <  |  _  |  __/ | |_) |
/_/   \_\__|\__\__,_|\___|_|\_\ |_| |_|\___|_| .__/
                                             |_|

Usage: ./tnp on <BATTLE_ID> <ATTACKNAME>"""

val commands: List<String> =
    listOf("new_trainer", "add_monster", "new_battle", "view_battle", "on", "help")

val commandsHelpMap: Map<String, String> =
    mapOf(
        commands[0] to newTrainerHelp,
        commands[1] to addMonsterHelp,
        commands[2] to newBattleHelp,
        commands[3] to viewBattleHelp,
        commands[4] to attackHelp,
        commands[5] to defaultHelp,
    )
