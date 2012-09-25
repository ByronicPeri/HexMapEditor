package hex.map;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.Matrix;

public class Helper {

	private static int[] size = new int[2];
	private static float[] transformationMatrix = new float[16];
	private static float[] modelMatrix = new float[16];
	
	public static void setTransMatrix(float[] matrix){
		transformationMatrix = matrix;
	}
	public static void setModelMatrix(float[] matrix){
		modelMatrix = matrix;
	}
	
	public static void setSize(int[] input){
		size = input;
	}
	public static int[] getSize(){
		return size;
	}
	
	public static String textFromFile(String filename){
		
		String text = new String();
		String nextLine = new String();
		
		try{
			FileInputStream instream = new FileInputStream(filename);
			DataInputStream in = new DataInputStream(instream);
			
			BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
		
			while((nextLine = buffered.readLine()) != null){
				text = text.concat(nextLine);
			}
		}
		catch(IOException e){
			return null;
		}
		
		return text;
	}
	
	// not working
	public static float[] frustum(float x, float y){
		float[] output = new float[4];
		float[] obj = new float[4];
		//float[] obj2 = new float[4];
		float ratio = (float) size[0] / size[1];
		float[] frustum = new float[16];
		//float[] camera = new float[16];
		//float[] mOut = new float[16];
		//float[] input = {x, y, 0, 0};
		int[] cam = { 0, 0 , size[0], size[1] };
		ByteBuffer bb = ByteBuffer.allocateDirect(4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer z = bb.asFloatBuffer();
		
		/*for(int i = 0; i < Camera.getView().length; i++){
			cam[i] = (int)Camera.getView()[i];
		}*/
		//GLES20.glGetIntegerv(GLES20.GL_VIEWPORT, cam, 0);
		
		/*
		float[] pos = {x * 2.0f / size[0] - 1, (y * -2.0f / size[1] + 1) * ratio, -1.0f, 1.0f}; 
		float[] invert = new float[16];
		Matrix.invertM(invert, 0, transformationMatrix, 0);
		Matrix.multiplyMV(obj, 0, invert, 0, pos, 0);
		*/
		
		GLES20.glReadPixels(0, 0, size[0], size[1], GLES20.GL_DEPTH_COMPONENT, GLES20.GL_FLOAT, z);
		z.position(0);
		//System.out.println(z.get(0));
		
		Matrix.orthoM(frustum, 0, -ratio, ratio, -1, 1, 2, 10);
		GLU.gluUnProject(x, (size[1] - y), Camera.getPosition()[2], Camera.getView(), 0, frustum, 0, cam, 0, obj, 0);
		//GLU.gluUnProject(x, (size[1] - y), z.get(0), Camera.getView(), 0, frustum, 0, cam, 0, obj, 0);

		//GLU.gluUnProject(x, size[1] - y, 1.0f, Camera.getView(), 0, transformationMatrix, 0, cam, 0, obj2, 0);
		
		
		//Matrix.setLookAtM(camera, 0, Camera.getEye()[0], Camera.getEye()[1], Camera.getEye()[2], 0, 0, 0, 0, 1.0f, 0);
		//Matrix.multiplyMM(mOut, 0, frustum, 0, camera, 0);
		
		//Matrix.multiplyMV(output, 0, transformationMatrix, 0, input, 0);
		
		for(int i = 0; i < output.length; i++){
			output[i] = obj[i];
			//System.out.println(obj[i]);
		}
		output[0] = output[0]*3/2;
		output[1] = output[1]*(ratio/0.4f) + 0.4f;
		
		//output[0] = (-1.0f * output[0] / ratio * 2.0f + 1.0f) * ratio;
		//output[1] = output[1] / size[1] * 2.0f - 1.0f;
		
		//output[0] = -1 + (float) x / size[0];
		//output[1] = 1 - (float) y / size[0];
		
		return output;
	}
	
	public static boolean tempMapAvailable(){
		try{
			FileInputStream instream = new FileInputStream("tempMapSave");
			DataInputStream in = new DataInputStream(instream);
			BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
			
			if(buffered.readLine() != "null"){
				return true;
			}
		}
		catch(Exception e){
		}
		
		return false;
	}
	
	public static Map mapFromFile(File file){
		Map map = null;
		
		try{
			FileInputStream fStream = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fStream);
			
			map = (Map)in.readObject();
			
			in.close();
		}
		catch(Exception e){
			System.out.println("Helper.mapFromFile() error");
		}
		
		return map;
	}
	
	public static File getTempMapFile(){
		File tempMap = new File("tempMapSave");
		return tempMap;
	}
	
}