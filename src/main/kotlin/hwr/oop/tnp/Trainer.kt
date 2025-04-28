package hwr.oop.tnp

class Trainer(
    val name: String,
    initialMonsters: List<Monster> = emptyList()
) {
    private val monsters: MutableList<Monster> =
        initialMonsters.take(MAX_ALLOWED_MONSTERS_PER_TRAINER).toMutableList()

    fun getMonsters(): List<Monster> = monsters.toList()

    fun addMonster(monster: Monster): Boolean {
        if (monsters.size >= MAX_ALLOWED_MONSTERS_PER_TRAINER) {
            return false
        }
        return monsters.add(monster)
    }
}
