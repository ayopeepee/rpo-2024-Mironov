package ru.iu3.fclient.utils

interface TransactionEvents {
    fun enterPin(attempts: Int, amount: String): String
    fun transactionResult(result: Boolean)
}