package hex.map;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;

public class HexMapEditorActivity extends Activity {
    
	private HexMapView view;
	public static AppControl appControl_;
	private static Context context_;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context_ = getApplicationContext();
        
        appControl_ = new AppControl();
        view = new HexMapView();
        setContentView(view);
    }
    
    public static Context getContext(){
    	return context_;
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig){
    	super.onConfigurationChanged(newConfig);
    	
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	//appControl_.saveTempMap();
    	
    }
    
    public boolean onTouchEvent(MotionEvent e){
    	return appControl_.onTouchEvent(e);
    }
}