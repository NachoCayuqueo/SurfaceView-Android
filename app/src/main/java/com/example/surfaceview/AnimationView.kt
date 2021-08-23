package com.example.surfaceview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.math.abs

class AnimationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {

    private lateinit var myPaint: Paint
    private var myCanvas: Canvas? = null

    private var canDraw: Boolean = true

    private var posX1: Float = 10f
    private var posY1: Float = 10f
    private var posX2: Float = 10f
    private var posY2: Float = 10f

    private var xPosCircle: Float = 50f
    private var yPosCircle: Float = 50f
    private var directionCircle: String = ""

    private val SPEED = 50F //velocidad de movimiento del circulo
    private val BLOCKSIZE = 100F

    private lateinit var metrics: DisplayMetrics
    private var SCREEN_WIDTH: Int = 0
    private var SCREEN_HEIGTH: Int = 0
    private var SCREEN_H: Int = 1750

    private lateinit var squareArray:List<Array<Float>>

    init {
        holder.addCallback(this)
        metrics = resources.displayMetrics
        SCREEN_WIDTH = metrics.widthPixels
        SCREEN_HEIGTH = metrics.heightPixels
    }

    /***3 METODOS PARA EL CICLO DE VIDA DE SURFACEHOLDER**/
    override fun surfaceCreated(holder: SurfaceHolder) {
        myPaint = Paint()

        Thread{
            while (canDraw){
                myCanvas = holder.lockCanvas()
                if(myCanvas != null){
                    myCanvas?.drawColor(Color.BLACK) //background color

                    drawSquare()
                    moveCircle()

                    holder.unlockCanvasAndPost(myCanvas)
                }
            }
        }.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        println("SurfaceChanged")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        println("SurfaceDestroyed")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

       when(event!!.action){
           MotionEvent.ACTION_DOWN -> touchStart(event)
           MotionEvent.ACTION_UP -> touchUP(event)
       }
        return true
    }
    private fun touchStart(event: MotionEvent) {
        posX1 = event.x
        posY1 = event.y
    }

    private fun touchUP(event: MotionEvent) {
        posX2 = event.x
        posY2 = event.y
        calculateDisplacementDirection()
    }

    /**SE CALCULA LA DIRECCION EN LA QUE SE MOVERA PACMAN**/
    private fun calculateDisplacementDirection() {
        val diffX = posX2 - posX1
        val diffY = posY2 - posY1
        var direction = ""

        if(abs(diffX) > abs(diffY)){ //me muevo en x
            if(diffX > 0)
                direction = "Rigth"
            else
                direction = "Left"
        }else{ //me muevo en y
            if(diffY > 0)
                direction = "Bottom"
            else
                direction = "Up"
        }
        directionCircle = direction
    }

    private fun moveCircle(){
        when(directionCircle){
            "Rigth" -> xPosCircle += SPEED / 10
            "Left" -> xPosCircle += -SPEED / 10
            "Bottom" -> yPosCircle += SPEED / 10
            "Up" -> yPosCircle -= SPEED / 10
            "Collision" -> xPosCircle *= 1
        }

        collisionLeftAndRigth()
        collisionUpAndBottom()
        collisionWithObjects()

        drawCircle()
    }

    private fun collisionLeftAndRigth(){
        val SCREENW: Int = myCanvas?.width!! //largo de pantalla
        //si traspaso izq, aparezco en derecha
        if(xPosCircle < 0)
            xPosCircle = SCREENW.toFloat() - 1
        //si traspaso der, aparezco en izq
        if(xPosCircle >= SCREENW)
            xPosCircle = 0F
    }
    //TODO: revisar el alto...
    private fun collisionUpAndBottom(){
        val screenH: Int = myCanvas?.height!! //largo de pantalla
        if (yPosCircle <= 0)
            directionCircle = "Collision"//yPosCircle = screenH.toFloat() - 1
        if(yPosCircle >= SCREEN_H)
            directionCircle = "Collision"//yPosCircle = 0F
    }

    private fun collisionWithObjects(){

        val index = (yPosCircle / 100).toInt()
        val posX = (xPosCircle / 100).toInt()

        if(index <= 3){
            if(index != 2) {
                if (existPosition(index, posX)) {
                    directionCircle = "Collision"
                }
            }
        }
    }

    private fun existPosition(index:Int,pos: Int): Boolean {
        var isPositionValid = false
        var i = 0
        val array = squareArray[index]

        while ((i<array.size) && (!isPositionValid)){
            if(pos.toFloat() == array[i]){
                isPositionValid = true
            }
            i += 1
        }

        return isPositionValid
    }

    /**Se dibuja el circulo (posición inicial, color y radio)**/
    private fun drawCircle(){
        myPaint.color = Color.RED
        myCanvas?.drawCircle(xPosCircle,yPosCircle,20F,myPaint)
    }

    /**Se dibujan todos los cuadrados que estaran en el tablero**/
    private fun drawSquare(){
        squareCoordinates()

        myPaint.color = Color.BLUE
        var left:Float
        var rigth:Float
        var top:Float
        var bottom:Float

        for (i in 0..3){
            if(i != 2) {
                top = i.toFloat()
                bottom = top + 1
                val internalArray = squareArray[i]
                for (j in internalArray.indices) {
                    rigth = internalArray[j]
                    left = rigth + 1
                    myCanvas?.drawRect(left*BLOCKSIZE, top*BLOCKSIZE, rigth*BLOCKSIZE, bottom*BLOCKSIZE, myPaint)
                }
            }
        }
    }

    /**Arreglo donde se guandan la direccion Izquierda de los cuadrados**/
    private fun squareCoordinates(){
        squareArray = listOf(
            arrayOf(5f),
            arrayOf(1f,3f,5f,7f,9f),
            arrayOf(0f),
            arrayOf(1f,2f,3f,5f,7f,8f,9f)
        )
    }

}