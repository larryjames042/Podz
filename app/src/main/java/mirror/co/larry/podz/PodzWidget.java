package mirror.co.larry.podz;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import mirror.co.larry.podz.services.MusicService;
import mirror.co.larry.podz.ui.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class PodzWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, String episodeName,
                                int appWidgetId) {


        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Intent playIntent = new Intent(context, MusicService.class);
        playIntent.setAction(MusicService.ACTION_PLAY_FROM_WIDGET);
        PendingIntent playPendingIntent = PendingIntent.getService(context, 1, playIntent, 0);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.podz_widget);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
        views.setTextViewText(R.id.appwidget_text, episodeName);
        views.setOnClickPendingIntent(R.id.iv_play_button, playPendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateLastPlayedEpisode(Context context, AppWidgetManager appWidgetManager, String episode, int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, episode, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

