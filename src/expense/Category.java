package expense;

import javafx.scene.control.RadioButton;

/**
 * Created by juddn on 2016/01/20.
 */
public class Category {
    private String categoryName;
    private String categoryType;
    private int categoryID;
    private int userID;
    private RadioButton categoryRadioButton;
    private String hiddenProp;
    public Category(String catname, String cattype, int catid, int usid, String hidden){
        categoryName = catname;
        categoryType = cattype;
        categoryID = catid;
        userID = usid;
        hiddenProp = hidden;
        if (hiddenProp == null){
            hiddenProp = "no";
        }
        categoryRadioButton = new RadioButton(categoryName);
    }
    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public int getUserID(){
        return userID;
    }

    public String getHiddenProp(){
        return hiddenProp;
    }

    public RadioButton getCategoryRadioButton(){
        return categoryRadioButton;
    }
}
