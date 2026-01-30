# Something Mod 1.21.X

A showcase mod built using NeoForge focused on custom Java logic, raycasting, and sound layering.


## 🛠 Included Items

### 1. The Spark Stick

**Animation**: Uses the SPEAR (Trident) hold animation.

**Logic**: Implements a custom charging mechanic.

**How to Use**: Hold Right-Click to charge (At least for one second).

**Minimum Charge**: 1 second (20 ticks).

**Scaling**: Every additional second of charging adds an extra lightning strike (up to 3 strikes total).

**Targeting**: Uses Raycasting to strike exactly where you are looking (up to 50 blocks away) rather than at your own position.

### 2. The Phase Blade

**Action**: Dash teleportation.

**How to Use**: Right-Click to instantly teleport 5 blocks in the direction you are facing.

**Sound Design**: Features layered sound effects using the new 1.21 BREEZE_SHOOT effect for the start of the dash and a quiet ENDERMAN_TELEPORT for the finish.

**Cooldown**: Built-in 2-second cooldown to prevent exploit.

## 🚀 How to Run

Find the items in the Creative Ingredients Tab or use /give @s somethingmod:[item_name].