package gamestates;

public class GameStateManager {
    //Class GameStateManager declares Member
    private static GameStateManager mGameStateManager;
    private GameState mGameState;
    private GameStateManager() {
        //private Constructor of GameStateManager to Singelies it
        mGameState=null;
    }

    public GameStateManager getInstance() {
        //Method getInstace to create Objekts from type GameStateManager and get they back to work with
        if (mGameStateManager == null) {
            mGameStateManager=new GameStateManager();
        }
        return mGameStateManager;

    }

    public static void changeGameState(GameState changer) {
        //Method changeGameState too change the Instance of mGamesState which is saved in mGameStateManager to the parameter
        mGameStateManager.mGameState = changer;

    }
    public void render(){

    }
    public void update(){

    }
}
