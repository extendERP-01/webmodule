package nirmalya.aathithya.webmodule.account.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DataSetAccountTree {


private String groupId;

private String groupName;

private String levelName;

private String parentName;

private String parentId;

public DataSetAccountTree() {
super();
// TODO Auto-generated constructor stub
}



public String getParentId() {
return parentId;
}



public void setParentId(String parentId) {
this.parentId = parentId;
}



public String getGroupId() {
return groupId;
}



public void setGroupId(String groupId) {
this.groupId = groupId;
}



public String getGroupName() {
return groupName;
}



public void setGroupName(String groupName) {
this.groupName = groupName;
}



public String getLevelName() {
return levelName;
}



public void setLevelName(String levelName) {
this.levelName = levelName;
}



public String getParentName() {
return parentName;
}



public void setParentName(String parentName) {
this.parentName = parentName;
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



