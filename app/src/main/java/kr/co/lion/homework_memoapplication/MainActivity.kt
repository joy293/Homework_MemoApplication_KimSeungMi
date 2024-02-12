package kr.co.lion.homework_memoapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import kr.co.lion.homework_memoapplication.databinding.ActivityMainBinding
import kr.co.lion.homework_memoapplication.databinding.RowMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding

    // InputActivity의 런처
    lateinit var inputActivityLauncher: ActivityResultLauncher<Intent>

    // ShowActivity의 런처
    lateinit var showActivityLauncher: ActivityResultLauncher<Intent>

    // EditActivity의 런처는 ShowActivity에 구현


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        initData()
        setView()
        setToolbar()
    }

    // 기본 데이터 설정
    fun initData(){

        // InputActivity의 런처
        val contract1 = ActivityResultContracts.StartActivityForResult()
        inputActivityLauncher = registerForActivityResult(contract1){
            // 작업 결과가 OK 일 때
            if(it.resultCode == RESULT_OK){
                // 전달된 Intent 객체가 있다면
                if(it.data != null){
                    // RecyclerView를 갱신해주고
                    activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                    // 등록 완료 메세지를 띄운다
                    Snackbar.make(activityMainBinding.root, "등록을 완료했습니다.", Snackbar.LENGTH_SHORT).show()

                }
                // 전달된 Intent 객체가 없다면
                else {
                    Toast.makeText(this@MainActivity,"입력한 메모가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // ShowActivity의 런처
        val contract2 = ActivityResultContracts.StartActivityForResult()
        showActivityLauncher = registerForActivityResult(contract2){
            // 작업 결과가 OK 일 때
            if(it.resultCode == RESULT_OK){
                // 전달된 Intent 객체가 있다면
                if(it.data != null){
                    // RecyclerView를 갱신해주고
                    activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                    // 삭제 완료 메시지를 띄운다
                    Snackbar.make(activityMainBinding.root, "삭제를 완료했습니다", Snackbar.LENGTH_SHORT).show()
                }
                // 전달된 Intent 객체가 없다면
                else {
                    Toast.makeText(this@MainActivity, "삭제된 메모가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // View 구성
    fun setView(){
        activityMainBinding.apply {
            // RecyclerView
            recyclerViewMain.apply {
                // 어댑터
                adapter = RecyclerViewMainAdapter()
                // 레이아웃 매니저
                layoutManager = LinearLayoutManager(this@MainActivity)
                // 구분선
                val deco = MaterialDividerItemDecoration(this@MainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    fun setToolbar(){
        activityMainBinding.apply {
            toolbarMain.apply {
                // 타이틀
                title = "메모 관리"
                // 메뉴
                inflateMenu(R.menu.menu_main)
                // 메뉴의 리스너
                setOnMenuItemClickListener {
                    when(it.itemId) {
                        // 추가 메뉴
                        R.id.menu_main_add -> {
                            // InputActivity를 실행한다.
                            val inputIntent = Intent(this@MainActivity, InputActivity::class.java)
                            inputActivityLauncher.launch(inputIntent)
                        }
                    }
                    // 리스너 중복 동작을 막기 위한 처리
                    true
                }
            }
        }
    }

    // RecyclerView 어댑터
    inner class RecyclerViewMainAdapter : RecyclerView.Adapter<RecyclerViewMainAdapter.ViewHolderMain>() {
        // ViewHolder
        inner class ViewHolderMain(rowMainBinding: RowMainBinding) : RecyclerView.ViewHolder(rowMainBinding.root){
            val rowMainBinding:RowMainBinding

            init {
                this.rowMainBinding = rowMainBinding

                // RecyclerView 항목은 사용자가 데이터를 입력함에 따라 계속 추가되기 때문에
                // 레이아웃이 동적으로 조정될 수 있게 설정.
                this.rowMainBinding.root.layoutParams = ViewGroup.LayoutParams(

                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                // RecyclerView 항목을 눌렀을 때의 리스너
                this.rowMainBinding.root.setOnClickListener {
                    // 선택된 항목의 MemoData 객체 가져오기
                    val selectedMemoData = Memo.memoList[adapterPosition]
                    // ShowActivity로 이동할 Intent 생성
                    val showIntent = Intent(this@MainActivity, ShowActivity::class.java)
                    showIntent.putExtra("position", selectedMemoData)
                    // Launcher 실행하여 Intent 전달
                    showActivityLauncher.launch(showIntent)
                }

            }



        }

        // viewHolder 만들어서 어디에 붙일지
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater)
            val viewHolderMain = ViewHolderMain(rowMainBinding)

            return viewHolderMain
        }


        // viewHolder 몇개 만들지
        override fun getItemCount(): Int {
            return Memo.memoList.size
        }

        // 생성된 viewHolder에 데이터 Bind(=holder)
        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {

            // 메모 제목
            holder.rowMainBinding.textViewRowMemoTitle.text = "${Memo.memoList[position].title}"
            // 메모 작성시각
            holder.rowMainBinding.textViewRowMemoTime.text = "${Memo.memoList[position].time}"
        }
    }

    override fun onResume() {
        super.onResume()
        // 다른곳 갔다 왔을 경우 출력 내용을 다시 구성해준다.
        setView()
    }
}