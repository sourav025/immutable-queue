package com.srv.queue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private Queue<Integer> queue;

    @Before
    public void setup() {
        // initialization with implementation
        queue = new QueueImpl<>();
    }

    @Test
    public void should_have_FIFO_property() {
        Queue<Integer> queue1 = this.queue;
        for (int i = 1; i < 10; i++) {
            queue1 = queue1.enQueue(i);
        }
        for (int i = 1; i < 10; i++) {
            assertEquals(i, (int) queue1.head());
            queue1 = queue1.deQueue();
        }
        assertTrue(queue1.isEmpty());
    }

    @Test
    public void should_be_idempotent_current_object_should_not_change() {
        Queue<Integer> queue1 = this.queue;
        for (int i = 0; i < 10; i++)
            queue1.enQueue(i);
        assertTrue(queue1.isEmpty());
    }

    @Test
    public void should_create_new_object_everytime() {
        // object reference should be changed
        Queue<Integer> queue1 = this.queue.enQueue(0);
        for (int i = 1; i < 10; i++) {
            assertFalse(queue1 == queue1.enQueue(i));
        }
    }

    @Test
    public void enQueue_should_workd_fine_and_object_maintain_immutability() {
        Queue<Integer> q1 = this.queue.enQueue(1);
        Queue<Integer> q2 = q1.enQueue(2);
        Queue<Integer> q3 = q2.enQueue(3);
        Queue<Integer> q4 = q3.enQueue(4);
        Queue<Integer> q5 = q3.enQueue(5);

        assertThat(dequesAndGetAll(q1), hasItems(1));
        assertThat(dequesAndGetAll(q2), hasItems(1, 2));
        assertThat(dequesAndGetAll(q3), hasItems(1, 2, 3));
        assertThat(dequesAndGetAll(q4), hasItems(1, 2, 3, 4));
        assertThat(dequesAndGetAll(q5), hasItems(1, 2, 3, 5));

        assertThat(q5.isEmpty(), is(false));
    }

    @Test
    public void deQueue_should_workd_fine_and_object_maintain_immutability() {
        Queue<Integer> q1 = this.queue.enQueue(1);
        Queue<Integer> q2 = q1.enQueue(2);
        Queue<Integer> q3 = q2.enQueue(3);
        Queue<Integer> q4 = q3.enQueue(4);

        assertThat(dequesAndGetAll(q4), hasItems(1, 2, 3, 4));

        Queue<Integer> q400 = q4.deQueue().deQueue();
        assertThat(dequesAndGetAll(q400), hasItems(3, 4));

        Queue<Integer> q40056 = q4.enQueue(5).enQueue(6);
        assertThat(dequesAndGetAll(q400), hasItems(3, 4)); // unchanged
        assertThat(dequesAndGetAll(q40056), hasItems(3, 4, 5, 6)); // new object
    }

    private List<Integer> dequesAndGetAll(Queue<Integer> q) {
        List<Integer> dqs = new ArrayList<>();
        while (!q.isEmpty()) {
            dqs.add(q.head());
            q = q.deQueue();
        }
        return dqs;
    }


}
