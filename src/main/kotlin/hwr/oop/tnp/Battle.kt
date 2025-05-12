package hwr.oop.tnp

class Battle(
    val trainer1: Trainer,
    val trainer2: Trainer,
    val battleId: Int = generateId()
) {
    companion object {
        private fun generateId(): Int {
            return (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
        }
    }


}
