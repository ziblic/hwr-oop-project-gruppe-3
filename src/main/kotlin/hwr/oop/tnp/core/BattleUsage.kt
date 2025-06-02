package hwr.oop.tnp.core

interface BattleUsage {
        val status: BattleStatus
        fun getTrainerByName(name: String): Trainer
        fun addTrainerToBattle(trainer: Trainer)
        fun startBattle()
        fun takeTurn(attack: Attack): Monster
        fun determineWinner(): Trainer
}
