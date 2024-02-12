package kr.co.lion.android01_first_test

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.android01_first_test.databinding.ActivityMainBinding
import kr.co.lion.android01_first_test.databinding.RowMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    // InputMemoActivity 의 런처
    lateinit var inputMemoActivityLauncher: ActivityResultLauncher<Intent>

    // ShowMemoActivity 의 런처
    lateinit var showMemoActivityLauncher: ActivityResultLauncher<Intent>

    // 메모의 내용을 담을 리스트
    val memoList = mutableListOf<MemoData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        initData()
        setToolbar()
        setView()
    }

    // 기본 데이터 및 객체 세팅
    fun initData(){
        // InputMemoActivity 런처
        val contract1 = ActivityResultContracts.StartActivityForResult()
        inputMemoActivityLauncher = registerForActivityResult(contract1){
            if (it.data != null){
                // 메모 객체를 추출한다.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    val memoData = it.data?.getParcelableExtra("memoData", MemoData::class.java)
                    memoList.add(memoData!!)
                    activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                } else {
                    val memoData = it.data?.getParcelableExtra<MemoData> ("memoData")
                    memoList.add(memoData!!)
                    activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                }
            }
        }

        // ShowMemoActivity 런처
        val contract2 = ActivityResultContracts.StartActivityForResult()
        showMemoActivityLauncher = registerForActivityResult(contract2){

        }
    }

    // Toolbar 구성
    fun setToolbar(){
        activityMainBinding.apply {
            toolbarMain.apply {
                // 타이틀
                title = "메모 관리"
                // 메뉴 아이템
                inflateMenu(R.menu.menu_main)
                // 메뉴의 리스너
                setOnMenuItemClickListener {
                    // 메뉴의 id 별로 분기
                    when(it.itemId){
                        // 메모 추가 메뉴 ( + )
                        R.id.menu_item_main_add -> {
                            // InputMemoActivity 를 실행한다.
                            val inputMemoIntent = Intent(this@MainActivity, InputMemoActivity::class.java)
                            inputMemoActivityLauncher.launch(inputMemoIntent)
                        }
                    }
                    true
                }
            }
        }
    }

    // View 구성
    fun setView(){
        activityMainBinding.apply {
            // RecyclerView
            recyclerViewMain.apply {
                // 어댑터 설정
                adapter = RecyclerViewMainAdapter()
                // 레이아웃 매니저
                layoutManager = LinearLayoutManager(this@MainActivity)
                // 데코레이션
                val deco = MaterialDividerItemDecoration(this@MainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }


    // RecyclerView 의 어댑터
    inner class RecyclerViewMainAdapter : RecyclerView.Adapter<RecyclerViewMainAdapter.ViewHolderMain>() {

        // ViewHolder
        inner class ViewHolderMain(rowMainBinding: RowMainBinding) : RecyclerView.ViewHolder(rowMainBinding.root) {
            val rowMainBinding:RowMainBinding

            init {
                this.rowMainBinding = rowMainBinding

                this.rowMainBinding.root.layoutParams = ViewGroup.LayoutParams(
                    // 항목 클릭 시 전체가 클릭 될 수 있도록
                    // 가로 세로 길이를 설정해준다.
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                // 리사이클러뷰 내에 있는 항목을 눌렀을 때 리스너
                this.rowMainBinding.root.setOnClickListener {

                    // ShowMemoActivity 를 실행한다.
                    val showMemoIntent = Intent(this@MainActivity, ShowMemoActivity::class.java)

                    // 선택한 항목 번째의 객체를 Intent 에 담아준다
                    showMemoIntent.putExtra("memoData", memoList[adapterPosition])

                    showMemoActivityLauncher.launch(showMemoIntent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater)
            val viewHolderMain = ViewHolderMain(rowMainBinding)

            return viewHolderMain
        }

        override fun getItemCount(): Int {
            return memoList.size
        }

        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
            holder.rowMainBinding.textViewRowMainTitle.text = memoList[position].title
            holder.rowMainBinding.textViewRowMainDate.text = memoList[position].currentTime
        }
    }
}