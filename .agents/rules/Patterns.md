# Patterns.md - Coding Standards & Architectural Patterns

This document outlines the coding standards, established design patterns, and anti-patterns within the **Tubes-PO2** codebase.

---

## 1. Established Design Patterns

### 1.1 The Semi-Fixed Timestep Game Loop
- **Intent:** Decouple game logic physics updates (deterministic 60 UPS) from display rendering (FPS).
- **Implementation:** Accumulator algorithm calculating elapsed nanoseconds divided by target time step.
- **Rules:**
  - Logic calculations and state updates occur exclusively in `update()`.
  - Rendering code in `draw(Graphics g)` must remain purely idempotent and never modify gameplay state.

### 1.2 State Pattern (Application State Machine)
- **Intent:** Allow the application to switch behaviors when changing contexts (Menu, Gameplay, Options).
- **Implementation:** State interface [`StateMethods`](file:///c:/Users/HP/Rafi/MyProject/Tubes-PO2/src/gameStates/StateMethods.java) implemented by concrete states (`MainMenu`, `PlayStates`, `GameOptions`).
- **Standard:** Every state must handle its own update, draw, and input events cleanly.

### 1.3 Flyweight & Sprite Atlas Pattern
- **Intent:** Prevent loading duplicate image assets into heap memory.
- **Implementation:** Entire animations and tilesets are packaged into unified PNG sprite sheets. Sub-images are cropped once upon startup and cached in multidimensional arrays (`BufferedImage[][]`).
- **Standard:** Never call `ImageIO.read()` or `LoadSave.GetSpriteAtlas()` during runtime gameplay updates.

### 1.4 AABB (Axis-Aligned Bounding Box) Pattern
- **Intent:** High-performance 2D collision detection.
- **Implementation:** Hitboxes are modeled via `Rectangle2D.Float`. Intersection testing uses `hitBox.intersects(otherBox)`.
- **Standard:** Use reusable bounding boxes or primitive coordinates to prevent heap allocations.

### 1.5 Overlay / Modal Decorator Pattern
- **Intent:** Render translucent contextual HUD screens over the active game world without destroying world state.
- **Implementation:** Overlays (`PauseOverlay`, `GameOverOverlay`, `InventoryOverlay`, `SelectLevelOverlay`) hold a reference to `PlayStates` and toggle their draw/update calls based on boolean flags.

---

## 2. Coding Standards & Conventions

### 2.1 Java Naming Standards
- **Classes & Interfaces:** PascalCase (e.g., `SaveLoadManager`, `EnemyManager`).
- **Methods & Variables:** camelCase (e.g., `updatePos()`, `currentHealth`).
  - *Refactoring Note:* Legacy methods using PascalCase (`GetSpriteAtlas`, `GetTilesData`) must be refactored to standard camelCase (`getSpriteAtlas`, `getTilesData`).
- **Constants:** UPPER_SNAKE_CASE (e.g., `GAME_WIDTH`, `SCALE`, `TILES_SIZE`).
- **Package Names:** All lowercase, single words where possible (`audio`, `entity`, `gamestates`, `inputs`, `main`, `objects`, `ui`, `utilitytools`, `world`).

### 2.2 Memory & Heap Hygiene (Zero-Allocation Rule)
- **Rule:** **No object allocations in `draw()` or `update()` methods.**
- **Enforcement:**
  - `Color` instances must be `static final` constants or pre-cached.
  - `Font` instances must be instantiated once during component initialization.
  - `BasicStroke` objects must be cached statically.
  - `String.format()` or heavy string concatenation should be avoided in 60 FPS drawing loops.

### 2.3 Concurrency Standards
- **Rule:** Any variable modified by UI/Event threads and read by the game loop thread must be declared `volatile` or accessed via synchronized/atomic methods.
- **Enforcement:** `GameStates.getState()` and `GameStates.setState()` must guarantee memory fences under the Java Memory Model (JMM).

### 2.4 Exception Handling & Resource Safety
- **Rule:** Never use empty `catch` blocks or catch raw `Throwable`/`Exception` blindly.
- **Rule:** Always use **`try-with-resources`** for all `InputStream`, `BufferedReader`, `Scanner`, and `PrintWriter` operations.
- **Rule:** Explicitly specify `StandardCharsets.UTF_8` when dealing with text I/O.

---

## 3. Anti-Patterns to Refactor

| Anti-Pattern | Description | Target Solution |
| :--- | :--- | :--- |
| **God Class** | Classes like [`Player.java`](file:///c:/Users/HP/Rafi/MyProject/Tubes-PO2/src/entity/Player.java) (818 LOC) containing physics, combat, UI, and inventory. | Decompose into distinct SRP components (`PlayerCombat`, `PlayerInventory`, `StatusBarUI`). |
| **Raw Int Constants** | Massive sets of `public static final int` constants in `Konstanta.java`. | Replace with type-safe enums (`EnemyType`, `ItemType`, `GameState`). |
| **Leaking Mutables** | Exposing `int[][] tilesData` or `ArrayList<Integer> inventory` directly. | Return unmodifiable views (`Collections.unmodifiableList`) or defensive clones. |
| **CPU Busy-Wait** | `while(gameThread != null)` loop without sleep/yield in `GameCore.java`. | Implement adaptive sleep when ahead of frame deadlines. |
