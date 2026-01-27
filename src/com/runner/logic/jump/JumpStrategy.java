package com.runner.logic.jump;

/**
 * Interface defining the jump strategy pattern for interchangeable jump
 * behaviors.
 * Different implementations (HighJump, LowJump) can be swapped at runtime.
 * Follows the Strategy design pattern.
 */
public interface JumpStrategy {
    /**
     * Executes the jump logic based on the concrete strategy implementation.
     */
    public void Jump();
}
