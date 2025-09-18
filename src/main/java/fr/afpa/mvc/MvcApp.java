package fr.afpa.mvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale du projet.
 * TODO ajouter l'annotation @SpringBootApplication à la classe
 * 
 * Cette annotation est FONDAMENTALE pour activer la recherche automatique des
 * beans dans les différents packages de l'
 * 
 * Documentation ->
 * https://medium.com/@boris.alexandre.rose/springbootapplication-ab08032a7049
 * 
 */
@SpringBootApplication
public class MvcApp {
    public static void main(String[] args) {

        // tentative de connexion à une base de données PostgreSQL
        String url = "jdbc:postgresql://postgresdb:5432/account";
        String username = "postgres";
        String password = "B@nLgU4qz*9?D~3n83";

        String query = "select * from account";

        // tentative de requêtage
        try {
            Connection con = DriverManager.getConnection(url, username, password);

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("id");
                System.out.println(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SpringApplication.run(MvcApp.class, args);
    }
}
