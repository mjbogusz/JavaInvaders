package pl.mbogusz3.invaders.controller;

import pl.mbogusz3.invaders.controller.eventHandlers.*;
import pl.mbogusz3.invaders.model.InvadersModel;
import pl.mbogusz3.invaders.types.InvadersEvent;
import pl.mbogusz3.invaders.types.InvadersEventHandler;
import pl.mbogusz3.invaders.types.InvadersExitException;
import pl.mbogusz3.invaders.types.InvadersNewGameException;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Controller class of Invaders game.
 * Responsible of managing the event queue (communication on View->Model line), executing received events and running timer loop (executing calculations every tick).
 */
public class InvadersController {
	/**
	 * Tickrate - rate at which internal ticks occur, i.e. how many times per second a new model state is being calculated based on passed time.
	 */
	public final static int tickRate = 60;
	/**
	 * Event queue, main Controller's concern.
	 */
	private final LinkedBlockingQueue<InvadersEvent> eventQueue;
	/**
	 * Invaders' game model, storing data and doing calculations.
	 */
	private final InvadersModel model;
	/**
	 * Event handlers' map, keyed by event name.
	 */
	private final HashMap<String, InvadersEventHandler> eventHandlerMap;
	/**
	 * Whether the timer thread is running.
	 */
	private boolean running;
	/**
	 * Timer loop thread, executing calculations every tick.
	 */
	private Thread tickRateThread;
	/**
	 * Last frame time, used only for getFPS().
	 */
	private double frameTime;

	/**
	 * Constructor of InvadersController class, initializing inner event queue and event handlers.
	 * @param model InvadersModel object, used for all the calculations.
	 */
	public InvadersController(InvadersModel model) {
		this.eventQueue = new LinkedBlockingQueue<InvadersEvent>();
		this.model = model;
		this.eventHandlerMap = new HashMap<String, InvadersEventHandler>();
		this.fillEventHandlerMap();
		this.running = false;
	}

	/**
	 * Put an event into the event queue.
	 * @param event Event to put in queue
	 */
	public void putEvent(InvadersEvent event) {
		try {
			eventQueue.put(event);
		} catch(InterruptedException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Start the main game loop - begin processing events.
	 */
	public void start() {
		InvadersEvent event;
		InvadersEventHandler eventHandler;

		System.out.println("Controller: starting main event loop");
		while (true) {
			try {
				event = eventQueue.take();
			} catch(Exception e) {
				System.out.print("Unexpected exception on event taking: " + e);
				continue;
			}

			try {
				eventHandler = this.eventHandlerMap.get(event.type);
				eventHandler.execute(event.payload);
			} catch(NullPointerException e) {
				System.out.println("Unknown event!");
			} catch(InvadersNewGameException e) {
				System.out.println("new game!");
				this.startTimer();
			} catch(InvadersExitException e) {
				System.out.println("Caught exit exception, ending!");
				break;
			} catch(Exception e) {
				System.out.println("Unexpected exception on event handling: " + e);
			}
		}

		this.stopTimer();
		System.exit(0);
	}

	/**
	 * Retrieve current 'backend' FPS (1/actual tickrate)
	 * @return current FPS count (not cached/averaged!)
	 */
	public double getFPS() {
		return (1.0/frameTime);
	}

	/**
	 * Start inner timer thread, executing calculations every tick
	 */
	private void startTimer() {
		System.out.println("Controller: starting timer");

		Runnable tickRateRunnable = () -> {
			long previousTime = System.nanoTime();
			long newTime;
			long nextTime = previousTime + 1000000000 / tickRate;

			long millisDelay = 1000/tickRate;

			InvadersController.this.running = true;
			while (InvadersController.this.running) {
				try {
					Thread.sleep(millisDelay);
				} catch (InterruptedException e) {
					System.out.println("Failed to sleep server timer thread: " + e);
				}

				newTime = System.nanoTime();
				InvadersController.this.frameTime = (double)(newTime - previousTime) / 1000000000.0;
				previousTime = newTime;
				nextTime = previousTime + 1000000000 / tickRate;

				synchronized (InvadersController.this.model) {
					InvadersController.this.model.recalculate(frameTime);
				}

				// Check game over or won
				if(InvadersController.this.model.isGameOver() || InvadersController.this.model.isGameWon()) {
					InvadersController.this.stopTimer();
				}

				long delay = (nextTime - System.nanoTime());
				if (delay < 0) {
					delay = 0;
				}
				delay += 500000;
				millisDelay = delay / 1000000;
			}
		};
		this.tickRateThread = new Thread(tickRateRunnable);
		this.tickRateThread.start();
	}

	/**
	 * Wait for inner timer thread to stop.
	 */
	private void stopTimer() {
		this.running = false;
		if(this.tickRateThread != null) {
			try {
				this.tickRateThread.join();
			} catch (InterruptedException e) {
				System.out.println("Failed stopping tickrate thread: " + e);
			}
		}
	}

	/**
	 * Fill up event handler map (statically).
	 */
	private void fillEventHandlerMap() {
		this.eventHandlerMap.put("newGame", new OnNewGameEvent(this.model));
		this.eventHandlerMap.put("exitGame", new OnExitEvent(this.model));
		this.eventHandlerMap.put("keyDown", new OnKeyDownEvent(this.model));
		this.eventHandlerMap.put("keyUp", new OnKeyUpEvent(this.model));
		this.eventHandlerMap.put("windowResized", new OnWindowResizedEvent(this.model));
	}
}