/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbserver;

/**
 * Класс представляет общее исключение, возникающее при работе с документами.
 * @author (C)Y.D.Zakovryashin, 12.11.2020
 */
public class DocumentException extends Exception {

    public DocumentException() {
    }

    public DocumentException(String string) {
        super(string);
    }

}