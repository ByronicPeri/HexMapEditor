package hex.map;

import java.util.ArrayList;

import android.content.Context;

public class MapFrag {

	private ArrayList<Iso> mapfrag = new ArrayList<Iso>();
	private Context context;
	public static final int size = 4;
	private float[] pos = new float[3];
	
	public MapFrag(Context context, float[] pos){
		this.context = context;
		
		for(int i = 0; i < 3; i++){
			this.pos[i] = pos[i];
		}
		
		Iso iso;
		
		for(int col = 0; col < size; col++){
			for(int row = 0; row < size; row++){
				iso = new Iso(context);
				
				if(col % 2 == 0){
					iso.setPosition(0.9f * (col / 2) + pos[0], (row) * -0.5f + pos[1] , pos[2]);
				}
				else{
					iso.setPosition(0.90f * (col / 2) + 0.45f + pos[0], -0.25f + row * -0.5f + pos[1], pos[2]);
				}
				
				mapfrag.add(iso);
			}
		}
		
		
		//addRow();
		//addCol();
	}
	
	public void setPosition(float[] pos){
		this.pos = pos;
	}
	
	public float[] getPosition(){
		return pos;
	}
	
	
	public Iso getIso(int x, int y){
		return mapfrag.get(x*size + y);
	}
	
	public void draw(float[] mvpMatrix){
		
		for(int i = 0; i < mapfrag.size(); i++){
			mapfrag.get(i).draw(mvpMatrix);
			//System.out.println(i);
		}
	}
	
	public void printLocLast(){
		Iso iso = mapfrag.get(mapfrag.size()-1);
		
		for(int k = 0; k < iso.getCPosition().length; k++){
			System.out.println(iso.getCPosition()[k] + "\n");
		}
	}
	/* made for Map.  Haven't implemented for MapFrag
	public void printLoc(){
		Iso iso;
		
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				iso = getIso(i,j);
				for(int k = 0; k < iso.getCPosition().length; k++){
					System.out.println(iso.getCPosition()[k] + "\n");
				}
				
				System.out.println(" ");
			}
		}
	}
	*/
}
