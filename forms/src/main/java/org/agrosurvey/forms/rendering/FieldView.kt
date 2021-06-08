package org.agrosurvey.forms.rendering

import org.agrosurvey.forms.widget.EnumFieldType

interface FieldView {
    fun keepType(myType: EnumFieldType)
    fun setTitle(string: String, isRequired: Boolean)
}