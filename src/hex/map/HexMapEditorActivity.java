package hex.map;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;

public class HexMapEditorActivity extends Activity {
    
	private HexMapView view;
	private Iso on_down;
	private Iso on_up;
	
	private float[] posInit = new float[2];
	private float[] posMid = new float[2];
	private float[] posEnd = new float[2];
	
	private final float scrollMin = 15;
	private final float[] touchSpeed = {2.0f,3.0f};
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        view = new HexMapView(this);
        setContentView(view);
    }
    
    public boolean onTouchEvent(MotionEvent e){
    	float distance = 0;
    	float[] deltaD = new float[2];
    	int[] size = view.getRend().getSize();
    	
    	if(e.getAction() == MotionEvent.ACTION_DOWN){
			posInit[0] = e.getRawX();
			posInit[1] = e.getRawY();
			
			posMid = posInit;
			
			//on_down = view.getCell(x,y);
    	}
    	else if(e.getAction() == MotionEvent.ACTION_UP){
    		posEnd[0] = e.getRawX();
			posEnd[1] = e.getRawY();
    		
    		for(int i = 0; i < 2; i++){
				deltaD[i] = Math.abs(posEnd[i] - posInit[i]);
			}
    		distance = deltaD[0] + deltaD[1];
    		//on_up = view.getCell(x,y);
    		
    		if(distance < scrollMin){
    			float x = e.getRawX();
    			float y = e.getRawY();
    			
    			// if iso not touched, then add to map size
    			//int[] num = view.getRend().getIso(x,y);
    			int[] num = {-1, -1};
    			//System.out.println(output[0] + "\n" + output[1] + "\n ");
    			if(num[0] != -1 || num[1] != -1){
    				//view.getRend().changeIso(num[0], num[1]);
    			}
    			else{
    				view.getRend().addMapCol();
    				view.getRend().addMapRow();
    			}
        	}
			
    	}
    	else if(e.getAction() == MotionEvent.ACTION_MOVE){
    		float[] temp = new float[2];
    		temp[0] = e.getRawX();
			temp[1] = e.getRawY();
			
			for(int i = 0; i < 2; i++){
				deltaD[i] = (temp[i] - posMid[i]) / size[i] * touchSpeed[i];
			}
			
			deltaD[1] = -1 * deltaD[1];
			posMid = temp;
			
	    	distance = (float) (Math.sqrt((double)(deltaD[0] * deltaD[0] + deltaD[1] * deltaD[1])));
	    	
	    	Camera.addPosition(deltaD[0], deltaD[1], 0);
    	}
    	
    	for(int i = 0; i < deltaD.length; i++){
    		deltaD[i] = 0;
    	}
    	
    	return true;
    }
    
    /*
    private float[] transformCart(float x, float y){
    	int[] size = view.getRend().getSize();
    	float ratio = (float) size[0] / size[1];
    	
    	float[] output = new float[2];
    	output[0] = (float)(x / (size[0] / 2.0f)) - 1.0f; 		// -1,1
    	output[1] = (float)(-y / (size[1] / ratio)) - ratio; 	// -ratio, ratio
    	
    	return output;
    }
    */
}