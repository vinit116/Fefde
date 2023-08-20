package club.potli.otpapi

interface OTPSenderCallback {
    fun onOtpSent(referenceId: String)
}