// This string is autogenerated by ChangeAppSettings.sh, do not change spaces amount anywhere
package com.xianle.doomtnt;

import android.util.Log;
import android.view.KeyEvent;


public class Globals {
	public static String ApplicationName = "doom";

	public static String outFilesDir = "/sdcard/doom";
	public static String wadFile = "doom1.wad";
	public static String fileExistFlag = "doomshare.flag";
	public static int VERSION = 0x45;
	// Should be zip file
	public static String DataDownloadUrl = "game is 12m|http://fuck.com/text.zip^n";
	public static boolean NeedDepthBuffer = false;

	// Set this value to true if you're planning to render 3D using OpenGL - it eats some GFX resources, so disabled for 2D
	public static boolean HorizontalOrientation = true;
	
	// Readme text to be shown on download page
	public static String ReadmeText = "^You may press \"Home\" now - the data will be downloaded in background".replace("^","\n");
	
	public static boolean AppUsesMouse = false;//edited by niuzb

	public static boolean AppNeedsArrowKeys = false;

	public static boolean AppUsesJoystick = false;
	
	public static boolean AppUsesMultitouch = true;

	public static int AppTouchscreenKeyboardKeysAmount = 4;

	public static int AppTouchscreenKeyboardKeysAmountAutoFire = 0;

	// Phone-specific config
	// It will download app data to /sdcard/alienblaster if set to true,
	// otherwise it will download it to /data/data/de.schwardtnet.alienblaster/files
	public static boolean DownloadToSdcard = true;
	public static boolean PhoneHasTrackball = true;
	public static boolean PhoneHasArrowKeys = true;
	public static boolean UseAccelerometerAsArrowKeys = false;
	public static boolean EnableMusic = true; //options for music 2011.11.2
    public static int TileSensitive = 2;
	public static boolean UseTouchscreenKeyboard = true;//edited by niuzb
	//added for change dapad left /right funciton to turn or strafe
	public static boolean UseDpadAsTurn = false; 
	public static int TouchscreenKeyboardSize = 1;
	public static int TouchscreenKeyboardTransparency=2;
	public static int TouchscreenKeyboardTheme = 1;
	public static int AccelerometerSensitivity = 0;
	public static int TrackballDampening = 0;
	public static int AudioBufferConfig = 1;
	public static boolean OptionalDataDownload[] = null;
	public static int leftKey;
	public static int rightKey;
	public static int upKey;
	public static int downKey;
	public static int fireKey;
	public static int doorKey;
	public static int tleftKey;
	public static int trightKey;
	
	public static int current_key = KeyEvent.KEYCODE_1;
    public static boolean keyBindingUseVolumeButton(){
       if(leftKey == KeyEvent.KEYCODE_VOLUME_DOWN ||
        leftKey == KeyEvent.KEYCODE_VOLUME_UP) {
            return true;
       } else if(rightKey == KeyEvent.KEYCODE_VOLUME_DOWN ||
        rightKey == KeyEvent.KEYCODE_VOLUME_UP) {
            return true;
       } else if(upKey == KeyEvent.KEYCODE_VOLUME_DOWN ||
        upKey == KeyEvent.KEYCODE_VOLUME_UP) {
            return true;
       } else if(downKey == KeyEvent.KEYCODE_VOLUME_DOWN ||
        downKey == KeyEvent.KEYCODE_VOLUME_UP) {
            return true;
       } else if(fireKey == KeyEvent.KEYCODE_VOLUME_DOWN ||
        fireKey == KeyEvent.KEYCODE_VOLUME_UP) {
            return true;
       } else if(doorKey == KeyEvent.KEYCODE_VOLUME_DOWN ||
        doorKey == KeyEvent.KEYCODE_VOLUME_UP) {
            return true;
       } else if(tleftKey == KeyEvent.KEYCODE_VOLUME_DOWN ||
        tleftKey == KeyEvent.KEYCODE_VOLUME_UP) {
            return true;
       } else if(trightKey == KeyEvent.KEYCODE_VOLUME_DOWN ||
        trightKey == KeyEvent.KEYCODE_VOLUME_UP) {
            return true;
       }
       return false;
    }
	public static int TranslateScancode(int code, boolean pressed) {
	 int ret = code;
                /*do not remap back key*/
                if(code == KeyEvent.KEYCODE_BACK ||
            code == KeyEvent.KEYCODE_MENU)
                        return code;
                if(code == Globals.leftKey)
                        ret = KeyEvent.KEYCODE_DPAD_LEFT;
                else if(code == Globals.rightKey)
                        ret =  KeyEvent.KEYCODE_DPAD_RIGHT;
                else if(code == Globals.upKey)
                        ret =  KeyEvent.KEYCODE_DPAD_UP;
                else if(code == Globals.downKey)
                        ret = KeyEvent.KEYCODE_DPAD_DOWN;
                else if(code == Globals.fireKey)
                        ret = KeyEvent.KEYCODE_BUTTON_Y;
                else if(code == Globals.doorKey) {
                        if(!pressed) {
                        ret =  current_key++;
                        } else {
                                ret = current_key;
                        }
                        if(current_key > KeyEvent.KEYCODE_9)
                                current_key = KeyEvent.KEYCODE_1;
                }else if(code == Globals.tleftKey)
                        ret = KeyEvent.KEYCODE_BUTTON_L1;
                else if(code == Globals.trightKey)
                        ret =  KeyEvent.KEYCODE_BUTTON_R1;
                
                if(ret != code) {
//                      Log.v("doom", "origin code:"+ code + "tran:"+ret);
                }
                return ret;
	}
}

class LoadLibrary {
	public LoadLibrary() { 
	System.loadLibrary("sdl"); 
    System.loadLibrary("sdl_image"); 
    System.loadLibrary("sdl_mixer"); 
    };
   
}
