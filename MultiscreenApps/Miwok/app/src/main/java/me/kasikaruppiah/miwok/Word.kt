package me.kasikaruppiah.miwok

/**
 * Created by ledlab on 8/11/17.
 */
data class Word(val defaultTranslation: String, val miwokTranslation: String, val audioResourceId: Int) {
    var imageResourceId = -1

    constructor(defaultTranslation: String, miwokTranslation: String, imageResourceId: Int, audioResourceId: Int) : this(defaultTranslation, miwokTranslation, audioResourceId) {
        this.imageResourceId = imageResourceId
    }
}