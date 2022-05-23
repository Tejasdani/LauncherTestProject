package com.example.jiolaunchertest.listner

interface BasePresenterListener<View> {

    var view: View

    /**
     * Called to initialize pagerPresenter
     */
    fun initialize() {}

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onResume() method.
     */
    fun resume() {}

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onPause() method.
     */
    fun pause() {}

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onDestroy() method.
     */
    fun destroy() {}
}