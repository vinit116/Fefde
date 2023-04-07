package club.potli.data

import club.potli.data.model.LoggedInUser
import com.mongodb.client.MongoClients
import com.mongodb.client.model.Filters.eq
import java.io.IOException

class LoginDataSource {
    private val mongoClient = MongoClients.create("<mongodb-connection-uri>")

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // Search
            val database = mongoClient.getDatabase("<database-name>")
            val usersCollection = database.getCollection("<collection-name>")
            val user = usersCollection.find(eq("username", username)).first()

            // Check if user exists and password is correct
            if (user != null && user["password"] == password) {
                val loggedInUser = LoggedInUser(user["_id"].toString(), user["name"].toString())
                return Result.Success(loggedInUser)
            } else {
                return Result.Error(IOException("Incorrect username or password"))
            }
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // Close the connection to the database
        mongoClient.close()
    }
}
