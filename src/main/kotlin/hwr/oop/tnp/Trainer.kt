package hwr.oop.tnp

class Trainer(private val name: String, monsters: MutableList<Monster> = mutableListOf()) {
    private val MAX_ALLOWED_MONSTERS_PER_TRAINER = 6
    private val monsters = if (monsters.size > MAX_ALLOWED_MONSTERS_PER_TRAINER)
        monsters.take(MAX_ALLOWED_MONSTERS_PER_TRAINER).toMutableList()
    else
        monsters

    fun getName(): String {
        return this.name
    }

    fun getMonsters(): List<Monster> {
        return this.monsters
    }

    fun addMonster(monster: Monster): Boolean {
        if (monsters.size >= MAX_ALLOWED_MONSTERS_PER_TRAINER) {
            return false
        }
        return monsters.add(monster)
    }
}
