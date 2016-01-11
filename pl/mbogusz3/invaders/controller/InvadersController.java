package pl.mbogusz3.invaders.controller;

import pl.mbogusz3.invaders.controller.eventHandlers.*;
import pl.mbogusz3.invaders.model.InvadersModel;
import pl.mbogusz3.invaders.types.InvadersEvent;
import pl.mbogusz3.invaders.types.InvadersEventHandler;
import pl.mbogusz3.invaders.types.InvadersExitException;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class InvadersController {
	private final LinkedBlockingQueue<InvadersEvent> eventQueue;
	private final InvadersModel model;
	private final HashMap<String, InvadersEventHandler> eventHandlerMap;

	private boolean running;
	private Thread tickRateThread;
	private final static int tickRate = 60;
	private double frameTime;

	public InvadersController(InvadersModel model) {
		this.eventQueue = new LinkedBlockingQueue<InvadersEvent>();
		this.model = model;
		this.eventHandlerMap = new HashMap<String, InvadersEventHandler>();
		this.fillEventHandlerMap();
		this.running = false;
	}

	public void putEvent(InvadersEvent event) {
		try {
			eventQueue.put(event);
		} catch(InterruptedException exception) {
			exception.printStackTrace();
		}
	}

	public void start() {
		InvadersEvent event;
		InvadersEventHandler eventHandler;

		this.startTimer();

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

	public double getFPS() {
		return (1.0/frameTime);
	}

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

	private void stopTimer() {
		this.running = false;
		try {
			this.tickRateThread.join();
		} catch (InterruptedException e) {
			System.out.println("Failed stopping tickrate thread: " + e);
		}
	}

	private void fillEventHandlerMap() {
		this.eventHandlerMap.put("newGame", new OnNewGameEvent(this.model));
		this.eventHandlerMap.put("exitGame", new OnExitEvent(this.model));
		this.eventHandlerMap.put("keyDown", new OnKeyDownEvent(this.model));
		this.eventHandlerMap.put("keyUp", new OnKeyUpEvent(this.model));
		this.eventHandlerMap.put("windowResized", new OnWindowResizedEvent(this.model));
	}
}