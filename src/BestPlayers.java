/*
 * BestPlayers.java
 *
 * Created on January 11, 2007, 9:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Rommel
 */

import java.io.*;
import java.util.*;

public class BestPlayers implements Serializable{
	private String[][] bestTimes;
	private String[][] fastMoves;
	private int bestTimesCounter, fastMovesCounter;
	private transient int btIndex, fmIndex;


	public BestPlayers(){
		bestTimes = new String[5][2];
		fastMoves = new String[5][2];
	}

	public boolean isBestTime(String time){
		int duration = getDuration(time);

		for(int i = 0; i < bestTimes.length; i++){
			if(bestTimes[i][0] == null){
				btIndex = i;
				return true;
			}else if(duration < getDuration(bestTimes[i][1])){
				btIndex = i;
				return true;
			}
		}
		btIndex = Solitaire.NO_ACTION;
		return false;
	}

	public void addBestTime(String name, String time){
		name = trimName(name);

		if(btIndex == Solitaire.NO_ACTION){
			return;
		}

		if(bestTimes[btIndex][0] == null){
			addToRecord(name, time);
			bestTimesCounter++;
		}else{
			moveBestTimes(btIndex);
			addToRecord(name, time);
			if(bestTimesCounter < bestTimes.length){
				bestTimesCounter++;
			}
		}
	}

	private int getDuration(String time){
		StringTokenizer tokenizer = new StringTokenizer(time, ":");
		int min = Integer.parseInt((String)tokenizer.nextElement());
		int sec = Integer.parseInt((String)tokenizer.nextElement());
		return min*60+sec;
	}

	private void addToRecord(String name, String time){
		bestTimes[btIndex][0] = name;
		bestTimes[btIndex][1] = time;
	}

	private void moveBestTimes(int index){
		for(int j = bestTimes.length-1; j > index; j--){
			bestTimes[j][0] = bestTimes[j-1][0];
			bestTimes[j][1] = bestTimes[j-1][1];
		}
	}

	public boolean isFastMove(int moves){
		for(int i = 0; i < fastMoves.length; i++){
			if(fastMoves[i][0] == null){
				fmIndex = i;
				return true;
			}else if(moves < getMoves(fastMoves[i][1])){
				fmIndex = i;
				return true;
			}
		}
		fmIndex = Solitaire.NO_ACTION;
		return false;
	}

	public void addFastMove(String name, int moves){
		name = trimName(name);

		if(fmIndex == Solitaire.NO_ACTION){
			return;
		}

		if(fastMoves[fmIndex][0] == null){
			addToRecord(name, moves);
			fastMovesCounter++;
		}else if(moves < getMoves(fastMoves[fmIndex][1])){
			moveFastMoves(fmIndex);
			addToRecord(name, moves);
			if(fastMovesCounter < fastMoves.length){
				fastMovesCounter++;
			}
		}
	}

	private int getMoves(String moves){
		return Integer.parseInt(moves);
	}

	private void addToRecord(String name, int moves){
		fastMoves[fmIndex][0] = name;
		fastMoves[fmIndex][1] = new Integer(moves).toString();
	}

	private void moveFastMoves(int index){
		for(int j = fastMoves.length-1; j > index; j--){
			fastMoves[j][0] = fastMoves[j-1][0];
			fastMoves[j][1] = fastMoves[j-1][1];
		}
	}

	private String trimName(String name){
		name = name.trim();
		if(name.length() > 15){
			return name.substring(0, 15);
		}else{
			return name;
		}
	}

	public String[][] getBestTimes(){
		return bestTimes;
	}

	public String[][] getFastMoves(){
		return fastMoves;
	}
}