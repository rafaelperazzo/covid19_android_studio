package pet.yoko.apps.covid.db;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private Context context;
    private static DatabaseClient mInstance;
    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx) {
        this.context = mCtx;
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "covidUFCA_db").build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

}
