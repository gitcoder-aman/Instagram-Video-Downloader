package com.tech.instasaver.util

import android.app.Activity
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

class InAppReview {
    fun askUserForReview(activity : Activity){
        val reviewManager : ReviewManager = ReviewManagerFactory.create(activity)
        val request : Task<ReviewInfo> = reviewManager.requestReviewFlow()
        request.addOnCompleteListener {task->
           try {
               if(task.isSuccessful){
                   val reviewInfo = task.result
                   val reviewFlow = reviewManager.launchReviewFlow(activity,reviewInfo)
                   reviewFlow.addOnCompleteListener { task1->

                   }.addOnFailureListener {error1->
                       Toast.makeText(activity, error1.localizedMessage, Toast.LENGTH_SHORT).show()
                   }
               }else{
                   Toast.makeText(activity, "Something went wrong.", Toast.LENGTH_SHORT).show()
               }
           }catch (e : Exception){
               e.printStackTrace()
           }
        }.addOnFailureListener {error->
            Toast.makeText(activity, error.localizedMessage, Toast.LENGTH_SHORT).show()
        }

    }
}