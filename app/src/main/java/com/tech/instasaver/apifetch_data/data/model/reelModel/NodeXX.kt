package com.tech.instasaver.apifetch_data.data.model.reelModel

data class NodeXX(
    val created_at: Int,
    val did_report_as_spam: Boolean,
    val edge_threaded_comments: EdgeThreadedComments,
    val id: String,
    val is_restricted_pending: Boolean,
    val text: String,
    val viewer_has_liked: Boolean
)