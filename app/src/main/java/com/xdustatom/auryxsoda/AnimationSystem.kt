package com.xdustatom.auryxsoda

import android.graphics.Point

class AnimationSystem {
    
    private val destroyAnimations = mutableMapOf<Point, DestroyAnimation>()
    private val swapAnimations = mutableListOf<SwapAnimation>()
    
    data class DestroyAnimation(
        val position: Point,
        var progress: Float = 0f,
        var alpha: Float = 1f,
        var scale: Float = 1f,
        var rotation: Float = 0f
    )
    
    data class SwapAnimation(
        val pos1: Point,
        val pos2: Point,
        var progress: Float = 0f
    )
    
    fun startDestroyAnimation(positions: List<Point>) {
        for (pos in positions) {
            destroyAnimations[pos] = DestroyAnimation(pos)
        }
    }
    
    fun startSwapAnimation(pos1: Point, pos2: Point) {
        swapAnimations.add(SwapAnimation(pos1, pos2))
    }
    
    fun updateDestroyAnimations(deltaTime: Float): Boolean {
        val iterator = destroyAnimations.iterator()
        var hasAnimations = false
        
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val anim = entry.value
            
            anim.progress += deltaTime * 3f
            anim.alpha = 1f - anim.progress
            anim.scale = 1f + anim.progress * 0.5f
            anim.rotation = anim.progress * 360f
            
            if (anim.progress >= 1f) {
                iterator.remove()
            } else {
                hasAnimations = true
            }
        }
        
        return hasAnimations
    }
    
    fun updateSwapAnimations(deltaTime: Float): Boolean {
        val iterator = swapAnimations.iterator()
        var hasAnimations = false
        
        while (iterator.hasNext()) {
            val anim = iterator.next()
            anim.progress += deltaTime * 4f
            
            if (anim.progress >= 1f) {
                iterator.remove()
            } else {
                hasAnimations = true
            }
        }
        
        return hasAnimations
    }
    
    fun getDestroyAnimation(pos: Point): DestroyAnimation? {
        return destroyAnimations[pos]
    }
    
    fun hasActiveAnimations(): Boolean {
        return destroyAnimations.isNotEmpty() || swapAnimations.isNotEmpty()
    }
    
    fun clear() {
        destroyAnimations.clear()
        swapAnimations.clear()
    }
}
