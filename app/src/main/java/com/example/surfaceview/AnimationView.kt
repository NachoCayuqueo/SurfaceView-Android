package com.example.surfaceview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
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

    private var xPosCircle: Float = 0f
    private var yPosCircle: Float = 0f
    private var directionCircle: String = ""

    private val BLOCKSIZE = 50F

    init {
        holder.addCallback(this)

    }

    /***3 METODOS PARA EL CICLO DE VIDA DE SURFACEHOLDER**/
    override fun surfaceCreated(holder: SurfaceHolder) {
        myPaint = Paint()

        Thread{
            while (canDraw){
                myCanvas = holder.lockCanvas()
                if(myCanvas != null){
                    myCanvas?.drawColor(Color.BLACK)

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
            "Rigth" -> xPosCircle += BLOCKSIZE / 10
            "Left" -> xPosCircle += -BLOCKSIZE / 10
            "Bottom" -> yPosCircle += BLOCKSIZE / 10
            "Up" -> yPosCircle += -BLOCKSIZE / 10
        }
        //si traspaso izq, aparezco en derecha
        if(xPosCircle < 0)
            xPosCircle = BLOCKSIZE*20
        //si traspaso der, aparezco en izq
        if(xPosCircle >= BLOCKSIZE*22)
            xPosCircle = 0F

        drawCircle()
    }

    private fun drawCircle(){
        myPaint.color = Color.RED
        myCanvas?.drawCircle(xPosCircle,yPosCircle,20F,myPaint)
    }

    private fun drawSquare(){
        val screenW: Int = myCanvas?.width!! //ancho de pantalla
        val screenH: Int = myCanvas?.height!! //largo de pantalla

        myPaint.color = Color.WHITE
        val left = 300F
        val rigth = 400F
        val top = 300F
        val bottom = 400F

        myCanvas?.drawRect(left,top,rigth,bottom,myPaint)
        myCanvas?.drawRect(left,top+200,rigth,bottom+200,myPaint)
        myCanvas?.drawRect(left,top+400,rigth,bottom+400,myPaint)
        myCanvas?.drawRect(left,top+600,rigth,bottom+600,myPaint)

        myCanvas?.drawRect(left+400,top,rigth+400,bottom,myPaint)
        myCanvas?.drawRect(left+400,top+200,rigth+400,bottom+200,myPaint)
        myCanvas?.drawRect(left+400,top+400,rigth+400,bottom+400,myPaint)
        myCanvas?.drawRect(left+400,top+600,rigth+400,bottom+600,myPaint)
    }

}