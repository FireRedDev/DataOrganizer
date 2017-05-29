/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data;

import java.io.File;

/**
* C.G
 */
public class RegexRule {
    private File Ordner;
    private String regex;
       public RegexRule(File Ordner,String regex) {
//     this.regex = regex;
        this.Ordner = Ordner;
    }

    public File getOrdner() {
        return Ordner;
    }

    public void setOrdner(File Ordner) {
        this.Ordner = Ordner;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

}
