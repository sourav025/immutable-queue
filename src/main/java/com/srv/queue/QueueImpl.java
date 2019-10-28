package com.srv.queue;

/**
 * Implementation is based on Two stack. One for all incoming values and other is to for query operations
 * <p>
 * Time complexity Analysis:
 * Queue Initialization : O(1)
 * enQueue : O(1)
 * deQueue :
 * - O(1) for Best Case
 * - O(K) worst case where K push operations are happened in-between deque or head
 * head: Similar to deQueue
 * isEmpty: O(1)
 */
public class QueueImpl<T> implements Queue<T> {

    /**
     * All incoming values are stored
     */
    private StackNode<T> orderedStack;
    /**
     * All deQueue operation and peek/head operations are supported here
     */
    private StackNode<T> reversedStack;

    public QueueImpl() {
        this.orderedStack = new StackNode<>();
        this.reversedStack = new StackNode<>();
    }

    private QueueImpl(StackNode<T> orderedStack, StackNode<T> reversedStack) {
        this.orderedStack = orderedStack;
        this.reversedStack = reversedStack;
    }

    @Override
    public Queue<T> enQueue(T value) {
        /**
         * Assuming NULL is not allowed
         */
        if (value == null) throw new NullPointerException("Invalid entry provided!!!");
        return new QueueImpl<>(this.orderedStack.addFront(value), this.reversedStack);
    }

    @Override
    public Queue<T> deQueue() {
        if (this.isEmpty())
            throw new NullPointerException("Queue is Empty!!!");
        if (this.reversedStack.isEmpty())
            this.balanceOrderForDequeue();
        return new QueueImpl<>(this.orderedStack, this.reversedStack.next);
    }

    @Override
    public T head() {
        if (this.isEmpty()) throw new RuntimeException("Queue is Empty!!!");
        if (this.reversedStack.isEmpty())
            this.balanceOrderForDequeue();
        return this.reversedStack.value;
    }

    @Override
    public boolean isEmpty() {
        return this.orderedStack.isEmpty() && this.reversedStack.isEmpty();
    }

    /**
     * Move all entries from orderedStack to reversedStack
     * and orderStack becomes empty
     */
    private void balanceOrderForDequeue() {
        this.reversedStack = this.orderedStack.reverse();
        this.orderedStack = new StackNode<>();
    }

    private class StackNode<E> {
        private E value;
        private StackNode<E> next;
        private int size;

        StackNode() {
            this.value = null;
            this.next = null;
            this.size = 0;
        }

        private StackNode(E value, StackNode<E> next) {
            this.value = value;
            this.next = next;
            this.size = next.size + 1;
        }

        private StackNode<E> reverse() {
            StackNode<E> newReversedStack = new StackNode<>();
            StackNode<E> currentNode = this;
            while (currentNode.size > 0) {
                newReversedStack = newReversedStack.addFront(currentNode.value);
                currentNode = currentNode.next;
            }
            return newReversedStack;
        }

        private StackNode<E> addFront(E value) {
            return new StackNode<>(value, this);
        }

        boolean isEmpty() {
            return this.size == 0;
        }
    }
}
