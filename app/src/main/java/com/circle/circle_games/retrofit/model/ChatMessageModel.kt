package com.circle.circle_games.retrofit.model

class ChatMessageModel {
    var dateTime: String? = ""
        private set
    var message: String? = ""
        private set
    var userName: String? = ""
        private set
    var sentByMe: Boolean? = false
        private set

    constructor() {
        // Dibutuhkan oleh Firebase
    }

    constructor(dateTime: String?,message: String?, userName: String?, sentByMe: Boolean?) {
        this.dateTime = dateTime
        this.message = message
        this.userName = userName
        this.sentByMe = sentByMe
    }
}