package club.potli.data.model

class User{
    var username: String? = null
    var email: String? = null
    var dateOfBirth: String? = null
    var phoneNumber: String? = null
    var monthlyBalance: String? = null
    var potlis: HashMap<String, Potli> = HashMap()
}

class Potli {
    var limit: Double? = 0.0
    var spent: Double? = 0.0
}

