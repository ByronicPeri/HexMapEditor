package hex.map;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import android.content.Context;

public class Map implements Serializable {

	//private ArrayList<MapFrag> map = new ArrayList<MapFrag>();
	private Iso[] map;
	private int row = 10;
	private int col = 10;
	private Context context;
	
	public Map(Context context){
		this.context = context;
		float[] startPos = {0,0,0};
		
		map = new Iso[row * col];
		
		for(int i = 0; i < map.length; i++){
			map[i] = new Iso(context);
		}
		
		for(int r = 0; r < row; r++){
			for(int c = 0; c < col; c++){
			
				int iso = r + c * row;
				if(c % 2 == 0){
					map[iso].setPosition(0.9f * (c / 2), (r) * -0.5f, 0);
				}
				else{
					map[iso].setPosition(0.90f * (c / 2) + 0.45f, -0.25f + r * -0.5f, 0);
				}
			}
		}

	}
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
	
	public Iso getIso(int i, int j){
		return map[i + j * row];
		
	}
	
	/* Need to redo these calculations
	public Iso getIso(int x, int y){
		return map.get(x*col + y);
	}*/
	
	public void draw(float[] mvpMatrix){
		
		for(int i = 0; i < map.length; i++){
			//if(Math.abs(map.get(i).getPosition()[0] - Camera.getPosition()[0]) < MapFrag.size * 0.45f){
				map[i].draw(mvpMatrix);
			//}
			//System.out.println(i);
		}
		
	}
	
	public void save(File file){
		
		try{
			FileOutputStream fStream = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fStream);
			
			out.writeObject(this);
			out.flush();
			out.close();
		}
		catch(Exception e){
			System.out.println("Map.makeSaveFile() error");
		}
	}
	
	/*
	public void printLocLast(){
		Iso iso = getIso(row-1, col-1);
		
		for(int k = 0; k < iso.getCPosition().length; k++){
			System.out.println(iso.getCPosition()[k] + "\n");
		}
	}
	public void printLoc(){
		Iso iso;
		
		for(int i = 0; i < row; i++){
			for(int j = 0; j < col; j++){
				iso = getIso(i,j);
				for(int k = 0; k < iso.getCPosition().length; k++){
					System.out.println(iso.getCPosition()[k] + "\n");
				}
				
				System.out.println(" ");
			}
		}
	}*/
}
