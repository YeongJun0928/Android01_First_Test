package kr.co.lion.android01_first_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.inputmethod.InputMethodManager
import com.google.android.material.textfield.TextInputEditText
import kr.co.lion.android01_first_test.databinding.ActivityInputMemoBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.concurrent.thread

class InputMemoActivity : AppCompatActivity() {

    lateinit var activityInputMemoBinding: ActivityInputMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityInputMemoBinding = ActivityInputMemoBinding.inflate(layoutInflater)
        setContentView(activityInputMemoBinding.root)

        setToolbar()
        setView()
    }

    // Toolbar 설정
    fun setToolbar(){
        activityInputMemoBinding.apply {
            toolbarInput.apply {
                // 타이틀
                title = "메모 작성"
                // 뒤로가기
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    // 현재 Activity 를 종료한다.
                    finish()
                }
                // 메뉴 확인 아이템 ( 체크 아이콘 )
                inflateMenu(R.menu.menu_input)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_item_input_done -> {
                            // 메모 입력 완료 처리 메소드
                            processInputDone()
                        }
                    }
                    true
                }
            }
        }
    }

    // View 설정
    fun setView(){
        activityInputMemoBinding.apply {
            // 원하는 View 에 포커스를 준다
            // InputMemoActivity 가 실행됐을 때 .requestFocus() 부분으로 포커스가 주어진다.
            textFieldInputTitle.requestFocus()
            // 단말기에 있는 키보드를 올려주는 메소드
            // 이때 View 를 지정해주어야한다.
            showSoftInput(textFieldInputTitle)
        }
    }


    // 입력 완료 처리
    fun processInputDone(){

        activityInputMemoBinding.apply {
            // 사용자가 입력한 정보를 가져온다.
            val title = textFieldInputTitle.text.toString()
            val contents = textFieldInputContents.text.toString()

            // 현재 시간을 가져온다. (아래 메소드로 시간 저장)
            val currentTime = getCurrentTime()

            // 입력받은 정보를 객체에 담아준다.
            val memoData = MemoData(title, currentTime, contents)

            // 데이터를 전해주고 이전으로 돌아간다.
            val resultIntent = Intent()
            resultIntent.putExtra("memoData", memoData)

            setResult(RESULT_OK, resultIntent)
            finish()

        }
    }

    // 현재 시간을 구하는 메소드
    fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val dataFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분")
        return dataFormat.format(calendar.time)
    }

    // 포커스를 주고 키보드를 올려주는 메소드
    fun showSoftInput(focusView: TextInputEditText){
        thread {
            SystemClock.sleep(300)
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(focusView, 0)
        }
    }
}