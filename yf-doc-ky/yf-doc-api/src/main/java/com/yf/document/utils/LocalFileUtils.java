/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.yf.document.utils;

import com.yf.boot.base.api.exception.ServiceException;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * 文件工具类
 * @author bool
 */
public class LocalFileUtils {


	/**
	 * 创建文件夹
	 * @param path
	 */
	public static void makeDirs(String path){
		String dirPath = path.substring(0, path.lastIndexOf("/"));
		File dir = new File(dirPath);
		if(!dir.exists()){
			dir.mkdirs();
		}
	}

	/**
	 * 支持以断点的方式输出文件，提供文件在线预览和视频在线播放
	 * @param request
	 * @param response
	 * @param filePath
	 * @throws IOException
	 */
	public static void writeRange(HttpServletRequest request,
								  HttpServletResponse response,
								  String filePath) throws IOException {

		// 读取文件
		File file = new File(filePath);

		//只读模式
		RandomAccessFile randomFile = new RandomAccessFile(file, "r");
		long contentLength = randomFile.length();
		String range = request.getHeader("Range");
		int start = 0, end = 0;
		if (range != null && range.startsWith("bytes=")) {
			String[] values = range.split("=")[1].split("-");
			start = Integer.parseInt(values[0]);
			if (values.length > 1) {
				end = Integer.parseInt(values[1]);
			}
		}
		int requestSize;
		if (end != 0 && end > start) {
			requestSize = end - start + 1;
		} else {
			requestSize = Integer.MAX_VALUE;
		}

		byte[] buffer = new byte[128];
		response.setContentType(MediaUtils.getContentType(filePath));
		response.setHeader("Accept-Ranges", "bytes");
		response.setHeader("ETag", file.getName());
		response.setHeader("Last-Modified", new Date().toString());
		//第一次请求只返回content length来让客户端请求多次实际数据
		if (range == null) {
			response.setHeader("Content-length", contentLength + "");
		} else {
			//以后的多次以断点续传的方式来返回视频数据
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			long requestStart = 0, requestEnd = 0;
			String[] ranges = range.split("=");
			if (ranges.length > 1) {
				String[] rangeData = ranges[1].split("-");
				requestStart = Integer.parseInt(rangeData[0]);
				if (rangeData.length > 1) {
					requestEnd = Integer.parseInt(rangeData[1]);
				}
			}
			long length;
			if (requestEnd > 0) {
				length = requestEnd - requestStart + 1;
				response.setHeader("Content-length", "" + length);
				response.setHeader("Content-Range", "bytes " + requestStart + "-" + requestEnd + "/" + contentLength);
			} else {
				length = contentLength - requestStart;
				response.setHeader("Content-length", "" + length);
				response.setHeader("Content-Range", "bytes " + requestStart + "-" + (contentLength - 1) + "/" + contentLength);
			}
		}
		ServletOutputStream out = response.getOutputStream();
		int needSize = requestSize;
		randomFile.seek(start);
		while (needSize > 0) {
			int len = randomFile.read(buffer);
			if (needSize < buffer.length) {
				out.write(buffer, 0, needSize);
			} else {
				out.write(buffer, 0, len);
				if (len < buffer.length) {
					break;
				}
			}
			needSize -= buffer.length;
		}
		randomFile.close();
		out.close();
	}


	/**
	 * 下载网络文件到本地
	 * @param httpUrl
	 * @param path
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static String downloadFile(String httpUrl, String path, String name) throws Exception {
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}

		URL url = new URL(httpUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(3 * 1000);
		//防止屏蔽程序抓取而放回403错误
		conn.setRequestProperty("User-Agent", "Mozilla/4.0(compatible;MSIE 5.0;Windows NT;DigExt)");
		Long totalSize = Long.parseLong(conn.getHeaderField("Content-Length"));
		if (totalSize > 0) {
			FileUtils.copyURLToFile(url, new File(path + name));
			return path + name;
		} else {
			throw new ServiceException("网络文件下载失败！");
		}
	}

}
