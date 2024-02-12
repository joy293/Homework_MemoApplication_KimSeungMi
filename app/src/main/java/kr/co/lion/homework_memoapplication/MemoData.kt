package kr.co.lion.homework_memoapplication


import android.os.Parcel
import android.os.Parcelable

// var title: String 입력시 typeMissMatch 오류 남
// String은 null미허용, parcel.readString()은 null허용 하기 때문

class MemoData(var title: String?, var time: String?, var content: String?) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(time)
        parcel.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MemoData> {
        override fun createFromParcel(parcel: Parcel): MemoData {
            return MemoData(parcel)
        }

        override fun newArray(size: Int): Array<MemoData?> {
            return arrayOfNulls(size)
        }
    }
}


// 정적멤버 선언
object Memo {
    var memoList = mutableListOf<MemoData>()
}