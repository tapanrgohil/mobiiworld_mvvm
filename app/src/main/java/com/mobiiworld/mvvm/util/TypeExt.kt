package com.mobiiworld.mvvm.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExperimentalContracts
fun Any?.isListAndEmpty(): Boolean {
    contract {
        returns(true) implies (this@isListAndEmpty is List<*>)
    }
    return this is List<*> && this.isEmpty()
}