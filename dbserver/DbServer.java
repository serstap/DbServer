/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbserver;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DbServer implements IDbService{

   private String connStr;

public DbServer(String connStr) {
        this.connStr = connStr;
    }


    @Override
    public boolean addAuthor(Author author) throws DocumentException {
        try {
            Connection conn = DriverManager.getConnection(connStr);
            if (author.getAuthor() != null) {
                try {
                    PreparedStatement prstmt = conn.prepareStatement("insert into authors values(?,?,?)");
                    prstmt.setInt(1, author.getAuthor_id());
                    prstmt.setString(2, author.getAuthor());
                    prstmt.setString(3, author.getNotes());
                    prstmt.executeUpdate();
                    return true;
                }
                catch (SQLException exception) {
                    throw new DocumentException(exception.getMessage());
                }
            }
            else {
                try {
                    PreparedStatement prstmt = conn.prepareStatement("update authors set notes = ? where author_id = ?");
                    prstmt.setString(1, author.getNotes());
                    prstmt.setInt(2, author.getAuthor_id());
                    prstmt.executeUpdate();
                    return false;
                }
                catch (SQLException exception) {
                    throw new DocumentException(exception.getMessage());
                }
            }
            } catch (SQLException exception) {
            throw new DocumentException(exception.getMessage());
              }
    }

    @Override
    public boolean addDocument(Document doc, Author author) throws DocumentException {
        try {
            Connection conn = DriverManager.getConnection(connStr);
            if (author.getAuthor() != null && doc.getTitle() != null && doc.getDate() != null && doc.getAuthor_id() != 0) {
                try {
                    PreparedStatement prstmt = conn.prepareStatement("insert into documents values(?,?,?,?,?)");
                    prstmt.setInt(1, doc.getDocument_id());
                    prstmt.setString(2, doc.getTitle());
                    prstmt.setString(3, doc.getText());
                    prstmt.setDate(4, (Date) doc.getDate());
                    prstmt.setInt(5, doc.getAuthor_id());
                    prstmt.executeUpdate();
                    return true;
                }
                catch (SQLException throwables) {
                    throw new DocumentException();
                }
            }
            else {


                    if (doc.getTitle() != null) try {
                        PreparedStatement prstmt = conn.prepareStatement("update documents values(?,?,?,?)");
                        prstmt.setString(1, doc.getTitle());
                        prstmt.setString(2, doc.getText());
                        prstmt.setInt(3, doc.getAuthor_id());
                        prstmt.setInt(4, doc.getDocument_id());
                    prstmt.executeUpdate();
                    return false;
                }
                    catch(SQLException throwables){
                        throw new DocumentException();
                    }
                if (doc.getDate() != null) try{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = sdf.format(doc.getDate());

                    PreparedStatement prstmt = conn.prepareStatement("update documents values(?,?,?,?)");
                    prstmt.setDate(1, (Date) doc.getDate());
                    prstmt.setString(2, doc.getText());
                    prstmt.setInt(3, doc.getAuthor_id());
                    prstmt.setInt(4, doc.getDocument_id());
                    prstmt.executeUpdate();
                    return false;
                }
                    catch(SQLException throwables){
                        throw new DocumentException();
                    }
            }


        } catch(SQLException throwables){
            throw new DocumentException();
        }
return false;
    }




    @Override
    public Document[] findDocumentByAuthor(Author author) throws DocumentException {
        if (author.getAuthor_id() <= 0 || author.getAuthor() == null || author.getAuthor().equals("")) {
            throw new DocumentException("Illegal author arguments");
        }
        try (Connection conn = DriverManager.getConnection(connStr);
             Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("select * from documents where author_id = " + author.getAuthor_id())) {

                List<Document> docs = new ArrayList();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nameDoc = rs.getString("name_doc");
                    String text = rs.getString("text");
                    Date date = rs.getDate("reg_date");
                    int authorId = rs.getInt("author_id");
                    Document doc = new Document(id, nameDoc, text, date, authorId);
                    docs.add(doc);
                }
             
             Document [] docsArr = new Document[docs.size()];
              
             return docs.toArray(docsArr);
                    
            }

//            try (ResultSet rs = stmt.executeQuery("select * from documents, authors where author = '" + author.getAuthor() + "' and documents.author_id = authors.author_id")) {
//                return new Document[rs];
//            }
        } catch (SQLException ex) {
            throw new DocumentException(ex.getMessage());
        }
    }

    @Override
    public Document[] findDocumentByContent(String content) throws DocumentException {
      if (content == null && content == ""){
          throw new DocumentException("Content is null or empty");
      }
        try (Connection conn = DriverManager.getConnection(connStr);
                Statement stmt = conn.createStatement()) {
            content = content.toLowerCase();

            try (ResultSet rs = stmt.executeQuery("select * from documents where lower(name_doc) like '%" + content + "%' OR lower(text) like '%" + content + "%'")) {
                List<Document> docs = new ArrayList();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nameDoc = rs.getString("name_doc");
                    String text = rs.getString("text");
                    Date date = rs.getDate("reg_date");
                    int authorId = rs.getInt("author_id");
                    Document doc = new Document(id, nameDoc, text, date, authorId);
                    docs.add(doc);
                }

                Document[] docsArr = new Document[docs.size()];
                       
                return docs.toArray(docsArr);

            }

        } catch (SQLException ex) {
            throw new DocumentException(ex.getMessage());
        }


    }

    @Override
    public boolean deleteAuthor(Author author) throws DocumentException {
        try (Connection conn = DriverManager.getConnection(connStr)) {
            PreparedStatement prstmt = conn.prepareStatement("delete from documents where author_id = (select id from authors where author = '" + author.getAuthor() + "')");
            prstmt.executeUpdate();
            int count = prstmt.executeUpdate();
            if (count >0){
                return true;
            }
            else{
                return false;
            }

        } catch (SQLException ex) {
            throw new DocumentException(ex.getMessage());
        }
       // return false;
    }

    @Override
    public boolean deleteAuthor(int id) throws DocumentException {
        try (Connection conn = DriverManager.getConnection(connStr)){
                PreparedStatement prstmt = conn.prepareStatement("delete from documents where author_id = ?");
                prstmt.setInt(1, id);
                PreparedStatement prstmt1 = conn.prepareStatement("delete from authors where id = ?");
                prstmt1.setInt(1, id);
                int count = prstmt1.executeUpdate();
                if (count > 0) {
                    return true;
                }
                else {
                    return false;
                }
               
        } catch (SQLException ex) {
            throw new DocumentException(ex.getMessage());
        }
     
    }

    @Override
    public void close() throws Exception {

    }
}