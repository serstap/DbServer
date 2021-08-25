/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbserver;

public interface IDbService extends AutoCloseable {

    boolean addAuthor(Author author) throws DocumentException;

    boolean addDocument(Document doc, Author author) throws DocumentException;

    Document[] findDocumentByAuthor(Author author) throws DocumentException;

    Document[] findDocumentByContent(String content) throws DocumentException;

    boolean deleteAuthor(Author author) throws DocumentException;

    boolean deleteAuthor(int id) throws DocumentException;
}
