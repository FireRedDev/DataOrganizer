/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package viewController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mover.DataMover;

/**
* C.G
 */
public class ActionListenerVar implements ActionListener {
 private DataMover mover;

    public ActionListenerVar(DataMover page) {
        this.mover = page;
    }



 @Override
    public void actionPerformed(ActionEvent e) {
  
     try {
         mover.sort();
     } catch (IOException ex) {
         Logger.getLogger(ActionListenerVar.class.getName()).log(Level.SEVERE, null, ex);
     }
         System.out.println("sorted");
  
    }

    public DataMover getMover() {
        return mover;
    }

    public void setMover(DataMover mover) {
        this.mover = mover;
    }

   
    
}
