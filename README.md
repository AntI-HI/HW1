# Game Engine Bug Fixes and Documentation

This document summarizes the changes and improvements made to the game engine to enhance stability, performance, and correctness.

## 1. Thread Safety and Concurrency
The game employs a multi-threaded architecture (the main game loop, rendering thread, and spawning thread). Previous iterations lacked proper synchronization, leading to potential `ConcurrentModificationException` and state corruption.

- **Synchronized Lists**: Game object lists in `GameManager` are now wrapped in `java.util.Collections.synchronizedList`.
- **Atomic Iteration**: All loops that iterate over `activeObjects` or `deactiveObjects` (in `Update`, `CollisionCheck`, and `paintComponent`) are now enclosed in `synchronized` blocks.

## 2. Performance Optimizations
- **ArrayList Transition**: Switched from `LinkedList` to `ArrayList` for game object management. This improves performance from $O(n)$ to $O(1)$ when accessing objects by index during the update and render loops.
- **Efficient Deactivation**: The `Update` loop in `GameManager` now iterates **backwards**. This allows for safe removal of objects from the list without skipping the next element or causing index errors.

## 3. Design Patterns
- **Singleton Enforcement**: The `GameManager` now strictly enforces the singleton pattern with a `private` constructor and a static factory method `CreateGameManager()`. This prevents accidental recreation of the manager and ensures global state consistency.
- **Factory Integration**: Updated the main `Game` class to use the new factory method for initialization.

## 4. Logic Fixes
- **Object Spawning**: Fixed an off-by-one error in `ObjectSelector` where the first object in each pool (at index 0) was never checked for spawning.
- **Robustness**: Added null checks for critical objects like the `Player` during the main game loop to prevent `NullPointerException` during initialization or post-death states.

## 5. Documentation Updates
Internal Javadocs for `GameManager`, `AnimationPane`, `PhysicsManager`, and `ObjectSelector` have been updated to reflect these technical changes and provide guidance for future development.
