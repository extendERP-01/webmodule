package nirmalya.aathithya.webmodule.master.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.master.model.ProductCategoryModel;
import nirmalya.aathithya.webmodule.master.model.ProductMasterModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = { "master/" })
public class ProductMasterController {

	Logger logger = LoggerFactory.getLogger(ProductMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping(value = { "manage-product" })
	public String manageProduct(Model model, HttpSession session) {
		logger.info("Method : manageProduct starts");
		
		try {
			DropDownModel[] brand = restClient.getForObject(env.getMasterUrl()+"getBrandListForProduct", DropDownModel[].class);
			List<DropDownModel> brandList = Arrays.asList(brand);
			
			model.addAttribute("brandList", brandList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] mode = restClient.getForObject(env.getMasterUrl()+"getModeListForProduct", DropDownModel[].class);
			List<DropDownModel> modeList = Arrays.asList(mode);
			
			model.addAttribute("modeList", modeList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] hsnCode = restClient.getForObject(env.getMasterUrl()+"getHSNCodeListForProduct", DropDownModel[].class);
			List<DropDownModel> hsnCodeList = Arrays.asList(hsnCode);
			
			model.addAttribute("hsnCodeList", hsnCodeList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] variationType = restClient.getForObject(env.getMasterUrl()+"getVariationTypeListtForProduct", DropDownModel[].class);
			List<DropDownModel> variationTypeList = Arrays.asList(variationType);
			
			model.addAttribute("variationTypeList", variationTypeList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] uom = restClient.getForObject(env.getMasterUrl()+"getUOMListForProduct", DropDownModel[].class);
			List<DropDownModel> unitList = Arrays.asList(uom);
			
			model.addAttribute("unitList", unitList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : manageProduct ends");
		return "master/manageProduct";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-product-get-total-list")
	public @ResponseBody JsonResponse<List<ProductCategoryModel>> getAllProductCategoryList(HttpSession session) {
		logger.info("Method : getAllProductCategoryList starts");
		
		JsonResponse<List<ProductCategoryModel>> resp = new JsonResponse<List<ProductCategoryModel>>();
		
		try {
			resp = restClient.getForObject(env.getMasterUrl() + "getAllProductCategoryList",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("Success");
		}
		
		logger.info("Method : getAllProductCategoryList starts");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/manage-product-get-category-list-by-id")
	public @ResponseBody JsonResponse<List<ProductCategoryModel>> getProductCategoryListById(@RequestBody String id,HttpSession session) {
		logger.info("Method : getProductCategoryListById starts");
		
		JsonResponse<List<ProductCategoryModel>> resp = new JsonResponse<List<ProductCategoryModel>>();
		
		try {
			resp = restClient.getForObject(env.getMasterUrl() + "getProductCategoryListById?id="+id,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("Success");
		}
		
		logger.info("Method : getProductCategoryListById starts");
		return resp;
	}
	
	@SuppressWarnings({ "unchecked", "restriction" })
	@PostMapping("/manage-product-save")
	public @ResponseBody JsonResponse<Object> saveProductMaster(@RequestBody ProductMasterModel product, HttpSession session) {
		logger.info("Method : saveProductMaster starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		product.setCreatedBy(userId);
		List<String> productImg = new ArrayList<String>();
		System.out.println(product.getImgList());
		if(product.getImgList().size() > 0) {
			System.out.println("Len==="+product.getImgList().size());
			for(int i = 0; i < product.getImgList().size(); i++) {
				//if(product.getImgList().get(i).split("_")[0].contentEquals("0")) {
					try {	
						byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(product.getImgList().get(i).split("base64,")[1]);
						String ext = product.getImgList().get(i).split("base64,")[0].split("/")[1].split(";")[0].trim();
						String imageName = saveAllImage(bytes,ext);
						productImg.add(imageName);
					} catch (IOException e) {
						e.printStackTrace();
						resp.setCode("Error");
						resp.setMessage("Unexpected Error : Unable To Save Images.");
						return resp;
					}
//				} else {
//					String imageName = product.getImgName().get(i).split("_")[1];
//					productImg.add(imageName);
//				}
			}
		}
		product.setImgName(productImg);
		
		try {
			resp = restClient.postForObject(env.getMasterUrl() + "saveProductMaster", product,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();

		if (message != null && message != "") {

		} else {
			resp.setMessage("Success");
		}
		
		logger.info("Method : saveProductMaster starts");
		return resp;
	}
	
	public String saveAllImage(byte[] imageBytes, String ext) {
		logger.info("Method : saveAllImage starts");
		
		String imageName = null;
		
		try {
			
			if(imageBytes!=null) {
				long nowTime = new Date().getTime();
				if(ext.contentEquals("jpeg")) {
					imageName = nowTime+".jpg";
				} else {
					imageName = nowTime+"."+ext;
				}
				
			}

			Path path = Paths.get(env.getFileUploadMaster() + imageName);
			if(imageBytes !=null) {
				Files.write(path, imageBytes);
				
				ByteArrayInputStream in = new ByteArrayInputStream(imageBytes);
				Integer height = 280;
				Integer width = 474;
				
				try{
					BufferedImage img = ImageIO.read(in);
					if(height == 0){
						height = (width * img.getHeight())/img.getWidth();
					}
					if(width == 0){
						width = (height * img.getWidth())/img.getHeight();
					}
					
					BufferedImage outputImage = new BufferedImage(width,
							height, img.getType());
			 
			        Graphics2D g2d = outputImage.createGraphics();
			        g2d.drawImage(img, 0, 0, width, height, null);
			        g2d.dispose();
			        String outputImagePath = env.getFileUploadMaster()+"thumb/"+imageName;
			        ImageIO.write(outputImage, ext, new File(outputImagePath));

				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : saveAllImage ends");
		return imageName;
	}
}
