### lock.tryLock(waitTime, leaseTime, TimeUnit)
巧妙运用CountDownLatch， Future等待通过CountDownLatch.await(timeOut, TimeUnit) 使线程等待
底层是LockSupport.parkNanos(this, nanosTime)