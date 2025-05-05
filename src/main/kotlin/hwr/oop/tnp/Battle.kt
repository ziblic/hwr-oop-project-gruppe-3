package hwr.oop.tnp


class Battle(
    private val trainerOne: Trainer,
    private val trainerTwo: Trainer
) {
    private var currentTurn: Int = 0
    private var finished: Boolean = false


    fun isFinished(): Boolean = finished

    fun getCurrentTurn(): Int = currentTurn


    fun startBattle() {
        currentTurn = 1
        finished = false
    }


    private fun endBattle() {
        finished = true
    }

    /**
     * 执行一回合的战斗：
     *  - 先比较两只怪物的 speed，速度高的先手
     *  - 先手攻击后如果被攻击方 KO 则直接结束战斗
     *  - 否则再执行后手攻击，若再被 KO 也结束战斗
     *  - 若双方都存活，则回合数加一
     */
    fun executeTurn() {
        if (finished) {
            throw IllegalStateException("Battle is already finished")
        }

        val m1 = trainerOne.getMonsters().firstOrNull()
            ?: throw IllegalStateException("Trainer one has no monsters")
        val m2 = trainerTwo.getMonsters().firstOrNull()
            ?: throw IllegalStateException("Trainer two has no monsters")

        val (first, second) =
            if (m1.getSpeed() >= m2.getSpeed()) m1 to m2 else m2 to m1

        // 先手攻击
        second.takeDamage(first.getAttack())
        if (second.isKO()) {
            endBattle()
            return
        }

        // 后手攻击
        first.takeDamage(second.getAttack())
        if (first.isKO()) {
            endBattle()
            return
        }

        // 回合结束，双方都存活
        currentTurn++
    }

    /**
     * 返回获胜的 Trainer：
     *  - 如果战斗没结束或平局，返回 null
     *  - 否则返回存活的一方
     */
    fun determineWinner(): Trainer? {
        if (!finished) return null

        val m1 = trainerOne.getMonsters().firstOrNull() ?: return null
        val m2 = trainerTwo.getMonsters().firstOrNull() ?: return null

        return when {
            !m1.isKO() && m2.isKO() -> trainerOne
            m1.isKO() && !m2.isKO() -> trainerTwo
            else -> null  // 平局或异常情况
        }
    }
}
