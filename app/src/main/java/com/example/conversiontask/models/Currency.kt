package com.example.conversiontask.models

import java.io.Serializable

data class Currency(
    var currency: String,
    var name: String,
    var rate: Double = 0.0
) : Serializable
