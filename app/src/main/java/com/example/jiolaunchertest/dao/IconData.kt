package com.example.jiolaunchertest.dao

import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable

/**
 * Created by Prabhakaran on 26,OCT,2020
 * Copyright (c) 2020 Jio Platform Limited. All rights reserved.
 */
class IconData {

    var shortcutIcon: String? = null
    var shortCutDrawable: Drawable = ShapeDrawable()

    constructor(shortcutIcon: String, shortCutDrawable: Drawable) {
        this.shortcutIcon = shortcutIcon
        this.shortCutDrawable = shortCutDrawable
    }
}