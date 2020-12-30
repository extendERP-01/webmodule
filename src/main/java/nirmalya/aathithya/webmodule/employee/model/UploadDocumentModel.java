package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UploadDocumentModel {
	
	private List<String> idDocument = new ArrayList<String>();
	
	private String key;

	public UploadDocumentModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<String> getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(List<String> idDocument) {
		this.idDocument = idDocument;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	@Override
	public String toString() {
		ObjectMapper mapperObj = new ObjectMapper();
		String jsonStr;
		try {
			jsonStr = mapperObj.writeValueAsString(this);
		} catch (IOException ex) {

			jsonStr = ex.toString();
		}
		return jsonStr;
	}

}
