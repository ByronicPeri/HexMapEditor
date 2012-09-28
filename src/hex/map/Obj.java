package hex.map;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

public class Obj {

	private float radius;
	private float[] position;
	private float[] cPosition = new float[1];
	private int textureID = hex.map.R.drawable.blank; // default
	private boolean updateTexture = false;
	
	//////////////////////////////////////////////////////////////////
	// not my code
	
	
	private final String vertexShaderCode =
	        // This matrix member variable provides a hook to manipulate
	        // the coordinates of the objects that use this vertex shader
	        "uniform mat4 uMVPMatrix;" +
	        
			"attribute vec2 a_TexCoordinate;" +
			"varying vec2 v_TexCoordinate;" +
			
	        "attribute vec4 vPosition;" +
	        "void main() {" +
	        "	v_TexCoordinate = a_TexCoordinate;" +
	        // the matrix must be included as a modifier of gl_Position
	        "  gl_Position = vPosition * uMVPMatrix;" +
	        "}";
	

	private final String fragmentShaderCode =
	        "precision mediump float;" +
	        "uniform vec4 vColor;" +
	        
	        // the input texture
	        "uniform sampler2D u_Texture;" +
	        "varying vec2 v_TexCoordinate;" +
	        
	        
	        "void main() {" +
	        "  gl_FragColor = vColor * texture2D(u_Texture, v_TexCoordinate);" +
	        "}";
	
	private int mProgram;
	private int mPositionHandle, mColorHandle;//, mNormalHandle, mLightPosHandle, mMVMatrixHandle;
	private FloatBuffer vertexBuffer;
	static final int COORDS_PER_VERTEX = 3;
	private float[] vertexesO = {
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
	
	private float[] vertexes = {
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
	
			
	private float[] textureCoordinateData = {
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
	
	private FloatBuffer textureBuffer;
	private int[] textures = new int[1];
	private int mTextureHandle;
	
	private int vertexCount = vertexes.length / COORDS_PER_VERTEX;
	private final int vertexStride = COORDS_PER_VERTEX * 4; // bytes per vertex
	private final int TEX_COORDS = 2;
	private final int textureStride = TEX_COORDS * 4; // bytes per
	
	private int mMVPMatrixHandle;
	
	/** Store our model data in a float buffer. */
	//private final FloatBuffer mTextureCoordinates;
	 
	/** This will be used to pass in the texture. */
	private int mTextureUniformHandle;
	 
	/** This will be used to pass in model texture coordinate information. */
	private int mTextureCoordinateHandle;
	 
	/** Size of the texture coordinate data in elements. */
	//private final int mTextureCoordinateDataSize = 3;
	 
	/** This is a handle to our texture data. */
	private int mTextureCoordHandle, /*mTextureId,*/ mSamplerHandle;
	
	//private int mProgramHandle;
	
	private int vertexShaderHandle, fragmentShaderHandle;
	
	float[] color = {0.0f, 1.0f, 1.0f, 0.5f}; // alpha?
	// end borrowed.
	///////////////////////////////////////////////////////////////////
	
	private float[] velocity = {0,0,0};
	
	
	public Obj(){
		position = new float[]{0.0f,0.0f,0.0f};
		vertexes = new float[vertexesO.length];
		for(int i = 0; i < vertexes.length; i++){
			vertexes[i] = vertexesO[i];
		}
		fixTextureCoordinates();
		
		// initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                vertexes.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put( vertexes /*triangleCoords*/);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
        
        //// begin texture buffer
        bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                textureCoordinateData.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        textureBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        textureBuffer.put( textureCoordinateData );
        // set the buffer to read the first coordinate
        textureBuffer.position(0);
        /// end texture buffer
        
		
		int vertexShader = HexMapRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
	    int fragmentShader = HexMapRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        //int vertexShader = CollisionRenderer.loadShader(GLES20.GL_VERTEX_SHADER,CollisionRenderer.getVertexShader());
        //int fragmentShader = CollisionRenderer.loadShader(GLES20.GL_VERTEX_SHADER,CollisionRenderer.getFragmentShader());

	    mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
	    GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
	    GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
	    GLES20.glLinkProgram(mProgram);                  // creates OpenGL ES program executables
	    // end borrowed
	}
	
	public void setVelocity(float x, float y, float z){
		velocity[0] = x;
		velocity[1] = y;
		velocity[2] = z;
	}
	
	public float[] getVelocity(){
		return velocity;
	}
	
	public float[] getPosition(){
		return position;
	}
	
	public void setPosition(float[] position){
		this.position = position;
		
		updateCPosition();
	}
	public void setPosition(float x, float y, float z){
		position[0] = x;
		position[1] = y;
		position[2] = z;
		
		updateCPosition();
	}
	
	public float getRadius(){
		return radius;
	}
	
	public void setRadius(float r){
		radius = r;
	}
	
	private void fixTextureCoordinates(){
		for(int i = 0; i < textureCoordinateData.length; i++){
			textureCoordinateData[i] += 0.3f;
		}
	}
	
	public void setTexture(int texID){
		textureID = texID;
		updateTexture = true;
	}
	public int getTexture(){
		return textureID;
	}
	public void setTexCoord(float[] texCoord, boolean fix){
		textureCoordinateData = new float[texCoord.length];
		for(int i = 0; i < texCoord.length; i++){
			textureCoordinateData[i] = texCoord[i];
		}
		if(fix){
			fixTextureCoordinates();
		}
		//// begin texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                textureCoordinateData.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        textureBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        textureBuffer.put( textureCoordinateData );
        // set the buffer to read the first coordinate
        textureBuffer.position(0);
        /// end texture buffer
	}
	public void setColor(float[] ncolor){
		for(int i = 0; i < 3 && i < ncolor.length; i++){
			color[i] = ncolor[i];
		}
	}
	public void setVertices(float[] vert){
		vertexesO = vert;
		updateVert();
	}
	
	private void updateVert(){
		vertexes = new float[vertexesO.length];
		for(int i = 0; i < vertexes.length; i++){
			vertexes[i] = vertexesO[i];
		}
	}
	
	public void loadGLTexture(){
		// load texture
		Bitmap bitmap = BitmapFactory.decodeResource(HexMapEditorActivity.getContext().getResources(), textureID);
		// check for textures already existing
		// or not...
		
		// generate texture pointer
		GLES20.glGenTextures(1, textures, 0);
		// ... bind it to array
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
		// create filtered texture
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		// specify 2D texture image from bitmap
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		
		// funny bug! Blocking everything between "check for textures..." and "specify 2D texture..." results in the app logo changing to TextureID
		
		// cleanup
		bitmap.recycle();
		
		if(textures[0] == 0){
			throw new RuntimeException("Error loading texture"); // if the texture isn't loaded, throw an exception.  Exception never thrown
		}
	}
	
	public void updateCPosition(){
		if(cPosition.length != position.length){
			cPosition = new float[position.length];
		}
		
		for(int i = 0; i < cPosition.length; i++){
			cPosition[i] = position[i] + Camera.getPosition()[i];
		}
	}
	
	public float[] getCPosition(){
		return cPosition;
	}
	
	public void update(){
		cPosition = new float[position.length];
		for(int i = 0; i < position.length; i++){
			position[i] = position[i] + velocity[i];
		}
		
		updateCPosition();
		
		for(int i = 0; i < vertexes.length; i++){
			vertexes[i] = vertexesO[i] + cPosition[i-3*(i/3)];
		}
		
		ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                vertexes.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(vertexes);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
        
        vertexCount = vertexes.length / COORDS_PER_VERTEX;
		
		if(updateTexture){
			loadGLTexture();
			updateTexture = false;
		}
	}
	
	public void draw(float[] mvpMatrix){
		
		update();
		
		////////////////////////////////////////////
		//borrowed
		 // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);
        
        
        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "u_Texture"); // these two were swapped
        mTextureHandle = GLES20.glGetAttribLocation(mProgram, "a_TexCoordinate"); // these two were swapped

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mTextureHandle);
        
        
      /// start binding textures
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glFrontFace(GLES20.GL_CW);
        
     // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0); // ---,0.  Should it use textures[0]?
        /// end binding textures
        

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride, vertexBuffer);
        
        // texture version
        //GLES20.glTexAttribPointer(2, GLES20.GL_FLOAT, 0, textureBuffer);
        
        // texture version
        GLES20.glVertexAttribPointer(mTextureHandle, TEX_COORDS,
                                     GLES20.GL_FLOAT, false,
                                     textureStride, textureBuffer);

        
        
        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //CollisionRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        //CollisionRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureHandle);
	}
	
}
