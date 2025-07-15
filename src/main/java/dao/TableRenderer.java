package dao;
import java.util.List;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe pour générer et afficher des tableaux HTML stylisés à partir de données.
 * Supporte deux formats de données :
 * 1. List<Map<String, Object>> (recommandé pour des données structurées)
 * 2. List<List<Object>> (avec en-têtes personnalisés)
 */
public class TableRenderer {
    private final List<?> data;
    private final String title;
    private final List<String> headers;
    private static final String DEFAULT_CSS = """
        <style>
            body {
                font-family: 'Arial', sans-serif;
                margin: 0;
                padding: 20px;
                background-color: #f9f9f9;
            }
            .table-container {
                max-width: 100%;
                overflow-x: auto;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                border-radius: 8px;
                background: white;
                margin: 20px auto;
            }
            h2 {
                color: #2c3e50;
                margin-bottom: 16px;
                text-align: center;
                padding-top: 20px;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin: 0 auto;
            }
            th {
                background-color: #3498db;
                color: white;
                font-weight: bold;
                padding: 12px 15px;
                text-align: left;
                position: sticky;
                top: 0;
            }
            td {
                padding: 10px 15px;
                border-bottom: 1px solid #ecf0f1;
            }
            tr:nth-child(even) {
                background-color: #f8f9fa;
            }
            tr:hover {
                background-color: #e8f4fc;
            }
            .no-data {
                text-align: center;
                padding: 30px;
                color: #7f8c8d;
                font-style: italic;
            }
        </style>
        """;

    /**
     * Constructeur pour des données sous forme de List<Map<String, Object>>
     * @param data Données sous forme de liste de maps
     * @param title Titre du tableau
     */
    public TableRenderer(List<Map<String, Object>> data, String title) {
        this.data = data;
        this.title = title;
        this.headers = !data.isEmpty() ? List.copyOf(data.get(0).keySet()) : List.of();
    }

    /**
     * Constructeur pour des données sous forme de List<List<Object>> avec en-têtes personnalisés
     * @param data Données sous forme de liste de listes
     * @param title Titre du tableau
     * @param headers Noms des colonnes
     */
    public TableRenderer(List<List<Object>> data, String title, List<String> headers) {
        this.data = data;
        this.title = title;
        this.headers = headers;
    }

    /**
     * Génère le code HTML complet du tableau
     * @return String contenant le HTML généré
     */
    public String generateHTML() {
        StringBuilder html = new StringBuilder();
        html.append("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>%s</title>
                %s
            </head>
            <body>
                <div class="table-container">
            """.formatted(escapeHTML(title), DEFAULT_CSS));

        html.append(generateTableHTML());

        html.append("""
                </div>
            </body>
            </html>
            """);

        return html.toString();
    }

    /**
     * Génère uniquement la partie tableau du HTML
     * @return String HTML du tableau
     */
    private String generateTableHTML() {
        if (data.isEmpty()) {
            return "<div class='no-data'>Aucune donnée disponible</div>";
        }

        StringBuilder tableHtml = new StringBuilder();
        tableHtml.append("<h2>").append(escapeHTML(title)).append("</h2>");
        tableHtml.append("<table>");

        // En-têtes
        tableHtml.append("<thead><tr>");
        for (String header : headers) {
            tableHtml.append("<th>").append(escapeHTML(header)).append("</th>");
        }
        tableHtml.append("</tr></thead>");

        // Données
        tableHtml.append("<tbody>");
        if (data.get(0) instanceof Map) {
            generateFromMaps(tableHtml);
        } else {
            generateFromLists(tableHtml);
        }
        tableHtml.append("</tbody></table>");

        return tableHtml.toString();
    }

    /**
     * Génère les lignes du tableau à partir de données Map
     * @param sb StringBuilder pour accumuler le HTML
     */
    private void generateFromMaps(StringBuilder sb) {
        for (Object rowObj : data) {
            @SuppressWarnings("unchecked")
            Map<String, Object> row = (Map<String, Object>) rowObj;
            sb.append("<tr>");
            for (String header : headers) {
                Object value = row.get(header);
                sb.append("<td>").append(escapeHTML(String.valueOf(value))).append("</td>");
            }
            sb.append("</tr>");
        }
    }

    /**
     * Génère les lignes du tableau à partir de données List
     * @param sb StringBuilder pour accumuler le HTML
     */
    private void generateFromLists(StringBuilder sb) {
        for (Object rowObj : data) {
            @SuppressWarnings("unchecked")
            List<Object> row = (List<Object>) rowObj;
            sb.append("<tr>");
            for (Object value : row) {
                sb.append("<td>").append(escapeHTML(String.valueOf(value))).append("</td>");
            }
            sb.append("</tr>");
        }
    }

    /**
     * Échappe les caractères spéciaux HTML pour éviter les problèmes d'affichage
     * @param input Texte à échapper
     * @return Texte échappé
     */
    private String escapeHTML(String input) {
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    /**
     * Sauvegarde le tableau dans un fichier HTML
     * @param filename Chemin du fichier de sortie
     * @throws IOException En cas d'erreur d'écriture
     */
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(generateHTML());
        }
    }

    /**
     * Affiche le HTML dans la console (pour débogage)
     */
    public void displayHTML() {
        System.out.println(generateHTML());
    }
}
