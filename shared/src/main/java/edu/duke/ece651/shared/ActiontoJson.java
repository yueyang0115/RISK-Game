package edu.duke.ece651.shared;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class ActiontoJson {
  private JSONObject ans;
  private JSONObject ansAllAction;
  private ArrayList<Action> actionList;
  private HashMap<Integer, ArrayList<Action>> AllAction; 
  public ActiontoJson(ArrayList<Action> myactionList) {
    this.ans = new JSONObject();
    this.actionList = myactionList;
    getActionListObj();
  }
  public ActiontoJson(HashMap<Integer, ArrayList<Action>> Actions){
    this.ansAllAction = new JSONObject();
    this.AllAction = Actions;
  }
  
  
  public JSONObject getJSON() {
    return this.ans;
  }
  
  public void ComposeAllAction(){
    for (HashMap.Entry<Integer, ArrayList<Action>> entry : AllAction.entrySet()){
      JSONArray currActionArray = new JSONArray();
      for(int i = 0; i < entry.getValue().size(); i++) {
        JSONObject actionObj = new JSONObject();
        Action action = entry.getValue().get(i);
        getActionObj(actionObj, action);
        currActionArray.put(actionObj);
      }
      String insertKey = "player_" + entry.getKey();
      ansAllAction.put(insertKey, currActionArray);
    }
  }
  public JSONObject getAllAction(){
    return this.ansAllAction;
  }
  private void getActionListObj() {
    JSONArray actionArray = new JSONArray();
    getActionArray(actionArray);
    ans.put(actionList.get(0).getType(), actionArray);
  }

  public void getActionArray(JSONArray actionArray) {
    for (int i = 0; i < actionList.size(); i++) {
      JSONObject actionObj = new JSONObject();
      Action action = actionList.get(i);
      getActionObj(actionObj, action);
      actionArray.put(actionObj);
    }
  }

  public void getActionObj(JSONObject actionObj, Action action) {
    MaptoJson myMaptoJson = new MaptoJson();
    JSONObject srcTerritoryObj = new JSONObject();
    myMaptoJson.getTerritoryObj(srcTerritoryObj, action.getSrc());
    actionObj.put("src", srcTerritoryObj);

    JSONObject dstTerritoryObj = new JSONObject();
    myMaptoJson.getTerritoryObj(dstTerritoryObj, action.getDst());
    actionObj.put("dst", dstTerritoryObj);

    int soldiers = action.getSoliders();
    actionObj.put("soldiers", soldiers);

    String owner = action.getOwner();
    actionObj.put("owner", owner);
  }
}