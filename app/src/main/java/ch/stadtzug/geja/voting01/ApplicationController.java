package ch.stadtzug.geja.voting01;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

/**
 * Created by gerlach jan on 18.04.2017.
 */

public class ApplicationController extends Application {

    private static  ApplicationController sInstance;
    private RequestQueue requestQueue;
    private static Context context;

    private ApplicationController(Context context){

        this.context = context;
        requestQueue = getRequestQueue();

    }

    public RequestQueue getRequestQueue() {

        if (requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return requestQueue;
    }

    public static synchronized ApplicationController getInstance(Context context)
    {
        if (sInstance == null)
        {
            sInstance = new ApplicationController(context);
        }

        return sInstance;
    }

    public<T> void addToRequestQueue(Request<T> request)
    {
        requestQueue.add(request);
    }

}
