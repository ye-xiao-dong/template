package com.yd.springbootdemo.utils.io;

/**
 * 快速缓冲，将数据存放在缓冲集中，取代以往的单一数组
 *
 * @author： 叶小东
 * @date： 2020/3/10 19:40
 */
public class FastByteBuffer {

    /**
     * 缓冲集
     */
    private byte[][] buffers = new byte[16][];

    /**
     * 缓冲数
     */
    private int buffersCount;

    /**
     * 当前缓冲索引
     */
    private int currentBufferIndex = -1;

    /**
     * 当前缓冲
     */
    private byte[] currentBuffer;

    /**
     * 当前缓冲偏移量
     */
    private int offset;

    /**
     * 缓冲字节数
     */
    private int size;

    /**
     * 一个缓冲区的最小字节数
     */
    private final int minChunkLen;

    public FastByteBuffer(int size) {
        this.minChunkLen = Math.abs(size);
    }

    /**
     * 分配下一个缓冲区，不会小于1024
     *
     * @param newSize 理想缓冲区字节数
     */
    private void needNewBuffer(int newSize) {
        int delta = newSize - size;
        int newBufferSize = Math.max(minChunkLen, delta);

        currentBufferIndex++;
        currentBuffer = new byte[newBufferSize];
        offset = 0;

        // add buffer
        if (currentBufferIndex >= buffers.length) {
            int newLen = buffers.length << 1;
            byte[][] newBuffers = new byte[newLen][];
            System.arraycopy(buffers, 0, newBuffers, 0, buffers.length);
            buffers = newBuffers;
        }
        buffers[currentBufferIndex] = currentBuffer;
        buffersCount++;
    }

    /**
     * 向快速缓冲加入数据
     *
     * @param array 数据
     * @param off 偏移量
     * @param len 字节数
     * @return 快速缓冲自身 @see FastByteBuffer
     */
    public FastByteBuffer append(byte[] array, int off, int len) {
        int end = off + len;
        if ((off < 0) || (len < 0) || (end > array.length)) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return this;
        }
        int newSize = size + len;
        int remaining = len;

        if (currentBuffer != null) {
            // first try to fill current buffer
            int part = Math.min(remaining, currentBuffer.length - offset);
            System.arraycopy(array, end - remaining, currentBuffer, offset, part);
            remaining -= part;
            offset += part;
            size += part;
        }
        if (remaining > 0) {
            // still some data left
            // ask for new buffer
            needNewBuffer(newSize);

            // then copy remaining
            // but this time we are sure that it will fit
            int part = Math.min(remaining, currentBuffer.length - offset);
            System.arraycopy(array, end - remaining, currentBuffer, offset, part);
            offset += part;
            size += part;
        }
        return this;
    }

    /**
     * 向快速缓冲加入一个字节
     *
     * @param element 一个字节的数据
     * @return 快速缓冲自身 @see FastByteBuffer
     */
    public FastByteBuffer append(byte element) {
        if ((currentBuffer == null) || (offset == currentBuffer.length)) {
            needNewBuffer(size + 1);
        }
        currentBuffer[offset] = element;
        offset++;
        size++;

        return this;
    }

    public int size() {
        return size;
    }

    /**
     * 当前缓冲位于缓冲区的索引位
     *
     * @return {@link #currentBufferIndex}
     */
    public int index() {
        return currentBufferIndex;
    }

    public int offset() {
        return offset;
    }

    /**
     * 根据索引位返回缓冲集中的缓冲
     *
     * @param index 索引位
     * @return 缓冲
     */
    public byte[] array(int index) {
        return buffers[index];
    }

    public void reset() {
        size = 0;
        offset = 0;
        currentBufferIndex = -1;
        currentBuffer = null;
        buffersCount = 0;
    }

    /**
     * 返回快速缓冲中的数据
     *
     * @return 快速缓冲中的数据
     */
    public byte[] toArray() {
        int pos = 0;
        byte[] array = new byte[size];

        if (currentBufferIndex == -1) {
            return array;
        }
        for (int i = 0; i < currentBufferIndex; i++) {
            int len = buffers[i].length;
            System.arraycopy(buffers[i], 0, array, pos, len);
            pos += len;
        }
        System.arraycopy(buffers[currentBufferIndex], 0, array, pos, offset);

        return array;
    }

}