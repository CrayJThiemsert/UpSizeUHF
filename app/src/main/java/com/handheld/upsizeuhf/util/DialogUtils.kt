package com.handheld.upsizeuhf.util

import android.app.Activity
import com.handheld.upsizeuhf.ui.dialog.ErrorMessageDialogFragment
import com.handheld.upsizeuhf.ui.dialog.SuccessMessageDialogFragment
import com.handheld.upsizeuhf.ui.dialog.WarningMessageDialogFragment


class DialogUtils {
    companion object {
        var warningMessageDialogFragment = WarningMessageDialogFragment()
        var successMessageDialogFragment = SuccessMessageDialogFragment()
        var errorMessageDialogFragment = ErrorMessageDialogFragment()

        fun showWarningDialog(activity: Activity, title: String, messageTop: String, messageBody: String) {
            warningMessageDialogFragment = WarningMessageDialogFragment.newInstance(title, messageTop, messageBody)
            warningMessageDialogFragment.show(activity.fragmentManager, "warning_fragment")
        }

        fun showSuccessDialog(activity: Activity, title: String, messageTop: String, messageBody: String) {
            successMessageDialogFragment = SuccessMessageDialogFragment.newInstance(title, messageTop, messageBody)
            successMessageDialogFragment.show(activity.fragmentManager, "success_fragment")
        }

        fun showErrorDialog(activity: Activity, title: String, messageTop: String, messageBody: String) {
            errorMessageDialogFragment = ErrorMessageDialogFragment.newInstance(title, messageTop, messageBody)
            errorMessageDialogFragment.show(activity.fragmentManager, "error_fragment")
        }
    }
}