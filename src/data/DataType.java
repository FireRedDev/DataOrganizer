/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.File;
import java.util.LinkedList;

/**
 * C.G
 */
public class DataType {

    private LinkedList<Extension> extensionlist;
    private File Ordner;

    public DataType() {
        extensionlist = new LinkedList<>();
        File dir = new File("Dateityp");
        dir.mkdir();
        setOrdner(dir);
    }

    public void addExtension(Extension aThis) {
        extensionlist.add(aThis);
    }

    public void removeExtension(Extension aThis) {
        extensionlist.remove(aThis);
    }

    public LinkedList<Extension> getExtensionlist() {
        return extensionlist;
    }

    public void setExtensionlist(LinkedList<Extension> extensionlist) {
        this.extensionlist = extensionlist;
    }

    public File getOrdner() {
        return Ordner;
    }

    public void setOrdner(File Ordner) {
        this.Ordner = Ordner;
    }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data;

import java.util.LinkedList;

/**
* C.G
 */
public class DataType {
    private LinkedList<Extension> extensionlist;
    public DataType () {
        extensionlist = new LinkedList<>();
    }

    void removeExtension(Extension aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void addExtension(Extension aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
