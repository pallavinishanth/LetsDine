package com.pallavinishanth.android.letsdine.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.pallavinishanth.android.letsdine.Network.Results;
import com.pallavinishanth.android.letsdine.R;

import java.util.ArrayList;

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

    private static final String[] items={"lorem", "ipsum", "dolor",
            "sit", "amet", "consectetuer",
            "adipiscing", "elit", "morbi",
            "vel", "ligula", "vitae",
            "arcu", "aliquet", "mollis",
            "etiam", "vel", "erat",
            "placerat", "ante",
            "porttitor", "sodales",
            "pellentesque", "augue",
            "purus"};

    private ArrayList<Results> widget_dataList = new ArrayList<Results>();

    private Context mContext;
    private int mAppWidgetId;

    public ResRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    public void onDestroy() {

    }

    public int getCount() {
        return (items.length);
    }

    public RemoteViews getViewAt(int position) {

        RemoteViews row=new RemoteViews(mContext.getPackageName(),
                R.layout.widget_item);

        row.setTextViewText(R.id.widget_res_name, items[position]);
        row.setTextViewText(R.id.widget_res_vicinity, items[position]);
        row.setTextViewText(R.id.widget_res_hours, items[position]);

        Intent i=new Intent();
        Bundle extras=new Bundle();

        extras.putString(ResWidgetProvider.EXTRA_WORD, items[position]);
        i.putExtras(extras);
        row.setOnClickFillInIntent(R.id.widget_res_name, i);

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
