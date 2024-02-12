package kr.co.lion.homework_memoapplication

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kr.co.lion.homework_memoapplication.databinding.ActivityEditBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditActivity : AppCompatActivity() {
    lateinit var activityEditBinding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityEditBinding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(activityEditBinding.root)

        setToolbar()
        setView()
    }

    // 툴바 설정
    fun setToolbar() {
        activityEditBinding.apply {
            toolbarEdit.apply {
                // 타이틀
                title = "메모 수정"
                // back
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                // 메뉴
                inflateMenu(R.menu.menu_edit)
                setOnMenuItemClickListener {
                    // 확인 메뉴
                    when (it.itemId) {
                        R.id.menu_edit_done -> {
                            editData()
                        }
                    }

                    // 이벤트 완료 처리
                    true
                }
            }
        }

    }

    // 뷰 설정
    fun setView() {
        activityEditBinding.apply {
            // 항목 순서값을 가져온다.
            val position = intent.getIntExtra("position", 0)
            // 포지션 번째 객체를 추출한다.
            val memoData = Memo.memoList[position]

            textFieldEditMemoTitle.setText("${memoData.title}")
            textViewEditMemoTime.setText("${memoData.time}")
            textFieldEditMemoContent.setText("${memoData.content}")
        }
    }

    // 수정 처리
    fun editData() {
        activityEditBinding.apply {
            // 항목 순서값을 가져온다.
            val position = intent.getIntExtra("position", 0)

            // 사용자가 수정한 값을 가져온다.
            val editedMemoTitle = textFieldEditMemoTitle.text.toString()
            val editedMemoContent = textFieldEditMemoContent.text.toString()

            // 수정시 현재시각
            val currentDateTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)
            textViewEditMemoTime.text = "$dateFormat"
            val editedMemoTime = textViewEditMemoTime.text.toString()

            // 입력 검사
            if (editedMemoTitle.isEmpty()) {
                showDialog("제목 입력 오류", "제목을 입력해주세요")
                return
            }
            if (editedMemoContent.isEmpty()) {
                showDialog("내용 입력 오류", "내용을 입력해주세요")
                return
            }

            // 변경된 항목 리스트에 담기
            val editedMemo = MemoData(editedMemoTitle, editedMemoTime, editedMemoContent)

            // 리스트 수정
            Memo.memoList.set(position, editedMemo)

            // 돌아갈때 사용할 Intent 객체 생성
            val editIntent = Intent(this@EditActivity, ShowActivity::class.java)
            // 변경된 항목 전달
            editIntent.putExtra("editedData", editedMemo)

            // 작업을 성공적으로 완료했을 경우 editInt 동작
            setResult(RESULT_OK, editIntent)
            finish()
        }
    }

    // 다이얼로그를 보여주는 메서드
    fun showDialog(title: String, message: String) {
        // 다이얼로그 설정
        val builder = MaterialAlertDialogBuilder(this@EditActivity).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->

            }
        }
        // 다이얼로그를 보여준다
        builder.show()
    }
}
