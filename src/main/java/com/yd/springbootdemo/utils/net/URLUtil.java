package com.yd.springbootdemo.utils.net;


import com.yd.springbootdemo.utils.lang.ObjUtil;
import com.yd.springbootdemo.utils.exceptions.UtilException;
import com.yd.springbootdemo.utils.io.IORuntimeException;
import com.yd.springbootdemo.utils.io.IoUtil;
import com.yd.springbootdemo.utils.lang.Assert;
import com.yd.springbootdemo.utils.text.CharUtil;
import com.yd.springbootdemo.utils.text.CharsetUtil;
import com.yd.springbootdemo.utils.text.StrUtil;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;

/**
 * 统一资源定位符相关工具类
 * 
 *
 * 
 */
public class URLUtil {

	/** URL 协议表示文件: "file" */
	public static final String URL_PROTOCOL_FILE = "file";
	/** URL 协议表示JBoss文件: "vfsfile" */
	public static final String URL_PROTOCOL_VFSFILE = "vfsfile";
	/** URL 协议表示JBoss VFS资源: "vfs" */
	public static final String URL_PROTOCOL_VFS = "vfs";

	/**
	 * 通过一个字符串形式的URL地址创建URL对象
	 * 
	 * @param url URL
	 * @return URL对象
	 */
	public static URL url(String url) {
		return url(url, null);
	}

	/**
	 * 通过一个字符串形式的URL地址创建URL对象
	 * 
	 * @param url URL
	 * @param handler {@link URLStreamHandler}
	 * @return URL对象
	 * @since 4.1.1
	 */
	public static URL url(String url, URLStreamHandler handler) {
		Assert.notNull(url, "URL must not be null");

		try {
			return new URL(null, url, handler);
		} catch (MalformedURLException e) {
			// 尝试文件路径
			try {
				return new File(url).toURI().toURL();
			} catch (MalformedURLException ex2) {
				throw new UtilException(e);
			}
		}
	}

	/**
	 * 将URL字符串转换为URL对象，并做必要验证
	 * 
	 * @param urlStr URL字符串
	 * @return URL
	 * @since 4.1.9
	 */
	public static URL toUrlForHttp(String urlStr) {
		return toUrlForHttp(urlStr, null);
	}

	/**
	 * 将URL字符串转换为URL对象，并做必要验证
	 * 
	 * @param urlStr URL字符串
	 * @param handler {@link URLStreamHandler}
	 * @return URL
	 * @since 4.1.9
	 */
	public static URL toUrlForHttp(String urlStr, URLStreamHandler handler) {
		Assert.notBlank(urlStr, "Url is blank !");
		// 编码空白符，防止空格引起的请求异常
		urlStr = encodeBlank(urlStr);
		try {
			return new URL(null, urlStr, handler);
		} catch (MalformedURLException e) {
			throw new UtilException(e);
		}
	}
	
	/**
	 * 单独编码URL中的空白符，空白符编码为%20
	 * 
	 * @param urlStr URL字符串
	 * @return 编码后的字符串
	 * @since 4.5.14
	 */
	public static String encodeBlank(CharSequence urlStr) {
		if (urlStr == null) {
			return null;
		}

		int len = urlStr.length();
		final StringBuilder sb = new StringBuilder(len);
		char c;
		for (int i = 0; i < len; i++) {
			c = urlStr.charAt(i);
			if (CharUtil.isBlankChar(c)) {
				sb.append("%20");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 获得URL，常用于使用绝对路径时的情况
	 * 
	 * @param file URL对应的文件对象
	 * @return URL
	 * @exception UtilException MalformedURLException
	 */
	public static URL getURL(File file) {
		Assert.notNull(file, "FileUtil is null !");
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new UtilException(e, "Error occured when get URL!");
		}
	}

	/**
	 * 获得URL，常用于使用绝对路径时的情况
	 * 
	 * @param files URL对应的文件对象
	 * @return URL
	 * @exception UtilException MalformedURLException
	 */
	public static URL[] getURLs(File... files) {
		final URL[] urls = new URL[files.length];
		try {
			for (int i = 0; i < files.length; i++) {
				urls[i] = files[i].toURI().toURL();
			}
		} catch (MalformedURLException e) {
			throw new UtilException(e, "Error occured when get URL!");
		}

		return urls;
	}

	/**
	 * 补全相对路径
	 * 
	 * @param baseUrl 基准URL
	 * @param relativePath 相对URL
	 * @return 相对路径
	 * @exception UtilException MalformedURLException
	 */
	public static String complateUrl(String baseUrl, String relativePath) {
		baseUrl = normalize(baseUrl, false);
		if (StrUtil.isBlank(baseUrl)) {
			return null;
		}

		try {
			final URL absoluteUrl = new URL(baseUrl);
			final URL parseUrl = new URL(absoluteUrl, relativePath);
			return parseUrl.toString();
		} catch (MalformedURLException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 编码URL，默认使用UTF-8编码<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。
	 * 
	 * @param url URL
	 * @return 编码后的URL
	 * @exception UtilException UnsupportedEncodingException
	 */
	public static String encodeAll(String url) {
		return encodeAll(url, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 编码URL<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。
	 * 
	 * @param url URL
	 * @param charset 编码
	 * @return 编码后的URL
	 * @exception UtilException UnsupportedEncodingException
	 */
	public static String encodeAll(String url, Charset charset) throws UtilException {
		try {
			return URLEncoder.encode(url, charset.toString());
		} catch (UnsupportedEncodingException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 编码URL，默认使用UTF-8编码<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于URL自动编码，类似于浏览器中键入地址自动编码，对于像类似于“/”的字符不再编码
	 * 
	 * @param url URL
	 * @return 编码后的URL
	 * @exception UtilException UnsupportedEncodingException
	 * @since 3.1.2
	 */
	public static String encode(String url) {
		return encode(url, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 编码字符为 application/x-www-form-urlencoded<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于URL自动编码，类似于浏览器中键入地址自动编码，对于像类似于“/”的字符不再编码
	 * 
	 * @param url 被编码内容
	 * @param charset 编码
	 * @return 编码后的字符
	 * @since 4.4.1
	 */
	public static String encode(String url, Charset charset){
		if (StrUtil.isEmpty(url)|| ObjUtil.isEmpty(charset)) {
			return url;
		}
		return encode(url,charset.name());
	}


	/**
	 * 编码URL字符为 application/x-www-form-urlencoded<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于URL自动编码，类似于浏览器中键入地址自动编码，对于像类似于“/”的字符不再编码
	 * 
	 * @param url URL
	 * @param charset 编码
	 * @return 编码后的URL
	 * @exception UtilException UnsupportedEncodingException
	 */
	public static String encode(String url, String charset) throws UtilException {
		String res;
		if (StrUtil.isEmpty(url)) {
			return url;
		}
		try{
			res = URLEncoder.encode(url, StrUtil.isBlank(charset) ? CharsetUtil.defaultCharsetName() : charset);
		}
		catch (Exception e){
			throw new UtilException(e.getMessage());
		}
		return res;
	}


	/**
	 * 解码URL<br>
	 * 将%开头的16进制表示的内容解码。
	 * 
	 * @param url URL
	 * @return 解码后的URL
	 * @exception UtilException UnsupportedEncodingException
	 * @since 3.1.2
	 */
	public static String decode(String url) throws UtilException {
		return decode(url, CharsetUtil.UTF_8);
	}

	/**
	 * 解码application/x-www-form-urlencoded字符
	 * 
	 * @param content 被解码内容
	 * @param charset 编码
	 * @return 编码后的字符
	 * @since 4.4.1
	 */
	public static String decode(String content, Charset charset) {
		if (null == charset) {
			charset = CharsetUtil.defaultCharset();
		}
		return decode(content, charset.name());
	}

	/**
	 * 解码URL<br>
	 * 将%开头的16进制表示的内容解码。
	 * 
	 * @param url URL
	 * @param charset 编码
	 * @return 解码后的URL
	 * @exception UtilException UnsupportedEncodingException
	 */
	public static String decode(String url, String charset) throws UtilException {
		if (StrUtil.isEmpty(url)) {
			return url;
		}
		try {
			return URLDecoder.decode(url, charset);
		} catch (UnsupportedEncodingException e) {
			throw new UtilException(e, "Unsupported encoding: [{}]", charset);
		}
	}

	/**
	 * 获得path部分<br>
	 * 
	 * @param uriStr URI路径
	 * @return path
	 * @exception UtilException 包装URISyntaxException
	 */
	public static String getPath(String uriStr) {
		URI uri = null;
		try {
			uri = new URI(uriStr);
		} catch (URISyntaxException e) {
			throw new UtilException(e);
		}
		return uri.getPath();
	}

	/**
	 * 从URL对象中获取不被编码的路径Path<br>
	 * 对于本地路径，URL对象的getPath方法对于包含中文或空格时会被编码，导致本读路径读取错误。<br>
	 * 此方法将URL转为URI后获取路径用于解决路径被编码的问题
	 * 
	 * @param url {@link URL}
	 * @return 路径
	 * @since 3.0.8
	 */
	public static String getDecodedPath(URL url) {
		if (null == url) {
			return null;
		}

		String path = null;
		try {
			// URL对象的getPath方法对于包含中文或空格的问题
			path = URLUtil.toURI(url).getPath();
		} catch (UtilException e) {
			// ignore
		}
		return (null != path) ? path : url.getPath();
	}

	/**
	 * 转URL为URI
	 * 
	 * @param url URL
	 * @return URI
	 * @exception UtilException 包装URISyntaxException
	 */
	public static URI toURI(URL url) throws UtilException {
		if (null == url) {
			return null;
		}
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 转字符串为URI
	 * 
	 * @param location 字符串路径
	 * @return URI
	 * @exception UtilException 包装URISyntaxException
	 */
	public static URI toURI(String location) throws UtilException {
		try {
			return new URI(location.replace(" ", "%20"));
		} catch (URISyntaxException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 提供的URL是否为文件<br>
	 * 文件协议包括"file", "vfsfile" 或 "vfs".
	 * 
	 * @param url {@link URL}
	 * @return 是否为文件
	 * @since 3.0.9
	 */
	public static boolean isFileURL(URL url) {
		String protocol = url.getProtocol();
		return (URL_PROTOCOL_FILE.equals(protocol) || //
				URL_PROTOCOL_VFSFILE.equals(protocol) || //
				URL_PROTOCOL_VFS.equals(protocol));
	}




	/**
	 * 从URL中获取流
	 * 
	 * @param url {@link URL}
	 * @return InputStream流
	 * @since 3.2.1
	 */
	public static InputStream getStream(URL url) {
		Assert.notNull(url);
		try {
			return url.openStream();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得Reader
	 * 
	 * @param url {@link URL}
	 * @param charset 编码
	 * @return {@link BufferedReader}
	 * @since 3.2.1
	 */
	public static BufferedReader getReader(URL url, Charset charset) {
		return IoUtil.getReader(getStream(url), charset);
	}


	/**
	 * 标准化URL字符串，包括：
	 * 
	 * <pre>
	 * 1. 多个/替换为一个
	 * </pre>
	 * 
	 * @param url URL字符串
	 * @return 标准化后的URL字符串
	 */
	public static String normalize(String url) {
		return normalize(url, false);
	}

	/**
	 * 标准化URL字符串，包括：
	 * 
	 * <pre>
	 * 1. 多个/替换为一个
	 * </pre>
	 * 
	 * @param url URL字符串
	 * @param isEncodeBody 是否对URL中body部分的中文和特殊字符做转义（不包括http:和/）
	 * @return 标准化后的URL字符串
	 * @since 4.4.1
	 */
	public static String normalize(String url, boolean isEncodeBody) {
		if (StrUtil.isBlank(url)) {
			return url;
		}
		final int sepIndex = url.indexOf("://");
		String pre;
		String body;
		if (sepIndex > 0) {
			pre = StrUtil.subPre(url, sepIndex + 3);
			body = StrUtil.subSuf(url, sepIndex + 3);
		} else {
			pre = "http://";
			body = url;
		}

		final int paramsSepIndex = StrUtil.indexOf(body, '?');
		String params = null;
		if (paramsSepIndex > 0) {
			params = StrUtil.subSuf(body, paramsSepIndex);
			body = StrUtil.subPre(body, paramsSepIndex);
		}

		// 去除开头的\或者/
		body = body.replaceAll("^[\\/]+", StrUtil.EMPTY);
		// 替换多个\或/为单个/
		body = body.replace("\\", "/").replaceAll("//+", "/");
		if (isEncodeBody) {
			body = encode(body);
		}
		return pre + body + StrUtil.nullToEmpty(params);
	}
}
