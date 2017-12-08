package gamestates;

public class GameStateManager {
    private static GameStateManager mGameStateManager;
    private GameState mGameState;
    private GameStateManager() {
        mGameState=null;
    }

    public GameStateManager getInstance() {
        if (mGameStateManager == null) {
            mGameStateManager=new GameStateManager();
        }
        return mGameStateManager;

    }
    public static void changeGamestate(GameState changer){
        mGameStateManager.mGameState = changer;

    }
    public void render(){

    }
    public void update(){

    }
}
