package com.rushadvisualexpressions.sudokuyoutube.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.rushadvisualexpressions.sudokuyoutube.R
import com.rushadvisualexpressions.sudokuyoutube.game.Cell
import com.rushadvisualexpressions.sudokuyoutube.view.custom.SudokuBoardView.OnTouchListener
import com.rushadvisualexpressions.sudokuyoutube.viewmodel.PlaySudokuViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class PlaySudokuActivity : AppCompatActivity(), OnTouchListener {

    private lateinit var viewModel : PlaySudokuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sudokuBoardView.registerListener(this)

        viewModel = ViewModelProviders.of(this).get(PlaySudokuViewModel::class.java)
        viewModel.sudokuGame.selectedCellLiveData.observe( this, Observer { updateSelectedCellUI(it) })
        viewModel.sudokuGame.cellLiveData.observe( this, Observer { updateCells(it) })

        val buttons = listOf( oneButton, twoButton, threeButton, fourButton, fiveButton,
            sixButton, sevenButton, eightButton, nineButton)

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {viewModel.sudokuGame.handleInput(index + 1) }
        }
    }

    private fun updateCells(cells: List<Cell>?) = cells?.let{
        sudokuBoardView.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        sudokuBoardView.updateSelectedCellUI( cell.first, cell.second)
    }

    override fun onCellTouched(row: Int, col: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }
}
