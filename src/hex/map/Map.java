package hex.map;

import java.util.ArrayList;

import android.content.Context;

public class Map {

	private ArrayList<MapFrag> map = new ArrayList<MapFrag>();
	private int row, col;
	private Context context;
	
	public Map(Context context){
		this.context = context;
		float[] startPos = {0,0,0};
		
		map.add(new MapFrag(context, startPos));
		row = 1;
		col = 1;
		
		//addRow();
		//addCol();
	}
	
	public void addRow(){
		MapFrag frag;
		float[] pos = {MapFrag.size * 0.45f * row, MapFrag.size * -0.25f, 0};

		for(int i = 0; i < col; i++){
			frag = new MapFrag(context, pos);
			map.add(frag);
			
			/* iso calculations.  Probably doesn't effect MapFrags, but I'm leaving them here just in case.
			if(i % 2 == 0){
				frag.setPosition(0.9f * (i / 2), (row) * -0.5f ,0);
			}
			else{
				frag.setPosition(0.90f * (i / 2) + 0.45f, -0.25f + row * -0.5f, 0);
			}*/
		}
		row++;
	}
	
	public void addCol(){
		MapFrag frag;
		float[] pos = {MapFrag.size * 0.45f * row, MapFrag.size * -0.25f, 0};
		
		for(int i = row; i >= 0; i--){
			frag = new MapFrag(context, pos);
			map.add(i * col + col-1, frag);
			
			/* iso calculations.  Shouldn't be needed
			if(col % 2 == 0){
				iso.setPosition(0.9f * (col / 2), i * -0.5f, 0);
			}
			else{
				iso.setPosition(0.90f * (col / 2) + 0.45f, -0.25f + i * -0.5f, 0);
			}*/
		}
		
		col++;
		
	}
	
	// not finished
	public void removeRow(){
		if(row > 0){
			
			row--;
		}
	}
	
	// not finished
	public void removeCol(){
		if(col > 0){
			
			col--;
		}
	}
	
	public int getCol(){
		return col;
	}
	public int getRow(){
		return row;
	}
	
	/* Need to redo these calculations
	public Iso getIso(int x, int y){
		return map.get(x*col + y);
	}*/
	
	public void draw(float[] mvpMatrix){
		
		for(int i = 0; i < map.size(); i++){
			//if(Math.abs(map.get(i).getPosition()[0] - Camera.getPosition()[0]) < MapFrag.size * 0.45f){
				map.get(i).draw(mvpMatrix);
			//}
			//System.out.println(i);
		}
		
		//System.out.println(row + "\n" + col + "\n ");
		/*
		for(int i = 0; i < row; i++){
			for(int j = 0; j < col; j++){
				getIso(i,j).draw(mvpMatrix);
				//System.out.println(i + " " + j);
			}
		}*/
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
