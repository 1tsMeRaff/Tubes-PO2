# Run.md - Execution, Compilation & Deployment Manual

This document provides step-by-step instructions for compiling, executing, and packaging the **Tubes-PO2** pure Java project across CLI and modern IDEs.

---

## 1. Prerequisites
- **Java Development Kit (JDK):** JDK 8 or higher (OpenJDK / Oracle JDK 17 or 21 recommended).
- **Environment Variable:** Ensure `JAVA_HOME` is set and `java` / `javac` are available in your system `PATH`.
- Verify installation:
  ```powershell
  javac -version
  java -version
  ```

---

## 2. Running via Command Line (CLI)

### On Windows (PowerShell)

1. **Navigate to the project root:**
   ```powershell
   cd c:\Users\HP\Rafi\MyProject\Tubes-PO2
   ```

2. **Ensure the output directory exists:**
   ```powershell
   if (!(Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" }
   ```

3. **Compile all Java source files:**
   ```powershell
   javac -encoding UTF-8 -d bin (Get-ChildItem -Path src -Filter *.java -Recurse).FullName
   ```

4. **Execute the game:**
   Classpath must include both `bin` (compiled `.class` bytecode) and `resources` (sprites, maps, and audio):
   ```powershell
   java -cp "bin;resources" main.Main
   ```

---

### On Linux / macOS (Bash)

1. **Compile:**
   ```bash
   mkdir -p bin
   javac -encoding UTF-8 -d bin $(find src -name "*.java")
   ```

2. **Run:**
   ```bash
   java -cp "bin:resources" main.Main
   ```

---

## 3. Running via Modern IDEs

### Eclipse IDE (Native Configuration)
1. Launch Eclipse.
2. Select **File** > **Import...** > **General** > **Existing Projects into Workspace**.
3. Select the `Tubes-PO2` root directory.
4. Eclipse will automatically recognize `.project` and `.classpath`.
5. Right-click [`src/main/Main.java`](file:///c:/Users/HP/Rafi/MyProject/Tubes-PO2/src/main/Main.java) > **Run As** > **Java Application**.

### IntelliJ IDEA
1. Open IntelliJ IDEA > **Open** > Select `Tubes-PO2` folder.
2. Go to **File** > **Project Structure** > **Modules**:
   - Mark `src` folder as **Sources**.
   - Mark `resources` folder as **Resources**.
3. Open [`src/main/Main.java`](file:///c:/Users/HP/Rafi/MyProject/Tubes-PO2/src/main/Main.java) and click the green **Run** triangle.

### VS Code
1. Install the **Extension Pack for Java**.
2. Open the `Tubes-PO2` workspace.
3. Locate [`src/main/Main.java`](file:///c:/Users/HP/Rafi/MyProject/Tubes-PO2/src/main/Main.java) in the Explorer.
4. Click **Run** or press `F5`.

---

## 4. Packaging as an Executable JAR

To create a standalone runnable `.jar` file:

```powershell
# 1. Compile
javac -encoding UTF-8 -d bin (Get-ChildItem -Path src -Filter *.java -Recurse).FullName

# 2. Copy resources into the bin folder
Copy-Item -Path "resources\*" -Destination "bin" -Recurse -Force

# 3. Create manifest file
"Main-Class: main.Main`n" | Out-File -Encoding ASCII manifest.txt

# 4. Package JAR
jar cvfm Tubes-PO2.jar manifest.txt -C bin .

# 5. Run Standalone JAR
java -jar Tubes-PO2.jar
```

---

## 5. Troubleshooting & Common Pitfalls

| Issue | Cause | Solution |
| :--- | :--- | :--- |
| `NullPointerException` on `GetSpriteAtlas` | Missing resource directory in classpath | Ensure `-cp "bin;resources"` is supplied to the `java` runner command. |
| Window UI looks blurred or incorrectly scaled | Windows OS DPI Scaling (125% / 150%) | Add JVM argument `-Dsun.java2d.uiScale=1.0` to force 1:1 pixel rendering. |
| `LineUnavailableException` | Audio device busy or occupied by another app | Check that audio output is not locked exclusively by another program. |
| High CPU usage / Fan running loud | Game loop busy-wait spinning | Check that Phase 1 sleep patch has been applied to `GameCore.java`. |
