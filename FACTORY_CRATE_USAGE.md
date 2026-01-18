# Factory Crate Library Functions - Usage Guide

This document provides examples and usage instructions for the factory crate library functions added to `craftinglib.java`.

## Overview

Two new public library functions have been added to support factory crate generation and conversion:

1. `craftinglib.generateFactoryCrate()` - Creates a factory crate from a manufacturing schematic or prototype
2. `craftinglib.makeIntoFactoryCrate()` - Converts an existing item into a factory crate

## Function Signatures

```java
public static obj_id generateFactoryCrate(obj_id sourceObject, obj_id targetContainer, int count) throws InterruptedException

public static obj_id makeIntoFactoryCrate(obj_id item, obj_id targetContainer, int count) throws InterruptedException
```

## Usage Examples

### Example 1: Creating a Factory Crate from a Manufacturing Schematic

```java
// Get the player's inventory
obj_id inventory = utils.getInventoryContainer(player);

// Get or create a manufacturing schematic
obj_id schematic = getManufacturingSchematic();

// Generate a factory crate with 100 items
obj_id crate = craftinglib.generateFactoryCrate(schematic, inventory, 100);

if (crate != null)
{
    // Crate was successfully created
    sendSystemMessage(player, "Factory crate created with " + getCount(crate) + " items!");
}
```

### Example 2: Converting an Item to a Factory Crate

```java
// Get an existing item (e.g., a crafted weapon)
obj_id weapon = createObject("object/weapon/ranged/rifle/rifle_e11.iff", inventory, "");

// Get the player's inventory
obj_id inventory = utils.getInventoryContainer(player);

// Convert the weapon into a factory crate with 50 copies
obj_id weaponCrate = craftinglib.makeIntoFactoryCrate(weapon, inventory, 50);

if (weaponCrate != null)
{
    // Item was successfully converted to a crate
    sendSystemMessage(player, "Item converted to factory crate!");
}
```

### Example 3: Using in a Menu Handler

```java
public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
{
    if (item == menu_info_types.SERVER_MENU1)
    {
        // Generate factory crate from this schematic
        obj_id inventory = utils.getInventoryContainer(player);
        obj_id crate = craftinglib.generateFactoryCrate(self, inventory, 250);
        
        if (crate != null)
        {
            sendSystemMessage(player, "Factory crate created successfully!");
        }
        else
        {
            sendSystemMessage(player, "Failed to create factory crate.");
        }
    }
    return SCRIPT_CONTINUE;
}
```

## Automatic Template Detection

The library functions automatically detect the appropriate factory crate template based on the source object:

- **Weapons** → `object/factory/factory_crate_weapon.iff`
- **Armor** → `object/factory/factory_crate_armor.iff`
- **Clothing** → `object/factory/factory_crate_clothing.iff`
- **Food** → `object/factory/factory_crate_food.iff`
- **Chemicals** → `object/factory/factory_crate_chemicals.iff`
- **Electronics** → `object/factory/factory_crate_electronics.iff`
- **Furniture** → `object/factory/factory_crate_furniture.iff`
- **Installations/Structures** → `object/factory/factory_crate_installation.iff`
- **Other items** → `object/factory/factory_crate_generic.iff`

## What Gets Copied

### generateFactoryCrate
- Source schematic reference
- Crafting attributes (if present)
- Draft schematic CRC
- Item name
- Relevant scripts (excluding crafting session scripts)

### makeIntoFactoryCrate
- Original item reference (stored as prototype)
- All objvars (under `item_data.` prefix)
- Item name
- Crafter information (if available)

## Parameters

### sourceObject / item
The source object to create or convert from. Must be a valid object ID.

### targetContainer
The container where the factory crate will be created (typically player inventory).

### count
The number of items the factory crate should contain. Must be greater than 0.

## Return Value

Both functions return:
- `obj_id` of the created factory crate on success
- `null` if creation failed (invalid parameters, missing object, etc.)

## Error Handling

Always check if the returned `obj_id` is valid:

```java
obj_id crate = craftinglib.generateFactoryCrate(schematic, inventory, 100);

if (crate == null || !isIdValid(crate))
{
    // Handle error case
    LOG("factory_crate", "Failed to create factory crate");
    return SCRIPT_CONTINUE;
}

// Continue with successful crate creation
```

## Integration with Existing Code

These functions have been integrated into existing factory crate scripts:

- `script.developer.bubbajoe.player_developer` - Developer tool for creating factory crates
- `script.developer.bubbajoe.factory_crate` - Factory crate object menu handler

## Notes

- Factory crates are container objects that store multiple identical items
- The crate count can be adjusted with `setCount(crate, newCount)`
- Factory crates automatically use the appropriate template based on item type
- These functions are thread-safe and can be called from any script context
