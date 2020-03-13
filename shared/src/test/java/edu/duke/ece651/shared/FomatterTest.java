package edu.duke.ece651.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

public class FomatterTest {
  @Test
  public void test_Formater() {
    String Input =
        "{'player_0': [{'owner': 'aaa', 'soldiers': 3, 'neighbor': [{'neighbor_0': 'aaa_neigh1'}, {'neighbor_1': 'aaa_neigh2'}], 'territoryName': 'aaaTerr1'}, {'owner': 'aaa', 'soldiers': 4, 'neighbor': [{'neighbor_0': 'aaa_neigh3'}, {'neighbor_1': 'aaa_neigh4'}], 'territoryName': 'aaaTerr2'}],'player_1': [{'owner': 'bbb', 'soldiers': 5, 'neighbor': [{'neighbor_0': 'bbb_neigh1'}, {'neighbor_1': 'bbb_neigh2'}], 'territoryName': 'bbbTerr1'}, {'owner': 'bbb', 'soldiers': 6, 'neighbor': [{'neighbor_0': 'bbb_neigh3'}, {'neighbor_1': 'bbb_neigh4'}], 'territoryName': 'bbbTerr2'}]}";
    MyFormatter Map = new MyFormatter(2);
    HashMap<Integer, ArrayList<Territory>> TerritoryMap = new HashMap<>();
    Map.MapParse(TerritoryMap, Input);

    String InputMoveAction = "{'Move': [{'src':{'owner': 'aaa', 'soldiers': 3, 'neighbor': [{'neighbor_0': 'aaa_neigh1'}, {'neighbor_1': 'aaa_neigh2'}], 'territoryName': 'aaaTerr1'}, 'dst': {'owner': 'aaa', 'soldiers': 4, 'neighbor': [{'neighbor_0': 'aaa_neigh3'}, {'neighbor_1': 'aaa_neigh4'}], 'territoryName': 'aaaTerr2'}, 'soldiers': 12,'owner': 'aaa'}, {'src': {'owner': 'bbb', 'soldiers': 5, 'neighbor': [{'neighbor_0': 'bbb_neigh1'}, {'neighbor_1': 'bbb_neigh2'}], 'territoryName': 'bbbTerr1'}, 'dst':{'owner': 'bbb', 'soldiers': 6, 'neighbor': [{'neighbor_0': 'bbb_neigh3'}, {'neighbor_1': 'bbb_neigh4'}], 'territoryName': 'bbbTerr2'}, 'soldiers': 20, 'owner': 'bbb'}]}";
    ArrayList<Action> MoveActionArray = new ArrayList<>();
    Map.ActionParse(MoveActionArray, InputMoveAction);
    System.out.println("[DEBUG] Parse Action-Move:");
    for(Action Temp: MoveActionArray){
      System.out.println("ActionType = " + Temp.getType());
      System.out.println("ActionOwner = " + Temp.getOwner());  
      System.out.println("ActionSoldiers = " + Temp.getSoliders());
      System.out.println("Source TerritoryName = " + Temp.getSrc().getTerritoryName());
      System.out.println("Destination TerritoryName = " + Temp.getDst().getTerritoryName());
    }

    String InputAttackAction = "{'Attack': [{'src':{'owner': 'aaa', 'soldiers': 3, 'neighbor': [{'neighbor_0': 'aaa_neigh1'}, {'neighbor_1': 'aaa_neigh2'}], 'territoryName': 'aaaTerr1'}, 'dst': {'owner': 'aaa', 'soldiers': 4, 'neighbor': [{'neighbor_0': 'aaa_neigh3'}, {'neighbor_1': 'aaa_neigh4'}], 'territoryName': 'aaaTerr2'}, 'soldiers': 12,'owner': 'aaa'}, {'src': {'owner': 'bbb', 'soldiers': 5, 'neighbor': [{'neighbor_0': 'bbb_neigh1'}, {'neighbor_1': 'bbb_neigh2'}], 'territoryName': 'bbbTerr1'}, 'dst':{'owner': 'bbb', 'soldiers': 6, 'neighbor': [{'neighbor_0': 'bbb_neigh3'}, {'neighbor_1': 'bbb_neigh4'}], 'territoryName': 'bbbTerr2'}, 'soldiers': 20, 'owner': 'bbb'}]}";
    ArrayList<Action> AttackActionArray = new ArrayList<>();
    Map.ActionParse(AttackActionArray, InputAttackAction);
    System.out.println("\n\n[DEBUG] Parse Action-Attack:");
    for(Action Temp: AttackActionArray){
      System.out.println("ActionType = " + Temp.getType());
      System.out.println("ActionOwner = " + Temp.getOwner());  
      System.out.println("ActionSoldiers = " + Temp.getSoliders());
      System.out.println("Source TerritoryName = " + Temp.getSrc().getTerritoryName());
      System.out.println("Destination TerritoryName = " + Temp.getDst().getTerritoryName());
    }
  }
}
