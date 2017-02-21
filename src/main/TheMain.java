/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import data.DataType;
import data.Extension;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mover.DataMover;

/**
 *
 * @author Christopher G
 */
public class TheMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            DataType type = new DataType();
            //könnte sein das der punkt weggehört
            type.addExtension(new Extension("jpg"));
            DataMover mover = new DataMover(type);
            mover.sort();
        } catch (IOException ex) {
            System.out.print(ex.getMessage());
            Logger.getLogger(TheMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
