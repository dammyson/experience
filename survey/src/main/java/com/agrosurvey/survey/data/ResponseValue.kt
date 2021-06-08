package com.agrosurvey.survey.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.agrosurvey.survey.data.question.option.Option
import java.sql.Time
import java.time.OffsetDateTime
import java.util.*

sealed class ResponseValue {
    class ShortText(val text : String? = null) : ResponseValue()
    class LongText(val text : String? = null) : ResponseValue()
    object None : ResponseValue()
    class SelectBox(val value : Option? = null) : ResponseValue()
    class RadioButton(val value : Option? = null) : ResponseValue()
    class ReferenceDataResponses(val id : Int? = null) : ResponseValue()
    class CheckedBox(val selected : Boolean? = null) : ResponseValue()
    class AutoGeneratedId(val id : String? = null) : ResponseValue()
    class Integer(val value : Int? = null) : ResponseValue()
    class Decimal(val value : Double? = null) : ResponseValue()
    class PhoneNumber(val number : String? = null) : ResponseValue()
    class DateTimePicker(val dateTime : OffsetDateTime? = null) : ResponseValue()
    class DatePicker(val date : Date? = null) : ResponseValue()
    class TimePicker(val time : Time? = null) : ResponseValue()
    class MonthPicker(val month : String? = null) : ResponseValue()
    class DayOfWeekPicker(val day : String? = null) : ResponseValue()
    class Signature(val path : String? = null) : ResponseValue()
    class Image (val path : String? = null): ResponseValue()
    class Audio(val path : String? = null) : ResponseValue()
    class PolyGonPlot(val points : List<LatLng>? = null) : ResponseValue()
    class PathLine(val points : List<LatLng>? = null) : ResponseValue()
    class SinglePoint(val location : LatLng? = null) : ResponseValue()
    class OneDimensionalTable(val number : String? = null) : ResponseValue()
    class TwoDimensionalTable(val number : String? = null) : ResponseValue()
    class BarCode(val number : String? = null) : ResponseValue()
    class QRCode(val number : String? = null) : ResponseValue()
    class PaymentButton(val number : String? = null) : ResponseValue()
    class Fingerprint(val number : String? = null) : ResponseValue()
    class Statement(val number : String? = null) : ResponseValue()
    class PdfFile(val number : String? = null) : ResponseValue()
}