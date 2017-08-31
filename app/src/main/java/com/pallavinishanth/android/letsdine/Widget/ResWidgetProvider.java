package com.pallavinishanth.android.letsdine.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.pallavinishanth.android.letsdine.DetailActivity;
import com.pallavinishanth.android.letsdine.R;

/**
 * Created by PallaviNishanth on 8/14/17.
 */

public class ResWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = ResWidgetProvider.class.getSimpleName();

    public static String EXTRA_WORD = "word";

    public static final String UPDATE_WIDGET = "android.appwidget.action.APPWIDGET_UPDATE";

    private String[] nameArray;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.d(LOG_TAG, "Widget OnUpdate");

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < appWidgetIds.length; ++i) {

            RemoteViews remoteViews = new
                    RemoteViews(context.getPackageName(), R.layout.widget_layout);

            // Set up the intent that starts the ResViewService, which will
            // provide the views for this collection.
            Intent serviceIntent = new Intent(context, ResWidgetRemoteViewsService.class);

            // Add the app widget ID to the intent extras.
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            // embed extras so they don't get ignored
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            // Set up the RemoteViews object to use a RemoteViews adapter.
            // This adapter connects to a RemoteViewsService  through the specified intent.
            // This is how you populate the data.
            remoteViews.setRemoteAdapter(R.id.widget_list, serviceIntent);

            // The empty view is displayed when the collection has no items.
            // It should be in the same layout used to instantiate the RemoteViews
            // object above.
            remoteViews.setEmptyView(R.id.widget_list, R.id.empty_list);

            // set intent for item click (opens main activity)
            Intent viewIntent = new Intent(context, DetailActivity.class);
//            viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            viewIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            viewIntent.setData(Uri.parse(viewIntent.toUri(Intent.URI_INTENT_SCHEME)));

            PendingIntent viewPendingIntent = PendingIntent.getActivity(context, 0, viewIntent, 0);
            remoteViews.setPendingIntentTemplate(R.id.widget_list, viewPendingIntent);

            // update widget
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);
    }

}
