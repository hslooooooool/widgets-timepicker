package vip.qsos.timepicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*

/**
 * @author : 华清松
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        test1.setOnClickListener {
            TimePicker.selectDate(
                context = this,
                limitMin = null, limitMax = null, selected = null,
                showDayTime = true, showSpecificTime = true,
                onDateListener = object : OnDateListener {
                    override fun setDate(type: Int?, date: Date?) {

                    }
                }
            )
        }
    }

}