package kr.co.lion.homework_memoapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import kr.co.lion.homework_memoapplication.databinding.ActivityShowBinding

class ShowActivity : AppCompatActivity() {

    lateinit var activityShowBinding: ActivityShowBinding

    // EditActivity의 런처
    lateinit var editActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        activityShowBinding = ActivityShowBinding.inflate(layoutInflater)
        setContentView(activityShowBinding.root)

        initView()
        setToolbar()
        setLauncher()

    }

    // 런처 설정
    fun setLauncher() {
        val contract3 = ActivityResultContracts.StartActivityForResult()
        editActivityLauncher = registerForActivityResult(contract3) {
            intent.getParcelableExtra<MemoData>("editedData")
            processEditDone()
        }
    }

    // 툴바 설정
    fun setToolbar() {
        activityShowBinding.apply {
            toolbarShow.apply {
                // 타이틀
                title = "메모 정보"
                // back
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                // 메뉴
                inflateMenu(R.menu.menu_show)
                setOnMenuItemClickListener {
                    // 사용자가 선택한 메뉴 항목의 id로 분기한다.
                    when (it.itemId) {
                        // 수정
                        R.id.menu_show_edit -> {
                            // EditActivity로 이동
                            val editIntent = Intent(this@ShowActivity, EditActivity::class.java)
                            editActivityLauncher.launch(editIntent)
                        }
                        // 삭제
                        R.id.menu_show_del -> {
                            processDeletedDone()
                        }
                    }
                    true
                }
            }
        }
    }

    // 뷰 설정
    fun initView() {
        activityShowBinding.apply {
            // 항목 순서값을 가져온다.
            val position = intent.getIntExtra("position", 0)
            // 포지션 번째 객체를 추출한다.
            val selectedData = Memo.memoList[position]
            // 선택된 메모 출력
            textFieldShowMemoTitle.setText("${selectedData.title}")
            textFieldShowMemoTime.setText("${selectedData.time}")
            textFieldShowMemoContent.setText("${selectedData.content}")
        }
    }

    // 수정 완료 처리
    fun processEditDone() {
        // 메모 정보 재설정
        activityShowBinding.apply {
            val position = intent.getIntExtra("position", 0)
            val editedMemo = Memo.memoList[position]

            textFieldShowMemoTitle.setText(editedMemo.title)
            textFieldShowMemoContent.setText(editedMemo.content)

            // 수정 완료 메시지를 띄운다
            Snackbar.make(activityShowBinding.root, "수정을 완료했습니다", Snackbar.LENGTH_SHORT).show()
        }
    }

    // 삭제 완료 처리
    fun processDeletedDone() {
        // 항목 순서 값을 가져온다.
        val position = intent.getIntExtra("position", 0)
        // 항목 번째 객체를 리스트에서 제거한다.
        Memo.memoList.removeAt(position)

        // 업데이트 된 메모 리스트를 저장한다.
        val updatedMemoDataList = ArrayList(Memo.memoList)

        // MainActivity로 돌아갈때 사용할 Intent 객체 생성
        val showIntentForDelete = Intent()
        // 업데이트된 리스트를 Intent에 담아서 전달한다
        showIntentForDelete.putParcelableArrayListExtra("memoData", updatedMemoDataList)

        // 작업을 성공적으로 완료했을 경우 showToMainIntent를 동작시키고
        setResult(RESULT_OK, showIntentForDelete)
        // 현재 Activity를 종료한다
        finish()
    }
}