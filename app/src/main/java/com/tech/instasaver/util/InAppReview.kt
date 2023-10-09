package com.tech.instasaver.util

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.tech.instasaver.MainActivity

class InAppReview {

    fun askUserForReview(activity: Activity) {
        var reviewManager: ReviewManager = ReviewManagerFactory.create(activity)
        val request: Task<ReviewInfo> = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            try {
                if (task.isSuccessful) {
                    Log.d("task@@", "askUserForReview: isSuccessful${task.result}")
                    val reviewInfo = task.result
                    val reviewFlow = activity.let { reviewManager.launchReviewFlow(it, reviewInfo) }
                    reviewFlow.addOnCompleteListener { task1 ->

                    }.addOnFailureListener { error1 ->
                        Toast.makeText(activity, error1.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity, "Something went wrong.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.addOnFailureListener { error ->
            Toast.makeText(activity, error.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}