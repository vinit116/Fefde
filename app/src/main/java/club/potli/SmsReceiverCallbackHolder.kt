package club.potli

interface SmsReceiverCallback {
    fun onAmountDetected(amount: Double)
}

object SmsReceiverCallbackHolder {
    private var callback: SmsReceiverCallback? = null

    fun setCallback(callback: SmsReceiverCallback) {
        this.callback = callback
    }

    fun notifyAmountDetected(amount: Double) {
        callback?.onAmountDetected(amount)
    }
}
