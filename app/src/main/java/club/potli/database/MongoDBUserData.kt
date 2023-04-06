package club.potli.database

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.MongoDatabase
import java.lang.System.getenv

object MyMongoDBClass {
    private const val DATABASE_NAME = "UserData"
    private val CONNECTION_URI = "mongodb+srv://${getenv("MONGO_USERNAME")}:${getenv("MONGO_PASSWORD")}@<cluster-address>/$DATABASE_NAME?retryWrites=true&w=majority"

    @JvmStatic
    fun main(args: Array<String>) {
        val uri = MongoClientURI(CONNECTION_URI)
        val mongoClient = MongoClient(uri)
        val database: MongoDatabase = mongoClient.getDatabase(DATABASE_NAME)
        println("Connected to database successfully.")
        mongoClient.close()
    }
}