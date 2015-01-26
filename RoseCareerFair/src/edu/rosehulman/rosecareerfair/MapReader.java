package edu.rosehulman.rosecareerfair;

import java.util.ArrayList;

public class MapReader {
	private ArrayList<ArrayList<String>> mTables;
	public MapReader(){
		
	}
	
	public void readMap(String s){
		char a;
		int index=0;
		int row=0;
		StringBuilder sb = new StringBuilder();
		while(index<s.length()){
			a =  s.charAt(index);
			if(a=='\n'){
				mTables.get(row).add(sb.toString());
				row++;
				index++;
				sb = new StringBuilder();
			} else if (a==','){
				mTables.get(row).add(sb.toString());
				index++;
				sb = new StringBuilder();
			} else{
				sb.append(a);
				index++;
			}
		}
	}
}
