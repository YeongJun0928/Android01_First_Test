package kr.co.lion.android01_first_test


import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.lion.android01_first_test.databinding.ActivityFixMemoBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class FixMemoActivity : AppCompatActivity() {

    lateinit var activityFixMemoBinding: ActivityFixMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityFixMemoBinding = ActivityFixMemoBinding.inflate(layoutInflater)
        setContentView(activityFixMemoBinding.root)

        setToolbar()
        setData()
    }

    // Toolbar 설정
    fun setToolbar(){
        activityFixMemoBinding.apply {
            toolbarFixMemo.apply {
                // 타이틀
                title = "메모 수정"
                // 뒤로가기
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    // 현재 Activity 종료
                    finish()
                }

                // 메뉴
                inflateMenu(R.menu.menu_fix)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.memo_item_fix_done -> {
                            // 수정 완료 처리
                            processFixDone()
                        }
                    }

                    true
                }
            }
        }
    }

    // 기본 데이터 설정
    fun setData(){
        activityFixMemoBinding.apply {




        }
    }

    // 입력 완료 처리
    fun processFixDone(){

        activityFixMemoBinding.apply {
            // 사용자가 입력한 정보를 가져온다.
            val title = textFieldFixTitle.text.toString()
            val contents = textFieldFixContents.text.toString()
            val currentTime = getCurrentTime()

            // 입력받은 정보를 객체에 담아준다.
            val memoData = MemoData(title, currentTime , contents)

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
}