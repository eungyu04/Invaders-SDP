package screen;

import engine.Cooldown;
import engine.DrawManager;
import engine.GameState;
import engine.InputManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StoryScoreScreenTest {

    private StoryScoreScreen storyScoreScreen;
    private GameState mockGameState;
    private Cooldown mockCooldown;
    private InputManager mockInputManager;
    private DrawManager mockDrawManager;

    Screen currentScreen;

    @BeforeEach
    void setUp() {
        mockCooldown = mock(Cooldown.class);
        mockInputManager = mock(InputManager.class);
        mockGameState = mock(GameState.class);
        mockDrawManager = mock(DrawManager.class);

        when(mockCooldown.checkFinished()).thenReturn(true);

        // StoryScoreScreen 초기화
        currentScreen = new StoryScoreScreen(800, 600, 60, mockGameState);
        currentScreen.inputManager = mockInputManager;
        currentScreen.drawManager = mockDrawManager;

        storyScoreScreen = (StoryScoreScreen) currentScreen;
        storyScoreScreen.setSelectionCooldown(mockCooldown);
    }

    @Test
    void level8_clear() {
        when(mockGameState.getLevel()).thenReturn(9);
        when(mockGameState.getLivesRemaining()).thenReturn(3);

        // 아무것도 안 눌렀을 때 (스토리 점수)
        storyScoreScreen.update();
        assertTrue(storyScoreScreen.getCurrentScoreScreen(), "Score screen should activate.");  // 변수 확인
        assertFalse(storyScoreScreen.getcurrentCreditScreen(), "Credit screen should deactivate.");
        verify(mockDrawManager).drawStoryGameOver(storyScoreScreen, mockGameState);     // drawManager 호출 확인

        // 1-1. space_bar 한 번 눌렀을 때 (엔딩 크레딧)
        when(mockInputManager.isKeyDown(KeyEvent.VK_SPACE)).thenReturn(true).thenReturn(false);
        storyScoreScreen.update();
        storyScoreScreen.update();  // 바뀐 변수로 draw()를 호출하기 위함

        assertFalse(storyScoreScreen.getCurrentScoreScreen(), "Score screen should deactivate.");   // 변수 확인
        assertTrue(storyScoreScreen.getcurrentCreditScreen(), "Credit screen should activate.");
        verify(mockDrawManager).drawEndingCredit(storyScoreScreen);     // drawManager 호출 확인

        // 1-2. space_bar 한 번 더 눌렀을 때 (메인 화면)
        when(mockInputManager.isKeyDown(KeyEvent.VK_SPACE)).thenReturn(true).thenReturn(false);
        storyScoreScreen.update();
        storyScoreScreen.update();

        assertFalse(storyScoreScreen.getcurrentCreditScreen(), "Credit screen should deactivate.");
        assertEquals(1, storyScoreScreen.returnCode);
    }

    @Test
    void level8_died() {
        when(mockGameState.getLevel()).thenReturn(9);
        when(mockGameState.getLivesRemaining()).thenReturn(0);

        // 아무것도 안 눌렀을 때 (스토리 점수)
        storyScoreScreen.update();

        assertTrue(storyScoreScreen.getCurrentScoreScreen(), "Score screen should activate.");
        assertFalse(storyScoreScreen.getcurrentCreditScreen(), "Credit screen should deactivate.");
        verify(mockDrawManager).drawStoryGameOver(storyScoreScreen, mockGameState);

        // 2. space_bar 한 번 눌렀을 때 (메인 화면)
        when(mockInputManager.isKeyDown(KeyEvent.VK_SPACE)).thenReturn(true).thenReturn(false);
        storyScoreScreen.update();
        storyScoreScreen.update();

        assertEquals(1, storyScoreScreen.returnCode);
    }



    @Test
    void level1_7() {
        when(mockGameState.getLevel()).thenReturn(1);
        when(mockGameState.getLivesRemaining()).thenReturn(3);

        // 아무것도 안 눌렀을 때
        storyScoreScreen.update();

        assertTrue(storyScoreScreen.getCurrentScoreScreen(), "Score screen should activate.");
        assertFalse(storyScoreScreen.getcurrentCreditScreen(), "Credit screen should deactivate.");
        verify(mockDrawManager).drawStoryGameOver(storyScoreScreen, mockGameState);


        // 3. space_bar 한 번 눌렀을 때 (게임 재시작)
        when(mockInputManager.isKeyDown(KeyEvent.VK_SPACE)).thenReturn(true).thenReturn(false);
        storyScoreScreen.update();
        storyScoreScreen.update();

        assertEquals(4, storyScoreScreen.returnCode);


        // 4. esc 한 번 눌렀을 때 (메인 메뉴)
        when(mockInputManager.isKeyDown(KeyEvent.VK_ESCAPE)).thenReturn(true).thenReturn(false);
        storyScoreScreen.update();
        storyScoreScreen.update();

        assertEquals(1, storyScoreScreen.returnCode);
    }
}