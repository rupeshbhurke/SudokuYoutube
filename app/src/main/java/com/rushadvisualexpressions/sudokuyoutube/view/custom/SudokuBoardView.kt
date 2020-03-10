package com.rushadvisualexpressions.sudokuyoutube.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.rushadvisualexpressions.sudokuyoutube.game.Cell

class SudokuBoardView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet){

    private var cells: List<Cell>? = null
    private var sqrtSize = 3 // Size of group of cells, 3x3
    private var size = 9 // Number of rows & Columns in sudoku grid
    private var cellSizePixels = 0F //Side of an individual cell
    private var selectedRow = 0 // Current selected row
    private var selectedCol = 0 // Current selected column

    private var listener: SudokuBoardView.OnTouchListener? = null

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 6F;
    } // Group border line

    private val thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 2F;
    } // Cell border line

    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#C8C8C8")
    }

    private val conflictingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#DADADA")
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        textSize = 48F
    }

    //
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizeInPixels = Math.min(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(sizeInPixels, sizeInPixels);
    }

    override fun onDraw(canvas: Canvas) {
        cellSizePixels = (width / size).toFloat()
        fillCells(canvas)
        drawLines(canvas)
        drawText(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true;
            }
            else->  false
        }
    }

    private fun handleTouchEvent(x: Float, y: Float) {
        var possibleSelectedRow = (y / cellSizePixels).toInt()
        var possibleSelectedCol = (x / cellSizePixels).toInt()
        listener?.onCellTouched(possibleSelectedRow, possibleSelectedCol)
    }

    private fun fillCells(canvas: Canvas) {
        /*
        if ( selectedRow == -1 || selectedCol == -1) return
        for (r in 0..size) {
            for (c in 0..size) {
                if (r == selectedRow && c == selectedCol) {
                    fillCell(canvas, r, c, selectedCellPaint)
                } else if (r == selectedRow || c == selectedCol) {
                    fillCell(canvas, r, c, conflictingCellPaint)
                } else if (r / sqrtSize == selectedRow / sqrtSize && c / sqrtSize == selectedCol / sqrtSize) {
                    fillCell(canvas, r, c, conflictingCellPaint)
                }
            }
        }
        */
        cells?.forEach {
            val r = it.row
            var c = it.col

            if (r == selectedRow && c == selectedCol) {
                fillCell(canvas, r, c, selectedCellPaint)
            } else if (r == selectedRow || c == selectedCol) {
                fillCell(canvas, r, c, conflictingCellPaint)
            } else if (r / sqrtSize == selectedRow / sqrtSize && c / sqrtSize == selectedCol / sqrtSize) {
                fillCell(canvas, r, c, conflictingCellPaint)
            }
        }
    }

    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint) {
        canvas.drawRect( c * cellSizePixels, r * cellSizePixels, (c + 1) * cellSizePixels, (r+1) * cellSizePixels, paint);
    }

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)
        for (i in 1 until size) {
            val paintToUse = when( i % sqrtSize ) {
                0 -> thickLinePaint
                else -> thinLinePaint
            }
            canvas.drawLine(
                i*cellSizePixels,
                0F,
                i*cellSizePixels,
                height.toFloat(),
                paintToUse)
            canvas.drawLine(
                0F,
                i*cellSizePixels,
                width.toFloat(),
                i*cellSizePixels,
                paintToUse
            );
        }
    }

    private fun drawText(canvas: Canvas) {
        cells?.forEach {
            val row = it.row
            val col = it.col
            val valueString = it.value.toString()
            val textBounds = Rect()
            textPaint.getTextBounds(valueString, 0,valueString.length, textBounds)
            val textWidth = textPaint.measureText(valueString)
            val textHeight = textBounds.height()
            canvas.drawText(valueString, (col * cellSizePixels) +((cellSizePixels - textWidth) / 2),
                (row * cellSizePixels) +((cellSizePixels - textHeight) / 2), textPaint)
        }
    }

    fun updateSelectedCellUI(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        invalidate()
    }

    fun registerListener(listener: SudokuBoardView.OnTouchListener)
    {
        this.listener = listener
    }

    fun updateCells(cells: List<Cell>) {
        this.cells = cells
        invalidate()
    }

    interface OnTouchListener {
        fun onCellTouched(row: Int, col: Int)
    }
}