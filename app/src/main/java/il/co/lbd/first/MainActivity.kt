package il.co.lbd.first

//import android.app.Activity
import android.app.ListActivity
import android.app.LoaderManager
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*

// These are the Contacts rows that we will retrieve
internal val PROJECTION = arrayOf(
    CallLog.Calls._ID,
    CallLog.Calls.NUMBER,
    CallLog.Calls.TYPE,
    CallLog.Calls.DATE,
    CallLog.Calls.DURATION,
    CallLog.Calls.CACHED_NAME
)

// This is the select criteria
internal const val SELECTION = "((" +
        CallLog.Calls.NUMBER + " NOTNULL) AND (" +
        CallLog.Calls.NUMBER + " != '' ))"

class MainActivity : ListActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    // This is the Adapter being used to display the list's data
    private lateinit var mAdapter: SimpleCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create a progress bar to display while the list loads
        val progressBar = ProgressBar(this)
        progressBar.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER)
        progressBar.isIndeterminate = true
        listView.emptyView = progressBar

        // Must add the progress bar to the root of the layout
        val root: ViewGroup = findViewById(android.R.id.content)
        root.addView(progressBar)

        // For the cursor adapter, specify which columns go into which views
        val fromColumns: Array<String> = arrayOf(CallLog.Calls.NUMBER, CallLog.Calls.DURATION)
        val toViews = intArrayOf(android.R.id.text1, android.R.id.text2)

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = SimpleCursorAdapter(this,
            android.R.layout.simple_list_item_2, null,
            fromColumns, toViews, 0)
        listAdapter = mAdapter

        // Prepare the loader. Either re-connect with an existing one,
        // or start a new one.
        loaderManager.initLoader(0, Bundle(), this)
    }

    // Called when a new Loader needs to be created
    override fun onCreateLoader(id: Int, args: Bundle): Loader<Cursor> {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return CursorLoader(this,
            CallLog.Calls.CONTENT_URI,
            PROJECTION, SELECTION, null, null)
    }

    // Called when a previously created loader has finished loading
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        // Swap the new cursor in. (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data)
    }

    // Called when a previously created loader is reset, making the data unavailable
    override fun onLoaderReset(loader: Loader<Cursor>) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed. We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null)
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        Log.d("Shemerdog", "Pressed $position")
    }
}