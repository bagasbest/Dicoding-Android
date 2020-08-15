package com.bagasbest.mywidgets;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.widget.RemoteViews;

public class UpdateWidgetService extends JobService {


    @Override
    public boolean onStartJob(JobParameters params) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.random_number_widget);
        ComponentName theWidget = new ComponentName(this, RandomNumberWidget.class);
        String lastUpdate = "Random:"  + NumberGenerator.Generate(100);
        remoteViews.setTextViewText(R.id.appWidgetText, lastUpdate);
        appWidgetManager.updateAppWidget(theWidget, remoteViews);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
