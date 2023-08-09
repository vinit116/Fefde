package club.potli.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.text.DateFormat
import java.util.Date

open class User() : RealmObject{
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var owner_id: String = ""
    var email: String = ""
    var password: String = ""
}

