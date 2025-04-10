package hwr.oop

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.captureStandardOut
import org.assertj.core.api.Assertions.assertThat

class MainTest : AnnotationSpec() {

    // CLI TESTS
    @Test
    fun `Get help message from cli`() {
        val output = captureStandardOut { main(arrayOf("help")) }.trim()
        assertThat(output).isEqualTo(defaultHelp)
    }

    @Test
    fun `Get help message for new_trainer command`() {
        val output = captureStandardOut { main(arrayOf("help", "new_trainer")) }.trim()
        assertThat(output).isEqualTo(newTrainerHelp)
    }

    @Test
    fun `Get help message for add_monster command`() {
        val output = captureStandardOut { main(arrayOf("help", "add_monster")) }.trim()
        assertThat(output).isEqualTo(addMonsterHelp)
    }

    @Test
    fun `Get help message for new_battle command`() {
        val output = captureStandardOut { main(arrayOf("help", "new_battle")) }.trim()
        assertThat(output).isEqualTo(newBattleHelp)
    }

    @Test
    fun `Get help message for view_battle command`() {
        val output = captureStandardOut { main(arrayOf("help", "view_battle")) }.trim()
        assertThat(output).isEqualTo(viewBattleHelp)
    }

    @Test
    fun `Get help message for on command`() {
        val output = captureStandardOut { main(arrayOf("help", "on")) }.trim()
        assertThat(output).isEqualTo(attackHelp)
    }
    // CLI TESTS END

    // GAMEPARSER TESTS
    @Test
    fun `Create new trainer`() {
        val output = captureStandardOut { main(arrayOf("new_trainer", "Bob")) }.trim()
        assertThat(output)
                .isEqualTo(
                        captureStandardOut { GameManager(GameLoader()).createTrainer("Bob") }.trim()
                )
    }

    @Test
    fun `Create new trainer with no arguments`() {
        val output = captureStandardOut { main(arrayOf("new_trainer")) }.trim()
        assertThat(output).isEqualTo(newTrainerHelp)
    }

    @Test
    fun `Add new monster`() {
        val output =
                captureStandardOut {
                            main(arrayOf("add_monster", "Bob", "100", "20", "10", "5", "20"))
                        }
                        .trim()
        assertThat(output)
                .isEqualTo(
                        captureStandardOut {
                                    GameManager(GameLoader()).addMonster("Bob", 100, 20, 10, 5, 20)
                                }
                                .trim()
                )
    }

    @Test
    fun `Add new monster with no arguments`() {
        val output = captureStandardOut { main(arrayOf("add_monster")) }.trim()
        assertThat(output).isEqualTo(addMonsterHelp)
    }

    @Test
    fun `Add new monster with not enough or to many arguments`() {
        val output1 =
                captureStandardOut {
                            main(
                                    arrayOf(
                                            "add_monster",
                                            "Bob",
                                            "100",
                                            "20",
                                            "10",
                                            "5",
                                            "20",
                                            "too many args"
                                    )
                            )
                        }
                        .trim()
        val output2 =
                captureStandardOut { main(arrayOf("add_monster", "Bob", "100", "20", "10")) }.trim()
        assertThat(output1).isEqualTo(addMonsterHelp)
        assertThat(output2).isEqualTo(addMonsterHelp)
    }

    @Test
    fun `Start new battle`() {
        val output = captureStandardOut { main(arrayOf("new_battle", "Bob", "Lisa")) }.trim()
        assertThat(output)
                .isEqualTo(
                        captureStandardOut {
                                    GameManager(GameLoader()).initiateBattle("Bob", "Lisa")
                                }
                                .trim()
                )
    }

    @Test
    fun `Start new battle with no arguments`() {
        val output = captureStandardOut { main(arrayOf("new_battle")) }.trim()
        assertThat(output).isEqualTo(newBattleHelp)
    }

    @Test
    fun `Start new battle with not enough or to many arguments`() {
        val output1 =
                captureStandardOut { main(arrayOf("new_battle", "Bob", "Lisa", "to many args")) }
                        .trim()
        val output2 = captureStandardOut { main(arrayOf("new_battle", "Bob")) }.trim()
        assertThat(output1).isEqualTo(newBattleHelp)
        assertThat(output2).isEqualTo(newBattleHelp)
    }

    @Test
    fun `View battle status`() {
        val output = captureStandardOut { main(arrayOf("view_battle", "0")) }.trim()
        assertThat(output)
                .isEqualTo(captureStandardOut { GameManager(GameLoader()).viewStatus() }.trim())
    }

    @Test
    fun `View battle with no arguments`() {
        val output = captureStandardOut { main(arrayOf("view_battle")) }.trim()
        assertThat(output).isEqualTo(viewBattleHelp)
    }

    @Test
    fun `Attack enemy`() {
        val output = captureStandardOut { main(arrayOf("on", "0", "Bob", "Tackle")) }.trim()
        assertThat(output)
                .isEqualTo(
                        captureStandardOut {
                                    GameManager(GameLoader()).performAttack(0, "Bob", "Tackle")
                                }
                                .trim()
                )
    }

    @Test
    fun `Attack enemy with no arguments`() {
        val output = captureStandardOut { main(arrayOf("on")) }.trim()
        assertThat(output).isEqualTo(attackHelp)
    }

    @Test
    fun `Attack enemy with not enough or to many arguments`() {
        val output1 =
                captureStandardOut { main(arrayOf("on", "0", "Lisa", "Tackle", "to many args")) }
                        .trim()
        val output2 = captureStandardOut { main(arrayOf("on", "0", "Lisa")) }.trim()
        assertThat(output1).isEqualTo(attackHelp)
        assertThat(output2).isEqualTo(attackHelp)
    }
    // GAMEPARSER TESTS END
}
