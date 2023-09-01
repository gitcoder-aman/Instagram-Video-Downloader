package com.tech.instasaver.apifetch_data.data.model.reelModel

data class OwnerXXX(
    val blocked_by_viewer: Boolean,
    val edge_owner_to_timeline_media: EdgeOwnerToTimelineMediaX,
    val followed_by_viewer: Boolean,
    val full_name: String,
    val has_blocked_viewer: Boolean,
    val id: String,
    val is_embeds_disabled: Boolean,
    val is_private: Boolean,
    val is_unpublished: Boolean,
    val is_verified: Boolean,
    val pass_tiering_recommendation: Boolean,
    val profile_pic_url: String,
    val requested_by_viewer: Boolean,
    val username: String
)