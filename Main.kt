package minesweeper

class MineFieldControl {

    companion object {
        const val START_QUESTION = "How many mines do you want on the field? "
        const val TURN_QUESTION = "Set/unset mine marks or claim a cell as free: "
        const val COMMAND_HELP = "available commands are 'x y free' and 'x y mine/mark'"
        const val GAME_OVER = "You stepped on a mine and failed!"
        const val WON_GAME = "Congratulations! You found all the mines!"
    }

    inner class Game(nMines: Int) {
        private val model = MineFieldModel(nMines)
        private val view = MineFieldView(model, cheating = false)

        /** Return Pair(command, point) containing player's chosen action */
        private fun getPlayerInput(): Pair<String, MineFieldModel.Point> {
            view.printField()
            print(TURN_QUESTION)
            return try {
                val answer = readLine()!!.split(" ")
                Pair(answer[2], model.Point(answer[1].toInt() - 1, answer[0].toInt() - 1))
            } catch (exception: RuntimeException) {
                Pair("help", model.Point(0, 0))
            }
        }

        private fun win() {
            view.printField()
            println(WON_GAME)
        }

        private fun lose() {
            view.printField(reveal = true)
            println(GAME_OVER)
        }

        tailrec fun playTurn() {
            val (command, point) = getPlayerInput()
            if (command == "free" && point.hasMine()) lose() else {
                when (command) {
                    "free" -> point.explore()
                    "mine", "mark" -> point.toggleMark()
                    else -> println(COMMAND_HELP)
                }
                if (model.foundAllMines() || model.exploredAllSafe()) win() else playTurn()
            }
        }
    }

    fun start() {
        print(START_QUESTION)
        Game(readLine()!!.toInt()).playTurn()
    }
}

fun main() = MineFieldControl().start()
