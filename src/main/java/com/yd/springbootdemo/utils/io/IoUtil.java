package com.yd.springbootdemo.utils.io;


import com.yd.springbootdemo.utils.text.HexUtil;
import com.yd.springbootdemo.utils.convert.Convert;
import com.yd.springbootdemo.utils.exceptions.UtilException;
import com.yd.springbootdemo.utils.lang.Assert;
import com.yd.springbootdemo.utils.text.CharsetUtil;
import com.yd.springbootdemo.utils.text.StrUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

/**
 * IO工具类<br>
 * IO工具类只是辅助流的读写，并不负责关闭流。原因是流可能被多次读写，读写关闭后容易造成问题。
 * 
 *
 *
 */
public class IoUtil {

	/** 默认缓存大小 8192*/
	public static final int DEFAULT_BUFFER_SIZE = 2 << 12;
	/** 默认中等缓存大小 16384*/
	public static final int DEFAULT_MIDDLE_BUFFER_SIZE = 2 << 13;
	/** 默认大缓存大小 32768*/
	public static final int DEFAULT_LARGE_BUFFER_SIZE = 2 << 14;

	/** 数据流末尾 */
	public static final int EOF = -1;

	// -------------------------------------------------------------------------------------- Copy start
	/**
	 * 将Reader中的内容复制到Writer中 使用默认缓存大小
	 * 
	 * @param reader Reader
	 * @param writer Writer
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer) throws IORuntimeException {
		return copy(reader, writer, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 将Reader中的内容复制到Writer中
	 * 
	 * @param reader Reader
	 * @param writer Writer
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize) throws IORuntimeException {
		return copy(reader, writer, bufferSize, null);
	}

	/**
	 * 将Reader中的内容复制到Writer中
	 * 
	 * @param reader Reader
	 * @param writer Writer
	 * @param bufferSize 缓存大小
	 * @param streamProgress 进度处理器
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
		char[] buffer = new char[bufferSize];
		long size = 0;
		int readSize;
		if (null != streamProgress) {
			streamProgress.start();
		}
		try {
			while ((readSize = reader.read(buffer, 0, bufferSize)) != EOF) {
				writer.write(buffer, 0, readSize);
				size += readSize;
				writer.flush();
				if (null != streamProgress) {
					streamProgress.progress(size);
				}
			}
		} catch (Exception e) {
			throw new IORuntimeException(e);
		}
		if (null != streamProgress) {
			streamProgress.finish();
		}
		return size;
	}

	/**
	 * 拷贝流，使用默认Buffer大小
	 * 
	 * @param in 输入流
	 * @param out 输出流
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(InputStream in, OutputStream out) throws IORuntimeException {
		return copy(in, out, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 拷贝流
	 * 
	 * @param in 输入流
	 * @param out 输出流
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize) throws IORuntimeException {
		return copy(in, out, bufferSize, null);
	}

	/**
	 * 拷贝流
	 * 
	 * @param in 输入流
	 * @param out 输出流
	 * @param bufferSize 缓存大小
	 * @param streamProgress 进度条
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
		Assert.notNull(in, "InputStream is null !");
		Assert.notNull(out, "OutputStream is null !");
		if (bufferSize <= 0) {
			bufferSize = DEFAULT_BUFFER_SIZE;
		}

		byte[] buffer = new byte[bufferSize];
		if (null != streamProgress) {
			streamProgress.start();
		}
		long size = 0;
		try {
			for (int readSize = -1; (readSize = in.read(buffer)) != EOF;) {
				out.write(buffer, 0, readSize);
				size += readSize;
				out.flush();
				if (null != streamProgress) {
					streamProgress.progress(size);
				}
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		if (null != streamProgress) {
			streamProgress.finish();
		}
		return size;
	}

	/**
	 * 拷贝流 thanks to: https://github.com/venusdrogon/feilong-io/blob/master/src/main/java/com/feilong/io/IOWriteUtil.java<br>
	 * 本方法不会关闭流
	 * 
	 * @param in 输入流
	 * @param out 输出流
	 * @param bufferSize 缓存大小
	 * @param streamProgress 进度条
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copyByNIO(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
		return copy(Channels.newChannel(in), Channels.newChannel(out), bufferSize, streamProgress);
	}

	/**
	 * 拷贝文件流，使用NIO
	 * 
	 * @param in 输入
	 * @param out 输出
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(FileInputStream in, FileOutputStream out) throws IORuntimeException {
		Assert.notNull(in, "FileInputStream is null!");
		Assert.notNull(out, "FileOutputStream is null!");

		final FileChannel inChannel = in.getChannel();
		final FileChannel outChannel = out.getChannel();

		try {
			return inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 拷贝流，使用NIO，不会关闭流
	 * 
	 * @param in {@link ReadableByteChannel}
	 * @param out {@link WritableByteChannel}
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 * @since 4.5.0
	 */
	public static long copy(ReadableByteChannel in, WritableByteChannel out) throws IORuntimeException {
		return copy(in, out, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 拷贝流，使用NIO，不会关闭流
	 * 
	 * @param in {@link ReadableByteChannel}
	 * @param out {@link WritableByteChannel}
	 * @param bufferSize 缓冲大小，如果小于等于0，使用默认
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 * @since 4.5.0
	 */
	public static long copy(ReadableByteChannel in, WritableByteChannel out, int bufferSize) throws IORuntimeException {
		return copy(in, out, bufferSize, null);
	}

	/**
	 * 拷贝流，使用NIO，不会关闭流
	 * 
	 * @param in {@link ReadableByteChannel}
	 * @param out {@link WritableByteChannel}
	 * @param bufferSize 缓冲大小，如果小于等于0，使用默认
	 * @param streamProgress {@link StreamProgress}进度处理器
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(ReadableByteChannel in, WritableByteChannel out, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
		Assert.notNull(in, "InputStream is null !");
		Assert.notNull(out, "OutputStream is null !");

		ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize <= 0 ? DEFAULT_BUFFER_SIZE : bufferSize);
		long size = 0;
		if (null != streamProgress) {
			streamProgress.start();
		}
		try {
			while (in.read(byteBuffer) != EOF) {
				byteBuffer.flip();// 写转读
				size += out.write(byteBuffer);
				byteBuffer.clear();
				if (null != streamProgress) {
					streamProgress.progress(size);
				}
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		if (null != streamProgress) {
			streamProgress.finish();
		}

		return size;
	}
	// -------------------------------------------------------------------------------------- Copy end

	// -------------------------------------------------------------------------------------- getReader and getWriter start
	/**
	 * 获得一个文件读取器
	 * 
	 * @param in 输入流
	 * @param charsetName 字符集名称
	 * @return BufferedReader对象
	 */
	public static BufferedReader getReader(InputStream in, String charsetName) {
		return getReader(in, Charset.forName(charsetName));
	}

	/**
	 * 获得一个Reader
	 * 
	 * @param in 输入流
	 * @param charset 字符集
	 * @return BufferedReader对象
	 */
	public static BufferedReader getReader(InputStream in, Charset charset) {
		if (null == in) {
			return null;
		}

		InputStreamReader reader = null;
		if (null == charset) {
			reader = new InputStreamReader(in);
		} else {
			reader = new InputStreamReader(in, charset);
		}

		return new BufferedReader(reader);
	}

	/**
	 * 获得{@link BufferedReader}<br>
	 * 如果是{@link BufferedReader}强转返回，否则新建。如果提供的Reader为null返回null
	 * 
	 * @param reader 普通Reader，如果为null返回null
	 * @return {@link BufferedReader} or null
	 * @since 3.0.9
	 */
	public static BufferedReader getReader(Reader reader) {
		if (null == reader) {
			return null;
		}

		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
	}

	/**
	 * 获得{@link PushbackReader}<br>
	 * 如果是{@link PushbackReader}强转返回，否则新建
	 * 
	 * @param reader 普通Reader
	 * @param pushBackSize 推后的byte数
	 * @return {@link PushbackReader}
	 * @since 3.1.0
	 */
	public static PushbackReader getPushBackReader(Reader reader, int pushBackSize) {
		return (reader instanceof PushbackReader) ? (PushbackReader) reader : new PushbackReader(reader, pushBackSize);
	}

	/**
	 * 获得一个Writer
	 * 
	 * @param out 输入流
	 * @param charsetName 字符集
	 * @return OutputStreamWriter对象
	 */
	public static OutputStreamWriter getWriter(OutputStream out, String charsetName) {
		return getWriter(out, Charset.forName(charsetName));
	}

	/**
	 * 获得一个Writer
	 * 
	 * @param out 输入流
	 * @param charset 字符集
	 * @return OutputStreamWriter对象
	 */
	public static OutputStreamWriter getWriter(OutputStream out, Charset charset) {
		if (null == out) {
			return null;
		}

		if (null == charset) {
			return new OutputStreamWriter(out);
		} else {
			return new OutputStreamWriter(out, charset);
		}
	}
	// -------------------------------------------------------------------------------------- getReader and getWriter end

	// -------------------------------------------------------------------------------------- read start
	/**
	 * 从流中读取内容
	 * 
	 * @param in 输入流
	 * @param charsetName 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String read(InputStream in, String charsetName) throws IORuntimeException {
		FastOutputStream out = read(in);
		return StrUtil.isBlank(charsetName) ? out.toString() : out.toString(charsetName);
	}

	/**
	 * 从流中读取内容，读取完毕后并不关闭流
	 * 
	 * @param in 输入流，读取完毕后并不关闭流
	 * @param charset 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String read(InputStream in, Charset charset) throws IORuntimeException {
		FastOutputStream out = read(in);
		return null == charset ? out.toString() : out.toString(charset);
	}

	/**
	 * 从流中读取内容，读取完毕后并不关闭流
	 * 
	 * @param channel 可读通道，读取完毕后并不关闭通道
	 * @param charset 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 * @since 4.5.0
	 */
	public static String read(ReadableByteChannel channel, Charset charset) throws IORuntimeException {
		FastOutputStream out = read(channel);
		return null == charset ? out.toString() : out.toString(charset);
	}

	/**
	 * 从流中读取内容，读到输出流中
	 * 
	 * @param in 输入流
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 */
	public static FastOutputStream read(InputStream in) throws IORuntimeException {
		final FastOutputStream out = new FastOutputStream();
		copy(in, out);
		return out;
	}

	/**
	 * 从流中读取内容，读到输出流中
	 * 
	 * @param channel 可读通道，读取完毕后并不关闭通道
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 */
	public static FastOutputStream read(ReadableByteChannel channel) throws IORuntimeException {
		final FastOutputStream out = new FastOutputStream();
		copy(channel, Channels.newChannel(out));
		return out;
	}

	/**
	 * 从Reader中读取String，读取完毕后并不关闭Reader
	 * 
	 * @param reader Reader
	 * @return String
	 * @throws IORuntimeException IO异常
	 */
	public static String read(Reader reader) throws IORuntimeException {
		final StringBuilder builder = StrUtil.builder();
		final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
		try {
			while (-1 != reader.read(buffer)) {
				builder.append(buffer.flip().toString());
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return builder.toString();
	}

	/**
	 * 从FileChannel中读取UTF-8编码内容
	 * 
	 * @param fileChannel 文件管道
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String readUtf8(FileChannel fileChannel) throws IORuntimeException {
		return read(fileChannel, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 从FileChannel中读取内容，读取完毕后并不关闭Channel
	 * 
	 * @param fileChannel 文件管道
	 * @param charsetName 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String read(FileChannel fileChannel, String charsetName) throws IORuntimeException {
		return read(fileChannel, CharsetUtil.charset(charsetName));
	}

	/**
	 * 从FileChannel中读取内容
	 * 
	 * @param fileChannel 文件管道
	 * @param charset 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String read(FileChannel fileChannel, Charset charset) throws IORuntimeException {
		MappedByteBuffer buffer;
		try {
			buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size()).load();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return StrUtil.str(buffer, charset);
	}

	/**
	 * 从流中读取bytes
	 * 
	 * @param in {@link InputStream}
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 */
	public static byte[] readBytes(InputStream in) throws IORuntimeException {
		final FastOutputStream out = new FastOutputStream();
		copy(in, out);
		return out.toByteArray();
	}

	/**
	 * 读取指定长度的byte数组，不关闭流
	 * 
	 * @param in {@link InputStream}，为null返回null
	 * @param length 长度，小于等于0返回空byte数组
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 */
	public static byte[] readBytes(InputStream in, int length) throws IORuntimeException {
		if (null == in) {
			return null;
		}
		if (length <= 0) {
			return new byte[0];
		}

		byte[] b = new byte[length];
		int readLength;
		try {
			readLength = in.read(b);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		if (readLength > 0 && readLength < length) {
			byte[] b2 = new byte[length];
			System.arraycopy(b, 0, b2, 0, readLength);
			return b2;
		} else {
			return b;
		}
	}

	/**
	 * 读取16进制字符串
	 * 
	 * @param in {@link InputStream}
	 * @param length 长度
	 * @param toLowerCase true 传换成小写格式 ， false 传换成大写格式
	 * @return 16进制字符串
	 * @throws IORuntimeException IO异常
	 */
	public static String readHex(InputStream in, int length, boolean toLowerCase) throws IORuntimeException {
		return HexUtil.encodeHexStr(readBytes(in, length), toLowerCase);
	}

	/**
	 * 从流中读取前28个byte并转换为16进制，字母部分使用大写
	 * 
	 * @param in {@link InputStream}
	 * @return 16进制字符串
	 * @throws IORuntimeException IO异常
	 */
	public static String readHex28Upper(InputStream in) throws IORuntimeException {
		return readHex(in, 28, false);
	}

	/**
	 * 从流中读取前28个byte并转换为16进制，字母部分使用小写
	 * 
	 * @param in {@link InputStream}
	 * @return 16进制字符串
	 * @throws IORuntimeException IO异常
	 */
	public static String readHex28Lower(InputStream in) throws IORuntimeException {
		return readHex(in, 28, true);
	}

	/**
	 * 从流中读取内容，读到输出流中
	 * 
	 * @param <T> 读取对象的类型
	 * @param in 输入流
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 * @throws UtilException ClassNotFoundException包装
	 */
	public static <T> T readObj(InputStream in) throws IORuntimeException, UtilException {
		if (in == null) {
			throw new IllegalArgumentException("The InputStream must not be null");
		}
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(in);
			@SuppressWarnings("unchecked") // may fail with CCE if serialised form is incorrect
			final T obj = (T) ois.readObject();
			return obj;
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new UtilException(e);
		}
	}
	// -------------------------------------------------------------------------------------- read end

	/**
	 * String 转为流
	 * 
	 * @param content 内容
	 * @param charsetName 编码
	 * @return 字节流
	 */
	public static ByteArrayInputStream toStream(String content, String charsetName) {
		return toStream(content, CharsetUtil.charset(charsetName));
	}

	/**
	 * String 转为流
	 * 
	 * @param content 内容
	 * @param charset 编码
	 * @return 字节流
	 */
	public static ByteArrayInputStream toStream(String content, Charset charset) {
		if (content == null) {
			return null;
		}
		return toStream(StrUtil.bytes(content, charset));
	}
	
	/**
	 * String 转为UTF-8编码的字节流流
	 * 
	 * @param content 内容
	 * @return 字节流
	 * @since 4.5.1
	 */
	public static ByteArrayInputStream toUtf8Stream(String content) {
		return toStream(content, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * String 转为流
	 * 
	 * @param content 内容bytes
	 * @return 字节流
	 * @since 4.1.8
	 */
	public static ByteArrayInputStream toStream(byte[] content) {
		if (content == null) {
			return null;
		}
		return new ByteArrayInputStream(content);
	}

	/**
	 * 文件转为流
	 * 
	 * @param file 文件
	 * @return {@link FileInputStream}
	 */
	public static FileInputStream toStream(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 转换为{@link BufferedInputStream}
	 * 
	 * @param in {@link InputStream}
	 * @return {@link BufferedInputStream}
	 * @since 4.0.10
	 */
	public static BufferedInputStream toBuffered(InputStream in) {
		return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in);
	}

	/**
	 * 转换为{@link BufferedOutputStream}
	 * 
	 * @param out {@link OutputStream}
	 * @return {@link BufferedOutputStream}
	 * @since 4.0.10
	 */
	public static BufferedOutputStream toBuffered(OutputStream out) {
		return (out instanceof BufferedOutputStream) ? (BufferedOutputStream) out : new BufferedOutputStream(out);
	}

	/**
	 * 将{@link InputStream}转换为支持mark标记的流<br>
	 * 若原流支持mark标记，则返回原流，否则使用{@link BufferedInputStream} 包装之
	 * 
	 * @param in 流
	 * @return {@link InputStream}
	 * @since 4.0.9
	 */
	public static InputStream toMarkSupportStream(InputStream in) {
		if (null == in) {
			return null;
		}
		if (false == in.markSupported()) {
			return new BufferedInputStream(in);
		}
		return in;
	}

	/**
	 * 转换为{@link PushbackInputStream}<br>
	 * 如果传入的输入流已经是{@link PushbackInputStream}，强转返回，否则新建一个
	 * 
	 * @param in {@link InputStream}
	 * @param pushBackSize 推后的byte数
	 * @return {@link PushbackInputStream}
	 * @since 3.1.0
	 */
	public static PushbackInputStream toPushbackStream(InputStream in, int pushBackSize) {
		return (in instanceof PushbackInputStream) ? (PushbackInputStream) in : new PushbackInputStream(in, pushBackSize);
	}

	/**
	 * 将byte[]写到流中
	 * 
	 * @param out 输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param content 写入的内容
	 * @throws IORuntimeException IO异常
	 */
	public static void write(OutputStream out, boolean isCloseOut, byte[] content) throws IORuntimeException {
		try {
			out.write(content);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isCloseOut) {
				close(out);
			}
		}
	}

	/**
	 * 将多部分内容写到流中，自动转换为UTF-8字符串
	 * 
	 * @param out 输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents 写入的内容，调用toString()方法，不包括不会自动换行
	 * @throws IORuntimeException IO异常
	 * @since 3.1.1
	 */
	public static void writeUtf8(OutputStream out, boolean isCloseOut, Object... contents) throws IORuntimeException {
		write(out, CharsetUtil.CHARSET_UTF_8, isCloseOut, contents);
	}

	/**
	 * 将多部分内容写到流中，自动转换为字符串
	 * 
	 * @param out 输出流
	 * @param charsetName 写出的内容的字符集
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents 写入的内容，调用toString()方法，不包括不会自动换行
	 * @throws IORuntimeException IO异常
	 */
	public static void write(OutputStream out, String charsetName, boolean isCloseOut, Object... contents) throws IORuntimeException {
		write(out, CharsetUtil.charset(charsetName), isCloseOut, contents);
	}

	/**
	 * 将多部分内容写到流中，自动转换为字符串
	 * 
	 * @param out 输出流
	 * @param charset 写出的内容的字符集
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents 写入的内容，调用toString()方法，不包括不会自动换行
	 * @throws IORuntimeException IO异常
	 * @since 3.0.9
	 */
	public static void write(OutputStream out, Charset charset, boolean isCloseOut, Object... contents) throws IORuntimeException {
		OutputStreamWriter osw = null;
		try {
			osw = getWriter(out, charset);
			for (Object content : contents) {
				if (content != null) {
					osw.write(Convert.toStr(content, StrUtil.EMPTY));
					osw.flush();
				}
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isCloseOut) {
				close(osw);
			}
		}
	}

	/**
	 * 将多部分内容写到流中
	 * 
	 * @param out 输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents 写入的内容
	 * @throws IORuntimeException IO异常
	 */
	public static void writeObjects(OutputStream out, boolean isCloseOut, Serializable... contents) throws IORuntimeException {
		ObjectOutputStream osw = null;
		try {
			osw = out instanceof ObjectOutputStream ? (ObjectOutputStream) out : new ObjectOutputStream(out);
			for (Object content : contents) {
				if (content != null) {
					osw.writeObject(content);
					osw.flush();
				}
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isCloseOut) {
				close(osw);
			}
		}
	}

	/**
	 * 从缓存中刷出数据
	 * 
	 * @param flushable {@link Flushable}
	 * @since 4.2.2
	 */
	public static void flush(Flushable flushable) {
		if (null != flushable) {
			try {
				flushable.flush();
			} catch (Exception e) {
				// 静默刷出
			}
		}
	}

	/**
	 * 关闭<br>
	 * 关闭失败不会抛出异常
	 * 
	 * @param closeable 被关闭的对象
	 */
	public static void close(Closeable closeable) {
		if (null != closeable) {
			try {
				closeable.close();
			} catch (Exception e) {
				// 静默关闭
			}
		}
	}

	/**
	 * 关闭<br>
	 * 关闭失败不会抛出异常
	 * 
	 * @param closeable 被关闭的对象
	 */
	public static void close(AutoCloseable closeable) {
		if (null != closeable) {
			try {
				closeable.close();
			} catch (Exception e) {
				// 静默关闭
			}
		}
	}

	/**
	 * 尝试关闭指定对象<br>
	 * 判断对象如果实现了{@link AutoCloseable}，则调用之
	 * 
	 * @param obj 可关闭对象
	 * @since 4.3.2
	 */
	public static void closeIfPosible(Object obj) {
		if (obj instanceof AutoCloseable) {
			close((AutoCloseable) obj);
		}
	}
}
