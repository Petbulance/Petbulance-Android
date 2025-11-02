package com.example.presentation.component.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Immutable

@ConsistentCopyVisibility
@Immutable
data class BgColors private constructor(
    val default: Color,
    val subtle: Color,
    val overlay: Color
) {
    internal companion object {
        fun create(
            default: Color,
            subtle: Color,
            overlay: Color
        ): BgColors {
            return BgColors(default, subtle, overlay)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class TextColors private constructor(
    val primary: Color,
    val secondary: Color,
    val inverse: Color,
    val tertiary: Color,
    val disabled: Color,
    val caption: Color
) {
    internal companion object {
        fun create(
            primary: Color,
            secondary: Color,
            inverse: Color,
            tertiary: Color,
            disabled: Color,
            caption: Color
        ): TextColors {
            return TextColors(primary, secondary, inverse, tertiary, disabled, caption)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class ActionStateColors private constructor(
    val default: Color,
    val hover: Color,
    val pressed: Color,
    val disabled: Color,
    val text: Color
) {
    internal companion object {
        fun create(
            default: Color,
            hover: Color,
            pressed: Color,
            disabled: Color,
            text: Color
        ): ActionStateColors {
            return ActionStateColors(default, hover, pressed, disabled, text)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class LinkStateColors private constructor(
    val default: Color,
    val hover: Color,
    val pressed: Color,
    val disabled: Color,
    val visited: Color
) {
    internal companion object {
        fun create(
            default: Color,
            hover: Color,
            pressed: Color,
            disabled: Color,
            visited: Color
        ): LinkStateColors {
            return LinkStateColors(default, hover, pressed, disabled, visited)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class ActionColors private constructor(
    val primary: ActionStateColors,
    val link: LinkStateColors
) {
    internal companion object {
        fun create(
            primary: ActionStateColors,
            link: LinkStateColors
        ): ActionColors {
            return ActionColors(primary, link)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class StatusSet private constructor(
    val default: Color,
    val bg: Color,
    val text: Color
) {
    internal companion object {
        fun create(
            default: Color,
            bg: Color,
            text: Color
        ): StatusSet {
            return StatusSet(default, bg, text)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class StatusColors private constructor(
    val success: StatusSet,
    val error: StatusSet,
    val warning: StatusSet,
    val info: StatusSet
) {
    internal companion object {
        fun create(
            success: StatusSet,
            error: StatusSet,
            warning: StatusSet,
            info: StatusSet
        ): StatusColors {
            return StatusColors(success, error, warning, info)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class BorderColors private constructor(
    val active: Color,
    val tertiary: Color,
    val secondary: Color,
    val white: Color
) {
    internal companion object {
        fun create(
            active: Color,
            tertiary: Color,
            secondary: Color,
            white: Color
        ): BorderColors {
            return BorderColors(active, tertiary, secondary, white)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class GnbIconColors private constructor(
    val selected: Color,
    val default: Color
) {
    internal companion object {
        fun create(
            selected: Color,
            default: Color
        ): GnbIconColors {
            return GnbIconColors(selected, default)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class IconColors private constructor(
    val gnb: GnbIconColors,
    val basic: Color
) {
    internal companion object {
        fun create(
            gnb: GnbIconColors,
            basic: Color
        ): IconColors {
            return IconColors(gnb, basic)
        }
    }
}

/**
 * Petbulance의 최종 Semantic Color Scheme
 */
@Immutable
data class PetbulanceColorScheme(
    val bg: BgColors,
    val text: TextColors,
    val action: ActionColors,
    val status: StatusColors,
    val border: BorderColors,
    val icon: IconColors,
    val isDark: Boolean
)
