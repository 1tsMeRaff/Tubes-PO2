# AGENTS.md - Project & Agent Operational Guide

Welcome to the **Tubes-PO2** repository guide for AI agents and developer contributors. This document defines project conventions, developer roles, file maps, and critical guidelines for modifying this codebase safely.

---

## 1. Project Identity & Purpose
- **Repository:** `Tubes-PO2` (Tugas Besar Pemrograman Berorientasi Objek 2)
- **Domain:** Pure Java 2D Side-Scrolling Platformer & Action RPG Game Engine
- **Core Technology:** Java Standard Edition (SE 8+), Java Swing (`JFrame`, `JPanel`), Java 2D (`Graphics2D`), Java Sound API (`javax.sound.sampled`).
- **Dependencies:** **Zero external dependencies / No frameworks**. Pure standard library only. Build tool agnostic (Eclipse project structure with native CLI compilation support).

---

## 2. Directory Layout
```text
Tubes-PO2/
├── .agents/                # Antigravity Workspace Customizations Root
│   ├── rules/              # Modular engineering rules and specifications
│   │   ├── Architecture.md # System architecture & design specification
│   │   ├── Patterns.md     # Established coding standards & design patterns
│   │   ├── Roadmap.md      # 4-Phase modernization & feature milestone roadmap
│   │   ├── Run.md          # Compilation, launch, and execution manual
│   │   └── Testing.md      # Headless unit testing & QA manual
│   └── skills/             # Agent skills (pure-java-codebase-auditor)
├── bin/                    # Compiled JVM bytecode output (.class files)
├── docs/                   # Documentation and historical archives
├── resources/              # Modular game asset hierarchy
│   ├── audio/              # Sound effects and music tracks (.wav)
│   ├── backgrounds/        # Parallax and static backdrop textures (.png)
│   ├── maps/               # Level definitions & CSV/TXT coordinate maps
│   ├── sprites/            # Animated spritesheets
│   │   ├── characters/     # Player sprites
│   │   ├── enemies/        # Mob & Boss spritesheets
│   │   ├── equipment/      # Weapons, armor, and gear icons
│   │   └── objects/        # Interactive world objects & potions
│   ├── tilesets/           # World terrain textures & tilesets
│   └── ui/                 # Buttons, fonts, overlays & frame graphics
├── src/                    # Core Java game engine source code
│   ├── audio/              # Audio playback & mixer subsystems
│   ├── entity/             # Player, Enemy hierarchy, Bosses, and NPC entities
│   ├── gameStates/         # Game state machine (Menu, Playing, Options, etc.)
│   ├── inputs/             # Keyboard and Mouse event listeners
│   ├── main/               # Game entrypoint, JFrame, JPanel, and loop coordinator
│   ├── objects/            # Interactive objects (Potions, Containers, Signs, Cages)
│   ├── ui/                 # Overlays (Pause, Game Over, Inventory, Select Level)
│   ├── utilitytools/       # Helper math, constants, file I/O, save/load managers
│   └── world/              # Tile maps, rendering, and level loading
├── test/                   # Headless unit testing suites (pure Java SE)
│   └── utilitytools/       # Tests for math, collisions, and resource loading
├── savegame.txt            # Persistent player save file (text-based key=value format)
└── AGENTS.md               # Root agent operational guide & entrypoint
```

---

## 3. Guiding Principles for AI Agents

When interacting with or refactoring this codebase, agents **MUST** respect the following rules:

1. **Pure Java SE Constraint:**
   - Do NOT introduce external game engines or heavy frameworks (e.g., LibGDX, LWJGL, Spring).
   - Maintain compatibility with standard JDK installations (`javac` / `java`).

2. **Garbage Collection Sensitivity:**
   - Avoid instantiating short-lived objects (`new Color()`, `new Font()`, `new Rectangle2D.Float()`) inside update loops or rendering methods (`update()` and `draw()`).
   - Use reusable scratch objects, cached constants, or primitive parameters.

3. **Concurrency & Thread Safety:**
   - Remember that Swing GUI events trigger on the **Event Dispatch Thread (EDT)**, whereas the game loop executes on the dedicated **`gameThread`**.
   - Always ensure cross-thread shared state (such as `GameStates`) is guarded by `volatile` or atomic structures.

4. **Preserve Asset Paths & Resource Loading:**
   - Assets are loaded from the classpath using `Class.getResourceAsStream("/filename")`. Do not alter case-sensitive file names or relative root slashes without verifying asset presence in `resources/`.

5. **Defensive Persistence:**
   - When modifying save/load logic in `SaveLoadManager.java`, always use `StandardCharsets.UTF_8` and maintain backward compatibility with `savegame.txt`.
