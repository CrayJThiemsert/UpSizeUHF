package com.handheld.upsizeuhf.model

import java.sql.Timestamp
import java.time.Instant
import java.time.Instant.*


data class User(
        var uid: String? = "",
        var username: String? = "",
        var email: String? = "",
        var active: Boolean? = false,
        var organization: String? = "",
        var sentNotification: Boolean? = false,
        var role: String? = "",
        var createdWhen: Timestamp? = Timestamp(System.currentTimeMillis()),
        var createdWhenTimestamp: Timestamp? = Timestamp(System.currentTimeMillis()),
        var createdBy: String?,
        var updatedWhen: Timestamp? = Timestamp(System.currentTimeMillis()),
        var updatedWhenTimestamp: Timestamp? = Timestamp(System.currentTimeMillis()),
        var updatedBy: String?
)
{

    constructor() :
        this(uid = "", username="", email="", sentNotification=false,
            active=false, role="",
            organization="",
            createdWhen=Timestamp(System.currentTimeMillis()), createdBy="", createdWhenTimestamp = Timestamp(System.currentTimeMillis()),
            updatedWhen=Timestamp(System.currentTimeMillis()), updatedBy="", updatedWhenTimestamp = Timestamp(System.currentTimeMillis()))

    constructor(username: String?) :
         this(uid = "", username=username, email="", sentNotification=false,
             active=false, role="",
             organization="",
             createdWhen=Timestamp(System.currentTimeMillis()), createdBy="", createdWhenTimestamp = Timestamp(System.currentTimeMillis()),
             updatedWhen=Timestamp(System.currentTimeMillis()), updatedBy="", updatedWhenTimestamp = Timestamp(System.currentTimeMillis()))

    constructor(username: String?, email: String?) :
            this(uid = "", username=username, email=email, sentNotification=false,
                active=false, role="",
                organization="",
                createdWhen=Timestamp(System.currentTimeMillis()), createdBy="", createdWhenTimestamp = Timestamp(System.currentTimeMillis()),
                updatedWhen=Timestamp(System.currentTimeMillis()), updatedBy="", updatedWhenTimestamp = Timestamp(System.currentTimeMillis()))
}