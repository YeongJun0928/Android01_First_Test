package kr.co.lion.android01_first_test

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kr.co.lion.android01_first_test.databinding.ActivityShowMemoBinding

class ShowMemoActivity : AppCompatActivity() {

    lateinit var activityShowMemoBinding: ActivityShowMemoBinding

    // FixMemoActivity 의 런처
    lateinit var fixMemoActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityShowMemoBinding = ActivityShowMemoBinding.inflate(layoutInflater)
        setContentView(activityShowMemoBinding.root)

        initData()
        setToolbar()
        setView()
    }

    // 기본 데이터 및 객체 세팅
    fun initData(){
        // FixMemoActivity 런처
        val contract1 = ActivityResultContracts.StartActivityForResult()
        fixMemoActivityLauncher = registerForActivityResult(contract1){

        }
    }

    // Toolbar 설정
    fun setToolbar(){
        activityShowMemoBinding.apply {
            toolbarShowMemo.apply {
                // 타이틀
                title = "메모 보기"
                // 뒤로가기
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    // 현재 Activity 를 종료한다
                    finish()
                }
                // 메뉴 아이템
                inflateMenu(R.menu.menu_show)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        // 삭제 아이콘 클릭 시
                        R.id.menu_item_show_delete -> {


                            finish()
                        }
                        // 수정 아이콘 클릭 시
                        R.id.menu_item_show_edit -> {
                            // FixMemoActivity 실행
                            val fixMemoIntent = Intent(this@ShowMemoActivity, FixMemoActivity::class.java)



                            fixMemoActivityLauncher.launch(fixMemoIntent)
                        }
                    }
                    true
                }
            }
        }
    }

    // View 설정
    fun setView(){
        // memoList 안에 있는 data 들 가져오기
        activityShowMemoBinding.apply {

            // 메모 내용 가져오기
            val memoData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                intent.getParcelableExtra<MemoData>("memoData", MemoData::class.java)
            } else {
                intent.getParcelableExtra<MemoData>("memoData")
            }

            // 해당 EditText 부분을 사용할 수 없도록 activity_show_memo.xml 파일에서
            // focusableInTouchMode = false 로 설정
            textViewShowTitle.apply {
                // String 타입으로 변환한 뒤 memoData 에 값 넣기
                text.toString()
                append("${memoData?.title}")
            }
            textViewShowDate.apply {
                text.toString()
                append("${memoData?.currentTime}")
            }
            textViewShowContents.apply {
                text.toString()
                append("${memoData?.contents}")
            }
        }
    }
}