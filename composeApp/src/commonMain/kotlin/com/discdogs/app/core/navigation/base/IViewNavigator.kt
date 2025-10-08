package com.discdogs.app.core.navigation.base

import androidx.navigation.NavGraphBuilder

interface IViewNavigator {
    fun build(navGraphBuilder: NavGraphBuilder)
    fun navigateBack()
}