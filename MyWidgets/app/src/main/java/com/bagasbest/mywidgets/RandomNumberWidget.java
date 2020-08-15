package com.bagasbest.mywidgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class RandomNumberWidget extends AppWidgetProvider {

    private static String WIDGET_CLICK = "widgetClick";
    private static String WIDGET_ID_EXTRA = "widget_id_extra";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.random_number_widget);
        String lastUpdate = "Random: " + NumberGenerator.Generate(100);

        views.setTextViewText(R.id.appWidgetText, lastUpdate);
        views.setOnClickPendingIntent(R.id.btnClick,getPendingSelfIntent(context, appWidgetId, WIDGET_CLICK));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (WIDGET_CLICK.equals(intent.getAction())) {
           AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
           RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.random_number_widget);
           String lastUpdate = "Random: " + NumberGenerator.Generate(100);
           int appWidgetId = intent.getIntExtra(WIDGET_ID_EXTRA, 0);
           remoteViews.setTextViewText(R.id.appWidgetText, lastUpdate);
           appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    protected static PendingIntent getPendingSelfIntent(Context context, int appWidgetId, String action)  {
        Intent intent = new Intent(context, RandomNumberWidget.class);
        intent.setAction(action);
        intent.putExtra(WIDGET_ID_EXTRA, appWidgetId);
        return PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
    }
}

