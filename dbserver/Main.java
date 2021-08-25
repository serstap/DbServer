/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbserver;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author s-ostapenko
 */
public class Main {

   
    public static void main(String[] args) {
        Author ath1 = new Author(6, "popov");
        Author ath2 = new Author(3, "Jim Beam");
        DbServer db = new DbServer("jdbc:derby://localhost:1527/sample;user=app;password=app");
        
        try {
        //  boolean aaf =  db.addAuthor(ath1);
       //   boolean daf =  db.deleteAuthor(6);
          //  System.out.println(daf); 
            Document [] fdc = db.findDocumentByContent("first");
             Document [] fd = db.findDocumentByAuthor(ath2);
            for (int i=0; i<fd.length; i++){
             System.out.println(fd);
           
                System.out.println("------");
             }
        } catch (DocumentException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
