package hex.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Iso extends Obj {

	private Context context;
	private final float[] verticesO = {
			0.15f, 0.25f, 0.0f,
			-0.15f, 0.25f, 0.0f,
			0.0f, 0.0f, 0.0f,
			
			-0.15f, 0.25f, 0.0f,
			-0.3f, 0.0f, 0.0f,
			0.0f, 0.0f, 0.0f,
			
			-0.3f, 0.0f, 0.0f,
			-0.15f, -0.25f, 0.0f,
			0.0f, 0.0f, 0.0f,
			
			-0.15f, -0.25f, 0.0f,
			0.15f, -0.25f, 0.0f,
			0.0f, 0.0f, 0.0f,
			
			0.15f, -0.25f, 0.0f,
			0.3f, 0.0f, 0.0f,
			0.0f, 0.0f, 0.0f,
			
			0.3f, 0.0f, 0.0f,
			0.15f, 0.25f, 0.0f,
			0.0f, 0.0f, 0.0f
			};
	
	
			
	private final float[] textureCoordinateData = {
			0.15f, 0.25f, 
			-0.15f, 0.25f, 
			0.0f, 0.0f, 
			
			-0.15f, 0.25f, 
			-0.3f, 0.0f, 
			0.0f, 0.0f, 
			
			-0.3f, 0.0f, 
			-0.15f, -0.25f, 
			0.0f, 0.0f, 
			
			-0.15f, -0.25f, 
			0.15f, -0.25f, 
			0.0f, 0.0f, 
			
			0.15f, -0.25f, 
			0.3f, 0.0f, 
			0.0f, 0.0f, 
			
			0.3f, 0.0f, 
			0.15f, 0.25f, 
			0.0f, 0.0f
			};
	private final float[] initColor = {1.0f, 1.0f, 1.0f, 0.0f};
	private final int TYPES = 5;
	private final float[][] colors = {	
			{1.0f, 0.0f, 0.0f, 0.5f},
			{0.0f, 1.0f, 0.0f, 0.5f},
			{0.0f, 0.0f, 1.0f, 0.5f},
			{1.0f, 1.0f, 0.0f, 0.5f},
			{1.0f, 0.0f, 1.0f, 0.5f},
			{0.0f, 1.0f, 1.0f, 0.5f}
	};
	private final int[] textureID = {
			hex.map.R.drawable.grass,
			hex.map.R.drawable.dirt,
			hex.map.R.drawable.sand,
			hex.map.R.drawable.rocky,
			hex.map.R.drawable.mud
	};
	
	private int type = 3;
	
	public Iso(){
		super();
		setVertices(verticesO);
		setTexCoord(textureCoordinateData, true);
		setColor(initColor);
		setTexture(textureID[type]);
	}
	
	public boolean tapThis(float x, float y){
		float slope = (float) (-5.0/3.0);
		float intercept = 0.5f;
		
		int[] size = Helper.getSize();
		float[] pos = Helper.frustum(x, y);
		
		//System.out.println(pos[0] + "\n" + getCPosition()[0] + "\n " + pos[1] + "\n" + getCPosition()[1] + "\n");
		// check square
		if(Math.abs(getCPosition()[0] - pos[0]) <= 0.10f && Math.abs(getCPosition()[1] - pos[1]) <= 0.25f){
			return true;
		}
		// check side
		/*
		else if(y <= slope * x + intercept && y >= -1*slope * x -intercept){
			return true;
		}
		// check other side
		else if((-1*y) <= slope * x + intercept && (-1*y) >= -1*slope * x -intercept){
			return true;
		}
		*/
		return false;
	}
	
	public void changeType(){
		type += 1;
		if(type >= TYPES){				// avoid crashes from out of bounds array calls
			type = 0;
		}
		setTexture(textureID[type]);	// change the texture for the isometric cell
		//setColor(colors[type]);		// change colors for the isometric cell
	}
	
}
