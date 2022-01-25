package minesweeper

class MineFieldView(private val model: MineFieldModel, private val cheating: Boolean = false) {

    companion object {
        const val SAFE = '/'
        const val MINE = 'X'
        const val MARK = '*'
        const val UNKNOWN = '.'
    }

    private fun content(reveal: Boolean = false) = List(model.nRows) { row ->
        MutableList(model.nCols) { col ->
            val point = model.Point(row, col)
            when {
                point.isMarked() && !reveal -> MARK
                point.hasMine() -> if (cheating || reveal) MINE else UNKNOWN
                point.isExplored() || reveal -> {
                    val neighbours = model.Point(row, col).countNearbyMines()
                    if (neighbours == 0) SAFE else neighbours.toString().first()
                }
                else -> UNKNOWN
            }
        }
    }

    fun printField(reveal: Boolean = false) {
        println()
        println(" │123456789│")
        println("—│—————————│")
        for ((nRow, row) in content(reveal).withIndex()) {
            println("${nRow + 1}|" + row.joinToString("") + "|")
        }
        println("—│—————————│")
        println(" │123456789│")
        println()
    }
}
