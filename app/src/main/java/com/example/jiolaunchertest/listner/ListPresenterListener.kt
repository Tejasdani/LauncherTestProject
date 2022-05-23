package com.example.jiolaunchertest.listner


interface ListPresenterListener<in ITEM> {

    /**
     * Number of items in collection
     */
    fun itemCount(): Int

    /**
     * Binds item to view holder
     */
    fun onBindViewOnPosition(position: Int, holder: ITEM)

}