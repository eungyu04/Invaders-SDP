package level_design;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import engine.DrawManager;
import engine.Frame;
import screen.GameScreen;

import javax.swing.*;

public class Background {
    private static Background instance;
    protected DrawManager drawManager;
    public static Graphics backBufferGraphics;

    private int backgroundWidth;
    private int backgroundHeight;
    private int screenWidth;
    private int screenHeight;
    private int horizontalOffset;
    private int verticalOffset;
    private int scrollSpeedHorizontal;
    private int scrollSpeedVertical;
    private int offsetUpdateInterval;
    private int storyModeScrollSpeedVertical;

    private Background() {
        // Empty constructor
    }

    public static synchronized Background getInstance() {
        if (instance == null) {
            instance = new Background();
        }
        return instance;
    }

    public void initialize(Frame frame) {
        this.screenWidth = frame.getWidth();
        this.screenHeight = frame.getHeight();
    }

    public void backgroundReset(int imageHeight, int imageWidth) {
        this.backgroundWidth = imageWidth;
        this.backgroundHeight = imageHeight;
        this.horizontalOffset = -screenWidth;
        this.verticalOffset = 0;
        this.scrollSpeedHorizontal = 3;
        this.scrollSpeedVertical = 10;
        this.offsetUpdateInterval = 0;
        this.storyModeScrollSpeedVertical = 20;
    }

    public static List<String> levelBackgrounds;
    private static final List<String> storyModeBackgrounds;
    // Static block to initialize levelBackgrounds
    static {
        levelBackgrounds = new ArrayList<>();
        levelBackgrounds.add("/backgrounds/background_level_1.jpg");
        levelBackgrounds.add("/backgrounds/background_level_2.jpg");
        levelBackgrounds.add("/backgrounds/background_level_3.jpg");
        levelBackgrounds.add("/backgrounds/background_level_4.jpg");
        levelBackgrounds.add("/backgrounds/background_level_5.jpg");
        levelBackgrounds.add("/backgrounds/background_level_6.jpg");
        levelBackgrounds.add("/backgrounds/background_level_7.jpg");
        levelBackgrounds.add("/backgrounds/background_level_8.jpg");

        storyModeBackgrounds = new ArrayList<>();
        storyModeBackgrounds.add("/Storybackgrounds/Sbackgrounds1-3,5-7.png"); // 예시
        storyModeBackgrounds.add("/Storybackgrounds/Sbackgrounds1-3,5-7.png"); // 예시
        storyModeBackgrounds.add("/Storybackgrounds/Sbackgrounds1-3,5-7.png"); // 예시
        storyModeBackgrounds.add("/Storybackgrounds/SbackgroundsBoss1.png"); // 예시
        storyModeBackgrounds.add("/Storybackgrounds/Sbackgrounds1-3,5-7.png"); // 예시
        storyModeBackgrounds.add("/Storybackgrounds/Sbackgrounds1-3,5-7.png"); // 예시
        storyModeBackgrounds.add("/Storybackgrounds/Sbackgrounds1-3,5-7.png"); // 예시
        storyModeBackgrounds.add("/Storybackgrounds/SbackgroundsBoss2.png"); // 예시

    }

    private static final List<String> storyModePainting = new ArrayList<>();
    static {
        storyModePainting.add("/Storybackgrounds/S1.png");
        storyModePainting.add("/Storybackgrounds/S2.png");
        storyModePainting.add("/Storybackgrounds/S3.png");
        storyModePainting.add("/Storybackgrounds/S4.png");
        storyModePainting.add("/Storybackgrounds/S5.png");
        storyModePainting.add("/Storybackgrounds/S6.png");
        storyModePainting.add("/Storybackgrounds/S7.png");
        storyModePainting.add("/Storybackgrounds/S8.png");
    }
    // Static method to get background image stream
    public static InputStream getBackgroundImageStream(int levelIndex, int returncode) {
        if (returncode == 4) { // story mode
            if (storyModeBackgrounds != null && levelIndex >= 0 && levelIndex <= storyModeBackgrounds.size()) {
                return Background.class.getResourceAsStream(storyModeBackgrounds.get(levelIndex - 1));
            } else {
                throw new IllegalArgumentException("Invalid index or storyModeBackgrounds not initialized");
            }
        } else {
            if (levelBackgrounds != null && levelIndex >= 0 && levelIndex <= levelBackgrounds.size()) {
                return Background.class.getResourceAsStream(levelBackgrounds.get(levelIndex - 1));
            } else {
                throw new IllegalArgumentException("Invalid index or levelBackgrounds not initialized");
            }
        }
    }
    public static InputStream getStoryModeBackgroundImageStream(int storyIndex) {
        if (storyModePainting != null && storyIndex >= 0 && storyIndex <= storyModePainting.size()) {
            return Background.class.getResourceAsStream(storyModePainting.get(storyIndex - 1));
        } else {
            throw new IllegalArgumentException("Invalid index or storyModePainting not initialized");
        }
    }

    // Dynamic method to update background image vertical offset
    public int getVerticalOffset(int returnCode, int level) {
        int scrollLimit = backgroundHeight - screenHeight;

        if (scrollLimit == -verticalOffset) {
            scrollSpeedVertical = 0;
        }

        if (offsetUpdateInterval % 3 == 0) {
            if (returnCode == 4) {
                if (!(level == 4 || level == 8)) {
                    verticalOffset += scrollSpeedVertical;
                } else {
                    verticalOffset = 0;
                }
            } else {
                verticalOffset -= scrollSpeedVertical;
            }
            offsetUpdateInterval = 0;
        }
        offsetUpdateInterval++;

        return verticalOffset;
    }

    // Dynamic method to update background image horizontal offset
    public int getHorizontalOffset(boolean backgroundMoveRight, boolean backgroundMoveLeft) {
        if (backgroundMoveRight) {
            horizontalOffset -= scrollSpeedHorizontal;
        }
        if (backgroundMoveLeft) {
            horizontalOffset += scrollSpeedHorizontal;
        }
        return horizontalOffset;
    }
}