package hwr.oop.tnp

class Trainer(val name: String, private var monsters: List<Monster> = emptyList()) {
    init {
        require(monsters.size <= MAX_ALLOWED_MONSTERS_PER_TRAINER) {
            "Too many monsters: $monsters"
        }
    }

    fun getMonsters(): List<Monster> = monsters

    fun addMonster(monster: Monster) {
        require(monsters.size <= MAX_ALLOWED_MONSTERS_PER_TRAINER) { "Too many monsters" }
        monsters = monsters.plus(monster)
    }
}
