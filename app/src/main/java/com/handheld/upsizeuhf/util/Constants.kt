package com.handheld.upsizeuhf.util

import com.handheld.upsizeuhf.model.QueryService

class Constants {
    companion object {
        val SYSTEM_EMAIL_EXTENSION = "@siam-royal-view.com"

        // Bounce
        val BOUNCE_FREQUENCY = 1.0
        val BOUNCE_AMPLITUDE = 0.05

        const val MIME_TEXT_PLAIN = "text/plain"

        val ERROR_DETECTED = "No NFC tag detected!"
        val WRITE_SUCCESS = "Text written to the NFC tag successfully!"
        val WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?"
        val ERROR_SEND_FORGOT_PASSWORD = "Sent forgot password request error!"
        val ERROR_SEND_SIGN_IN = "Sent sign in request error!"
        val ERROR_GOTO_SIGN_UP_SCREEN = "Open sign up screen error!"

        const val  DEFAULT_TRAP_REPLACEMENT = 90
        const val  DEFAULT_TRAP_VISUAL_INSPECTION = 90
        const val  DEFAULT_PALM_VISUAL_INSPECTION = 120

        const val HISTORY_ACTION_STARTED = "started"
        const val HISTORY_ACTION_PHEROMONE_REPLACEMENT = "pheromone replacement"
        const val HISTORY_ACTION_TRAP_VISUAL_INSPECTION = "trap visual inspection"
        const val HISTORY_ACTION_PALM_VISUAL_INSPECTION = "palm visual inspection"
        const val HISTORY_ACTION_EMERGENCY = "emergency"
        const val HISTORY_ACTION_EMERGENCY_CANCELED = "emergency canceled"
        const val HISTORY_ACTION_TREE_STATUS = "tree status"
        const val ACTION_CANCEL = "cancel"
        const val ACTION_SAVE = "save"

        const val CONFIG_PHEROMONE_REPLACEMENT = "pheromone replacement"
        const val CONFIG_ACTION_TRAP_VISUAL_INSPECTION = "trap visual inspection"
        const val CONFIG_ACTION_PALM_VISUAL_INSPECTION = "palm visual inspection"
        const val CONFIG_TRAP_MISSING_LOCATION_DISTANCE = "trap missing location distance"
        const val CONFIG_TAG_MISSING_LOCATION_DISTANCE = "tag missing location distance"
        const val CONFIG_LATEST_TRAP_ID = "latest trap id"
        const val CONFIG_LATEST_TREE_ID = "latest tree id"
        const val CONFIG_SENT_NOTIFY_EVERY_DAYS = "sent notify every days"
        const val CONFIG_SHOW_TEST_TAG = "show test tag"

        const val CONFIG_TREE_STATUS_VERY_GOOD = "tree status very good"
        const val CONFIG_TREE_STATUS_UNUSUAL = "tree status unusual"
        const val CONFIG_TREE_STATUS_KEEP_MONITORING = "tree status keep monitoring"
        const val CONFIG_TREE_STATUS_TREATMENT = "tree status treatment"
        const val CONFIG_TREE_STATUS_EMERGENCY = "tree status emergency"
        const val CONFIG_TREE_STATUS_DEAD = "tree status dead"

        const val CONFIG_LAST_SENT_NOTIFICATION_DATE = "last sent notification date"
        const val CONFIG_SWITCH_CALCULATED_DATE_DIFFERENCE_METHOD = "switch calculated date difference method"

        const val CONFIG_AUTO_ACTIVATED_TAG = "auto activated tag"

        const val METHOD_SECOND = 0.0
        const val METHOD_DAY = 1.0

        const val TREE_STATUS_VERY_GOOD = "Very good"
        const val TREE_STATUS_UNUSUAL = "Unusual"
        const val TREE_STATUS_KEEP_MONITORING = "Keep monitoring"
        const val TREE_STATUS_TREATMENT = "Treatment"
        const val TREE_STATUS_EMERGENCY = "Emergency"
        const val TREE_STATUS_DEAD = "Dead"

        const val ACTION_USER_EDIT = "user edit"

        const val ROLE_SYSTEM_ADMIN = "system admin"
        const val ROLE_ADMIN = "admin"
        const val ROLE_USER = "user"

        // Change to use value from cloud config "trap missing location distance"
        const val LOCATION_DISTANCE_ACCEPTABLE = 5

        // For create csv report file
        val APP_FOLDER = "Insea"
        val CSV_FILE_REPORT = "report"

        //Delimiter used in CSV file
        val COMMA_DELIMITER = ","
        val NEW_LINE_SEPARATOR = "\n"

        const val POTENTIAL_SYMPTOM_OTHER = "Other"

        public val IMAGE_DIRECTORY = "/palmrescue"

        const val COSTUME_All = 1
        const val ACTOR_All = 2
        val COSTUME_All_QUERY : QueryService = QueryService(COSTUME_All, "http://192.168.1.101/costume/costume/list/")
        val ACTOR_All_QUERY : QueryService = QueryService(ACTOR_All, "http://192.168.1.101/costume/costume/actors/")

        fun getCostumeAllQuery() : QueryService {
            return COSTUME_All_QUERY
        }

        fun getActorAllQuery() : QueryService {
            return ACTOR_All_QUERY
        }
    }
}