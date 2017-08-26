package com.pallavinishanth.android.letsdine.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.pallavinishanth.android.letsdine.DetailActivity;
import com.pallavinishanth.android.letsdine.R;

/**
 * Created by PallaviNishanth on 8/14/17.
 */

public class ResWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new ResRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class ResRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = ResRemoteViewsFactory.class.getSimpleName();

    private Context mContext;
    private int mAppWidgetId;
    private String name;
    private int size;
    private String[] nameArray;
    private String[] vicinityArray;
    private boolean[] hours;
    private String[] place_id_array;

    public ResRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        size = preferences.getInt("DataSize", 0);

        Log.d(LOG_TAG, "Widget data size" + size);

        nameArray = new String[size];
        vicinityArray = new String[size];
        hours = new boolean[size];
        place_id_array = new String[size];

        for(int i=0; i<size; i++) {
            place_id_array[i] = preferences.getString("PlaceID"+"_" + i, null);
        }

        for(int i=0; i<size; i++) {
            nameArray[i] = preferences.getString("RESName" + "_" + i, null);

        }
        for(int i=0; i<size; i++) {
            vicinityArray[i] = preferences.getString("RESVicinity"+"_" + i, null);
        }
        for(int i=0; i<size; i++) {
            hours[i] = preferences.getBoolean("RESHours"+"_" + i, false);
        }

    }

    @Override
    public void onCreate() {

    }

    public void onDestroy() {

    }

    public int getCount() {

        return size;

    }

    public RemoteViews getViewAt(int position) {

        RemoteViews row=new RemoteViews(mContext.getPackageName(),
                R.layout.widget_item);

        row.setTextViewText(R.id.widget_res_name, nameArray[position]);
        row.setTextViewText(R.id.widget_res_vicinity, vicinityArray[position]);

        if(hours[position] == true) {
            row.setTextViewText(R.id.widget_res_hours, "Open Now");
            row.setTextColor(R.id.widget_res_hours, Color.parseColor("#4CAF50"));
        }else{
            row.setTextViewText(R.id.widget_res_hours, "Closed Now");
            row.setTextColor(R.id.widget_res_hours, Color.RED);
        }

        final Intent fillInIntent = new Intent();
        fillInIntent.putExtra(DetailActivity.PLACE_ID, place_id_array[position]);
        row.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

        return(row);
    }

    public RemoteViews getLoadingView() {
        return null;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {

    }

}
