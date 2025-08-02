package com.example.challangesetfeb

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log

class SmsReceiver : BroadcastReceiver() {

    companion object {
        const val TARGET_SENDER = "+4444555551"
        const val TARGET_MESSAGE = "Congratulations! You've earned 500 points."

        private var onValidSmsReceived: (() -> Unit)? = null

        fun setOnValidSmsReceived(callback: () -> Unit) {
            onValidSmsReceived = callback
        }

        fun clearCallback() {
            onValidSmsReceived = null
        }

        fun triggerConfetti() {
            onValidSmsReceived?.invoke()
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            Log.d("SmsReceiver", "SMS Received")
            val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

            for (smsMessage in smsMessages) {
                val sender = smsMessage.originatingAddress
                val messageBody = smsMessage.messageBody

                Log.d("SmsReceiver", "Sender: $sender, Message: $messageBody")

                if (sender == TARGET_SENDER && messageBody?.trim() == TARGET_MESSAGE) {
                    onValidSmsReceived?.invoke()
                    break
                }
            }
        }
    }
}