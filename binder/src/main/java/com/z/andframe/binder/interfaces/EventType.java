package com.z.andframe.view.interfaces;

/**
 * CREATED BY ROY ON 15-3-19.
 */
public interface EventType {
    public int EVENT_AUTO	                        = 0x00_0000_00;
    public int EVENTMASK_VIEW                       = 0x01_0000_00;
    public int EVENT_APPLY_WINDOW_INSETS	        = 0x01_0001_00;
    public int EVENT_CREATE_CONTEXT_MENU	        = 0x01_0002_00;
    public int EVENT_CLICK	                        = 0x01_0003_00;
    public int EVENT_DRAG	                        = 0x01_0004_00;
    public int EVENT_FOCUS_CHANGE	                = 0x01_0005_00;
    public int EVENT_GENERIC_MOTION	                = 0x01_0006_00;
    public int EVENT_HOVER	                        = 0x01_0007_00;
    public int EVENT_KEY	                        = 0x01_0008_00;
    public int EVENT_LONG_CLICK	                    = 0x01_0009_00;
    public int EVENT_SYSTEM_UI_VISIBILITY_CHANGE	= 0x01_000A_00;
    public int EVENT_TOUCH	                        = 0x01_000B_00;
    public int EVENT_ITEM_CLICK	                    = 0x01_000C_00;
    public int EVENT_ITEM_LONG_CLICK	            = 0x01_000D_00;
    public int EVENT_ITEM_SELECTED	                = 0x01_000E_00;
    public int EVENT_NOTHING_SELECTED	            = 0x01_000E_01;
    public int EVENT_SCROLL	                        = 0x01_000F_00;
    public int EVENT_SCROLL_STATE_CHANGED	        = 0x01_000F_01;
    public int EVENT_LAYOUT_CHANGE	                = 0x01_0010_00;
    public int EVENT_VIEW_ATTACHED_TO_WINDOW	    = 0x01_0011_00;
    public int EVENT_VIEW_DETACHED_FROM_WINDOW	    = 0x01_0011_01;
    public int EVENTMASK_MENUITEM	                = 0x02_0000_00;
    public int EVENT_MENU_ITEM_ACTION_EXPAND	    = 0x02_0001_00;
    public int EVENT_MENU_ITEM_ACTION_COLLAPSE	    = 0x02_0001_01;
    public int EVENT_MENU_ITEM_CLICK	            = 0x02_0002_00;



}
