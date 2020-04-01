package edu.duke.ece651.player;

import edu.duke.ece651.shared.*;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.*;

public class Player extends Application {
  private HashMap<Integer, ArrayList<Territory>> territoryMap;
  private Pair<Integer, String> playerInfo;
  private ArrayList<Action> MoveAction;
  private ArrayList<Action> AttackAction;
  private HashMap<Integer, ArrayList<Action>> AllAction;
  private ArrayList<Upgrade> UpgradeAction;
  private Displayable displayer;
  private Communicator communicator;
  private int playerNum;
  private int FoodResource;
  private int TechResource;
  private Stage Window;
  private int id;

  public Player() {
    this.territoryMap = new HashMap<>();
    this.MoveAction = new ArrayList<>();
    this.AttackAction = new ArrayList<>();
    this.AllAction = new HashMap<>();
    this.communicator = new Communicator("127.0.0.1", 1234);
    this.playerNum = 0;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Scanner s = new Scanner(System.in);
    Window = primaryStage;
    Button BtnStart = new Button("Start Game");
    Label GameName = new Label("Strategic Conquest Game (RISC)");
    GridPane layout = new GridPane();
    layout.setVgap(30);
    layout.setHgap(30);
    layout.setAlignment(Pos.CENTER);
    layout.add(GameName, 0, 0);
    layout.add(BtnStart, 0,2);
    Scene Start = new Scene(layout, 300, 250);
    Window.setScene(Start);
    BtnStart.setOnAction(e->init(s));
    Window.show();

  }


  public void init(Scanner scanner) {

    //the first player input the total number of players
    if (id == 0) {
      System.out.println(
          "=======You're the first player, please enter the number of all players ([2:5])========");
      int playerNum = scanner.nextInt();
      while (playerNum < 2 || playerNum > 5) {
        System.out.println("========Invalid playerNumber, try again ([2:5])========");
        playerNum = scanner.nextInt();
      }
      sendString(String.valueOf(playerNum));
      //send it to server
    }
    // System.out.println("[DEBUG] my id is " + id);
    String color = new ColorID().getPlayerColor(id);
    this.playerInfo = new Pair<>(id, color);
    playerNum = Integer.parseInt(receiveString());
  }

  public void PlayGame(Scanner scanner) throws IOException {
    String msg;
    boolean Ask = false;
    boolean Lose = false;
    boolean LoseButWatch = false;

    while (true) {
      msg = receiveString();
      //if the msg is game end, end the game
      if (msg.contains("Game End!")) {
        System.out.println(msg);
        break;
      }
      if (msg.contains("Lose Game") && !Ask) {
        Ask = true;
        Lose = true;
        System.out.println("========You lose the game========\n"
                    + "Do you want to still watch the game? Please choose Y/N");
            //wait for player to input their choice
            while (true) {
              String choice = scanner.nextLine().toUpperCase();
              //make sure they only input Y/N
              if (!choice.equals("Y") && !choice.equals("N")) {
                System.out.println("Your Input is invalid.\n"
                        + "Please choose Y/N");
                continue;
              }
              //send the choice to server
              sendString(choice);
              if (choice.equals("Y")) {
                LoseButWatch = true;
            System.out.println("Choose Y");
            break;
          } else {
            return;
          }
        }
      }
      MyFormatter myformatter = new MyFormatter(playerNum);
      if (!LoseButWatch) {
        //if it is lose but watch this turn will not display the current world map
        territoryMap.clear();
        // System.out.println("Received Map = " + msg);
        myformatter.MapParse(territoryMap, msg);
        displayMap();
      }
      LoseButWatch = false;
      
      WaitAction(Lose, myformatter);
    }
  }

  public void WaitAction(boolean Lose, MyFormatter myformatter) throws IOException {
    if (!Lose) {
      //send player input actions to server: move actions and attack actions
      OperateAction PlayerAction = new OperateAction(playerInfo, territoryMap);
      PlayerAction.readAction();

      //Send upgrades first
      UpgradeAction = PlayerAction.getUpgradeActions();
      CheckUpgrade();

      String UpgradeString = myformatter.UpgradeCompose(UpgradeAction).toString();
      sendString(UpgradeString);

      MoveAction = PlayerAction.getMoveActions();
      // System.out.println("[DEBUG PLAYER] Size Move Action" + this.MoveAction.size());
      String MoveString = myformatter.ActionCompose(MoveAction, "Move").toString();
      sendString(MoveString);
      AttackAction = PlayerAction.getAttackActions();
      String AttackString = myformatter.ActionCompose(AttackAction, "Attack").toString();
      sendString(AttackString);
      //receive the result of these actions from server
      System.out.println("Action Validate : " + receiveString());
    }
    //receive all players' actions
    String OtherActions = receiveString();
    // System.out.println(OtherActions);
    if (OtherActions.contains("valid")) {
      //if it is the turn game end and if received validation of the actions
      //receive another time to all actions
      OtherActions = receiveString();
    }
    AllAction.clear();
    myformatter.AllActionParse(AllAction, OtherActions);
    displayAction();
  }

  public void CheckUpgrade() throws IOException {
      Cost Cal = new Cost();
      int TotalCost = 0;
      //calculate the total cost to do the upgrade actions
      for(Upgrade Current: UpgradeAction){
        TotalCost = TotalCost + Current.getNumber() * Cal.getCosts(Current.getPrevLevel(), Current.getNextLevel());
      }
      //if the Actual cost to do the upgrade is already higher than the technology resources, clear UpgradeAction
      if(TotalCost > TechResource){
        UpgradeAction.clear();
        return;
      }
      TechResource -= TotalCost;
  }



  public void sendString(String str) {
    communicator.sendString(str);
  }

  public String receiveString() {
    return communicator.receive();
  }

  public void addDisplayable(Displayable d) {
    this.displayer = d;
  }

  public void displayMap() {
    displayer.showMap(territoryMap, playerInfo);
  }
  public void displayAction() {
    displayer.showAction(AllAction, playerInfo);
  }
  public void close() {
    communicator.close();
  }
  //for testcases
  public void setTerritoryMap(HashMap<Integer, ArrayList<Territory>> TestMap){
    this.territoryMap = TestMap;
  }
  public void setAllAction(HashMap<Integer, ArrayList<Action>> TestAllAction){
    this.AllAction = TestAllAction;
  }
  public void setPlayerInfo(Pair<Integer, String> TestPlayerInfo){
    this.playerInfo = TestPlayerInfo;
  }
  public void setID(int ID){this.id = ID;}
  public static void main(String[] args) throws IOException {
    Scanner scanner = new Scanner(System.in);
    Player player = new Player();
    Displayable d = new Text();

    player.addDisplayable(d);

    System.out.println("Waiting for id");
    player.setID(Integer.parseInt(player.receiveString()));
    System.out.println("Get id");

    launch(args);

    //player.init(scanner);
    //player.PlayGame(scanner);
    //close the socket
    //player.close();
  }
}
