package com.tech.instasaver.apifetch_data.data.model.reelModel

data class Node(

    val is_video : Boolean,
    val video_url : String = "",
    val display_resources : List<DisplayResource> = emptyList()
)