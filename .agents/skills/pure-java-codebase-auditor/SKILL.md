---
name: pure-java-codebase-auditor
description: >-
  Audits pure Java codebases across 6 software engineering dimensions: Modularity/SOLID, Effective Java, JMM/Concurrency, GC Allocation & Resource Leaks, Collections Complexity, and Exception Handling. Produces a formal audit report with a Severity Matrix and 4-Phase Modernization Roadmap. Use when asked to audit Java code, review Java architecture, or when /pure-java-codebase-auditor is invoked.
---

# Pure Java Codebase Auditor

The `pure-java-codebase-auditor` skill executes a rigorous, aggressive, and production-grade engineering audit on pure Java codebases (free of framework bias, focusing directly on JVM mechanics, OOP integrity, and system-level performance).

---

## 🎯 Scope and Trigger

Use this skill whenever:
- The user requests an audit, code review, architectural evaluation, or refactoring roadmap for a pure Java project.
- The user invokes `/pure-java-codebase-auditor`.
- You are reviewing performance bottlenecks, concurrency hazards, memory leaks, or architectural technical debt in core Java applications.

---

## 🔬 The 6 Engineering Audit Dimensions

When executing the audit, evaluate every package and class against these 6 core dimensions:

### 1. Modularity & SOLID Principles
- **Single Responsibility Principle (SRP):** Identify God classes, monolithic coordinators (e.g., God-object GamePanels, 1000+ line controllers), and mixed concerns (e.g., UI rendering logic inside business entities).
- **Open/Closed Principle (OCP):** Detect hardcoded `switch-case` or `if-else` cascades on enum/type fields that require editing existing classes instead of using polymorphism/strategy patterns.
- **Liskov Substitution Principle (LSP):** Check for overridden methods throwing `UnsupportedOperationException`, breaking base contracts, or narrowing return constraints unexpectedly.
- **Interface Segregation Principle (ISP):** Identify "fat" interfaces forcing implementations to stub unused methods with empty bodies.
- **Dependency Inversion Principle (DIP):** Identify tight coupling, concrete `new` instantiation in high-level classes, lack of constructor dependency injection, and static accessor abuse.
- **Package Architecture & Encapsulation:** Package-private visibility underutilization (everything marked `public`), circular dependencies between packages.

### 2. Effective Java Best Practices (Joshua Bloch)
- **Object Creation & Destruction:**
  - Telescoping constructors instead of static factory methods or Builder pattern.
  - Unnecessary object instantiation (e.g., redundant wrapper instances, non-reusable immutable objects).
  - Proper implementation of `AutoCloseable` vs obsolete finalizers (`finalize()`) or `Cleaner`.
- **Method Contracts:**
  - `equals()` and `hashCode()` contract compliance: consistency, symmetry, transitivity, and hash bucket collisions.
  - Proper `Comparable` and `compareTo()` consistency with `equals()`.
  - Override `toString()` meaningfully for diagnostics.
- **Immutability & Defensive Programming:**
  - Mutable internal state exposed via public getters (escaping internal arrays, collections, or mutable dates).
  - Missing defensive copies in constructors and accessors.
- **Class & Member Design:**
  - Prefer composition over inheritance.
  - Public mutable fields (`public static int state`) vs encapsulated types.
  - Constants in utility classes vs `enum` types.

### 3. Java Memory Model (JMM) & Concurrency
- **Thread Safety & Visibility:**
  - Missing `volatile` keywords on shared loop flags (e.g., `while (running)` or thread termination guards).
  - Lack of memory fences causing instruction reordering and stale cache reads across CPU cores.
- **Synchronization & Race Conditions:**
  - Check-then-act race conditions (e.g., non-atomic `if (count > 0) count--`).
  - Coarse-grained `synchronized` methods locking out unrelated operations.
  - Potential deadlocks, lock ordering inconsistencies.
- **Modern Concurrency Utilities (`java.util.concurrent`):**
  - Uncontrolled raw `Thread` spawning instead of managed `ExecutorService` or thread pools.
  - Busy-waiting loops spinning CPU at 100% without timed yield, sleep, or lock waits.
  - Misuse of low-level `wait()`/`notify()` instead of `CountDownLatch`, `Semaphore`, `ReentrantLock`, or `Condition`.

### 4. Garbage Collection (GC) Allocation & Resource Leaks
- **Hot-Path Allocations:**
  - Frequent allocations inside loops, update loops, or game loops (`new Point()`, `new Rectangle()`, `new StringBuilder()` every tick).
  - Premature generation-0 GC pressure causing frequent minor GC pauses and frame drops.
- **Resource Lifecycle Management:**
  - Failure to use `try-with-resources` on I/O streams, `Scanner`, `Reader`, `Writer`, audio clips, or file handles.
  - Hanging file locks or unreleased audio hardware lines.
- **Static & Lifetime Leaks:**
  - Static collections (`static List<Listener>`, `static Map<Key, Value>`) accumulating references indefinitely.
  - Failure to unregister event listeners or callbacks leading to memory retention of large UI trees.

### 5. Collections & Algorithmic Complexity
- **Time Complexity (Big-O):**
  - $O(N^2)$ nested iterations for lookup/filtering instead of $O(1)$ Hash lookup or $O(\log N)$ Tree structures.
  - Using `ArrayList.contains()` or `indexOf()` repeatedly instead of `HashSet`.
- **Space & Resizing Overhead:**
  - Unsized collections (`new ArrayList<>()`, `new HashMap<>()`) subjected to frequent dynamic resizing and array copy overhead.
  - Choosing `LinkedList` where `ArrayList` or `ArrayDeque` would provide vastly superior cache locality and lower pointer overhead.
- **Boxing & Autoboxing:**
  - Inadvertent autoboxing/unboxing inside tight computational loops (e.g., `Integer` in arithmetic loops instead of primitive `int`).

### 6. Exception Handling & Failure Isolation
- **Anti-patterns in Error Handling:**
  - Swallowed exceptions (empty `catch (Exception e) {}`).
  - Catching `Throwable` or `Exception` indiscriminately instead of specific checked/runtime exceptions.
  - Swallowing `InterruptedException` without restoring interrupt status (`Thread.currentThread().interrupt()`).
- **Control Flow & Fail-Fast:**
  - Using exceptions for normal control flow (e.g., `try { Integer.parseInt(s) } catch (...)`).
  - Missing early parameter validation (`Objects.requireNonNull()`, precondition checks).
  - Discarded original cause (lost stack traces when wrapping exceptions).

---

## 📋 Audit Execution Workflow

When conducting the audit:

1. **Discovery & Exploration:**
   - Scan the project structure (`src/`, packages, build files, configuration).
   - Identify entry points (`main()`), core engine/loop, state machines, and data access layers.

2. **In-Depth Analysis:**
   - Review classes systematically across each of the 6 dimensions.
   - Extract exact file names and line numbers for all detected issues.
   - Formulate concrete "Before vs After" code corrections.

3. **Synthesis & Severity Grading:**
   - Classify findings into **CRITICAL**, **HIGH**, **MEDIUM**, and **LOW / INFO**.
   - Assign impact and likelihood scores.

4. **Roadmap Generation:**
   - Structure a realistic 4-Phase Modernization Plan prioritizing stability first, architecture second, performance third, and idiomatic refactoring fourth.

---

## 📊 Report Output Structure

The generated audit report MUST follow this formal markdown structure:

```markdown
# 🛡️ PURE JAVA CODEBASE AUDIT REPORT

## 1. Executive Summary
- **Codebase Overview:** (Name, approximate LOC, package count, domain)
- **Target Java Version:** (e.g., Java 8 / 11 / 17 / 21)
- **Overall Health Score:** [XX / 100]
- **Key Takeaways & Core Risks:** (Brief summary of primary hazards)

## 2. Architecture & Quality Scorecard
| Dimension | Rating (A-F) | Score (/10) | Major Bottleneck |
| :--- | :---: | :---: | :--- |
| 1. Modularity & SOLID | ... | ... | ... |
| 2. Effective Java Practices | ... | ... | ... |
| 3. JMM & Concurrency | ... | ... | ... |
| 4. GC Allocation & Leaks | ... | ... | ... |
| 5. Collections & Complexity | ... | ... | ... |
| 6. Exception Handling | ... | ... | ... |

## 3. Severity Matrix
| ID | Finding | Dimension | Severity | Affected File & Line | Impact |
| :--- | :--- | :--- | :---: | :--- | :--- |
| SEC-01 | ... | JMM/Concurrency | CRITICAL | `src/...:L45` | Thread lockup |

## 4. Deep-Dive Findings & Concrete Refactoring
### [ID] <Finding Title>
- **Dimension:** <Dimension Name>
- **Severity:** `CRITICAL` / `HIGH` / `MEDIUM` / `LOW`
- **Location:** `[File.java:Line](file:///...)`
- **Root Cause & Architectural Risk:**
  <Detailed technical explanation>
- **Problematic Code Snippet:**
  ```java
  // current flawed implementation
  ```
- **Recommended Production Refactor:**
  ```java
  // modernized, idiomatic, and safe implementation
  ```

## 5. 4-Phase Modernization Roadmap
- **Phase 1: Critical Fixes & Stability (Day 1 - Week 1)**
  - Priority: Stop memory leaks, eliminate thread races, fix resource leaks.
- **Phase 2: Decoupling & Structural Refactoring (Week 2 - Week 3)**
  - Priority: Break God classes, apply SOLID & Dependency Inversion, clean state management.
- **Phase 3: GC Tuning & Hot-Path Performance (Week 3 - Week 4)**
  - Priority: Zero-allocation loops, optimized collections, cache locality.
- **Phase 4: Modern Java Idioms & Clean Code (Week 4+)**
  - Priority: Effective Java patterns, records/sealed classes (if Java 17+), robust exception taxonomy.
```
