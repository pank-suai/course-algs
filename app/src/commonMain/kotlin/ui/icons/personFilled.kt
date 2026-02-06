package ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val personFilled: ImageVector
    get() {
        if (_personFilled != null) return _personFilled!!
        
        _personFilled = ImageVector.Builder(
            name = "personFilled",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF1f1f1f))
            ) {
                moveTo(367f, 433f)
                quadToRelative(-47f, -47f, -47f, -113f)
                reflectiveQuadToRelative(47f, -113f)
                quadToRelative(47f, -47f, 113f, -47f)
                reflectiveQuadToRelative(113f, 47f)
                quadToRelative(47f, 47f, 47f, 113f)
                reflectiveQuadToRelative(-47f, 113f)
                quadToRelative(-47f, 47f, -113f, 47f)
                reflectiveQuadToRelative(-113f, -47f)
                close()
                moveTo(160f, 800f)
                verticalLineToRelative(-112f)
                quadToRelative(0f, -34f, 17.5f, -62.5f)
                reflectiveQuadTo(224f, 582f)
                quadToRelative(62f, -31f, 126f, -46.5f)
                reflectiveQuadTo(480f, 520f)
                quadToRelative(66f, 0f, 130f, 15.5f)
                reflectiveQuadTo(736f, 582f)
                quadToRelative(29f, 15f, 46.5f, 43.5f)
                reflectiveQuadTo(800f, 688f)
                verticalLineToRelative(112f)
                horizontalLineTo(160f)
                close()
            }
        }.build()
        
        return _personFilled!!
    }

private var _personFilled: ImageVector? = null

