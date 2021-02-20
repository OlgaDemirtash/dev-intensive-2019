package ru.skillbranch.devintensive

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.models.Bender


class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var benderImage: ImageView
    lateinit var textTxt: TextView
    lateinit var messageEt: EditText
    lateinit var sendBtn: ImageView
    lateinit var benderObj: Bender


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send

        messageEt.setText(savedInstanceState?.getString("MESSAGEET") ?: "")
        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name
        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))

        val (r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
        textTxt.setText(benderObj.askQuestion())
        Log.d("M_MainActivity", "On Create$status $question")
        sendBtn.setOnClickListener(this)





        messageEt.setOnEditorActionListener { v, actionId, event ->
            Log.d("M_MainActivity", "setOnEditorActionListener")
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                val (phrase, color) = benderObj.listenAnswear(messageEt.text.toString().toLowerCase())
                messageEt.setText("")
                val (r, g, b) = color
                benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
                textTxt.text = phrase
                hideKeyboard()

                true
            } else {
                false
            }
        }


    }


    override fun onRestart() {
        super.onRestart()
        Log.d("M_MainActivity", "On Restart")

    }


    override fun onStart() {
        super.onStart()
        Log.d("M_MainActivity", "On Start")


    }


    override fun onResume() {
        super.onResume()
        Log.d("M_MainActivity", "On Resume")

    }

    override fun onPause() {
        super.onPause()
        Log.d("M_MainActivity", "On Pause")


    }

    override fun onStop() {
        super.onStop()
        Log.d("M_MainActivity", "On Stop")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("M_MainActivity", "On Destroy")

    }

    override fun onClick(v: View?) {

        if (v?.id == R.id.iv_send) {
            val (phrase, color) = benderObj.listenAnswear(messageEt.text.toString().toLowerCase())
            messageEt.setText("")
            val (r, g, b) = color
            benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
            textTxt.text = phrase
            hideKeyboard()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("STATUS", benderObj.status.name)
        outState.putString("QUESTION", benderObj.question.name)
        outState.putString("MESSAGEET", messageEt.text.toString())
        Log.d("M_MainActivity", "On Save Instance Save: ${benderObj.status.name} ${benderObj.question.name}")
    }


}

// Add these extension functions to an empty kotlin file
fun Activity.getRootView(): View {
    return findViewById<View>(android.R.id.content)
}

fun Context.convertDpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        this.resources.displayMetrics
    )
}

fun Activity.isKeyboardOpen(): Boolean {
    val visibleBounds = Rect()
    this.getRootView().getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = getRootView().height - visibleBounds.height()
    val marginOfError = Math.round(this.convertDpToPx(50F))
    return heightDiff > marginOfError
}

fun Activity.isKeyboardClosed(): Boolean {
    return !this.isKeyboardOpen()
}

fun Activity.hideKeyboard() {
    val view: View = currentFocus ?: View(this)
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}