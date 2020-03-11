package com.rushadvisualexpressions.sudokuyoutube.game

import android.arch.lifecycle.MutableLiveData

class SudokuGame {
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellLiveData = MutableLiveData<List<Cell>>()
    private var selectedRow = -1
    private var selectedCol = -1

    private val board: Board

    init {
        val cells = List( 9 * 9) { i-> Cell( i /9, i % 9, i % 9)}

        for (i in 1..10) {
            cells[(Math.random() * 81).toInt()].isStartingCell = true
        }

        board = Board(9, cells)
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellLiveData.postValue(board.cells)
    }

    fun handleInput(number : Int)
    {
        if ( selectedRow == -1 || selectedCol == -1) return
        if ( board.getCell(selectedRow, selectedCol).isStartingCell) return
        board.getCell(selectedRow, selectedCol).value = number
        cellLiveData.postValue(board.cells)
    }

    fun updateSelectedCell( row: Int, col: Int) {
        if ( board.getCell(row, col).isStartingCell) return
        selectedRow = row
        selectedCol = col
        selectedCellLiveData.postValue(Pair(row, col))
    }
}