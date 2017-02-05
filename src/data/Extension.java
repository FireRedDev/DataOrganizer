/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data;

/**
* C.G
 */
public class Extension {
private String extension;
private DataType dataType;

    public Extension(String Extension) {
       setExtension(Extension);
    }

    
    
    
    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }


    public void setDataTyp(DataType neu) {
        if( neu != dataType) {
            if (dataType!= null) {
                dataType.removeExtension(this);
            }
            dataType = neu;
            if(neu != null) {
                neu.addExtension(this);
            }
        }
    }
  
}
 if( neu != extension) {
            if (extension!= null) {
                extension.removeExtension(this);
            }
            extension = neu;
            if(neu != null) {
                neu.addExtension(this);
            }
        }