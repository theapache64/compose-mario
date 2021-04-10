package com.theapache64.composemario.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.sp

val GoogleSans = FontFamily(Font("fonts/Fixedsys500c.ttf", FontWeight.Normal))
val ComposeMarioTypography = Typography(defaultFontFamily = GoogleSans,
    h1 = TextStyle(fontSize = 95.sp),
    h2 = TextStyle(fontSize = 59.sp),
    h3 = TextStyle(fontSize = 48.sp),
    h4 = TextStyle(fontSize = 34.sp),
    h5 = TextStyle(fontSize = 24.sp),
    h6 = TextStyle(fontSize = 20.sp),
    subtitle1 = TextStyle(fontSize = 16.sp),
    subtitle2 = TextStyle(fontSize = 14.sp),
    body1 = TextStyle(fontSize = 18.sp),
    body2 = TextStyle(fontSize = 14.sp),
    button = TextStyle(fontSize = 14.sp),
    caption = TextStyle(fontSize = 12.sp),
    overline = TextStyle(fontSize = 10.sp)
)