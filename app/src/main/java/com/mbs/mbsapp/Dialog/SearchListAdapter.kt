package com.mbs.mbsapp.Dialog


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.mbs.mbsapp.R
import java.util.Locale


class SearchListAdapter(
    context: Context?,
    resource: Int,
    textViewResourceId: Int,
    objects: List<*>?
) :
    ArrayAdapter<Any?>(context!!, resource, textViewResourceId, objects!!) {
    var searchListItems: List<SearchListItem>?
    var suggestions: MutableList<SearchListItem> = ArrayList()
    var filter = CustomFilter()
    var inflater: LayoutInflater
    private val textviewResourceID: Int

    init {
        searchListItems = objects as List<SearchListItem>?
        textviewResourceID = textViewResourceId
        inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return searchListItems!!.size
    }

    override fun getItem(i: Int): Any? {
        return searchListItems!![i].title
    }

    override fun getItemId(i: Int): Long {
        return searchListItems!![i].id.toLong()
    }

    fun getposition(id: Int): Int {
        var position = 0
        for (i in searchListItems!!.indices) {
            if (id == searchListItems!![i].id) {
                position = i
            }
        }
        return position
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var inflateview = view
        if (inflateview == null) {
            inflateview = inflater
                .inflate(R.layout.items_view_layout, null)
        }
        val tv = inflateview!!.findViewById<View>(textviewResourceID) as TextView
        tv.text = searchListItems!![i].title
        return inflateview
    }

    override fun getFilter(): Filter {
        return filter
    }

    inner class CustomFilter : Filter() {
        protected override fun performFiltering(constraint: CharSequence?): FilterResults {
            suggestions.clear()
            if (searchListItems != null && constraint != null) { // Check if the Original List and Constraint aren't null.
                for (i in searchListItems!!.indices) {
                    if (searchListItems!![i].title.lowercase(Locale.getDefault())
                            .contains(constraint)
                    ) { // Compare item in original searchListItems if it contains constraints.
                        suggestions.add(searchListItems!![i]) // If TRUE add item in Suggestions.
                    }
                }
            }
            val results =
                FilterResults() // Create new Filter Results and return this to publishResults;
            results.values = suggestions
            results.count = suggestions.size
            return results
        }

        protected override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            if (results.count > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }

    companion object {
        var TAG = "SearchListAdapter"
    }
}