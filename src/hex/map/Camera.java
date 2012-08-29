package hex.map;

public class Camera {

	// variables
	private static float[] position = {0,0,0};
	private static float[] velocity = {0,0,0};
	private static float pitch = 0.0f;			// tilting forward/backward	= x-axis
	private static float rolling = 0.0f;		// tilting side/side		= y-axis.  Not needed?
	private static float yawning = 0.0f;		// turning left/right		= z-axis
	private static float[] eye = new float[3];
	private static float distance = 3.0f;
	
	private static float height = 0.0f;
	
	
	private static float[] view = new float[16];
	
	
	// set variables
	public static void setPosition(float[] pos){
		for(int i = 0; i < pos.length && i < position.length; i++){
			position[i] = pos[i];
		}
	}
	public static void setPosition(float x, float y, float z){
		position[0] = x;
		position[1] = y;
		position[2] = z;
	}
	
	public static void addPosition(float x, float y, float z){
		position[0] += x;
		position[1] += y;
		position[2] += z;
	}
	
	public static void setVelocity(float[] vel){
		for(int i = 0; i < vel.length && i < velocity.length; i++){
			velocity[i] = vel[i];
		}
	}
	
	public static void setPitch(float nPitch){
		pitch = nPitch;
	}
	
	public static void setRolling(float roll){
		rolling = roll;
	}
	
	public static void setYawning(float yawn){
		yawning = yawn;
	}
	
	// return variables
	public static float[] getPosition(){
		return position;
	}
	
	public static float[] getVelocity(){
		return velocity;
	}
	
	public static float getRolling(){
		return rolling;
	}
	
	public static float getPitch(){
		return pitch;
	}
	
	public static float getYawning(){
		return yawning;
	}
	
	public static float[] getEye(){
		//updateEye();
		return eye;
	}
	
	
	// update
	public static void update(){
		updatePosition();
		updateEye();
	}
	private static void updatePosition(){
		for(int i = 0; i < position.length && i < velocity.length; i++){
			position[i] += velocity[i];
		}
	}
	
	private static void updateEye(){
		eye[0] = (float) (Math.cos(rolling) * Math.sin(pitch) * Math.sin(yawning)) * distance;// + position[0];
		eye[1] = (float) (Math.cos(pitch) * Math.sin(rolling) * Math.sin(yawning)) * distance;// + position[1];
		eye[2] = (float) (Math.cos(yawning)) * distance;// + height + position[2];
	}
	
	// view matrix
	public static void setView(float[] viewM){
		view = viewM;
	}
	public static float[] getView(){
		return view;
	}
}
