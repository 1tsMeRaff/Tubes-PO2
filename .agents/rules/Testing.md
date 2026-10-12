# Testing.md - Testing Strategy & Verification Manual

This document outlines the testing strategy, headless verification procedures, and QA checklists for the **Tubes-PO2** pure Java game engine.

---

## 1. Testing Philosophy for Pure Java 2D Games

Interactive games combine graphical presentation with deterministic simulation logic. The testing strategy separates:
1. **Headless Unit Tests (Deterministic Logic):** Mathematics, grid collisions, leveling formulas, and save/load serialization executed without rendering UI.
2. **Integration / Simulation Tests:** Running a synthetic `update()` loop for $N$ ticks to verify state stability without spawning GUI frames.
3. **Manual Functional QA:** Visual validation of sprite rendering, user input responsiveness, audio playback, and level flow.

---

## 2. Headless Unit Testing Setup (JUnit 5 Standalone)

Because this repository does not use Maven or Gradle by default, automated tests can be executed using the standalone JUnit Console Runner.

### 2.1 Directory Structure for Tests
```text
Tubes-PO2/
├── src/                # Main game source code
├── test/               # Test source code
│   └── utilitytools/   # Unit tests for core logic
└── lib/                # Standalone test libraries (e.g., junit-platform-console-standalone.jar)
```

### 2.2 Critical Test Cases to Implement

#### 1. Collision Math Tests (`HelpMethodsTest.java`)
- **Objective:** Verify boundary edge cases in `canMoveHere()` and `IsEntityOnFloor()`.
- **Test Scenarios:**
  - Verify entity on flat solid floor returns `true`.
  - Verify entity hovering over empty tile returns `false` (airborne).
  - Verify entity overlapping solid tile boundary returns collision `false`.

#### 2. Persistence Tests (`SaveLoadManagerTest.java`)
- **Objective:** Verify savegame serialization and deserialization integrity.
- **Test Scenarios:**
  - Write mock player data with full inventory to a test file.
  - Read back and assert level, health, mana, coordinates, and equipped gear match exactly.
  - Verify graceful handling when save file is corrupted or missing.

#### 3. Progression & Math Tests (`PlayerStatsTest.java`)
- **Objective:** Verify player defense and experience formulas.
- **Test Scenarios:**
  - Assert adding EXP triggers level-up threshold calculations properly.
  - Assert defense mitigates damage down to minimum threshold of 1.

---

## 3. Running Headless Tests via CLI

When running tests in automated environments or CI/CD pipelines, enable Java's headless mode to prevent Swing from attempting to open native window displays:

```powershell
# Run with headless flag
java -Djava.awt.headless=true -jar lib/junit-platform-console-standalone.jar --class-path "bin;test-bin;resources" --scan-class-path
```

---

## 4. Manual QA Verification Checklist

Before releasing or merging pull requests, execute the following interactive test suite:

### A. Core Player Controls & Physics
- [ ] **Movement:** Walking left/right moves player smoothly without clipping into walls.
- [ ] **Jump & Double Jump:** Pressing Space once jumps; pressing Space in mid-air activates double jump.
- [ ] **Dash:** Pressing dash key accelerates player horizontally and enters cooldown.
- [ ] **Combat:** Regular attack swings hitbox; charging attack triggers heavy slash and audio effect.
- [ ] **Chasm Check:** Falling off the bottom of the map teleports player back to `lastSafeX/lastSafeY` with health penalty.

### B. Enemy Interaction & AI
- [ ] **Patrol:** Enemies walk back and forth on platforms without walking off edges (unless designed to do so).
- [ ] **Aggro & Tracking:** Enemies face and approach player when in detection range.
- [ ] **Hit Registration:** Player attacks deal damage and apply knockback; enemy health drops.
- [ ] **Boss Fight:** Boss phase transitions trigger at designated HP thresholds (e.g., 70%, 40%, 10%).

### C. UI & System Menus
- [ ] **Main Menu:** Buttons highlight on mouse hover and trigger appropriate states.
- [ ] **Level Select:** Can choose and load Level 1 (Tutorial), Level 2 (Jungle), and Level 3 (Castle).
- [ ] **Pause Overlay:** Pressing ESC pauses game loop; volume slider adjusts audio gain in real-time.
- [ ] **Inventory Overlay:** Shows equipped items and grid; clicking items allows equipping/using.
- [ ] **Save & Continue:** Saving from options/state persists to `savegame.txt`; clicking Continue on Menu restores exact player state.
