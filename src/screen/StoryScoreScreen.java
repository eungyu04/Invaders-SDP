package screen;

import engine.Cooldown;
import engine.Core;
import engine.GameState;

import java.awt.event.KeyEvent;

public class StoryScoreScreen extends Screen {
    private static final int SELECTION_TIME = 200;

    private final GameState gameState;

    private Cooldown selectionCooldown;
    private boolean currentScoreScreen;
    private boolean currentCreditScreen;

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width
     *            Screen width.
     * @param height
     *            Screen height.
     * @param fps
     *            Frames per second, frame rate at which the game is run.
     */
    public StoryScoreScreen(final int width, final int height, final int fps, GameState gameState) {
        super(width, height, fps);

        this.currentScoreScreen = true;
        this.currentCreditScreen = false;

        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();

        this.gameState = gameState;
        this.returnCode = 1;

    }

    /**
     * Starts the action.
     *
     * @return Next screen code.
     */
    public final int run() {
        super.run();

        return this.returnCode;
    }


    protected final void update() {
        super.update();

        draw();
        if (currentScoreScreen && this.selectionCooldown.checkFinished()) {
            if (gameState.getLevel() == 9 && inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                // 1-1. 8레벨 클리어 (space 누르면 엔딩 크레딧)
                if (gameState.getLivesRemaining() > 0) {
                    currentScoreScreen = false;
                    currentCreditScreen = true;
                    this.selectionCooldown.reset();
                    // 2. 8레벨 게임 오버 (space 누르면 메인 화면)
                } else {
                    this.returnCode = 1;
                    this.isRunning = false;
                }
            }
            if (gameState.getLevel() != 9 ) {
                // 3. 1~7레벨 (space 누르면 게임 재시작)
                if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {//&& inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                    this.returnCode = 4;
                    this.isRunning = false;
                }
                // 4. 1~7레벨 (esc 누르면 메인 화면)
                else if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
                    this.returnCode = 1;
                    this.isRunning = false;
                }
            }
        }

        // 1-2. 8레벨 클리어 후 엔딩 크레딧 (space 또는 esc 누르면 메인 화면)
        if (currentCreditScreen && this.selectionCooldown.checkFinished()) {
            if ((inputManager.isKeyDown(KeyEvent.VK_SPACE)
                    || inputManager.isKeyDown(KeyEvent.VK_ESCAPE))) {
                currentCreditScreen = false;
                this.returnCode = 1;
                this.isRunning = false;
            }
        }
    }

    private void draw() {
        drawManager.initDrawing(this);

        if (gameState.getLevel() == 9) {
            if (currentScoreScreen)
                drawManager.drawStoryGameOver(this, gameState);
            if (this.gameState.getLivesRemaining() > 0 && currentCreditScreen)
                drawManager.drawEndingCredit(this);
        } else {
            drawManager.drawStoryGameOver(this, gameState);
        }

        super.drawPost();
        drawManager.completeDrawing(this);
    }

    // for test
    protected boolean getCurrentScoreScreen() {
        return currentScoreScreen;
    }
    protected boolean getcurrentCreditScreen() {
        return currentCreditScreen;
    }
    protected void setSelectionCooldown(Cooldown selectionCooldown) {
        this.selectionCooldown = selectionCooldown;
    }
}
