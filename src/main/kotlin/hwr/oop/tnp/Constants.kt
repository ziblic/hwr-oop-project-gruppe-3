package hwr.oop.tnp

const val MAX_ALLOWED_MONSTERS_PER_TRAINER = 6

val commands: List<String> =
    listOf("new_trainer", "add_monster", "new_battle", "view_battle", "on", "help")

val defaultHelp =
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

val newTrainerHelp =
    """._____          _                   _   _      _
|_   _| __ __ _(_)_ __   ___ _ __  | | | | ___| |_ __
  | || '__/ _` | | '_ \ / _ \ '__| | |_| |/ _ \ | '_ \
  | || | | (_| | | | | |  __/ |    |  _  |  __/ | |_) |
  |_||_|  \__,_|_|_| |_|\___|_|    |_| |_|\___|_| .__/
                                                |_|

Usage: ./tnp new_trainer <TRAINERNAME>"""

val addMonsterHelp =
    """.   _       _     _   __  __                 _              _   _      _
   / \   __| | __| | |  \/  | ___  _ __  ___| |_ ___ _ __  | | | | ___| |_ __
  / _ \ / _` |/ _` | | |\/| |/ _ \| '_ \/ __| __/ _ \ '__| | |_| |/ _ \ | '_ \
 / ___ \ (_| | (_| | | |  | | (_) | | | \__ \ ||  __/ |    |  _  |  __/ | |_) |
/_/   \_\__,_|\__,_| |_|  |_|\___/|_| |_|___/\__\___|_|    |_| |_|\___|_| .__/
                                                                        |_|

Usage: ./tnp add_monster <MONSTERNAME> <HP_VALUE> <ATTACK_VALUE> <DEFENSE_VALUE> <SPECIALATTACK_VALUE> <SPECIALDEFENSE_VALUE>"""

val newBattleHelp =
    """._   _                 ____        _   _   _        _   _      _
| \ | | _____      __ | __ )  __ _| |_| |_| | ___  | | | | ___| |_ __
|  \| |/ _ \ \ /\ / / |  _ \ / _` | __| __| |/ _ \ | |_| |/ _ \ | '_ \
| |\  |  __/\ V  V /  | |_) | (_| | |_| |_| |  __/ |  _  |  __/ | |_) |
|_| \_|\___| \_/\_/   |____/ \__,_|\__|\__|_|\___| |_| |_|\___|_| .__/
                                                                |_|

Usage: ./tnp new_battle <TRAINERNAME 1> <TRAINERNAME 2>"""

// TODO: Add help message when implemented
val viewBattleHelp =
    """__     ___                 ____        _   _   _        _   _      _
\ \   / (_) _____      __ | __ )  __ _| |_| |_| | ___  | | | | ___| |_ __
 \ \ / /| |/ _ \ \ /\ / / |  _ \ / _` | __| __| |/ _ \ | |_| |/ _ \ | '_ \
  \ V / | |  __/\ V  V /  | |_) | (_| | |_| |_| |  __/ |  _  |  __/ | |_) |
   \_/  |_|\___| \_/\_/   |____/ \__,_|\__|\__|_|\___| |_| |_|\___|_| .__/
                                                                    |_|

Usage: ./tnp view_battle <ARGUMENTS>"""

val attackHelp =
    """.   _   _   _             _      _   _      _
   / \ | |_| |_ __ _  ___| | __ | | | | ___| |_ __
  / _ \| __| __/ _` |/ __| |/ / | |_| |/ _ \ | '_ \
 / ___ \ |_| || (_| | (__|   <  |  _  |  __/ | |_) |
/_/   \_\__|\__\__,_|\___|_|\_\ |_| |_|\___|_| .__/
                                             |_|

Usage: ./tnp on <BATTLE_ID> <TRAINERNAME> <ATTACKNAME>"""
