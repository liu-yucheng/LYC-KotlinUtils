package lyc.ktutils.libs.gputils

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/** Time formats. */
class TimeFormats private constructor() {
    companion object {
        /** Local date time to timestamp string.
         *
         * The timestamp format is like: 20220102-030405-678901.
         *
         * @param localDateTime: a local date time
         * @return result: the result*/
        fun timestampStringOf(localDateTime: LocalDateTime): String {
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSSSSS")
            val result = localDateTime.format(formatter)
            return result
        } // end fun

        /** Duration to duration string.
         *
         * The duration format is like: 2 days, 3:4:5.678 (days, hours: minutes: seconds. milliseconds).
         *
         * @param duration: a duration
         * @return result: the result
         */
        fun durationStringOf(duration: Duration): String {
            val days = duration.toDaysPart()
            val hours = duration.toHoursPart()
            val minutes = duration.toMinutesPart()
            val seconds = duration.toSecondsPart()
            val milliseconds = duration.toMillisPart()
            val units = "(days, hours: minutes: seconds. milliseconds)"
            val result = "$days days, $hours:$minutes:$seconds.$milliseconds $units"
            return result
        } // end fun
    } // end companion
} // end class
