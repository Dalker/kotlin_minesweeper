package minesweeper

class MineFieldModel(val nMines: Int, val nRows: Int = N_ROWS, val nCols: Int = N_COLS) {

    companion object {
        const val N_COLS = 9
        const val N_ROWS = 9
    }

    private val mines: MutableList<Int> = mutableListOf()
    private val marked: MutableList<Int> = mutableListOf()
    private val explored: MutableList<Int> = mutableListOf()

    inner class Point(private val row: Int, private val col: Int) {
        private fun index() = row * nCols + col
        fun isMarked() = index() in marked
        fun isExplored() = index() in explored
        fun hasMine() = index() in mines

        private fun getNeighbours(): List<Point> {
            val rows = (row - 1..row + 1).filter { it in 0 until nRows }
            val cols = (col - 1..col + 1).filter { it in 0 until nCols }
            return rows.map { r ->
                cols.filterNot { r == row && it == col }
                    .map { c -> Point(r, c) }
            }.flatten()
        }

        fun countNearbyMines(): Int = getNeighbours().filter { it.hasMine() }.size

        fun toggleMark() = if (index() in marked) marked.remove(index()) else marked.add(index())

        private fun placeMines() {
            mines += (0 until nRows * nCols).filterNot { it == index() }.shuffled().take(nMines)
        }

        fun explore() {
            if (isExplored()) return // stop condition for recursion
            if (isMarked()) toggleMark()
            if (mines.size == 0) placeMines()
            explored.add(index())
            if (getNeighbours().all { !it.hasMine() })
                getNeighbours().forEach { it.explore() }
        }
    }

    fun foundAllMines() = mines.intersect(marked.toSet()).size == mines.size
    fun exploredAllSafe() = mines.size + explored.size == nRows * nCols
}