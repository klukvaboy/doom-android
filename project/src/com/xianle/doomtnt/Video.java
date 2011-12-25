// This string is autogenerated by ChangeAppSettings.sh, do not change spaces amount
package com.xianle.doomtnt;

import javax.microedition.khronos.opengles.GL10;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

import tk.niuzb.game.Keycodes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import android.widget.TextView;
import java.lang.Thread;
import java.util.concurrent.locks.ReentrantLock;
import android.os.Build;
import android.util.Log;
	abstract class DifferentTouchInput
	{
	    
		public static DifferentTouchInput getInstance()
		{
			if ((Build.VERSION.SDK_INT) <= 4){
				return SingleTouchInput.Holder.sInstance;}
			else{
                Log.v("doom", "multi touch");
				return MultiTouchInput.Holder.sInstance;}
		}
		public abstract void process(final MotionEvent event);
		private static class SingleTouchInput extends DifferentTouchInput
		{
		    private long mLastTouchTime = 0L;
			private static class Holder 
			{
				private static final SingleTouchInput sInstance = new SingleTouchInput();
			}
			public void process(final MotionEvent event)
			{
			     final long time = System.currentTimeMillis();
	        if (event.getAction() == MotionEvent.ACTION_MOVE && time - 
mLastTouchTime < 32) {
		        // Sleep so that the main thread doesn't get flooded with UI events.
		      return;
               
	        }
	        mLastTouchTime = time;

            
				int action = -1;
				if( event.getAction() == MotionEvent.ACTION_DOWN )
					action = 0;
				if( event.getAction() == MotionEvent.ACTION_UP )
					action = 1;
				if( event.getAction() == MotionEvent.ACTION_MOVE )
					action = 2;
				if ( action >= 0 )
					DemoGLSurfaceView.nativeMouse( (int)event.getX(), (int)event.getY(), action, 0, 
													(int)(event.getPressure() * 1000.0),
													(int)(event.getSize() * 1000.0) );
			}
		}
		private static class MultiTouchInput extends DifferentTouchInput
		{
		
            private long mLastTouchTime = 0L;
			private static class Holder 
			{
				private static final MultiTouchInput sInstance = new MultiTouchInput();
			}
			public void process(final MotionEvent event)
			{

                 final long time = System.currentTimeMillis();
                 if ((event.getAction()& MotionEvent.ACTION_MASK) == 
MotionEvent.ACTION_MOVE && time - 
                mLastTouchTime < 32) {
                
                   return;
                }
                mLastTouchTime = time;    

               
					for( int i = 0; i < event.getPointerCount(); i++ )
					{
						int action = -1;
					    boolean point = false;
					    switch (event.getAction() & MotionEvent.ACTION_MASK) {
					    case MotionEvent.ACTION_DOWN:
					        action = 0;
					        break;
					    case MotionEvent.ACTION_POINTER_DOWN:
					        point = true;
					        action = 0;
					        break;
					     case MotionEvent.ACTION_UP:
					        action = 1;
					        break;
					     case MotionEvent.ACTION_POINTER_UP:     
					        point = true;
					        action = 1;
					        break;
					     case MotionEvent.ACTION_MOVE:
					        action = 2;
					        break;
					    }
					    int pid = event.getPointerId(i);
					    int act = event.getAction();
					    int id = act >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
					   
						try {
							if (((point && pid == id)||!point) && action >= 0 ) {
							   // Log.v("Supertux", "pid:"+pid+", id:"+id+" action:"+action);
								DemoGLSurfaceView.nativeMouse( (int)event.getX(pid), 
																(int)event.getY(pid), 
																action, 
																event.getPointerId(i),
																(int)(event.getPressure(pid) * 1000.0),
																(int)(event.getSize(pid) * 
																1000.0));
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							DemoGLSurfaceView.nativeMouse( (int)event.getX(0), 
									(int)event.getY(0), 
									action, 
									0,
									(int)(event.getPressure(pid) * 1000.0),
									(int)(event.getSize(0) * 
									1000.0));
						}
					
				} 
			}
		}
	}


class DemoRenderer extends GLSurfaceView_SDL.Renderer {

	public DemoRenderer(MainActivity _context)
	{
		context = _context;
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// nativeInit();
	}

	public void onSurfaceChanged(GL10 gl, int w, int h) {
		//gl.glViewport(0, 0, w, h);
		nativeResize(w, h);
	}

	public void onDrawFrame(GL10 gl) {

		nativeInitJavaCallbacks();
		
		// Make main thread priority lower so audio thread won't get underrun
		// Thread.currentThread().setPriority((Thread.currentThread().getPriority() + Thread.MIN_PRIORITY)/2);
		//System.loadLibrary("physfs");
        //System.loadLibrary("iconv");
		System.loadLibrary("application");
		System.loadLibrary("sdl_main");
		Settings.Apply(context);
		// Tweak video thread priority, if user selected big audio buffer
		if(Globals.AudioBufferConfig >= 2)
			Thread.currentThread().setPriority( (Thread.NORM_PRIORITY + Thread.MIN_PRIORITY) / 2 ); // Lower than normal
		String param = "-iwad "+Globals.wadFile;
		if(!Globals.EnableMusic)
			param = param+" -nosound";
		nativeInit(param); // Calls main() and never returns, hehe - we'll call eglSwapBuffers() from native code
		System.exit(0); // The main() returns here - I don't bother with deinit stuff, just terminate process
	}

	public int swapBuffers() // Called from native code, returns 1 on success, 0 when GL context lost (user put app to background)
	{
		synchronized (this) {
			this.notify();
		}
		//Thread.yield();
		return super.SwapBuffers() ? 1 : 0;
	}
  
	public void showScreenKeyboard() // Called from native code
	{
		class Callback implements Runnable {
			public MainActivity parent;

			public void run() {
				parent.showScreenKeyboard();
			}
		}
		Callback cb = new Callback();
		cb.parent = context;
		context.runOnUiThread(cb);
	}

	public void exitApp() {
		 nativeDone();
	};

	private native void nativeInitJavaCallbacks();
	private native void nativeInit(String CommandLine);
	private native void nativeResize(int w, int h);
	private native void nativeDone();
	public static native void nativeTextInput(int ascii, int unicode);

	private MainActivity context = null;
	
	private EGL10 mEgl = null;
	private EGLDisplay mEglDisplay = null;
	private EGLSurface mEglSurface = null;
	private EGLContext mEglContext = null;
}

class DemoGLSurfaceView extends GLSurfaceView_SDL {
	public DemoGLSurfaceView(MainActivity context) {
		super(context);
		mParent = context;
		touchInput = DifferentTouchInput.getInstance();
		setEGLConfigChooser(Globals.NeedDepthBuffer);
		mRenderer = new DemoRenderer(context);
		setRenderer(mRenderer);
        
		accelerometer = new AccelerometerReader(context);
	}


	@Override
	public boolean onTouchEvent(final MotionEvent event) 
	{
		touchInput.process(event);
		// Wait a bit, and try to synchronize to app framerate, or event thread will eat all CPU and we'll lose FPS
		synchronized (mRenderer) {
			try {
				mRenderer.wait(300L);
			} catch (InterruptedException e) { }
		}
		return true;
	};

	public void exitApp() {
		mRenderer.exitApp();
		accelerometer.stop();
		accelerometer = null;
	};

	@Override
	public void onPause() {
		super.onPause();
		System.exit(0); // Not implemented yet
	};

	@Override
	public void onResume() {
		super.onResume();
	};

	@Override
	public boolean onKeyDown(int keyCode, final KeyEvent event) {
	   if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ||
        keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            return super.onKeyDown(keyCode, event);
       }
		 nativeKey( Globals.TranslateScancode(keyCode, true), 1 );
		 return true;
	 }
	
	@Override
	public boolean onKeyUp(int keyCode, final KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ||
        keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            return super.onKeyDown(keyCode, event);
       }
		 nativeKey( Globals.TranslateScancode(keyCode, false), 0 );
		 return true;
	 }

	DemoRenderer mRenderer;
	MainActivity mParent;
	AccelerometerReader accelerometer = null;
	DifferentTouchInput touchInput = null;

	public static native void nativeMouse( int x, int y, int action, int pointerId, int pressure, int radius );
	public static native void nativeKey( int keyCode, int down );
}


