package pigareva_10moa.lab45.data;

import android.app.Activity;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import pigareva_10moa.lab45.R;

public class TelephonySMSActivity extends FragmentActivity {

    // пример работы с Telephony.SMS провайдером. Здесь получается список смс
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephony_sms);

        ListView listView = (ListView) findViewById(R.id.listView);

        final SimpleCursorAdapter simpleCursorAdapter;

        String[] from = new String[] { Telephony.Sms.Inbox.BODY  };
        int[] to = new int[] { R.id.textViewName};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.theater_list_item, null, from, to, 0);

        listView.setAdapter(simpleCursorAdapter);

        getSupportLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                // получаем все смс
                return new android.support.v4.content.CursorLoader(TelephonySMSActivity.this,Telephony.Sms.Inbox.CONTENT_URI, // Official CONTENT_URI from docs
                        new String[] { Telephony.Sms.Inbox.BODY }, // Select body text
                        null,
                        null,
                        Telephony.Sms.Inbox.DEFAULT_SORT_ORDER);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

                int count = cursor.getCount();

                Log.i("TEST",Integer.toString(count));

                for(int i = 0; i < count; i++){
                    Log.i("TEST",String.format("%s -> %s",cursor.getColumnName(i),cursor.getString(i)));
                }
                if(count > 0)
                    simpleCursorAdapter.swapCursor(cursor);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {

            }
        });

    }


}
