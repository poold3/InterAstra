package io.github.interastra.services;

import java.util.concurrent.locks.ReentrantLock;

public class LockService<T> {
    private T data;
    private final ReentrantLock lock;

    public LockService() {
        this.lock = new ReentrantLock();
    }

    public LockService(final T data) {
        this.data = data;
        this.lock = new ReentrantLock();
    }

    public LockService(final boolean fair) {
        this.lock = new ReentrantLock(fair);
    }

    public LockService(final T data, final boolean fair) {
        this.data = data;
        this.lock = new ReentrantLock(fair);
    }

    public void lock() {
        this.lock.lock();
    }

    public void unlock() {
        this.lock.unlock();
    }

    public T getData() {
        return this.data;
    }

    public void setData(final T data) {
        this.data = data;
    }
}
