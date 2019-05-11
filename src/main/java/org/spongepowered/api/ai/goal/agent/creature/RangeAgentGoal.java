/*
 * This file is part of SpongeAPI, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.api.ai.goal.agent.creature;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.ai.goal.Goal;
import org.spongepowered.api.ai.goal.GoalBuilder;
import org.spongepowered.api.entity.living.Ranger;

/**
 * An {@link Goal} which uses the ranging aspect of the Ranger to attack
 * the target.
 */
public interface RangeAgentGoal<O extends Ranger> extends Goal<O> {

    /**
     * Creates a new {@link Builder} to build a new {@link RangeAgentGoal}.
     *
     * @return A new builder
     */
    static <O extends Ranger, B extends Builder<O, RangeAgentGoal<O>, B>> B builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    /**
     * Gets the movement speed modifier for moving towards a targeted
     * {@link Entity}.
     *
     * @return The movement speed modifier
     */
    double getMoveSpeed();

    /**
     * Sets the movement speed modifier for moving towards a targeted
     * {@link Entity}.
     *
     * @param speed The movement speed modifier
     * @return This goal, for chaining
     */
    RangeAgentGoal<O> setMoveSpeed(double speed);

    /**
     * Gets the delay in ticks between attempts to attack the targeted
     * {@link Entity}.
     *
     * @return The delay in ticks between attempts to attack
     */
    int getDelayBetweenAttacks();

    /**
     * The time, in ticks, this {@link Ranger} will wait before attacking
     * again.
     *
     * @param delay The delay, in ticks
     * @return This goal, for chaining
     */
    RangeAgentGoal<O> setDelayBetweenAttacks(int delay);

    /**
     * Gets the radius of which the owning {@link Ranger} will attempt to
     * attack a targeted {@link Entity}.
     *
     * @return The radius of which the owning entity will attempt to attack
     */
    float getAttackRadius();

    /**
     * Sets the radius of which the owning {@link Ranger} will attempt to
     * attack a targeted {@link Entity}.
     *
     * @param radius The radius of which the owning entity will attempt to
     *     attack
     * @return This goal, for chaining
     */
    RangeAgentGoal<O> setAttackRadius(float radius);

    interface Builder<O extends Ranger, A extends RangeAgentGoal<O>, B extends Builder<O, A, B>> extends GoalBuilder<O, A, B> {

        B moveSpeed(double speed);

        B delayBetweenAttacks(int delay);

        B attackRadius(float radius);
    }

}
