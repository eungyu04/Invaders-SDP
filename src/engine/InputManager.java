package engine;

import java.awt.event.*;

/**
 * Manages keyboard input for the provided screen.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public final class InputManager implements KeyListener, MouseListener, MouseMotionListener {

	/** Number of recognised keys. */
	private static final int NUM_KEYS = 256;
	/** Number of recognised mouse buttons. */
	private static final int NUM_MOUSE_BUTTONS = 5;
	/** Array with the jeys marked as pressed or not. */
	private static boolean[] keys;
	/** Array with the mouse buttons marked as pressed or not. */
	private static boolean[] mouseButtons;
	/** Mouse cursor X position. */
	private static int mouseX;
	/** Mouse cursor Y position. */
	private static int mouseY;
	/** Singleton instance of the class. */
	private static InputManager instance;

	/**
	 * Private constructor.
	 */
	private InputManager() {
		keys = new boolean[NUM_KEYS];
		mouseButtons = new boolean[NUM_MOUSE_BUTTONS];
		mouseX = 0;
		mouseY = 0;
	}

	/**
	 * Returns shared instance of InputManager.
	 * 
	 * @return Shared instance of InputManager.
	 */
	protected static InputManager getInstance() {
		if (instance == null)
			instance = new InputManager();
		return instance;
	}

	/**
	 * Returns true if the provided key is currently pressed.
	 * 
	 * @param keyCode
	 *            Key number to check.
	 * @return Key state.
	 */
	public boolean isKeyDown(final int keyCode) {
		return keys[keyCode];
	}

	/**
	 * Changes the state of the key to pressed.
	 * 
	 * @param key
	 *            Key pressed.
	 */
	@Override
	public void keyPressed(final KeyEvent key) {
		if (key.getKeyCode() >= 0 && key.getKeyCode() < NUM_KEYS)
			keys[key.getKeyCode()] = true;
	}

	/**
	 * Changes the state of the key to not pressed.
	 * 
	 * @param key
	 *            Key released.
	 */
	@Override
	public void keyReleased(final KeyEvent key) {
		if (key.getKeyCode() >= 0 && key.getKeyCode() < NUM_KEYS)
			keys[key.getKeyCode()] = false;
	}

	/**
	 * Does nothing.
	 * 
	 * @param key
	 *            Key typed.
	 */
	@Override
	public void keyTyped(final KeyEvent key) {

	}

	// 마우스
	/**
	 * Returns true if the provided mouse button is currently pressed.
	 *
	 * @param button
	 *            Mouse button to check (1 = left, 2 = middle, 3 = right).
	 * @return Mouse button state.
	 */
	public boolean isMouseButtonDown(final int button) {
		return button >= 1 && button <= NUM_MOUSE_BUTTONS && mouseButtons[button - 1];
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		if (e.getButton() >= 1 && e.getButton() <= NUM_MOUSE_BUTTONS)
			mouseButtons[e.getButton() - 1] = true;
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		if (e.getButton() >= 1 && e.getButton() <= NUM_MOUSE_BUTTONS)
			mouseButtons[e.getButton() - 1] = false;
	}

	/**
	 *
	 * Returns the current X position of the mouse cursor.
	 *
	 * @return Mouse X position.
	 */
	public int getMouseX() {
		return mouseX;
	}

	/**
	 * Returns the current Y position of the mouse cursor.
	 *
	 * @return Mouse Y position.
	 */
	public int getMouseY() {
		return mouseY;
	}

	/**
	 * Tracks the mouse cursor's position.
	 *
	 * @param e
	 *            Mouse moved.
	 */
	@Override
	public void mouseMoved(final MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}
	@Override
	public void mouseEntered(MouseEvent e) {

	}
	@Override
	public void mouseExited(MouseEvent e) {

	}

}