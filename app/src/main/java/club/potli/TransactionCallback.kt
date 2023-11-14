package club.potli

interface TransactionCallback {
    fun onTransactionAmountUpdated(amount: Double, imageId: Int)
}