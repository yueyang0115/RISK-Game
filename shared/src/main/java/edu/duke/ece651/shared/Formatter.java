package edu.duke.ece651.shared;
import java.util.*;
import org.json.*;
public class Formatter{
  private int NumPlayers;
  Formatter(int num){
    NumPlayers = num;
  }
  public void MapCompose(){
  }
  public void MapParse(HashMap<Integer, ArrayList<Territory>>Input, String MapJson){
    JSONObject InputMap = new JSONObject(MapJson);
    for(int i = 0; i < NumPlayers; i++){
      JSONArray PlayerTemp = InputMap.getJSONArray("player_" + Integer.toString(i));
      System.out.println("[DEBUG] Player = " + i);
      ArrayList<Territory> InnerTerr = new ArrayList<Territory>();
      for(int j = 0; j < PlayerTemp.length(); j++){
        Territory Inner = new Territory();
        JSONObject TerrTemp = PlayerTemp.optJSONObject(j);
        String owner = TerrTemp.optString("owner");
        System.out.println("[DEBUG] OwnerName = " + owner);
        Inner.setOwner(owner);
        int SoldierNum = TerrTemp.optInt("soldiers");
        Inner.setSoldiers(SoldierNum);
        System.out.println("[DEBUG] SoldierNum = " + SoldierNum);
        JSONArray NeighborArray = TerrTemp.optJSONArray("neighbor");
        for(int k = 0; k < NeighborArray.length(); k++){
          JSONObject InnerNeigh = NeighborArray.optJSONObject(k);
          String NeighName = InnerNeigh.optString("neighbor_" + Integer.toString(k));
          System.out.println("[DEBUG] NeighborName_" + i + " = " + NeighName);
          Inner.setNeighbor(NeighName);
        }
        String TerritoryName = TerrTemp.optString("territoryName");
        Inner.setTerritoryName(TerritoryName);
        InnerTerr.add(Inner);
       }
      Input.put(i, InnerTerr);
    }
  }
}