package hwr.oop.tnp

class Trainer(private val name: String, monsters: List<Monster> = emptyList()) {
    private val monsters = if (monsters.size > MAX_ALLOWED_MONSTERS_PER_TRAINER)
        monsters.take(MAX_ALLOWED_MONSTERS_PER_TRAINER)
    else
        monsters

    fun getName(): String {
        return this.name
    }

    fun getMonsters(): List<Monster> {
        return this.monsters
    }
}
