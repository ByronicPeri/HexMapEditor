package hex.map;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;

public class HexMapEditorActivity extends Activity {
    
	private HexMapView view;
	public static AppControl appControl_;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        appControl_ = new AppControl(this);
        view = new HexMapView(this);
        setContentView(view);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig){
    	
    }
    
    @Override
    public void onPause(){
    	//appControl_.saveTempMap();
    	
    }
    
    public boolean onTouchEvent(MotionEvent e){
    	return appControl_.onTouchEvent(e);
    }
}