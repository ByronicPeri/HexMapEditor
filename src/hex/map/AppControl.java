package hex.map;

import java.io.File;
import android.content.Context;
import android.view.MotionEvent;

public class AppControl {

	private Context context;
	private Map map = null;
	
	private boolean init = false;
	
	private float[] posInit = new float[2];
	private float[] posMid = new float[2];
	private float[] posEnd = new float[2];
	
	private final float scrollMin = 15;
	private final float[] touchSpeed = {2.0f,3.0f};
	
	AppControl(Context con){
		context = con;
		
		map = new Map(context);
		
		/*
		if(!(Helper.tempMapAvailable())){
        	map = new Map(context);
        }
        else{
        	map = getMapFromFile(Helper.getTempMapFile());
        	map.setContext(context);
        }*/
	}
	
	// From Activity
	public boolean onTouchEvent(MotionEvent e){
    	float distance = 0;
    	float[] deltaD = new float[2];
    	int[] size = Helper.size;
    	
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
    			int[] num = getIso(x,y);
    			//int[] num = {-1, -1};
    			//System.out.println(output[0] + "\n" + output[1] + "\n ");
    			if(num[0] != -1 || num[1] != -1){
    				changeIso(num[0], num[1]);
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
	
	
	
	
	
	// From Renderer
	
	public void init(){
		if(!init){
			map = new Map(context);
			init = true;
		}
	}
	
	///////////////
	// DRAW HERE //
	///////////////
	public void draw(float[] mMVPMatrix){
		map.draw(mMVPMatrix);
	}
	
	//////////////
	// END DRAW //
	//////////////
	
	public static String getVertexShader(){
    	return Helper.textFromFile("vertexShaderTextureLights");
    }
    public static String getFragmentShader(){
    	return Helper.textFromFile("fragmentShaderPerPixel");
    }
    
    // Map object test
    
    public int[] getIso(float x, float y){
    	int[] ret = {-1, -1};
    	//int i = 0;
    	for(int i = 0; i < map.getCol(); i++){
    		for(int j = 0; j < map.getRow(); j++){
    			if(map.getIso(i, j).tapThis(x, y)){
    				ret[0] = i;
    				ret[1] = j;
    				return ret;
    			}
		    }
    	}
    	
    	return ret; //not in map
    }
    
    
    
    public void changeIso(int i, int j){
    	map.getIso(i, j).changeType();
    }
    
    /*
    public void saveTempMap(){
    	File file = new File("tempMapSave");
    	map.save(file);
    }*/
    
    public Map getMapFromFile(File file){
    	Map temp  = new Map(context);
    	Map temp2 = Helper.mapFromFile(file);
    	if(temp2 != null){
    		temp = temp2;
    	}
    	
    	return temp;
    }
}
