package com.mbs.mbsapp.Dialog


import android.app.Activity
import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.mbs.mbsapp.R
import java.util.Locale


class SearchableDialog(
    var activity: Activity?,
    var searchListItems: MutableList<SearchListItem>?,
    var dTitle: String?
) {


    private val TAG = "SearchableDialog"
    var onSearchItemSelected: OnSearchItemSelected? = null
    var alertDialog: AlertDialog? = null
    var position = 0
    var style = 0
    var searchListItem: SearchListItem? = null

    var searchListAdapter: SearchListAdapter? = null
    var listView: ListView? = null

//    fun SearchableDialog(
//
//    ) {
//        this.searchListItems = searchListItems
//        this.activity = activity
//        dTitle = dialogTitle
//    }

//    fun SearchableDialog(
//        activity: Activity?,
//        searchListItems: MutableList<SearchListItem>?,
//        dialogTitle: String?,
//        style: Int
//    ) {
//        this.searchListItems = searchListItems
//        this.activity = activity
//        dTitle = dialogTitle
//        this.style = style
//    }


    /***
     *
     * @param searchItemSelected
     * return selected position & item
     */
    fun setOnItemSelected(searchItemSelected: OnSearchItemSelected?) {
        onSearchItemSelected = searchItemSelected
    }

    /***
     *
     * show the seachable dialog
     */
    fun show() {
        val adb: AlertDialog.Builder = AlertDialog.Builder(activity)
        val view: View = activity!!.layoutInflater.inflate(R.layout.search_dialog_layout, null)
        val rippleViewClose = view.findViewById<View>(R.id.close) as TextView
        val title = view.findViewById<View>(R.id.spinerTitle) as TextView
        title.text = dTitle
        listView = view.findViewById<View>(R.id.list) as ListView
        val searchBox = view.findViewById<View>(R.id.searchBox) as EditText
        searchListAdapter =
            SearchListAdapter(activity, R.layout.items_view_layout, R.id.text1, searchListItems)
        listView!!.adapter = searchListAdapter
        adb.setView(view)
        alertDialog = adb.create()
        alertDialog?.window?.attributes?.windowAnimations =
            style //R.style.DialogAnimations_SmileWindow;
        alertDialog?.setCancelable(false)
        listView!!.onItemClickListener =
            OnItemClickListener { adapterView, view, i, l ->
                val t = view.findViewById<TextView>(R.id.text1)
                for (j in searchListItems!!.indices) {
                    if (t.text.toString().equals(searchListItems!![j].toString(), ignoreCase = true)
                    ) {
                        position = j
                        searchListItem = searchListItems!![position]
                    }
                }
                try {
                    onSearchItemSelected!!.onClick(position, searchListItem!!, searchListItem!!.type)
                    Log.e(TAG, "Text")
                } catch (e: Exception) {
                    Log.e(TAG, e.message!!)
                }
                alertDialog?.dismiss()
            }
        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                val filteredValues: MutableList<SearchListItem> = ArrayList<SearchListItem>()
                for (i in searchListItems!!.indices) {
                    if (searchListItems!![i] != null) {
                        val item: SearchListItem = searchListItems!![i]
                        if (item.title.toLowerCase().trim()
                                .contains(searchBox.text.toString().lowercase(
                                    Locale.getDefault()
                                ).trim { it <= ' ' })
                        ) {
                            filteredValues.add(item)
                        }
                    }
                }
                searchListAdapter = SearchListAdapter(
                    activity,
                    R.layout.items_view_layout,
                    R.id.text1,
                    filteredValues
                )
                listView!!.adapter = searchListAdapter
            }
        })
        rippleViewClose.setOnClickListener { alertDialog?.dismiss() }
        alertDialog?.show()
    }

    /***
     *
     * clear the list
     */
    fun clear() {
        searchListItems!!.clear()
    }

    /***
     *
     * refresh the adapter (notifyDataSetChanged)
     */
    fun refresh() {
        searchListAdapter?.notifyDataSetChanged()
    }


    fun getAdapter(): SearchListAdapter? {
        return searchListAdapter
    }
}