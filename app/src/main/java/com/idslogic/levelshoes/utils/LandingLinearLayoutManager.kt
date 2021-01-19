package com.idslogic.levelshoes.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class LandingLinearLayoutManager(context: Context?, orientation: Int, reverseLayout: Boolean) :
    LinearLayoutManager(context, orientation, reverseLayout) {
    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}