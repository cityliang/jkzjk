package com.huntto.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;

import org.apache.log4j.Logger;

public class FileUtil {

	private static Logger log = Logger.getLogger(FileUtil.class);

	/**
	 * 同步图片
	 * 
	 * @param sourcePath 输出文件路径
	 * @param tarPath    源文件路径加名称
	 * @param idCard     图片字节数据
	 */
	public static void photo2Img(String sourcePath, String tarPath, byte[] idCard) {
		File desk = new File(sourcePath);
		if (!desk.exists()) {
			// 先得到文件的上级目录，并创建上级目录，在创建文件
			desk.getParentFile().mkdirs();
			try {
				desk.createNewFile();// 创建文件
			} catch (IOException e) {
				log.info("创建文件失败" + e.getMessage());
				e.printStackTrace();
			}
		}
		try {
			FileImageOutputStream fios = new FileImageOutputStream(new File(tarPath));
			fios.write(idCard, 0, idCard.length);
			fios.close();
			log.info("图片复制成功");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.info("文件不存在：" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.info("文件读取错误：" + e.getMessage());
		}
	}

	public static void main(String[] args) {
//		String sourcePath = "C:\\STS\\IMG\\psb2.jpg";
//		String tarPath = "C:\\STS\\psb.jpg";

		String tarPath = "C:\\STS\\IMG\\psb2.jpg";
		String sourcePath = "C:\\STS\\psb.jpg";

		File file = new File(sourcePath);
		File desk = new File("C:\\STS\\IMG");
		if (!desk.exists()) {
			desk.mkdir();
		}
		try {
			FileImageInputStream fiis = new FileImageInputStream(file);
			FileImageOutputStream fios = new FileImageOutputStream(new File(tarPath));
			int ch = fiis.read();
			while (ch != -1) {
				fios.write(ch);
				ch = fiis.read();
			}
			fiis.close();
			fios.close();
			System.out.println("图片复制成功！");
		} catch (FileNotFoundException e) {
			System.out.println("文件不存在：" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("文件读取错误：" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 输出文件到某个路径
	 * 
	 * @param sourcePath 源文件路径
	 * @param tarPath    目标路径
	 * @param deskPath   输出路径
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map tbJpg(String sourcePath, String tarPath, String deskPath) {
		Map map = new HashMap<>();
		File file = new File(sourcePath);
		File desk = new File(deskPath);
		if (!desk.exists()) {
			desk.mkdir();
		}
		try {
			FileImageInputStream fiis = new FileImageInputStream(file);
			FileImageOutputStream fios = new FileImageOutputStream(new File(tarPath));
			int ch = fiis.read();
			while (ch != -1) {
				fios.write(ch);
				ch = fiis.read();
			}
			fiis.close();
			fios.close();
			map.put("msg", "图片复制成功！");
			return map;
		} catch (FileNotFoundException e) {
			map.put("msg", "文件不存在: " + e.getMessage());

			e.printStackTrace();
			return map;
		} catch (IOException e) {
			map.put("msg", "文件读取错误: " + e.getMessage());
			e.printStackTrace();
			return map;
		}
	}
}
