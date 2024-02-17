package com.wikicoding.composespeechtotext

data class VoiceToTextParserState(
    var spokenText: String = "",
    val isSpeaking: Boolean = false,
    val error: String? = null
)
