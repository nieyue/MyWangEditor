package com.nieyue.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.nieyue.util.DateUtil;
import com.nieyue.util.FileUploadUtil;
import com.nieyue.util.ThumbnailatorUtils;
import com.nieyue.util.UploaderPath;



/**
 * Html控制类
 * @author yy
 *
 */
@RestController
public class HtmlController {
	/**
	 * Html单个加载
	 * @return
	 */
//	@RequestMapping(value={"/index","/"})
//	public ModelAndView index(Model model){
//		return new ModelAndView("index");
//	}
	@RequestMapping(value={"/404"})
	public ModelAndView go404(){
		return new ModelAndView("404");
	}
	@RequestMapping("/date")
	public Date date(@RequestParam("date")Date date){
		System.out.println(date.toLocaleString());
		return date;
	}
	@RequestMapping("/date1")
	public String date1(@RequestParam("date")Date date){
		System.out.println(date.toLocaleString());
		return date.toString();
	}
	
	/**
	 * 文件增加、修改
	 * @param editorUpload 上传参数
	 * @param width 固定图片宽度
	 * @param height 固定图片高度
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/file/add", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody String addFile(
			@RequestParam("editorUpload") MultipartFile file,
			HttpServletRequest request,HttpSession session ) throws IOException  {
		String fileUrl = null;
		String filedir=DateUtil.getImgDir();
		try{
			fileUrl = FileUploadUtil.FormDataMerImgFileUpload(file, session,UploaderPath.GetValueByKey(UploaderPath.ROOTPATH),UploaderPath.GetValueByKey(UploaderPath.IMG),filedir);
		}catch (IOException e) {
			throw new IOException();
		}
		StringBuffer url=request.getRequestURL();
		String redirect_url = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString(); 
		return redirect_url+fileUrl;
	}
	/**
	 * 图片增加、修改
	 * @param editorUpload 上传图片
	 * @param width （可选）固定图片宽度
	 * @param height （可选）固定图片高度
	 * @param compression 默认0，原图不压缩，1压缩最优
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/img/add", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody String addAdvertiseImg(
			@RequestParam("editorUpload") MultipartFile file,
			@RequestParam(value="width",required=false) Integer width,
			@RequestParam(value="height",required=false) Integer height,
			@RequestParam(value="compression",required=false,defaultValue="0")Integer compression,
			HttpServletRequest request,HttpSession session ) throws IOException  {
		String imgUrl = null;
		String imgdir=DateUtil.getImgDir();
		Map<String ,Integer> kgmap=new HashMap<String,Integer>();
		kgmap.put("width", width);
		kgmap.put("height", height);
		kgmap.put("compression", compression);
		try{
			//imgUrl = FileUploadUtil.FormDataMerImgFileUpload(file, session,UploaderPath.GetValueByKey(UploaderPath.ROOTPATH),UploaderPath.GetValueByKey(UploaderPath.IMG),imgdir);
			imgUrl = ThumbnailatorUtils.fileUpload(file, session,UploaderPath.GetValueByKey(UploaderPath.ROOTPATH),UploaderPath.GetValueByKey(UploaderPath.IMG),imgdir,kgmap);
		}catch (IOException e) {
			throw new IOException();
		}
		StringBuffer url=request.getRequestURL();
		String redirect_url = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString(); 
		//String redirect_url="http://118.123.15.27";
		return redirect_url+imgUrl;
	}
	/**
	 * 图片增加、修改
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/ckeditor/img/add", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody String addCkeditorImg(
			HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session ) throws IOException  {
		String imgUrl = null;
		String imgdir=DateUtil.getImgDir();
		imgUrl = ThumbnailatorUtils.upload( request,UploaderPath.GetValueByKey(UploaderPath.ROOTPATH),UploaderPath.GetValueByKey(UploaderPath.IMG),imgdir);
		StringBuffer url=request.getRequestURL();
		String redirect_url = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString(); 
		 String callback = request.getParameter("CKEditorFuncNum");
		String result="<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + redirect_url+imgUrl + "',''" + ")</script>";
//		 response.setContentType("text/html;charset=UTF-8");
//	        String callback = request.getParameter("CKEditorFuncNum");
//	        PrintWriter out = response.getWriter();
//	        out.println("<script type=\"text/javascript\">");
//	        out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + redirect_url+imgUrl + "',''" + ")");
//	        out.println("</script>");
//	        out.flush();
//	        out.close();
		 return result;
	}
}
