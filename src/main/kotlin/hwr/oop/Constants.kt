package hwr.oop

val commands: List<String> =
        listOf("new_trainer", "add_monster", "new_battle", "view_battle", "on", "help")

val defaultHelp =
        """Usage: command [options]
   Commands:
      new_trainer     - Creates a new trainer
      add_monster     - Adds a new monster to your roster
      battle          - Starts a battle sequence
      view            - Views current game status
      on              - Select a attack to perform
      help            - Shows this help message"""

val newTrainerHelp = """Trainer Help"""
val addMonsterHelp = """Add Monster Help"""
val newBattleHelp = """New Battle"""
val viewBattleHelp = """View Battle"""
val attackHelp = """Attack Help"""
