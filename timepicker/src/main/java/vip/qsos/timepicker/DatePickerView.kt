package vip.qsos.timepicker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.Paint.Style
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.math.abs

/**
 * @author 华清松
 * @doc 类说明：时间滚轮样式视图
 */
class DatePickerView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    /**新增字段 控制是否首尾相接循环显示 默认为循环显示*/
    private var loop = true
    private var mDataList: MutableList<String>? = null
    /**选中的位置，这个位置是mDataList的中心位置，一直不变*/
    private var mCurrentSelected: Int = 0
    private var mPaint: Paint? = null
    private var nPaint: Paint? = null
    private var mMaxTextSize = 80f
    private var mMinTextSize = 40f
    private val mMaxTextAlpha = 255f
    private val mMinTextAlpha = 120f
    private var mViewHeight: Int = 0
    private var mViewWidth: Int = 0
    private var mLastDownY: Float = 0.toFloat()
    /**滑动的距离*/
    private var mMoveLen = 0f
    private var isInit = false
    private var canScroll = true
    private var mSelectListener: OnSelectListener? = null
    private var timer: Timer? = null

    private var mTask: MyTimerTask? = null

    @SuppressLint("HandlerLeak")
    private val updateHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (abs(mMoveLen) < SPEED) {
                mMoveLen = 0f
                if (mTask != null) {
                    mTask!!.cancel()
                    mTask = null
                    performSelect()
                }
            } else {
                // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                mMoveLen -= mMoveLen / Math.abs(mMoveLen) * SPEED
            }
            invalidate()
        }
    }

    override fun onDetachedFromWindow() {
        if (mTask != null) {
            mTask!!.cancel()
            mTask = null
        }
        super.onDetachedFromWindow()
    }

    init {
        init()
    }

    fun setOnSelectListener(listener: OnSelectListener) {
        mSelectListener = listener
    }

    private fun performSelect() {
        if (mSelectListener != null) {
            mSelectListener!!.onSelect(mDataList!![mCurrentSelected])
        }
    }

    fun setData(list: MutableList<String>) {
        mDataList = list
        mCurrentSelected = list.size / 4
        invalidate()
    }

    /**
     * 选择选中的item的index
     */
    fun setSelected(selected: Int) {
        mCurrentSelected = selected
        if (loop) {
            val distance = mDataList!!.size / 2 - mCurrentSelected
            if (distance < 0) {
                for (i in 0 until -distance) {
                    moveHeadToTail()
                    mCurrentSelected--
                }
            } else if (distance > 0) {
                for (i in 0 until distance) {
                    moveTailToHead()
                    mCurrentSelected++
                }
            }
        }
        invalidate()
    }

    /**
     * 选择选中的内容
     */
    fun setSelected(mSelectItem: String) {
        for (i in mDataList!!.indices) {
            if (mDataList!![i] == mSelectItem) {
                setSelected(i)
                break
            }
        }
    }

    private fun moveHeadToTail() {
        if (loop) {
            val head = mDataList!![0]
            mDataList!!.removeAt(0)
            mDataList!!.add(head)
        }
    }

    private fun moveTailToHead() {
        if (loop) {
            val tail = mDataList!![mDataList!!.size - 1]
            mDataList!!.removeAt(mDataList!!.size - 1)
            mDataList!!.add(0, tail)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mViewHeight = measuredHeight
        mViewWidth = measuredWidth
        // 按照View的高度计算字体大小
        mMaxTextSize = mViewHeight / 7f
        mMinTextSize = mMaxTextSize / 2.2f
        isInit = true
        invalidate()
    }

    private fun init() {
        timer = Timer()
        mDataList = ArrayList()
        //第一个paint
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.style = Style.FILL
        mPaint!!.textAlign = Align.CENTER
        mPaint!!.color = Color.BLACK
        //第二个paint
        nPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        nPaint!!.style = Style.FILL
        nPaint!!.textAlign = Align.CENTER
        nPaint!!.color = Color.GRAY
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 根据index绘制view
        if (isInit) {
            drawData(canvas)
        }
    }

    private fun drawData(canvas: Canvas) {
        // 先绘制选中的text再往上往下绘制其余的text
        val scale = parabola(mViewHeight / 4.0f, mMoveLen)
        val size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize
        mPaint!!.textSize = size
        mPaint!!.alpha = ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha).toInt()
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        val x = (mViewWidth / 2.0).toFloat()
        val y = (mViewHeight / 2.0 + mMoveLen).toFloat()
        val fmi = mPaint!!.fontMetricsInt
        val baseline = (y - (fmi.bottom / 2.0 + fmi.top / 2.0)).toFloat()

        canvas.drawText(mDataList!![mCurrentSelected], x, baseline, mPaint!!)
        // 绘制上方data
        run {
            var i = 1
            while (mCurrentSelected - i >= 0) {
                drawOtherText(canvas, i, -1)
                i++
            }
        }
        // 绘制下方data
        var i = 1
        while (mCurrentSelected + i < mDataList!!.size) {
            drawOtherText(canvas, i, 1)
            i++
        }
    }

    /**
     * @param position 距离mCurrentSelected的差值
     * @param type     1表示向下绘制，-1表示向上绘制
     */
    private fun drawOtherText(canvas: Canvas, position: Int, type: Int) {
        val d = MARGIN_ALPHA * mMinTextSize * position.toFloat() + type * mMoveLen
        val scale = parabola(mViewHeight / 4.0f, d)
        val size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize
        nPaint!!.textSize = size
        nPaint!!.alpha = ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha).toInt()
        val y = (mViewHeight / 2.0 + type * d).toFloat()
        val fmi = nPaint!!.fontMetricsInt
        val baseline = (y - (fmi.bottom / 2.0 + fmi.top / 2.0)).toFloat()
        canvas.drawText(mDataList!![mCurrentSelected + type * position],
                (mViewWidth / 2.0).toFloat(), baseline, nPaint!!)
    }

    /**
     * 抛物线
     *
     * @param zero 零点坐标
     * @param x    偏移量
     */
    private fun parabola(zero: Float, x: Float): Float {
        val f = (1 - Math.pow((x / zero).toDouble(), 2.0)).toFloat()
        return if (f < 0F) 0F else f
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> doDown(event)

            MotionEvent.ACTION_MOVE -> {
                mMoveLen += event.y - mLastDownY
                if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
                    if (!loop && mCurrentSelected == 0) {
                        mLastDownY = event.y
                        invalidate()
                        return true
                    }
                    if (!loop) {
                        mCurrentSelected--
                    }
                    // 往下滑超过离开距离
                    moveTailToHead()
                    mMoveLen -= MARGIN_ALPHA * mMinTextSize
                } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
                    if (mCurrentSelected == mDataList!!.size - 1) {
                        mLastDownY = event.y
                        invalidate()
                        return true
                    }
                    if (!loop) {
                        mCurrentSelected++
                    }
                    // 往上滑超过离开距离
                    moveHeadToTail()
                    mMoveLen += MARGIN_ALPHA * mMinTextSize
                }
                mLastDownY = event.y
                invalidate()
            }

            MotionEvent.ACTION_UP -> doUp()
        }
        return true
    }

    private fun doDown(event: MotionEvent) {
        if (mTask != null) {
            mTask!!.cancel()
            mTask = null
        }
        mLastDownY = event.y
    }

    private fun doUp() {

        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0f
            return
        }
        if (mTask != null) {
            mTask!!.cancel()
            mTask = null
        }
        mTask = MyTimerTask(updateHandler)
        timer!!.schedule(mTask, 0, 10)
    }


    interface OnSelectListener {
        fun onSelect(text: String)
    }

    fun setCanScroll(canScroll: Boolean) {
        this.canScroll = canScroll
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        return canScroll && super.dispatchTouchEvent(event)
    }

    /**
     * 控制内容是否首尾相连
     */
    fun setIsLoop(isLoop: Boolean) {
        loop = isLoop
    }

    internal inner class MyTimerTask(var handler: Handler) : TimerTask() {

        override fun run() {
            handler.sendMessage(handler.obtainMessage())
        }
    }

    companion object {
        /**
         * text之间间距和minTextSize之比
         */
        val MARGIN_ALPHA = 2.8f
        /**
         * 自动回滚到中间的速度
         */
        val SPEED = 10f
    }
}