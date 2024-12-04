package screen;

import engine.Cooldown;
import engine.Core;
import engine.GameState;
import inventory_develop.StoryModeTrait;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class TraitScreen extends Screen {
    private static final int SELECTION_TIME = 200;
    private Cooldown selectionCooldown;

    private final GameState gameState;
    private final StoryModeTrait storyModeTrait;

    private String[] traits;
    private String[] rarities;
    private int traitIndex;

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width  Screen width.
     * @param height Screen height.
     * @param fps    Frames per second, frame rate at which the game is run.
     */
    public TraitScreen(int width, int height, int fps, GameState gameState, StoryModeTrait storyModeTrait, String[] traits) {
        super(width, height, fps);

        this.gameState = gameState;
        this.storyModeTrait = storyModeTrait;
        this.returnCode = 4;

        this.traits = traits;

        List<String> tempRaritys = new ArrayList<>();
        for (int i = 0; i < traits.length; i++) {
            String rarity = storyModeTrait.getTraits().get(traits[i]);
            rarity = storyModeTrait.setNextRarity(rarity);
            tempRaritys.add(rarity);
        }
        this.rarities = tempRaritys.toArray(new String[0]);
        this.traitIndex = 0;

        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();
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

    /**
     * Updates the elements on screen and checks for events.
     */
    protected final void update() {
        super.update();

        draw();
        if (this.selectionCooldown.checkFinished()
                && this.inputDelay.checkFinished()) {

            if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
                    || inputManager.isKeyDown(KeyEvent.VK_A)) {
                indexleft();
                this.selectionCooldown.reset();
//                SoundManager.getInstance().playES("menuSelect_es");
            }

            if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                    || inputManager.isKeyDown(KeyEvent.VK_D)) {
                indexright();
                this.selectionCooldown.reset();
//                SoundManager.getInstance().playES("menuSelect_es");
            }

            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
                    && this.inputDelay.checkFinished()) {
                this.applyTrait();
                this.isRunning = false;
            }
        }
    }
    
    // space를 눌렀을 때 레벨에 따라 StoryModeTrait을 호출해서 trait 적용
    private void applyTrait() {
        if(gameState.getLevel() == 4) { // 보스 깨면
            storyModeTrait.upgradeSelectedTraits(traits[0]);
            storyModeTrait.upgradeSelectedTraits(traits[1]);
            storyModeTrait.upgradeSelectedTraits(traits[2]);
        } else {
            storyModeTrait.upgradeSelectedTraits(traits[traitIndex]);
        }
    }
    
    // 특성 인덱스 값을 변경
    private void indexleft() {
        if(this.traitIndex > 0)
            this.traitIndex--;
    }
    private void indexright() {
        if(this.traitIndex < 2)
            this.traitIndex++;
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawTarit(this, this.gameState, this.traits, this.rarities, this.traitIndex);

        super.drawPost();
        drawManager.completeDrawing(this);

    }
}
