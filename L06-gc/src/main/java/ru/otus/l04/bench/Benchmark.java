package ru.otus.l04.bench;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by tully.
 */
class Benchmark implements BenchmarkMBean {
    private final int loopCounter;
    private volatile int size = 0;
    private final float ReSizeCoefficient = 1.01f;

    public Benchmark( int loopCounter ) {
        this.loopCounter = loopCounter;
    }

    private String getNewObjectForFill() {
        return new String( new char[ 0 ] );
    }

    void run() throws InterruptedException {
        for ( int idx = 0; idx < loopCounter; idx++ ) {
            List<String> stringList = new ArrayList<>(size);
            int oldSize = size;
            //int halfOldSize = size % 2;
            for ( int i = 0; i < size; i++ ) {
                stringList.add(getNewObjectForFill());
            }
            Thread.sleep( 10 ); //Label_1
            size *= ReSizeCoefficient; // bobble
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize( int size ) {
        System.out.println( "new size:" + size );
        this.size = size;
    }

}
