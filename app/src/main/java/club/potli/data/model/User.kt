package club.potli.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class User() : RealmObject{
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var email: String = ""
    var password: String = ""
}