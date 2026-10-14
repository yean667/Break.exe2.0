package com.example.breakexe.model

import com.google.gson.annotations.SerializedName

enum class BreakReason(
    val id: String,
    val label: String,
    val emoji: String,
    val humorFallback: String
) {
    WATER("water", "water", "💧", "hydration era ✨"),
    FOOD("food", "food", "🥪", "had to refuel the machine"),
    PHONE("phone", "phone", "📱", "one reel turned into research"),
    BATHROOM("bathroom", "bathroom", "🚽", "nature called"),
    BRAIN("brain", "brain not braining", "🧠", "my brain took a vacation"),
    GRASS("grass", "touching grass", "🌿", "remembered the sun exists"),
    EXISTING("existing", "just existing", "💤", "staring into the abyss politely"),
    OTHER("other", "other...", "✨", "unclassified secret mission");

    companion object {
        fun fromId(id: String): BreakReason {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: OTHER
        }
    }
}

enum class CharacterModel(val id: String, val label: String, val emoji: String) {
    BLOB("blob", "Dumpling Blob", "🍡"),
    SLIME("slime", "Slime Droplet", "💧"),
    BUNNY("bunny", "Boba Bunny", "🐰"),
    BEAR("bear", "Matcha Bear", "🐻"),
    CLOUD("cloud", "Fluffy Cloud", "☁️");

    companion object {
        fun fromId(id: String): CharacterModel {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: BLOB
        }
    }
}

enum class CharacterAccessory(val id: String, val label: String, val icon: String) {
    HEADPHONES("headphones", "Headphones", "🎧"),
    BOW("bow", "Pastel Bow", "🎀"),
    SPROUT("sprout", "Tiny Sprout", "🌱"),
    STAR("star", "Star Pin", "⭐");

    companion object {
        fun fromId(id: String): CharacterAccessory {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: HEADPHONES
        }
    }
}

data class ColorOption(val name: String, val hex: String)

object CharacterPalettes {
    val BODY_COLORS = listOf(
        ColorOption("Fluffy White", "#FFFFFF"),
        ColorOption("Sky Slime", "#D8F2FE"),
        ColorOption("Sakura Pink", "#FFE5EC"),
        ColorOption("Matcha Mint", "#E6F8EE"),
        ColorOption("Cloud Lilac", "#F3E8FF"),
        ColorOption("Butter Cream", "#FEF8D8"),
        ColorOption("Peach Sorbet", "#FFE5D4")
    )

    val HEADPHONE_COLORS = listOf(
        ColorOption("Strawberry", "#FFAEC0"),
        ColorOption("Baby Blue", "#88CEF9"),
        ColorOption("Lavender", "#D0A9F5"),
        ColorOption("Sage Mint", "#86EFAC"),
        ColorOption("Warm Peach", "#FDBA74"),
        ColorOption("Slate Gray", "#94A3B8")
    )

    val PRESETS = listOf(
        CharacterConfig(
            model = CharacterModel.BLOB,
            name = "Mochi Blob",
            bodyColor = "#FFFFFF",
            headphoneColor = "#FFAEC0",
            accessory = CharacterAccessory.HEADPHONES,
            blushColor = "#FFAEC0"
        ),
        CharacterConfig(
            model = CharacterModel.SLIME,
            name = "Slimey Pip",
            bodyColor = "#D8F2FE",
            headphoneColor = "#88CEF9",
            accessory = CharacterAccessory.HEADPHONES,
            blushColor = "#FFAEC0"
        ),
        CharacterConfig(
            model = CharacterModel.BUNNY,
            name = "Boba Bunny",
            bodyColor = "#FFE5EC",
            headphoneColor = "#FFAEC0",
            accessory = CharacterAccessory.BOW,
            blushColor = "#FDA4AF"
        ),
        CharacterConfig(
            model = CharacterModel.BEAR,
            name = "Matcha Bear",
            bodyColor = "#E6F8EE",
            headphoneColor = "#86EFAC",
            accessory = CharacterAccessory.HEADPHONES,
            blushColor = "#FDBA74"
        ),
        CharacterConfig(
            model = CharacterModel.CLOUD,
            name = "Cloudy Poko",
            bodyColor = "#F3E8FF",
            headphoneColor = "#D0A9F5",
            accessory = CharacterAccessory.STAR,
            blushColor = "#F472B6"
        )
    )
}

data class CharacterConfig(
    @SerializedName("model") val model: CharacterModel = CharacterModel.BLOB,
    @SerializedName("name") val name: String = "Mochi Blob",
    @SerializedName("bodyColor") val bodyColor: String = "#FFFFFF",
    @SerializedName("headphoneColor") val headphoneColor: String = "#FFAEC0",
    @SerializedName("accessory") val accessory: CharacterAccessory = CharacterAccessory.HEADPHONES,
    @SerializedName("blushColor") val blushColor: String = "#FFAEC0"
)

data class BreakRecord(
    @SerializedName("id") val id: String,
    @SerializedName("startTime") val startTime: Long,
    @SerializedName("endTime") val endTime: Long,
    @SerializedName("durationSeconds") val durationSeconds: Long,
    @SerializedName("reason") val reason: BreakReason,
    @SerializedName("note") val note: String,
    @SerializedName("dateKey") val dateKey: String // YYYY-MM-DD
)

data class ActiveBreak(
    @SerializedName("startTime") val startTime: Long,
    @SerializedName("targetEndTime") val targetEndTime: Long? = null,
    @SerializedName("reason") val reason: BreakReason = BreakReason.PHONE,
    @SerializedName("note") val note: String = "",
    @SerializedName("targetMinutes") val targetMinutes: Int = 13,
    @SerializedName("notifiedAboutToEnd") val notifiedAboutToEnd: Boolean = false,
    @SerializedName("alarmDismissed") val alarmDismissed: Boolean = false
)

data class TodayStats(
    val totalSeconds: Long = 0,
    val count: Int = 0,
    val longestSeconds: Long = 0,
    val averageSeconds: Long = 0
)

data class TargetPreset(
    val minutes: Int,
    val label: String
)

enum class BlobExpression {
    DANCING, HAPPY, SLEEPY, CONFUSED
}

data class BlobSpeech(
    val main: String,
    val subtitle: String,
    val expression: BlobExpression
)

object BreakQuotes {
    val CELEBRATIONS = listOf(
        "welcome back, scholar! ✨",
        "brain successfully recharged 🔋",
        "you survived the break! time to cook 📚",
        "back in the arena 🧠",
        "100 aura points regained ✨"
    )

    fun getSpeech(seconds: Long, reason: BreakReason, isOverdue: Boolean): BlobSpeech {
        if (isOverdue) {
            return BlobSpeech(
                main = "Break khatam ho gaya! ⏰",
                subtitle = "ready to get back, bestie? 🌸",
                expression = BlobExpression.HAPPY
            )
        }
        if (seconds < 60) {
            return BlobSpeech(
                main = "vibing... (temporarily)",
                subtitle = "your books can wait (again)",
                expression = BlobExpression.DANCING
            )
        }
        if (seconds < 180) {
            return BlobSpeech(
                main = "just catching breath",
                subtitle = "the academic comeback is buffering…",
                expression = BlobExpression.DANCING
            )
        }
        if (seconds < 360) {
            if (reason == BreakReason.PHONE) {
                return BlobSpeech(
                    main = "one reel became a docu",
                    subtitle = "your algorithm knows you too well",
                    expression = BlobExpression.HAPPY
                )
            }
            if (reason == BreakReason.WATER) {
                return BlobSpeech(
                    main = "hydration era ✨",
                    subtitle = "80% water, 20% stress",
                    expression = BlobExpression.HAPPY
                )
            }
            return BlobSpeech(
                main = "bro said one minute.",
                subtitle = "time is an illusion anyway",
                expression = BlobExpression.HAPPY
            )
        }
        if (seconds < 900) {
            return BlobSpeech(
                main = "your chair misses you.",
                subtitle = "your books are getting cold",
                expression = BlobExpression.CONFUSED
            )
        }
        if (seconds < 1800) {
            return BlobSpeech(
                main = "are we still studying today?",
                subtitle = "future self is sending distress signals",
                expression = BlobExpression.SLEEPY
            )
        }
        return BlobSpeech(
            main = "you live here now.",
            subtitle = "academic comeback has left the chat",
            expression = BlobExpression.SLEEPY
        )
    }
}
