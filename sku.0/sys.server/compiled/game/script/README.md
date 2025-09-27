# Scripting 101

## Introduction

When making a script, you should start out with a basic template as such:

```package script.[package];/*  
@Origin: dsrc.script.[this.scripts.name]
@Author: [Author(s)]
@Purpose: [Purpose of this script and how to use it.]
@Requirements
    [Denote required items. $SWG-Source/dsrc:3.1 for example, otherwise put <no requirements>
@Notes:
    [Denote any notes about this script here]

@Copyright © SWG-OR $YEAR.
    Unauthorized usage, viewing or sharing of this file is prohibited.
*/
import script.obj_id;  
import script.location;  
import script.menu_info_types;  
import script.menu_info_data;  
import script.dictionary;  
  
public class class_name extends script.base_script  
{  
    public class class_name()
    {
    }
    public int OnAttach(obj_id self)  
    {  
        return SCRIPT_CONTINUE;  
    }  
    public int OnInitialize(obj_id self)  
    {  
        return SCRIPT_CONTINUE;  
    }  
}  
```  

### What is OnAttach?

OnAttach() is triggered when the gameserver applies the script to any object.

### What is OnInitialize?

OnInitialize() is triggered when the object is initialized in the world. This is not the same as OnAttach(), as it gets called on each startup. This is useful for objects that are persisted and don't get the script reattached to the object upon boot.

### What other triggers are there?

There are many triggers, for all of them, check [here, in the SWGNB/src/](https://github.com/SWGNB/src/blob/live/engine/server/library/serverScript/src/shared/ScriptFunctionTable.cpp) repo.

#### Trigger symbols:

Values for script parameters:\
b = boolean\
i = integer         (4 byte)\
f = float           (4 byte)\
s = string          (const char *)\
u = unicode string  (const uint16 *)\
V = class ValueDictionary\
O = class objId\
S = class stringId\
E = class slotData\
A = class attribMod\
M = class mentalStateMod\
L = class location\
m = class menu_info\
D = class draft_schematic\
I = struct Crafting::IngredientSlot\
U = unsigned char\
[ = previous identifier was an array\
*= previous identifier was the modifiable version of the data (currently int, float, and stringId)

**Example**: <br>

DSRC: public int OnGroupLeaderChanged(obj_id self, obj_id oldLeader, obj_id newLeader)<br>
<br>
SRC: {Scripting::TRIG_GROUP_LEADER_CHANGED,      "OnGroupLeaderChanged", "OOO"},

## Triggers

Triggers are how the C++ code communicates with the java code. For a full list of triggers, in your IDE or Notepad++ search for the following in `/dsrc/sku.0/sys.server/compiled/game/script`: ```public int On```

### How do I know if a trigger is a trigger?

Triggers are always in the format of ```public int On[TRIGGER]()``` with some cases using a different format, like ```aiCorpsePrepared()```

### What is a trigger?

A trigger is a function that is called when a certain event happens. For example, ```OnAttach()``` is called when the script is attached to an object.

### What is a trigger's return value?

A trigger's return value is an integer that tells the C++ code what to do. For example, ```return SCRIPT_CONTINUE;``` tells the C++ code to continue running the script.  
If you want to stop the script from running, you can use ```return SCRIPT_OVERRIDE;``` This is useful for blocking container transfers, whether it be a cell or inventory.

### What is a trigger's parameters?

A trigger's parameters are the variables that are passed to the trigger. For example, ```public int OnAttach(obj_id self)``` has one parameter, ```obj_id self```. This parameter is the object that the script is attached to.

### What is keyword ```obj_id```?

```obj_id``` is a variable type that is used to store an object's ID. For example, ```obj_id self``` is the object that the script is attached to.

### What does ```extends script.base_script``` mean?

```extends script.base_script``` means that the script is extending the ```base_script``` class. This is required for all scripts in the SWG environment.

### What is keyword```public```?

```public``` is a keyword that means that the function can be accessed from outside of the class. For example, ```public int OnAttach(obj_id self)``` can be accessed from outside of the class.

- Example: ```public String[] getPlayerScripts(obj_id player)``` can be accessed from outside of the class, so if it was called from another script, it would work.

### What is keyword ```private```?

```private``` is a keyword that means that the function can only be accessed from inside of the class. For example, ```private int killTarget(obj_id who)``` can only be accessed from inside of the class.

- Example: ```private int startQuest(obj_id player)``` can only be accessed from inside of the class, so if it was called from another script, it would not work.

### What is the difference between import script.library.* and import script.library.[library]?

```import script.library.*``` imports all of the classes for use from the library, while ```import script.library.[library]``` only imports the specified library.

## Limitations of the SWG Scripting Language

- Many of the functions are not documented, so you have to figure out what they do by looking at the code.
- Some of the scripts are made from an older version of the code, so they may not work as intended, or be ambiguous.
- Cannot process client events, unless specifically made to do so via `swgServerNetworkMessages` in SWGNB/src/

## Scripting and Reloading

- When developing scripts, SWG allows you to reload scripts without having to restart the server. This is useful for testing scripts, as you don't have to restart the server every time you make a change to a script.
- You can compile scripts by using the following command: `/developer git pull` then `/developer crs [script.name]`
- Caveats: library should not be reloaded unless you know what you are doing. If you reload a library, it will cause issues. If you reload a script that uses a library, it will not reload the library, so you will have to restart the server to reload the
  library.

## Debugging

- When developing scripts, you should make use of LOG(); to print out information to the log server. This is useful for debugging scripts, as you can see what is happening in the script.

- Example: `LOG("bubbajoe", "Informational information here");` which will log to the log server as: [bubbajoe] Informational information here. (depending on how you have your log server targets setup)

## Recommended Development Environment

For SWG development and scripting, it is required to use one of the following:

- IDE: IntelliJ IDEA
- Apache NetBeans
- Visual Studio Code
- Eclipse

Other required software:

- Git
- Java OpenJDK 11
- SwgGodClient_r/d.exe (or) SwgClient_r/d.exe

Optional Utilities

- Notepad++
- Sytner's IFF Editor - (Note: we do not edit .iff files via Sytner's IFF Editor, we use it to view the contents of .iff files, UNLESS the .iff file is not in the repo to be compiled, then we use it to edit the .iff file)
- Tre Explorer
- Any of Borrie's Tools

Notes: using IntelliJ IDEA is recommended, as it is the IDE that is used by BubbaJoe and can help with setting up the project.

## Setting up IntelliJ Idea

![enter image description here](https://i.imgur.com/oznvH7l.png)

- Clone [DSRC](https://github.com/SWGNB/dsrc/) to a drive of your choice.

![enter image description here](https://i.imgur.com/CIJ9mCt.png)

- Open Project -> select "game" as the root folder.
- Install Java OpenJDK 11 (Temurin), this can be downloaded from the IntelliJ app.
- Right click game click "Build Module 'dsrc' as shown below.
  <br>![Compile](https://i.imgur.com/DmJPSJk.png)

Once compiled, you are ready to start working on scripts!