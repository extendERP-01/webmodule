/**
 * 
 */
package nirmalya.aathithya.webmodule.document.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;



/**
 * @author Nirmalya Labs
 *
 */
@Controller
public class DocumentViewController {
	@Autowired
	EnvironmentVaribles env;
	Logger logger = LoggerFactory.getLogger(DocumentViewController.class);
	/**
	 * document controller to load images instantly
	 *
	 */
	@RequestMapping(value="document/image/{docname}")
	@ResponseBody

	public ResponseEntity<byte[]> getDocument(@PathVariable(value="docname")String docname)throws IOException{
		logger.info("Method : getDocument controller function starts");
		
		File dir = ResourceUtils.getFile(env.getFileUploadDocumenttUrl());
		File file = new File(dir.getAbsolutePath() + "/" + docname);
		byte[] bytearr = Files.readAllBytes(file.toPath());
		if(docname.endsWith(".png")) {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytearr);
		}
		else if(docname.endsWith(".jpeg")) {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytearr);
		}
		else if(docname.endsWith(".pdf")) {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytearr);
		}
		else {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.ALL).body(bytearr);
		}
	}
	
	/**
	 * document controller to load images instantly
	 *
	 */
	@RequestMapping(value="document/excel/{docname}")
	@ResponseBody
	public HttpServletResponse getDocument1(@PathVariable(value="docname")String docname,HttpServletResponse response)throws IOException{
		logger.info("Method : getDocument controller function starts");
		
		File dir = ResourceUtils.getFile(env.getFileUploadDocumenttUrl());
		File file = new File(dir.getAbsolutePath() + "/" + docname);
		byte[] bytearr = Files.readAllBytes(file.toPath());
		if(docname.endsWith(".png")) {
			logger.info("Method : getDocument controller function starts");
			return null;
		}
		else if(docname.endsWith(".jpeg")) {
			logger.info("Method : getDocument controller function starts");
		return null;
		}
		else if(docname.endsWith(".pdf")) {
			logger.info("Method : getDocument controller function starts");
			return null;
		}
		else {
			logger.info("Method : getDocument controller function starts");
			response.setHeader("Content-disposition","attachment; filename=" + docname);
			File xls = new File(docname);
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
			int length = 0;
			byte[] buffer= new byte[8192];
			while ((length = in.read(buffer)) > 0){
			     out.write(buffer, 0, length);
			}
			in.close();
			out.close();
			return response;
		}
	}
	@RequestMapping(value="document/image/thumb/{docname}")
	@ResponseBody
	public ResponseEntity<byte[]> getDocumentThumb(@PathVariable(value="docname")String docname)throws IOException{
		logger.info("Method : image controller function starts");
		
		File dir = ResourceUtils.getFile(env.getFileUploadDocumenttUrl()+"/thumb");
		File file = new File(dir.getAbsolutePath() + "/" + docname);
		byte[] bytearr = Files.readAllBytes(file.toPath());
		if(docname.endsWith(".png")) {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytearr);
		}
		else if(docname.endsWith(".jpeg")) {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytearr);
		}
		else if(docname.endsWith(".pdf")) {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytearr);
		}
		else {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.ALL).body(bytearr);
		}
	}
}
