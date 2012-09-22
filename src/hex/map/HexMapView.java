package hex.map;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class HexMapView extends GLSurfaceView {
	
	private HexMapRenderer renderer;
	
	public HexMapView(Context context) {
		super(context);
		
		// not my code
		// Create an OpenGL ES 2.0 context
		setEGLContextClientVersion(2);
		
		renderer = new HexMapRenderer(context);
		setRenderer(renderer);
	}
	
	public HexMapRenderer getRend(){
		return renderer;
	}
	
}