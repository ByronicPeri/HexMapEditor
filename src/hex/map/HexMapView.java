package hex.map;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class HexMapView extends GLSurfaceView {
	
	private HexMapRenderer renderer;
	
	public HexMapView() {
		super(HexMapEditorActivity.getContext());
		
		// Create an OpenGL ES 2.0 context
		setEGLContextClientVersion(2);
		
		renderer = new HexMapRenderer();
		setRenderer(renderer);
	}
	
	public HexMapRenderer getRend(){
		return renderer;
	}
	
}