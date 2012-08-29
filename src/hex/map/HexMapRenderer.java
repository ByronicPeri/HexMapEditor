package hex.map;

import java.io.File;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

public class HexMapRenderer implements GLSurfaceView.Renderer {

	private Context context;
	
	public Obj obj;
	//private ArrayList<Iso> map = new ArrayList<Iso>();
	private Map map;
	
	private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
    //private final float[] mRotationMatrix = new float[16];
    
    private int[] size = new int[2];
    
    private float[] temp;
    
    private static final String TAG = "CollisionRenderer";
    
    public HexMapRenderer(Context con){
    	context = con;
    }
	
	// not my code
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        
        //obj = new Obj(context);
        //obj.loadGLTexture();
        //obj.setVelocity(0.01f, 0.0f, 0.0f);
        //map.add(new Iso(context));
        //map.add(new Iso(context));
        //map.get(1).setPosition(0.45f,-0.25f,0.0f);
        
        map = new Map(context);
        
        //addMapCol();
        //addMapRow();
        
        
     // Use culling to remove back faces.
		//GLES20.glEnable(GLES20.GL_CULL_FACE);
        
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        // didn't enable GL_SMOOTH because there is no GLES20 equivalent.
        GLES20.glClearDepthf(1.0f);
		// Enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		//GLES20.glHint(GLES20.GL_PERSPECTIVE_CORRECTION_HINT, GLES20.GL_NICEST);
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
    	
    	GLES20.glClearColor(0.25f, 0.25f, 0.25f, 0.5f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
		// Set the camera position (View matrix)
        Camera.update();
        //System.out.println(Camera.getEye()[0] + "\n" + Camera.getEye()[1] + "\n" + Camera.getEye()[2] + "\n ");
        Matrix.setLookAtM(mVMatrix, 0, Camera.getEye()[0], Camera.getEye()[1], Camera.getEye()[2], 0, 0, 0, 0, 1.0f, 0);
        Camera.setView(mVMatrix);
        
        //Camera.setRolling(Camera.getRolling() + 0.1f);
        //Camera.setPitch(Camera.getPitch() + 0.1f);
        //Camera.setYawning(Camera.getYawning() + 0.1f);
        
		// Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        Helper.setTransMatrix(mMVPMatrix);
        
        // draw map
        map.draw(mMVPMatrix);
        //for(int i = 0; i < map.size(); i++){
        //	map.get(i).draw(mMVPMatrix);
        //}
        
    }
    
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        
        float ratio = (float) width / height;
        //System.out.println(width + "\n" + height);
        size[0] = width;
        size[1] = height;
        Helper.setSize(size);

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 2, 10); // 3 determines size, don't change these values or it may crash on startup.
        //mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7
    }
   

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
    
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Helper methods
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setContext(Context context){
    	this.context = context;
    }
    public Context getContext(){
    	return context;
    }
    public int[] getSize(){
    	return size;
    }
    
    
    public static String getVertexShader(){
    	return Helper.textFromFile("vertexShaderTextureLights");
    }
    public static String getFragmentShader(){
    	return Helper.textFromFile("fragmentShaderPerPixel");
    }
    
    // Map object test
    /*
    public int[] getIso(float x, float y){
    	int[] ret = {-1, -1};
    	//int i = 0;
    	for(int i = 0; i < map.getCol(); i++){
    		for(int j = 0; j < map.getRow(); j++){
    			if(map.getIso(i,j).tapThis(x, y)){
    				ret[0] = i;
    				ret[1] = j;
    				return ret;
    			}
		    }
    	}
    	
    	return ret; //not in map
    }*/
    
    public void addMapRow(){
    	map.addRow();;
    }
    public void addMapCol(){
    	map.addCol();
    }
    
    /*
    public void changeIso(int i, int j){
    	map.getIso(i, j).changeType();
    }*/
        
    //////////////////
    // test methods //
    //////////////////
    
    /*
    public void testChangeType(){
    	map.getIso(0,0).changeType();
    }*/
    
}
