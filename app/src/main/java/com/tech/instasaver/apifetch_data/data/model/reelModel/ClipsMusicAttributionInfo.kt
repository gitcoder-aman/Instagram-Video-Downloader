package com.tech.instasaver.apifetch_data.data.model.reelModel

data class ClipsMusicAttributionInfo(
    val artist_name: String,
    val audio_id: String,
    val should_mute_audio: Boolean,
    val should_mute_audio_reason: String,
    val song_name: String,
    val uses_original_audio: Boolean
)