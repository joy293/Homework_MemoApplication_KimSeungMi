package kr.co.lion.homework_memoapplication

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kr.co.lion.homework_memoapplication.databinding.ActivityInputBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class InputActivity : AppCompatActivity() {

    lateinit var activityInputBinding: ActivityInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        activityInputBinding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(activityInputBinding.root)

        setToolbar()
    }

    // 툴바 설정
    fun setToolbar() {
        activityInputBinding.apply {
            toolbarInput.apply {
                // 타이틀
                title = "메모 작성"
                // back
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                // 메뉴
                inflateMenu(R.menu.menu_input)
                setOnMenuItemClickListener {
                    // 확인 메뉴
                    when (it.itemId) {
                        R.id.menu_input_done -> {
                            processInputDone()
                        }

                        else -> {

                        }
                    }

                    // 이벤트 완료 처리
                    true
                }
            }
        }

    }

    // 입력 완료 처리
    fun processInputDone() {
        activityInputBinding.apply {
            // 현재시각을 가져온다.
            val currentDateTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)
            textViewInputMemoTime.text = "작성시각 ${dateFormat}"

            // 사용자가 입력한 내용 및 작성시각을 가져온다.
            val memoTitle = textFieldInputMemoTitle.text.toString()
            val memoContent = textFieldInputMemoContent.text.toString()
            val memoTime = textViewInputMemoTime.text.toString()

            // 입력 검사
            if (memoTitle.isEmpty()) {
                showDialog("제목 입력 오류", "제목을 입력해주세요")
                return
            }
            if (memoContent.isEmpty()) {
                showDialog("내용 입력 오류", "내용을 입력해주세요")
                return
            }

            // 입력받은 정보를 객체에 담아준다.
            val inputMemo = MemoData(memoTitle, memoTime, memoContent)
            Memo.memoList.add(inputMemo)

            // 돌아갈때 사용할 Intent 객체생성
            val resultIntent = Intent()

            // 생성한 Intent 객체에 데이터 넣기
            resultIntent.putExtra("memoData", inputMemo)

            // 작업을 성공적으로 완료했을 경우 resultIntent를 동작시키고
            setResult(RESULT_OK, resultIntent)

            // 현재 Activity를 종료한다
            finish()
        }
    }


    // 다이얼로그를 보여주는 메서드
    fun showDialog(title: String, message: String) {
        // 다이얼로그 설정
        val builder = MaterialAlertDialogBuilder(this@InputActivity).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->

            }
        }
        // 다이얼로그를 보여준다
        builder.show()
    }
}
