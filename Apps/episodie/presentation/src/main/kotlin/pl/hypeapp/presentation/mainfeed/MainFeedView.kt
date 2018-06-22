package pl.hypeapp.presentation.mainfeed

import pl.hypeapp.presentation.base.View

interface MainFeedView : View {

    fun initToolbar()

    fun initPagerAdapter()

    fun navigateToSearch()

    fun addFabButtonLandscapePadding()
}
