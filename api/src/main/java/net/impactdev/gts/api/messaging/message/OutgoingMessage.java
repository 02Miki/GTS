/*
 * This file is part of LuckPerms, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package net.impactdev.gts.api.messaging.message;

import net.impactdev.gts.api.messaging.IncomingMessageConsumer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an outgoing {@link Message}.
 *
 * <p>Outgoing messages are ones which have been generated by this instance.
 * (in other words, they are implemented by GTS)</p>
 *
 * <p>Note that all implementations of this interface are guaranteed to be an
 * instance of one of the interfaces extending {@link Message} in the
 * 'api.messenger.message.type' package.</p>
 */
public interface OutgoingMessage extends Message {

    /**
     * Gets an encoded string form of this message.
     *
     * <p>The format of this string is likely to change between versions and
     * should not be depended on.</p>
     *
     * <p>Implementations which want to use a standard method of serialisation
     * can send outgoing messages using the string returned by this method, and
     * pass on the message on the "other side" using
     * {@link IncomingMessageConsumer#consumeIncomingMessageAsString(String)}.</p>
     *
     * @return an encoded string form of the message
     */
    @NonNull String asEncodedString();

}
