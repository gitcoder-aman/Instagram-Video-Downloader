package com.tech.instasaver.downloadProcess

import android.os.Environment
import com.tech.instasaver.apifetch_data.data.model.reelModel.InstaModel
import java.io.File

fun makeMediaFile(
    body: InstaModel,
    isReelSelected: Boolean,
    isPhotoSelected: Boolean,
) : String {
    val storageDirectory = if (isReelSelected) {
        val STORAGE_DIRECTORY_FOR_VIDEO = "/Movies/InstaSaver"
        //check file is created or not
        val file = File(Environment.getExternalStorageDirectory().toString() + STORAGE_DIRECTORY_FOR_VIDEO)
        if (!file.exists()) {
            file.mkdirs()
        }
        Environment.getExternalStorageDirectory()
            .toString() + STORAGE_DIRECTORY_FOR_VIDEO + "/${"insta" + body.graphql.shortcode_media.id}" + ".mp4"
    } else {
        var extension = ".jpg"
        var STORAGE_DIRECTORY_FOR_PICTURE_IGTV = "/Pictures/InstaSaver"

        if (body.graphql.shortcode_media.edge_sidecar_to_children?.edges?.isNotEmpty() == true && body.graphql.shortcode_media.edge_sidecar_to_children.edges[0].node.is_video) {
            extension = ".mp4"
            STORAGE_DIRECTORY_FOR_PICTURE_IGTV = "/Movies/InstaSaver"
        }
        //check folder in present or not if not then create a folder
        val file = File(
            Environment.getExternalStorageDirectory()
                .toString() + STORAGE_DIRECTORY_FOR_PICTURE_IGTV
        )
        if (!file.exists()) {
            file.mkdirs()
        }
        Environment.getExternalStorageDirectory()
            .toString() + STORAGE_DIRECTORY_FOR_PICTURE_IGTV + "/${"insta" + body.graphql.shortcode_media.id}" + extension
    }
    return storageDirectory
}